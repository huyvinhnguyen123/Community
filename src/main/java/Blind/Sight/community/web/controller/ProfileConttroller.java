package Blind.Sight.community.web.controller;

import Blind.Sight.community.domain.service.write.ProfileServicePg;
import Blind.Sight.community.web.response.common.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("api/v1/users")
public class ProfileConttroller {
    private final ProfileServicePg profileServicePg;

    @PostMapping("/create-profile")
    public ResponseEntity<ResponseDto<Object>> createProfile(Authentication authentication) {
        log.info("Request updating user...");
        profileServicePg.createProfile(authentication);
        return ResponseEntity.ok(ResponseDto.build().withMessage("OK"));
    }
}
