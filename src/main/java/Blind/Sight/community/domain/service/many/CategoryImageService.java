package Blind.Sight.community.domain.service.many;

import Blind.Sight.community.domain.entity.Category;
import Blind.Sight.community.domain.entity.Image;
import Blind.Sight.community.domain.entity.many.CategoryImage;
import Blind.Sight.community.domain.repository.postgresql.CategoryImageRepositoryPg;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryImageService {
    private final CategoryImageRepositoryPg categoryImageRepositoryPg;

    public Iterable<CategoryImage> findAll() {
        Iterable<CategoryImage> categoryImages = categoryImageRepositoryPg.findAll();
        log.info("Find all category image success");
        return categoryImages;
    }

    public CategoryImage findCategoryImageById(Long categoryImageId) {
        CategoryImage existCategoryImage = categoryImageRepositoryPg.findById(categoryImageId).orElseThrow(
                () -> new NullPointerException("Not found this category image: " + categoryImageId)
        );

        log.info("Found category image");
        return existCategoryImage;
    }

    public void createCategoryImage(Category category, Image image) {
        CategoryImage categoryImage = new CategoryImage();
        categoryImage.setCategory(category);
        categoryImage.setImage(image);
        categoryImageRepositoryPg.save(categoryImage);

        log.info("Create category's image success");
    }

    public void updateCategoryImage(Long categoryImageId, Category category, Image image) {
        CategoryImage existCategoryImage = findCategoryImageById(categoryImageId);
        existCategoryImage.setCategory(category);
        existCategoryImage.setImage(image);
        categoryImageRepositoryPg.save(existCategoryImage);

        log.info("Update category's image success");
    }

    public void deleteCategoryImage(Long categoryImageId) {
        CategoryImage existCategoryImage = findCategoryImageById(categoryImageId);
        categoryImageRepositoryPg.delete(existCategoryImage);

        log.info("Delete category's image success");
    }
}
