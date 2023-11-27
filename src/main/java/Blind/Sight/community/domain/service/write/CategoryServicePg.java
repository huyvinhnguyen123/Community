package Blind.Sight.community.domain.service.write;

import Blind.Sight.community.domain.entity.Category;
import Blind.Sight.community.domain.entity.Image;
import Blind.Sight.community.domain.entity.many.CategoryImage;
import Blind.Sight.community.domain.repository.postgresql.CategoryRepositoryPg;
import Blind.Sight.community.domain.service.many.CategoryImageService;
import Blind.Sight.community.dto.category.CategoryInput;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDate;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryServicePg {
    private final CategoryRepositoryPg categoryRepositoryPg;
    private final ImageServicePg imageServicePg;
    private final CategoryImageService categoryImageService;
    @Value("${drive.folder.categories}")
    private String categoryPath;

    public Category findCategoryById(String categoryId) {
        Category existCategory = categoryRepositoryPg.findById(categoryId).orElseThrow(
                () -> new NullPointerException("Not found this category: " + categoryId)
        );

        log.info("Found category");
        return existCategory;
    }


    public void createCategory(CategoryInput categoryInput) throws GeneralSecurityException, IOException {
        Category category = new Category(categoryInput.getName());
        log.info("Save category success");
        categoryRepositoryPg.save(category);

        Image image = imageServicePg.createImage(categoryInput.getFile(), categoryPath, "category");
        categoryImageService.createCategoryImage(category, image);

        log.info("Create category success");
    }

    public void updateCategory(String categoryId, CategoryInput categoryInput) throws GeneralSecurityException, IOException {
        Category existCategory = findCategoryById(categoryId);
        existCategory.setCategoryName(categoryInput.getName());

        for(CategoryImage categoryImage: categoryImageService.findAll()) {
            if(categoryInput.getFileId().equals(categoryImage.getImage().getImageId())) {
                imageServicePg.updateReplaceImage(categoryInput.getFileId(), categoryInput.getFile(), categoryPath);
            }
        }

        existCategory.setUpdateAt(LocalDate.now());
        categoryRepositoryPg.save(existCategory);
        log.info("Update category success");
    }

    public void deleteCategory(String categoryId, String imageId, Long categoryImageId) throws GeneralSecurityException, IOException {
        Category existCategory = findCategoryById(categoryId);
        CategoryImage existCategoryImage = categoryImageService.findCategoryImageById(categoryImageId);
        categoryImageService.deleteCategoryImage(existCategoryImage.getCategoryImageId());
        imageServicePg.deleteImage(imageId);
        categoryRepositoryPg.delete(existCategory);
    }
}
