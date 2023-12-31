package Blind.Sight.community.domain.service.write;

import Blind.Sight.community.domain.entity.*;
import Blind.Sight.community.domain.entity.many.ProductCategory;
import Blind.Sight.community.domain.entity.many.ProductImage;
import Blind.Sight.community.domain.repository.postgresql.ProductRepositoryPg;
import Blind.Sight.community.domain.repository.postgresql.ReviewRepositoryPg;
import Blind.Sight.community.domain.repository.postgresql.UserRepositoryPg;
import Blind.Sight.community.domain.repository.postgresql.query.ReviewDataForSendMail;
import Blind.Sight.community.domain.service.custom.EmailDetailsService;
import Blind.Sight.community.domain.service.many.ProductCategoryService;
import Blind.Sight.community.domain.service.many.ProductImageService;
import Blind.Sight.community.dto.email.EmailDetails;
import Blind.Sight.community.dto.product.ProductInput;
import Blind.Sight.community.util.common.DeleteFlag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServicePg {
    private final ProductRepositoryPg productRepositoryPg;
    private final ReviewRepositoryPg reviewRepositoryPg;
    private final ProductImageService productImageService;
    private final ProductCategoryService productCategoryService;
    private final ImageServicePg imageServicePg;
    private final CategoryServicePg categoryServicePg;
    private final UserRepositoryPg userRepositoryPg;
    private final EmailDetailsService emailDetailsService;

    private static final String LOG_CREATE_SUCCESS = "Save product success";
    private static final Double SERVER_DEFAULT_EXCHANGE_RATE = 24290.50;

    @Value("${drive.folder.products}")
    private String productPath;

    private void sendProductUpdateMail(String[] emails, String sku, String productName, Double price) {
        EmailDetails emailDetails = new EmailDetails();
        emailDetails.setRecipients(emails);
        emailDetails.setSubject("You have 1 notification about product's information");
        emailDetailsService.sendMultipleMailsWithTemplate(emailDetails, sku, productName, price);
    }

    public Product findProductById(String productId) {
        Product existProduct = productRepositoryPg.findById(productId).orElseThrow(
                () -> new NullPointerException("Not found this product: " + productId)
        );

        log.info("Found product");
        return existProduct;
    }

    /**
     * Calculate point base on price (1 point = 1 USD)
     *
     * @param price - get price from product
     * @return - point
     */
    public Double calculatePoint(Double price) {
        double point = (price * 1) / SERVER_DEFAULT_EXCHANGE_RATE;
        return (double) Math.round(point * 100) / 100;
    }

    public void createProduct(ProductInput productInput) throws GeneralSecurityException, IOException {
        Product product = new Product(productInput.getName(), productInput.getPrice(), productInput.getDescription());
        product.setPoint(calculatePoint(product.getPrice()));
        product.setLength(productInput.getLength());
        product.setWidth(productInput.getWidth());
        product.setHeight(productInput.getWeight());
        product.setWeight(productInput.getWeight());
        productRepositoryPg.save(product);
        log.info(LOG_CREATE_SUCCESS);

        Image image = imageServicePg.createImage(productInput.getFile(), productPath, "product");
        productImageService.createProductImage(product, image);

        Category existCategory = categoryServicePg.findCategoryById(productInput.getCategoryId());
        productCategoryService.createProductCategory(product, existCategory);

        log.info("Create product success");
    }

    public void updateProduct(String productId, ProductInput productInput) throws GeneralSecurityException, IOException {
        Product existProduct = findProductById(productId);
        existProduct.setName(productInput.getName());
        existProduct.setPrice(productInput.getPrice());
        existProduct.setPoint(calculatePoint(existProduct.getPrice()));
        existProduct.setDescription(productInput.getDescription());
        existProduct.setLength(productInput.getLength());
        existProduct.setWidth(productInput.getWidth());
        existProduct.setHeight(productInput.getWeight());
        existProduct.setWeight(productInput.getWeight());
        existProduct.setUpdateAt(LocalDate.now());
        productRepositoryPg.save(existProduct);
        log.info(LOG_CREATE_SUCCESS);

        for(ProductImage productImage: productImageService.findAll()) {
            if(productInput.getFileId().equals(productImage.getImage().getImageId())) {
                Image image = imageServicePg.createImage(productInput.getFile(), productPath,"product");
                productImageService.createProductImage(existProduct, image);
            }
        }

        Category existCategory = categoryServicePg.findCategoryById(productInput.getCategoryId());
        for(ProductCategory productCategory: productCategoryService.findAll()) {
            if(productInput.getProductCategoryId().equals(productCategory.getProductCategoryId())) {
                productCategoryService.updateProductCategory(productInput.getProductCategoryId(), existProduct, existCategory);
                log.info("Change category success");
            } else {
                productCategoryService.createProductCategory(existProduct, existCategory);
                log.info("Add more category to product success");
            }
        }

        log.info("Update product success");

        sendMailUpdateProduct(existProduct.getProductId());
    }

    private void sendMailUpdateProduct(String productId) {
        Product existProduct = findProductById(productId);
        List<ReviewDataForSendMail> existReviews = reviewRepositoryPg.findReviewByProductId(existProduct.getProductId());
        List<String> emails = new ArrayList<>();

        for(ReviewDataForSendMail data: existReviews) {
            User existUser = userRepositoryPg.findById(data.getUserId()).orElseThrow(
                    () -> new NullPointerException("Not found this user: " + data.getUserId())
            );
            log.info("Found user");

            emails.add(existUser.getEmail());
            String[] emailArray = emails.toArray(new String[0]);
            sendProductUpdateMail(
                    emailArray,
                    existProduct.getSku(),
                    existProduct.getName(),
                    existProduct.getPrice()
            );

        }
    }

    public void deleteProduct(String productId) {
        Product existProduct = findProductById(productId);
        existProduct.setOldSku(existProduct.getOldSku());
        existProduct.setSku("Not exist");
        existProduct.setDeleteFlag(DeleteFlag.DELETED.getCode());
        existProduct.setUpdateAt(LocalDate.now());
        productRepositoryPg.save(existProduct);
        log.info(LOG_CREATE_SUCCESS);
    }

    public void deleteProductAndProductRelation(String productId, String imageId,
                                                Long productImageId, Long productCategoryId) throws GeneralSecurityException, IOException {
        Product existProduct = findProductById(productId);
        existProduct.setOldSku(existProduct.getOldSku());
        existProduct.setSku("Not exist");
        existProduct.setDeleteFlag(DeleteFlag.DELETED.getCode());
        productRepositoryPg.save(existProduct);
        log.info(LOG_CREATE_SUCCESS);

        productImageService.deleteProductImage(productImageId);
        productCategoryService.deleteProductCategory(productCategoryId);
        imageServicePg.deleteImage(imageId);
    }

}
