package movieinventory.domain.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
@Entity
public class Rent {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Date createdDate;
    private Integer days;
    @ManyToOne(fetch = FetchType.LAZY)
    private Customer customer;
    @ManyToOne
    private Film film;

    public Rent() {

    }

    public Rent(Date createdDate, Integer days, Customer customer, Film film) {
        this.createdDate = createdDate;
        this.days = days;
        this.customer = customer;
        this.film = film;
    }


    @Override
    public String toString() {
        return "Rent{" +
                "id=" + id +
                ", createdDate=" + createdDate +
                ", days=" + days +
                ", customer=" + customer +
                ", film=" + film +
                '}';
    }
}
