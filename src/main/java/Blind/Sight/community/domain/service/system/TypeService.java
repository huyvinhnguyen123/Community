package Blind.Sight.community.domain.service.system;

import Blind.Sight.community.domain.entity.Type;
import Blind.Sight.community.domain.repository.postgresql.TypeRepositoryPg;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class TypeService {
    private final TypeRepositoryPg typeRepositoryPg;

    public Type findTypeById(String id) {
        Type existType = typeRepositoryPg.findById(id).orElseThrow(
                () -> new NullPointerException("Not found this type: " + id)
        );

        log.info("Found type");
        return existType;
    }

    public Type findTypeByName(String name) {
        Optional<Type> type = typeRepositoryPg.findByTypeName(name);
        return type.orElseThrow(() -> new NullPointerException("Not found this type: " + name));
    }

    public void createDefaultType() {
        Type user = new Type("user");
        Type category = new Type("category");
        Type product = new Type("product");
        Type city = new Type("city");
        Type district = new Type("district");
        Type ward = new Type("ward");
        Type file = new Type("file");

        List<Type> types = new ArrayList<>();
        types.add(user);
        types.add(category);
        types.add(product);
        types.add(city);
        types.add(district);
        types.add(ward);
        types.add(file);

        typeRepositoryPg.saveAll(types);
        log.info("Create system default types success!");
    }

    public void createCustomType(String name) {
        Type type = new Type(name);
        typeRepositoryPg.save(type);
        log.info("Create type success!");
    }

    public void deleteType(String id) {
        Type existType = findTypeById(id);
        typeRepositoryPg.delete(existType);
        log.info("Delete type success!");
    }
}
