package Blind.Sight.community.domain.entity;

import Blind.Sight.community.util.random.RandomId;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "Types")
public class Type {
    @Id
    @Column(name = "typeId", updatable = false)
    private String typeId;
    @Column(name = "typeName", unique = true, nullable = false)
    private String typeName;

    @Column(name = "createAt")
    private LocalDate createAt;
    @Column(name = "updateAt")
    private LocalDate updateAt;

    public Type() {
        this.typeId = RandomId.generateCounterIncrement("Type-");
        this.createAt = LocalDate.now();
        this.updateAt = LocalDate.now();
    }

    public Type(String name) {
        this.typeId = RandomId.generateCounterIncrement("Type-");
        this.typeName = name;
        this.createAt = LocalDate.now();
        this.updateAt = LocalDate.now();
    }
}
