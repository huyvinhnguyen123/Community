package Blind.Sight.community.domain.repository.postgresql;

import Blind.Sight.community.domain.entity.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TypeRepositoryPg extends JpaRepository<Type, String> {
    Optional<Type> findByTypeName(String typeName);

    @Query(value = """
            ALTER TABLE types
            RENAME COLUMN type_id TO typeId,
            RENAME COLUMN typename TO typeName
            """,nativeQuery = true)
    void alterTable();
}
