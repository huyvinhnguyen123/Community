package Blind.Sight.community.dto.product;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class ProductInput {
    private String name;
    private Double price;
    private String description;
    private Integer length;
    private Integer width;
    private Integer height;
    private Integer weight;
    private String categoryId;
    private String categoryName;
    private Long productCategoryId;
    private MultipartFile file;
    private String fileId;
}
