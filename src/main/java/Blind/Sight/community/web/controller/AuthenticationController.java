package Blind.Sight.community.web.controller;

import Blind.Sight.community.config.security.JwtUtil;
import Blind.Sight.community.domain.service.write.UserServicePg;
import Blind.Sight.community.dto.user.LoginInput;
import Blind.Sight.community.dto.user.ResetPasswordInput;
import Blind.Sight.community.dto.user.UnlockUserInput;
import Blind.Sight.community.dto.user.UserDataInput;
import Blind.Sight.community.exception.HandleRequest;
import Blind.Sight.community.web.response.LoginResponse;
import Blind.Sight.community.web.response.common.ResponseDto;
import Blind.Sight.community.web.response.mapper.LoginMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("api/v1")
public class AuthenticationController {
    private final UserServicePg userServicePg;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<ResponseDto<Object>> createUser(@Valid @RequestBody UserDataInput userDataInput, BindingResult bindingResult) {
        log.info("Request creating user...");

        // Check for validation errors in the input
        if (bindingResult.hasErrors()) {
            Map<String, String> fieldErrors = new HashMap<>();
            return HandleRequest.validateRequest(HttpStatus.BAD_REQUEST, fieldErrors, bindingResult);
        }

        userServicePg.createUser(userDataInput);
        return ResponseEntity.ok(ResponseDto.build().withMessage("OK"));
    }

    @PostMapping("/send-reset-password")
    public ResponseEntity<ResponseDto<Object>> sendEmailResetPassword(@RequestParam String email) {
        log.info("Request sending mail...");
        userServicePg.sendMailResetPassword(email);
        return ResponseEntity.ok(ResponseDto.build().withMessage("OK"));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ResponseDto<Object>> resetPassword(@Valid @RequestBody ResetPasswordInput resetPasswordInput) throws SocketTimeoutException {
        log.info("Request resetting password...");
        userServicePg.resetPassword(resetPasswordInput);
        return ResponseEntity.ok(ResponseDto.build().withMessage("OK"));
    }

    @PostMapping("/send-unlock-account")
    public ResponseEntity<ResponseDto<Object>> sendEmailUnlockUser(@RequestParam String email) {
        log.info("Request sending mail...");
        userServicePg.sendMailUnlockUser(email);
        return ResponseEntity.ok(ResponseDto.build().withMessage("OK"));
    }

    @PostMapping("/unlock-account")
    public ResponseEntity<ResponseDto<Object>> unlockUser(@Valid @RequestBody UnlockUserInput unlockUserInput) throws SocketTimeoutException {
        log.info("Request unlocking user...");
        userServicePg.unlockUser(unlockUserInput);
        return ResponseEntity.ok(ResponseDto.build().withMessage("OK"));
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDto<Object>> login(@Valid @RequestBody LoginInput loginInput, BindingResult bindingResult) {
        log.info("Request authenticating user...");

        // Check for validation errors in the input
        if (bindingResult.hasErrors()) {
            Map<String, String> fieldErrors = new HashMap<>();
            return HandleRequest.validateRequest(HttpStatus.BAD_REQUEST, fieldErrors, bindingResult);
        }

        String token = userServicePg.login(loginInput, authenticationManager, jwtUtil);
        String refreshToken = UUID.randomUUID().toString();
        LoginResponse loginResponse = LoginMapper.mapToLogin(token, refreshToken);
        log.info("Login successfully");
        return ResponseEntity.ok(ResponseDto.build().withData(loginResponse));
    }

    @PostMapping("/logout")
    public ResponseEntity<ResponseDto<Object>> logout(Authentication authentication) {
        log.info("Request logout...");
        userServicePg.logout(authentication, jwtUtil);
        return ResponseEntity.ok(ResponseDto.build().withMessage("OK"));
    }
}
