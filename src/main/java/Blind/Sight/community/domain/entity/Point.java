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
@Table(name = "Points")
public class Point {
    @Id
    @Column(name = "pointId", updatable = false)
    private String pointId;
    @Column(name = "pointExchange", nullable = false)
    private Double pointExchange;
    @Column(name = "decimalPlace", nullable = false)
    private Double decimalPlace;
    @Column(name = "exchangeValue", nullable = false)
    private Double exchangeValue;

    @Column(name = "createAt")
    private LocalDate createAt;
    @Column(name = "updateAt")
    private LocalDate updateAt;

    @ManyToOne
    @JoinColumn(name = "profileId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Profile profile;

    public Point() {
        this.pointId = RandomId.generateCounterIncrement("Point-");
        this.createAt = LocalDate.now();
        this.updateAt = LocalDate.now();
    }

    public Point(Double pointExchange, Double decimalPlace, Double exchangeValue) {
        this.pointId = RandomId.generateCounterIncrement("Point-");
        this.pointExchange = pointExchange;
        this.decimalPlace = decimalPlace;
        this.exchangeValue = exchangeValue;
        this.createAt = LocalDate.now();
        this.updateAt = LocalDate.now();
    }
}
