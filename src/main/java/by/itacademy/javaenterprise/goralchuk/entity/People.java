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
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "people")
public class People implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "people_id")
    private Long id;
    private String name;
    private String surname;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "pet_id")
    private Pet petPeople;

    public People(String name, String surname, Pet petPeople) {
        this.name = name;
        this.surname = surname;
        this.petPeople = petPeople;
    }

    @Override
    public String toString() {
        return "\n People{" +
                "Id=" + id +
                ", name='" + name + "'" +
                ", surname='" + surname + "'" +
                ", pet=" + petPeople +
                '}';
    }
}
