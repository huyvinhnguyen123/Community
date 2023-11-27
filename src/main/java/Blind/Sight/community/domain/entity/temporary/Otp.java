package Blind.Sight.community.domain.entity.temporary;

import Blind.Sight.community.domain.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalTime;

@Getter
@Setter
@Entity
@Table(name = "Otps")
public class Otp {
    @Id
    @Column(name = "otpId", updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long otpId;
    @Column(name = "code")
    private String code;
    @Column(name = "timeCreated")
    private LocalTime timeCreated;
    @Column(name = "timeDeleted")
    private LocalTime timeDeleted;
    @Column(name = "timeSpecify")
    private Integer timeSpecify;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    public Otp() {
        this.timeCreated = LocalTime.now();
        this.timeDeleted = LocalTime.now().plusMinutes(5);
    }
}
