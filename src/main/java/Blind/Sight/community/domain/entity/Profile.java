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
@Table(name = "Profiles")
public class Profile {
    @Id
    @Column(name = "profileId", updatable = false)
    private String profileId;
    @Column(name = "dateJoin", nullable = false)
    private LocalDate dateJoin;
    @Column(name = "phone", unique = true)
    private String phone;
    @Column(name = "totalPoint", nullable = false)
    private Double totalPoint;

    @Column(name = "createAt")
    private LocalDate createAt;
    @Column(name = "updateAt")
    private LocalDate updateAt;

    @OneToOne
    @JoinColumn(name = "userId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    public Profile() {
        this.profileId = RandomId.generateCounterIncrement("Profile-");
        this.dateJoin = LocalDate.now();
        this.createAt = LocalDate.now();
        this.updateAt = LocalDate.now();
    }
}
