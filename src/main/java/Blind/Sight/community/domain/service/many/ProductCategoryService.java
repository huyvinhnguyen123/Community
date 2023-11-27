package Blind.Sight.community.domain.service.many;

import Blind.Sight.community.domain.entity.Category;
import Blind.Sight.community.domain.entity.Product;
import Blind.Sight.community.domain.entity.many.ProductCategory;
import Blind.Sight.community.domain.repository.postgresql.ProductCategoryRepositoryPg;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductCategoryService {
    private final ProductCategoryRepositoryPg productCategoryRepositoryPg;

    public Iterable<ProductCategory> findAll() {
        Iterable<ProductCategory> productCategories = productCategoryRepositoryPg.findAll();
        log.info("Find all product category success");
        return productCategories;
    }

    public ProductCategory findByProductCategoryId(Long productCategoryId) {
        ProductCategory existProductCategory = productCategoryRepositoryPg.findById(productCategoryId).orElseThrow(
                () -> new NullPointerException("Not found this product category:" + productCategoryId)
        );

        log.info("Found this product category");
        return  existProductCategory;
    }

    public void createProductCategory(Product product, Category category) {
        ProductCategory productCategory = new ProductCategory();
        productCategory.setProduct(product);
        productCategory.setCategory(category);
        productCategoryRepositoryPg.save(productCategory);

        log.info("Create product's category success");
    }

    public void updateProductCategory(Long productCategoryId, Product product, Category category) {
        ProductCategory existProductCategory = findByProductCategoryId(productCategoryId);
        existProductCategory.setProduct(product);
        existProductCategory.setCategory(category);
        productCategoryRepositoryPg.save(existProductCategory);

        log.info("Update product's category success");
    }

    public void deleteProductCategory(Long productCategoryId) {
        ProductCategory existProductCategory = findByProductCategoryId(productCategoryId);
        productCategoryRepositoryPg.delete(existProductCategory);

        log.info("Delete product's category success");
    }
}
