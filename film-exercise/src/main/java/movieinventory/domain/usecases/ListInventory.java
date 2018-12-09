package movieinventory.domain.usecases;

import movieinventory.dataproviders.FilmRepository;
import movieinventory.domain.entities.Film;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class ListInventory {
    private final FilmRepository filmRepository;

    public ListInventory(FilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }

    public Page<Film> run(Pageable page) {
        return this.filmRepository.findAll(page);
    }

}
