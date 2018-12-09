package movieinventory.usecases;

import movieinventory.dataproviders.FilmRepository;
import movieinventory.domain.entities.Film;
import movieinventory.domain.exceptions.NotExistsFilmException;
import movieinventory.domain.usecases.CalculateRenting;
import org.junit.Assert;
import org.junit.Test;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CalculateRentingTest {
    private CalculateRenting calculateRenting;
    private FilmRepository filmRepository = mock(FilmRepository.class);

    @Test
    public void should_calculate_new_release_renting_correctly() throws NotExistsFilmException {
        calculateRenting = new CalculateRenting(filmRepository);

        when(filmRepository.findOne(any())).thenReturn(new Film("1", 1, Film.CategoryMovie.NEW_RELEASES));
        when(filmRepository.exists(any())).thenReturn(true);

        Assert.assertEquals(40., calculateRenting.run(1L, 10), 0.1);

    }

    @Test
    public void should_calculate_regular_renting_correctly() throws NotExistsFilmException {
        calculateRenting = new CalculateRenting(filmRepository);

        when(filmRepository.findOne(any())).thenReturn(new Film("1", 1, Film.CategoryMovie.REGULAR));
        when(filmRepository.exists(any())).thenReturn(true);
        Assert.assertEquals(24., calculateRenting.run(1L, 10), 0.1);

    }

    @Test
    public void should_calculate_old_renting_correctly() throws NotExistsFilmException {
        calculateRenting = new CalculateRenting(filmRepository);
        when(filmRepository.findOne(any())).thenReturn(new Film("1", 1, Film.CategoryMovie.OLD));
        when(filmRepository.exists(any())).thenReturn(true);
        Assert.assertEquals(18., calculateRenting.run(1L, 10), 0.1);
    }

}

