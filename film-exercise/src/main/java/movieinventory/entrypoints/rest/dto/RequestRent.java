package movieinventory.entrypoints.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RequestRent {
    private Long idFilm;
    private Integer days;
}
