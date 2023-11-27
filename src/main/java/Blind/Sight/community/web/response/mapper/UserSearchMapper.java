package Blind.Sight.community.web.response.mapper;

import Blind.Sight.community.dto.common.Pages;
import Blind.Sight.community.dto.user.UserDataForUser;
import Blind.Sight.community.web.response.UserSearchResponse;
import org.springframework.data.domain.Pageable;

public class UserSearchMapper {
    private UserSearchMapper(){}
    public static UserSearchResponse mapToUserSearch(Iterable<UserDataForUser> userDataList, Pageable pageable, Long totalUsers, Integer totalPages) {
        Pages pages = new Pages();
        pages.setNumber(pageable.getPageNumber());
        pages.setSize(pageable.getPageSize());
        pages.setSort(pageable.getSort());

        return UserSearchResponse.builder()
                .users(userDataList)
                .pages(pages)
                .totalUsers(totalUsers)
                .totalPages(totalPages)
                .build();
    }
}
