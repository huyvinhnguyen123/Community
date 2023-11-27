package Blind.Sight.community.domain.entity.address;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "Ward")
public class Ward {
    @Id
    @Column(name = "wardId", updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long wardId;
    @Column(name = "wardName", nullable = false)
    private String wardName;

    @Column(name = "createAt")
    private LocalDate createAt = LocalDate.now();
    @Column(name = "updateAt")
    private LocalDate updateAt = LocalDate.now();

    @ManyToOne
    @JoinColumn(name = "districtId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private District district;
}
