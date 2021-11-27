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
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "people")
public class People {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "people_id")
    private Long id;
    private String name;
    private String surname;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "pet_id")
    private Pet pet;

    public People(String name, String surname, Pet pet) {
        this.name = name;
        this.surname = surname;
        this.pet = pet;
    }

    public People(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "\n People{" +
                "Id=" + id +
                ", name='" + name + "'" +
                ", surname='" + surname + "'" +
                ", pet=" + pet +
                '}';
    }
}
