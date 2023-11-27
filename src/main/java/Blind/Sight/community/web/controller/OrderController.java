package Blind.Sight.community.web.controller;

import Blind.Sight.community.domain.service.write.OrderServicePg;
import Blind.Sight.community.dto.order.OrderInput;
import Blind.Sight.community.dto.payment.PaymentInput;
import Blind.Sight.community.web.response.common.ResponseDto;
import com.stripe.exception.StripeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("api/v1")
public class OrderController {
    private final OrderServicePg orderServicePg;

    @PostMapping("/order")
    public ResponseEntity<ResponseDto<Object>> createOrder(Authentication authentication,
                                                           @RequestParam String cartId) {
        log.info("Request ordering product...");
        orderServicePg.createOrder(authentication, cartId);
        return ResponseEntity.ok(ResponseDto.build().withMessage("OK"));
    }

    @PostMapping("/order/payment")
    public ResponseEntity<ResponseDto<Object>> createPayment(Authentication authentication,
                                                             @RequestParam String orderId,
                                                             @RequestBody PaymentInput paymentInput) throws StripeException {
        log.info("Request paying...");
        orderServicePg.createPayment(authentication, orderId, paymentInput);
        return ResponseEntity.ok(ResponseDto.build().withMessage("OK"));
    }
}
