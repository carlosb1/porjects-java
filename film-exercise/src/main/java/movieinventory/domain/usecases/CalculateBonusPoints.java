package movieinventory.domain.usecases;

import movieinventory.dataproviders.CustomerRepository;
import movieinventory.domain.entities.Film;
import movieinventory.domain.entities.Rent;
import movieinventory.domain.exceptions.NotExistsCustomerException;

import java.util.List;

public class CalculateBonusPoints {
    private final CustomerRepository customerRepository;

    public CalculateBonusPoints(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Double run(Long idCostumer) throws NotExistsCustomerException {
        if (!this.customerRepository.exists(idCostumer)) {
            throw new NotExistsCustomerException();
        }
        List<Rent> rents = this.customerRepository.findOne(idCostumer).getRents();
        Double bonus = 0.;
        for (Rent rent : rents) {
            if (rent.getFilm().getCategory() == Film.CategoryMovie.NEW_RELEASES) {
                bonus += 2;
            } else {
                bonus += 1;
            }
        }
        return bonus;
    }


}
