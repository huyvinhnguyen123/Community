package Blind.Sight.community.web.controller;

import Blind.Sight.community.domain.service.read.UserServiceMy;
import Blind.Sight.community.domain.service.write.UserServicePg;
import Blind.Sight.community.dto.user.UserDataForUser;
import Blind.Sight.community.dto.user.UserDataInput;
import Blind.Sight.community.dto.user.UserSearchInput;
import Blind.Sight.community.web.response.UserSearchResponse;
import Blind.Sight.community.web.response.common.ResponseDto;
import Blind.Sight.community.web.response.mapper.UserSearchMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("api/v1/users")
public class UserController {
    private final UserServicePg userServicePg;
    private final UserServiceMy userServiceMy;

    @PutMapping("/user/update")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ResponseDto<Object>> updateUser(@RequestParam String userId, Authentication authentication,
                                                          @RequestBody @ModelAttribute UserDataInput userDataInput) throws GeneralSecurityException, IOException {

        log.info("Request updating user...");
        userServicePg.updateUserAndImage(authentication, userId, userDataInput);
        return ResponseEntity.ok(ResponseDto.build().withMessage("OK"));
    }

    @DeleteMapping("/user/update/delete-image")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ResponseDto<Object>> deleteImageFromUser(@RequestParam String userId, Authentication authentication,
                                                          @RequestBody @ModelAttribute UserDataInput userDataInput) throws GeneralSecurityException, IOException {
        log.info("Request updating user...");
        userServicePg.updateUserAndDeleteImage(authentication, userId, userDataInput.getFileId());
        return ResponseEntity.ok(ResponseDto.build().withMessage("OK"));
    }

    @PostMapping("/lock-account")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ResponseDto<Object>> lockUser(Authentication authentication,
                                                        @Valid @RequestParam String userId, @RequestParam String imageId) throws GeneralSecurityException, IOException {
        log.info("Request locking account...");
        userServicePg.lockUser(authentication,userId,imageId);
        return ResponseEntity.ok(ResponseDto.build().withMessage("OK"));
    }


    @GetMapping("/search")
    public ResponseEntity<ResponseDto<Object>> searchListOfUsers(@RequestParam(defaultValue = "0") int page,
                                                                 @RequestParam(defaultValue = "1") int size,
                                                                 @RequestBody @ModelAttribute UserSearchInput userSearchInput) {
        log.info("Request searching...");

        Pageable pageable = PageRequest.of(page, size, Sort.by("username"));
        Page<UserDataForUser> userDataForUserPage = userServiceMy.searchListOfUser(userSearchInput, pageable);
        Long totalUsers = userDataForUserPage.getTotalElements();
        Integer totalPages = userDataForUserPage.getTotalPages();
        Iterable<UserDataForUser> userDataForUserList = userDataForUserPage.getContent();

        UserSearchResponse userSearchResponse = UserSearchMapper.mapToUserSearch(userDataForUserList, pageable, totalUsers, totalPages);
        return ResponseEntity.ok(ResponseDto.build().withData(userSearchResponse));
    }

    @PostMapping("/synchronize-data")
    public ResponseEntity<ResponseDto<Object>> synchronizeAllData() {
        log.info("Request synchronizing data...");
        userServiceMy.synchronizeAllData();
        return ResponseEntity.ok(ResponseDto.build().withMessage("OK"));
    }
}
