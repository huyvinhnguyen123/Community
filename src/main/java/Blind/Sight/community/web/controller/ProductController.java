package Blind.Sight.community.web.controller;

import Blind.Sight.community.domain.service.write.ProductServicePg;
import Blind.Sight.community.dto.product.ProductInput;
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
public class ProductController {
    private final ProductServicePg productServicePg;

    @PostMapping("/products/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDto<Object>> createProduct(@RequestBody @ModelAttribute ProductInput productInput) throws GeneralSecurityException, IOException {
        log.info("Request creating product...");
        productServicePg.createProduct(productInput);
        return ResponseEntity.ok(ResponseDto.build().withMessage("OK"));
    }

    @PutMapping("/products/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDto<Object>> updateProduct(@RequestParam String productId,
                                                             @RequestBody @ModelAttribute ProductInput productInput) throws GeneralSecurityException, IOException {
        log.info("Request updating product...");
        productServicePg.updateProduct(productId,productInput);
        return ResponseEntity.ok(ResponseDto.build().withMessage("OK"));
    }

    @DeleteMapping("/products/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDto<Object>> deleteProduct(@RequestParam String productId) {
        log.info("Request deleting product...");
        productServicePg.deleteProduct(productId);
        return ResponseEntity.ok(ResponseDto.build().withMessage("OK"));
    }

    @DeleteMapping("/products/delete-all-relations")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDto<Object>> deleteProductAndProductRelation(@RequestParam String productId, @RequestParam String imageId,
                                                                               @RequestParam Long productImageId, @RequestParam  Long productCategoryId) throws GeneralSecurityException, IOException {
        log.info("Request deleting product...");
        productServicePg.deleteProductAndProductRelation(productId, imageId, productImageId, productCategoryId);
        return ResponseEntity.ok(ResponseDto.build().withMessage("OK"));
    }
}
