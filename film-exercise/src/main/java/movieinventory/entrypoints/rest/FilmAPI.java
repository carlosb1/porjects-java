package movieinventory.entrypoints.rest;


import movieinventory.domain.entities.Film;
import movieinventory.domain.exceptions.NotExistsFilmException;
import movieinventory.domain.usecases.CalculateRenting;
import movieinventory.domain.usecases.ListInventory;
import movieinventory.entrypoints.rest.dto.RequestCheckRent;
import movieinventory.entrypoints.rest.dto.ResponseInfoRent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1.0/films")
public class FilmAPI {

    public static final String FILM_DOESN_T_EXIST = "Film doesn't exist";
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    ListInventory listInventory;
    @Autowired
    CalculateRenting calculateRenting;


    @RequestMapping(method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity<Page<Film>> list(Pageable pageable) {
        log.debug("Listing available films for page number=" + pageable.getPageNumber() + " and size=" + pageable.getPageSize());
        Page<Film> films = listInventory.run(pageable);
        return new ResponseEntity<>(films, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}/checkrent", method = RequestMethod.PUT, consumes = {"application/json"}, produces = {"application/json"})
    public ResponseEntity<ResponseInfoRent> get(@PathVariable("id") Long id, @RequestBody RequestCheckRent infoRent) {
        try {
            log.debug("Checking rent for film=" + id);
            Double valueRent = calculateRenting.run(id, infoRent.getDays());
            ResponseInfoRent responseInfoRent = new ResponseInfoRent(id, valueRent);
            return new ResponseEntity(responseInfoRent, HttpStatus.OK);
        } catch (NotExistsFilmException e) {
            log.debug("It doesn't exist film =" + id);
            return new ResponseEntity(FILM_DOESN_T_EXIST, HttpStatus.BAD_REQUEST);
        }
    }


}
