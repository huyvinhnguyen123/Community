package Blind.Sight.community.web.response.mapper;

import Blind.Sight.community.dto.common.Pages;
import Blind.Sight.community.dto.user.UserData;
import Blind.Sight.community.web.response.UserResponse;
import org.springframework.data.domain.Pageable;

public class UserMapper {
    private UserMapper() {}
    public static UserResponse mapToUser(Iterable<UserData> userDataList, Pageable pageable, Long totalUsers, Integer totalPages) {
        Pages pages = new Pages();
        pages.setNumber(pageable.getPageNumber());
        pages.setSize(pageable.getPageSize());
        pages.setSort(pageable.getSort());

        return UserResponse.builder()
                .users(userDataList)
                .pages(pages)
                .totalUsers(totalUsers)
                .totalPages(totalPages)
                .build();
    }
}
