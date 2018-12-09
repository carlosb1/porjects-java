package movieinventory.entrypoints.rest;

import movieinventory.domain.exceptions.FilmStockException;
import movieinventory.domain.exceptions.NotExistsCustomerException;
import movieinventory.domain.exceptions.NotExistsFilmException;
import movieinventory.domain.usecases.CalculateBonusPoints;
import movieinventory.domain.usecases.RentFilms;
import movieinventory.entrypoints.rest.dto.RequestRent;
import movieinventory.entrypoints.rest.dto.ResponseInfoRent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/v1.0/customers")
public class CustomersAPI {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    public static final String NOT_ENOUGH_STOCK_FOR_FILM = "Not enough stock for film :";
    public static final String CUSTOMER_DOESN_T_EXIST = "Customer doesn't exist";
    public static final String FILM_DOESN_T_EXIST = "Film doesn't exist";
    @Autowired
    RentFilms rentFilms;

    @Autowired
    CalculateBonusPoints calculateBonusPoints;

    @RequestMapping(value = "/{id}/rent", method = RequestMethod.PUT, consumes = {"application/json"}, produces = {"application/json"})
    public ResponseEntity<List<ResponseInfoRent>> rent(@PathVariable("id") Long id, @RequestBody List<RequestRent> infoRent) {
        List<ResponseInfoRent> rents = new ArrayList<>();
        for (RequestRent rent : infoRent) {
            try {
                Double price = rentFilms.run(rent.getIdFilm(), id, rent.getDays());
                rents.add(new ResponseInfoRent(rent.getIdFilm(), price));
            } catch (NotExistsCustomerException e) {
                log.debug("It doesn't exist customer =" + id);
                return new ResponseEntity(CUSTOMER_DOESN_T_EXIST, HttpStatus.BAD_REQUEST);
            } catch (NotExistsFilmException e) {
                log.debug("It doesn't exist film =" + rent.getIdFilm());
                return new ResponseEntity(FILM_DOESN_T_EXIST, HttpStatus.BAD_REQUEST);
            } catch (FilmStockException e) {
                log.debug("It doesn't have enough stock for film=" + rent.getIdFilm());
                return new ResponseEntity(NOT_ENOUGH_STOCK_FOR_FILM + id, HttpStatus.OK);
            }
        }
        return new ResponseEntity(rents, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}/points", method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity<Double> points(@PathVariable("id") Long id) {
        try {
            log.debug("Getting points for customer=" + id);
            Double points = calculateBonusPoints.run(id);
            return new ResponseEntity(points, HttpStatus.OK);
        } catch (NotExistsCustomerException e) {
            return new ResponseEntity(CUSTOMER_DOESN_T_EXIST, HttpStatus.BAD_REQUEST);
        }
    }
}
