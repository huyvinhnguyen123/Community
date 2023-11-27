package Blind.Sight.community.web.response;

import Blind.Sight.community.dto.common.Pages;
import Blind.Sight.community.dto.user.UserData;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserResponse {
    private Iterable<UserData> users;
    private Pages pages;
    private Long totalUsers;
    private Integer totalPages;
}
