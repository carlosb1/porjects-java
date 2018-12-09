package movieinventory.integration.dataproviders;

import movieinventory.dataproviders.FilmRepository;
import movieinventory.domain.entities.Film;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
@ComponentScan(basePackages = "movieinventory")
public class FilmRepositoryTest {
    @Autowired
    FilmRepository filmRepository;

    @Before
    public void setUp() {
        filmRepository.deleteAll();
    }

    @Test
    public void should_add_one_film_correctly() {
        Film added = filmRepository.save(new Film("Movie1", 1, Film.CategoryMovie.REGULAR));
        Assert.assertNotNull(added);
    }

    @Test
    public void should_get_one_film_correctly() {
        Film added = filmRepository.save(new Film("Movie1", 1, Film.CategoryMovie.REGULAR));
        Film restored = filmRepository.findOne(added.getId());
        Assert.assertNotNull(restored);
        Assert.assertNotNull(restored.getId());
        Assert.assertNotNull(restored.getTitle());
        Assert.assertNotNull(restored.getStock());
    }

    @Test
    public void should_list_pageable_films_with_size_of_two_elems() {
        List<Film> films = setUpFilms();
        filmRepository.save(films);

        Pageable page = new PageRequest(0, 2);
        Page<Film> pagedFilms = filmRepository.findAll(page);
        Assert.assertEquals(10, pagedFilms.getTotalElements());
        Assert.assertEquals(5, pagedFilms.getTotalPages());

    }

    private List<Film> setUpFilms() {
        List<Film> films = new ArrayList();
        for (int index = 0; index < 10; index++) {
            films.add(new Film("movie" + index, index, Film.CategoryMovie.REGULAR));
        }
        return films;
    }


}
