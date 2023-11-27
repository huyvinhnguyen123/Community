package Blind.Sight.community.web.controller;

import Blind.Sight.community.domain.service.custom.CSVParserService;
import Blind.Sight.community.domain.service.custom.PDFParserService;
import Blind.Sight.community.domain.service.read.UserServiceMy;
import Blind.Sight.community.domain.service.write.AdminServicePg;
import Blind.Sight.community.dto.image.UserImageData;
import Blind.Sight.community.dto.user.UserData;
import Blind.Sight.community.dto.user.UserDataInput;
import Blind.Sight.community.exception.HandleRequest;
import Blind.Sight.community.web.response.UserResponse;
import Blind.Sight.community.web.response.common.ResponseDto;
import Blind.Sight.community.web.response.mapper.UserMapper;
import com.opencsv.exceptions.CsvValidationException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.Writer;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("api/v1")
public class AdminController {
    private final AdminServicePg adminServicePg;
    private final UserServiceMy userServiceMy;
    private final CSVParserService csvParserService;
    private final PDFParserService pdfParserService;
    private static final String DEFAULT_URL = "/create/admin/ZbGkKnmOqkllQIe9";
    private static final String DEFAULT_ADMIN_URL = "/admin";

    @PostMapping("/system/prepare")
    public ResponseEntity<ResponseDto<Object>> createSyStem(@Valid @RequestBody UserDataInput userDataInput, BindingResult bindingResult) {
        log.info("Request creating user...");

        // Check for validation errors in the input
        if (bindingResult.hasErrors()) {
            Map<String, String> fieldErrors = new HashMap<>();
            return HandleRequest.validateRequest(HttpStatus.BAD_REQUEST, fieldErrors, bindingResult);
        }

        adminServicePg.createSystem(userDataInput);
        return ResponseEntity.ok(ResponseDto.build().withMessage("OK"));
    }

    @PostMapping(DEFAULT_URL)
    @PreAuthorize("hasAnyRole('ADMIN','PRE')")
    public ResponseEntity<ResponseDto<Object>> createAdmin(@Valid @RequestBody UserDataInput userDataInput, BindingResult bindingResult) {
        log.info("Request creating user...");

        // Check for validation errors in the input
        if (bindingResult.hasErrors()) {
            Map<String, String> fieldErrors = new HashMap<>();
            return HandleRequest.validateRequest(HttpStatus.BAD_REQUEST, fieldErrors, bindingResult);
        }

        adminServicePg.createAdmin(userDataInput);
        return ResponseEntity.ok(ResponseDto.build().withMessage("OK"));
    }

    @PutMapping(DEFAULT_ADMIN_URL + "/users/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDto<Object>> updateAdmin(@RequestParam String userId,
                                                          @RequestBody @ModelAttribute UserDataInput userDataInput) throws GeneralSecurityException, IOException {
        log.info("Request updating user...");
        adminServicePg.updateUser(userId, userDataInput);
        return ResponseEntity.ok(ResponseDto.build().withMessage("OK"));
    }

    @DeleteMapping(DEFAULT_ADMIN_URL + "/users/delete-image")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDto<Object>> deleteUserImage(@RequestBody UserImageData userImageData) throws GeneralSecurityException, IOException {
        log.info("Request updating user...");
        adminServicePg.deleteUserImage(userImageData.getUserId(), userImageData.getImageId());
        return ResponseEntity.ok(ResponseDto.build().withMessage("OK"));
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDto<Object>> findAllUsers( @RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "1") int size) {
        log.info("Request selecting all users...");

        Pageable pageable = PageRequest.of(page, size, Sort.by("username").ascending());
        Page<UserData> userDataPage = userServiceMy.findAllUsers(pageable);
        Long totalUsers = userDataPage.getTotalElements();
        Integer totalPages = userDataPage.getTotalPages();
        Iterable<UserData> userDataList = userDataPage.getContent();

        UserResponse userResponse = UserMapper.mapToUser(userDataList,pageable,totalUsers,totalPages);
        return ResponseEntity.ok(ResponseDto.build().withData(userResponse));
    }

    @DeleteMapping(DEFAULT_ADMIN_URL + "/users/delete-1")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDto<Object>> deleteUserForever(@RequestParam String userId) {
        log.info("Request deleting user...");
        adminServicePg.deleteUserForever(userId);
        return ResponseEntity.ok(ResponseDto.build().withMessage("OK"));
    }

    @PostMapping(DEFAULT_ADMIN_URL + "/import")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDto<Object>> importDataCSV(@RequestParam("file") MultipartFile file,
                                                          @RequestParam String type) throws CsvValidationException, IOException, GeneralSecurityException {
        log.info("request importing data...");
        csvParserService.importCSVFile(file, type);
        return ResponseEntity.ok(ResponseDto.build().withMessage("OK"));
    }

    @PostMapping(DEFAULT_ADMIN_URL + "/export")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDto<Object>> exportDataCSV(@RequestParam String type, @RequestParam String time,
                                                             HttpServletResponse response) throws IOException {
        log.info("request exporting data...");

        String contentDisposition = "Content-Disposition";
        response.setContentType("text/csv");

        if (time.equals("today")) {
            response.setHeader(contentDisposition, "attachment; filename=today-users.csv");
        }

        if (time.equals("week")) {
            response.setHeader(contentDisposition, "attachment; filename=week-users.csv");
        }

        if (time.equals("month")) {
            response.setHeader(contentDisposition, "attachment; filename=month-users.csv");
        }

        if (time.equals("year")) {
            response.setHeader(contentDisposition, "attachment; filename=year-users.csv");
        }

        Writer writer = response.getWriter();
        csvParserService.exportCSVFile(writer, type, time);
        return ResponseEntity.ok(ResponseDto.build().withMessage("OK"));
    }

    @PostMapping(DEFAULT_ADMIN_URL + "/upload-pdf")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDto<Object>> uploadFilePDF(@RequestParam("file") MultipartFile file) throws GeneralSecurityException, IOException {
        log.info("request uploading file...");
        pdfParserService.importPDFFile(file);
        return ResponseEntity.ok(ResponseDto.build().withMessage("OK"));
    }
}
