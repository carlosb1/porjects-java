package movieinventory.domain.usecases;

import movieinventory.dataproviders.CustomerRepository;
import movieinventory.dataproviders.FilmRepository;
import movieinventory.domain.entities.Customer;
import movieinventory.domain.entities.Film;
import movieinventory.domain.entities.Rent;
import movieinventory.domain.exceptions.FilmStockException;
import movieinventory.domain.exceptions.NotExistsCustomerException;
import movieinventory.domain.exceptions.NotExistsFilmException;
import movieinventory.domain.usecases.utils.CalculatorPrices;
import movieinventory.domain.usecases.utils.ConstantFilms;

import java.util.Calendar;
import java.util.List;

public class RentFilms {
    private final CustomerRepository customerRepository;
    private final FilmRepository filmRepository;

    public RentFilms(CustomerRepository customerRepository, FilmRepository filmRepository) {
        this.customerRepository = customerRepository;
        this.filmRepository = filmRepository;
    }

    public Double run(Long idFilm, Long idCustomer, Integer days) throws NotExistsCustomerException, NotExistsFilmException, FilmStockException {

        if (!this.customerRepository.exists(idCustomer)) {
            throw new NotExistsCustomerException();
        }
        if (!this.filmRepository.exists(idFilm)) {
            throw new NotExistsFilmException();
        }

        Film film = this.filmRepository.findOne(idFilm);
        if (film.getStock() <= ConstantFilms.MINIM_STOCK) {
            throw new FilmStockException(film.getTitle(), film.getStock());
        }

        Double price = new CalculatorPrices().calculate(film.getCategory(), days);

        Customer customer = this.customerRepository.findOne(idCustomer);
        film.setStock(film.getStock() - 1);

        List<Rent> rents = customer.getRents();
        rents.add(new Rent(Calendar.getInstance().getTime(), days, customer, film));
        return price;

    }


}
