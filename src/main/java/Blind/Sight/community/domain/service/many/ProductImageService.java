package Blind.Sight.community.domain.service.many;

import Blind.Sight.community.domain.entity.Image;
import Blind.Sight.community.domain.entity.Product;
import Blind.Sight.community.domain.entity.many.ProductImage;
import Blind.Sight.community.domain.repository.postgresql.ProductImageRepositoryPg;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductImageService {
    private final ProductImageRepositoryPg productImageRepositoryPg;

    public Iterable<ProductImage> findAll() {
        Iterable<ProductImage> productImages = productImageRepositoryPg.findAll();
        log.info("Find all product image success");
        return productImages;
    }

    public ProductImage findProductImageById(Long productImageId) {
        ProductImage existProductImage = productImageRepositoryPg.findById(productImageId).orElseThrow(
                () -> new NullPointerException("Not found this product image: " + productImageId)
        );

        log.info("Found category image");
        return existProductImage;
    }

    public void createProductImage(Product product, Image image) {
        ProductImage productImage = new ProductImage();
        productImage.setProduct(product);
        productImage.setImage(image);
        productImageRepositoryPg.save(productImage);

        log.info("Create product's image success");
    }

    public void updateProductImage(Long productImageId, Product product, Image image) {
        ProductImage existProductImage = findProductImageById(productImageId);
        existProductImage.setProduct(product);
        existProductImage.setImage(image);
        productImageRepositoryPg.save(existProductImage);

        log.info("Update product's image success");
    }

    public void deleteProductImage(Long productImageId) {
        ProductImage existProductImage = findProductImageById(productImageId);
        productImageRepositoryPg.delete(existProductImage);

        log.info("Delete product's image success");
    }
}
