package Blind.Sight.community.domain.entity.address;

import Blind.Sight.community.domain.entity.Profile;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Addresses")
public class Address {
    @Id
    @Column(name = "addressId", updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;
    @Column(name = "addressName", nullable = false)
    private String addressName;

    @OneToOne
    @JoinColumn(name = "cityId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private City city;

    @OneToOne
    @JoinColumn(name = "districtId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private District district;

    @OneToOne
    @JoinColumn(name = "wardId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Ward ward;

    @ManyToOne
    @JoinColumn(name = "profileId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Profile profile;
}
