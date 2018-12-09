package movieinventory.domain.usecases;

import movieinventory.dataproviders.FilmRepository;
import movieinventory.domain.exceptions.NotExistsFilmException;
import movieinventory.domain.usecases.utils.CalculatorPrices;

public class CalculateRenting {
    private final FilmRepository filmRepository;

    public CalculateRenting(FilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }

    public Double run(Long idFilm, Integer days) throws NotExistsFilmException {

        if (!this.filmRepository.exists(idFilm)) {
            throw new NotExistsFilmException();
        }

        return new CalculatorPrices().calculate(this.filmRepository.findOne(idFilm).getCategory(), days);
    }

}
