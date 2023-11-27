package Blind.Sight.community.dto.category;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class CategoryInput {
    private String name;
    private MultipartFile file;
    private String fileId;
}
