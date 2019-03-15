package projects.emailmanager.entrypoints;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import projects.emailmanager.domain.Task;

import java.util.Arrays;
import java.util.List;


@RestController
@RequestMapping("/api/v1.0/tasks")
public class TaskRestController {
    @RequestMapping(method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity<List> list(SpringDataWebProperties.Pageable pageable) {
        return new ResponseEntity<>(Arrays.asList(new Task("text1")), HttpStatus.OK);
    }
}
