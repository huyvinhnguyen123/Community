package Blind.Sight.community.domain.entity.address;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "City")
public class City {
    @Id
    @Column(name = "cityId", updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cityId;
    @Column(name = "cityName", nullable = false)
    private String cityName;

    @Column(name = "createAt")
    private LocalDate createAt = LocalDate.now();
    @Column(name = "updateAt")
    private LocalDate updateAt = LocalDate.now();
}
