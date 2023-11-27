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
@Table(name = "District")
public class District {
    @Id
    @Column(name = "districtId", updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long districtId;
    @Column(name = "districtName", nullable = false)
    private String districtName;

    @Column(name = "createAt")
    private LocalDate createAt = LocalDate.now();
    @Column(name = "updateAt")
    private LocalDate updateAt = LocalDate.now();

    @ManyToOne
    @JoinColumn(name = "cityId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private City city;
}
