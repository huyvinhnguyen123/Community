package Blind.Sight.community.web.response.mapper;

import Blind.Sight.community.web.response.LoginResponse;

public class LoginMapper {
    private LoginMapper(){}
    public static LoginResponse mapToLogin(String token, String refreshToken) {
        return LoginResponse.builder()
                .token(token)
                .refreshToken(refreshToken)
                .build();
    }
}
