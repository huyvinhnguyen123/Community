package Blind.Sight.community.domain.entity;

import Blind.Sight.community.util.random.RandomId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "Categories")
public class Category {
    @Id
    @Column(name = "categoryId", updatable = false)
    private String categoryId;
    @Column(name = "categoryName", unique = true, nullable = false)
    private String categoryName;

    @Column(name = "createAt")
    private LocalDate createAt;
    @Column(name = "updateAt")
    private LocalDate updateAt;

    public Category() {
        this.categoryId = RandomId.generateCounterIncrement("Category-");
        this.categoryName = "Uncategorized";
        this.createAt = LocalDate.now();
        this.updateAt = LocalDate.now();
    }

    public Category(String categoryName) {
        this.categoryId = RandomId.generateCounterIncrement("Category-");
        this.categoryName = categoryName;
        this.createAt = LocalDate.now();
        this.updateAt = LocalDate.now();
    }
}
