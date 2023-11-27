package Blind.Sight.community.web.response;

import lombok.*;

@Getter
@Setter
@Builder
public class LoginResponse {
    private String token;
    private String refreshToken;
}
