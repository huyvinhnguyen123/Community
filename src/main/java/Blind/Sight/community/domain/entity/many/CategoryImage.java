package Blind.Sight.community.domain.entity.many;

import Blind.Sight.community.domain.entity.Category;
import Blind.Sight.community.domain.entity.Image;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Category_Images")
public class CategoryImage {
    @Id
    @Column(name = "categoryImageId", updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryImageId;

    @ManyToOne
    @JoinColumn(name = "categoryId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "imageId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Image image;
}
