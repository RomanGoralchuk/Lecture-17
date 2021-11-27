package by.itacademy.javaenterprise.goralchuk.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
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
    @OneToOne(mappedBy = "pet", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    private People people;

    public Pet(String animalName, PetType petType, Date birthday) {
        this.name = animalName;
        this.type = petType;
        this.birthday = birthday;
    }

    public Pet(Long id) {
        this.id = id;
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
