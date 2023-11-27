package Blind.Sight.community.domain.entity.temporary;

import Blind.Sight.community.domain.entity.Type;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "Files")
public class File {
    @Id
    @Column(name = "fileId", updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileId;
    @Column(name = "fileName", nullable = false)
    private String fileName;
    @Column(name = "fileSize", nullable = false)
    private Long fileSize;
    @Column(name = "linkDownload", nullable = false)
    private String linkDownload;
    @Column(name = "datePost", nullable = false)
    private LocalDate datePost;

    @ManyToOne
    @JoinColumn(name = "typeId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Type type;

    public File() {
        this.datePost = LocalDate.now();
    }

    public File(String name, Long size, String linkDownload) {
        this.fileName = name;
        this.fileSize = size;
        this.linkDownload = linkDownload;
        this.datePost = LocalDate.now();
    }

}
