package movieinventory.domain.entities;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Setter
@EqualsAndHashCode
@Entity
public class Film {


    public enum CategoryMovie {NEW_RELEASES, REGULAR, OLD}

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private Integer stock;
    private CategoryMovie category;

    public Film() {
        
    }

    public Film(String title, Integer stock, CategoryMovie category) {
        this.title = title;
        this.stock = stock;
        this.category = category;
    }
    @Override
    public String toString() {
        return "Film{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", stock=" + stock +
                ", category=" + category +
                '}';
    }

}
