package Blind.Sight.community.domain.repository.mysql;

import Blind.Sight.community.domain.entity.User;
import Blind.Sight.community.domain.repository.mysql.query.UserDataForSearching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepositoryMy extends JpaRepository<User, String> {
    @Query(value = """
                    SELECT * FROM Users
            """,
            countQuery = """
                    SELECT COUNT(*) FROM Users
            """,
            nativeQuery = true)
    Page<User> findAllUser(Pageable pageable);

    @Query(value = """
                    SELECT u.userId,u.userName,u.birthDate,u.email,
                      i.imageId,i.imageName,i.imagePath
                      FROM Users u
                      LEFT JOIN User_Images ui ON u.userId = ui.userId
                      LEFT JOIN Images i ON i.imageId = ui.imageId
                      WHERE u.role='ROLE_USER'
                      AND (
                      (u.username IS NULL OR u.userName LIKE %:username%)
                      OR
                      (u.email IS NULL OR u.email LIKE %:email%)
                      )
            """,
            countQuery = """
                    SELECT COUNT(u.userId)
                      FROM Users u
                      LEFT JOIN User_Images ui ON u.userId = ui.userId
                      LEFT JOIN Images i ON i.imageId = ui.imageId
                      WHERE u.role='ROLE_USER'
                      AND (
                      (u.username IS NULL OR u.userName LIKE %:username%)
                      OR
                      (u.email IS NULL OR u.email LIKE %:email%)
                      )
            """,
            nativeQuery = true)
    Page<UserDataForSearching> searchListOfUser(@Param("username") String username,
                                                @Param("email") String email,
                                                Pageable pageable);
}
