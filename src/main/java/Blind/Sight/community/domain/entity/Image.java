package Blind.Sight.community.domain.entity;

import Blind.Sight.community.util.random.RandomId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "Images")
public class Image {
    @Id
    @Column(name = "imageId", updatable = false)
    private String imageId;
    @Column(name = "imagePathId", nullable = false)
    private String imagePathId;
    @Column(name = "imageName", nullable = false)
    private String imageName;
    @Column(name = "imagePath", nullable = false)
    private String imagePath;
    @Column(name = "timeUpload", nullable = false)
    private LocalDate timeUpload;

    @Column(name = "createAt")
    private LocalDate createAt;
    @Column(name = "updateAt")
    private LocalDate updateAt;

    @ManyToOne
    @JoinColumn(name = "typeId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Type type;

    public Image() {
        this.imageId = "Image-"+RandomId.generateUUIDString(10);
        this.timeUpload = LocalDate.now();
        this.createAt = LocalDate.now();
        this.updateAt = LocalDate.now();
    }

    public Image(String pathId, String name, String path) {
        this.imageId = "Image-"+RandomId.generateUUIDString(10);
        this.imagePathId = pathId;
        this.imageName = name;
        this.imagePath = path;
        this.timeUpload = LocalDate.now();
        this.createAt = LocalDate.now();
        this.updateAt = LocalDate.now();
    }
}
