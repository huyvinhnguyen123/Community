package Blind.Sight.community.web.controller;

import Blind.Sight.community.domain.service.system.CurrencyService;
import Blind.Sight.community.domain.service.system.TypeService;
import Blind.Sight.community.web.response.common.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("api/v0")
public class SystemController {
    private final TypeService typeService;
    private final CurrencyService currencyService;
    private static final String LIST_TYPE_INSERT_URL = "/types/insert";
    private static final String LIST_CURRENCY_INSERT_URL = "/currencies/insert";
    private static final String TYPE_INSERT_URL = "/type/insert";

    @PostMapping(LIST_TYPE_INSERT_URL)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDto<Object>> createDefaultType() {
        log.info("Request system creating default type...");
        typeService.createDefaultType();
        return ResponseEntity.ok(ResponseDto.build().withMessage("OK"));
    }

    @PostMapping(LIST_CURRENCY_INSERT_URL)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDto<Object>> createDefaultCurrency() {
        log.info("Request system creating default currency...");
        currencyService.createDefaultCurrency();
        return ResponseEntity.ok(ResponseDto.build().withMessage("OK"));
    }

    @PostMapping(TYPE_INSERT_URL)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDto<Object>> createType(@RequestParam String name) {
        log.info("Request system creating type...");
        typeService.createCustomType(name);
        return ResponseEntity.ok(ResponseDto.build().withMessage("OK"));
    }

}
