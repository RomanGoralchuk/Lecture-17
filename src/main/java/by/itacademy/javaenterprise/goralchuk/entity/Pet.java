package by.itacademy.javaenterprise.goralchuk.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "pet")
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pet_id")
    private Long id;
    private String name;
    private PetType type;
    @Temporal(TemporalType.DATE)
    private Date birthday;
    @OneToOne(mappedBy = "petPeople", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    private People master;

    public Pet(String animalName, PetType petType, Date birthday) {
        this.name = animalName;
        this.type = petType;
        this.birthday = birthday;
    }

    @Override
    public String toString() {
        return "Pet{" +
                "Id=" + id +
                ", PetName='" + name + "'" +
                ", PetType=" + type +
                ", PetBirthday=" + birthday +
                "}";
    }
}
