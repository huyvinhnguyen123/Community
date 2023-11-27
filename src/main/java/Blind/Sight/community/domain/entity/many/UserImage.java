package Blind.Sight.community.domain.entity.many;

import Blind.Sight.community.domain.entity.Image;
import Blind.Sight.community.domain.entity.User;
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
@Table(name = "User_Images")
public class UserImage {
    @Id
    @Column(name = "userImageId", updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userImageId;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @ManyToOne
    @JoinColumn(name = "imageId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Image image;
}