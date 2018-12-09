package movieinventory.dataproviders;

import movieinventory.domain.entities.Film;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface FilmRepository extends PagingAndSortingRepository<Film, Long> {
    @Override
    Film findOne(Long id);

    @Override
    boolean exists(Long id);

    @Override
    Page<Film> findAll(Pageable page);


    @Override
    void deleteAll();


}
