package Blind.Sight.community.domain.repository.postgresql;

import Blind.Sight.community.domain.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepositoryPg extends JpaRepository<Profile, String> {
    @Query(value = """
            SELECT * FROM profiles p
            WHERE p.userid = ?1
            """, nativeQuery = true)
    Optional<Profile> findByUserId(String userId);
}
