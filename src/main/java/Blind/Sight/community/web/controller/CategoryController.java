package Blind.Sight.community.web.controller;

import Blind.Sight.community.domain.service.write.CategoryServicePg;
import Blind.Sight.community.dto.category.CategoryInput;
import Blind.Sight.community.web.response.common.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("api/v1")
public class CategoryController {
    private final CategoryServicePg categoryServicePg;

    @PostMapping("/categories/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDto<Object>> createCategory(@RequestBody @ModelAttribute CategoryInput categoryInput) throws GeneralSecurityException, IOException {
        log.info("Request creating category...");
        categoryServicePg.createCategory(categoryInput);
        return ResponseEntity.ok(ResponseDto.build().withMessage("OK"));
    }

    @PutMapping("/categories/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDto<Object>> updateCategory(@RequestParam String categoryId,
                                                              @RequestBody @ModelAttribute CategoryInput categoryInput) throws GeneralSecurityException, IOException {
        log.info("Request updating category...");
        categoryServicePg.updateCategory(categoryId, categoryInput);
        return ResponseEntity.ok(ResponseDto.build().withMessage("OK"));
    }

    @DeleteMapping("/categories/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDto<Object>> deleteCategory(@RequestParam String categoryId, @RequestParam String imageId,
                                                              @RequestParam Long categoryImageId) throws GeneralSecurityException, IOException {
        log.info("Request deleting category...");
        categoryServicePg.deleteCategory(categoryId, imageId, categoryImageId);
        return ResponseEntity.ok(ResponseDto.build().withMessage("OK"));
    }
}
