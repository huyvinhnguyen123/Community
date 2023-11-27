package Blind.Sight.community.web.controller;

import Blind.Sight.community.domain.service.write.CartServicePg;
import Blind.Sight.community.dto.cart.CartInput;
import Blind.Sight.community.web.response.common.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("api/v1")
public class CartController {
    private final CartServicePg cartServicePg;

    @PostMapping("/add-to-cart")
    public ResponseEntity<ResponseDto<Object>> addToCart(Authentication authentication,
                                                          @RequestParam String productId, @RequestBody CartInput cartInput) {
        log.info("Request creating cart...");
        cartServicePg.addToCart(authentication, productId, cartInput);
        return ResponseEntity.ok(ResponseDto.build().withMessage("OK"));
    }

    @PostMapping("/synchronize-cart")
    public ResponseEntity<ResponseDto<Object>> synchronizeCart(Authentication authentication, @RequestBody CartInput cartInput) {
        log.info("Request synchronizing cart...");
        cartServicePg.synchronizeCart(authentication, cartInput);
        return ResponseEntity.ok(ResponseDto.build().withMessage("OK"));
    }
}
