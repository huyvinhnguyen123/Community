package Blind.Sight.community.domain.entity;

import Blind.Sight.community.util.random.RandomId;
import Blind.Sight.community.util.random.RandomString;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "Products")
public class Product {
    @Id
    @Column(name = "productId", updatable = false)
    private String productId;
    @Column(name = "sku", unique = true, nullable = false)
    private String sku;
    @Column(name = "name", unique = true, nullable = false)
    private String name;
    @Column(name = "price", nullable = false)
    private Double price;
    @Column(name = "point", nullable = false)
    private Double point;
    @Column(name = "description", nullable = false)
    private String description;
    @Column(name = "datePost", nullable = false)
    private LocalDate datePost;

    // product appearance
    @Column
    private Integer length;
    @Column
    private Integer width;
    @Column
    private Integer height;
    @Column
    private Integer weight;

    // for prepare deleting product
    @Column(name = "deleteFlag")
    private int deleteFlag = 0;
    @Column(name = "oldSku")
    private String oldSku;

    @Column(name = "createAt")
    private LocalDate createAt;
    @Column(name = "updateAt")
    private LocalDate updateAt;

    public Product() {
        this.productId = RandomId.generateCounterIncrement("Product-");
        this.sku = "SKU-" + RandomString.generateRandomString(10);
        this.datePost = LocalDate.now();
        this.createAt = LocalDate.now();
        this.updateAt = LocalDate.now();
    }

    public Product(String name, Double price, String description) {
        this.productId = RandomId.generateCounterIncrement("Product-");
        this.sku = "SKU-" + RandomString.generateRandomString(10);
        this.name = name;
        this.price = price;
        this.description = description;
        this.datePost = LocalDate.now();
        this.createAt = LocalDate.now();
        this.updateAt = LocalDate.now();
    }
}
