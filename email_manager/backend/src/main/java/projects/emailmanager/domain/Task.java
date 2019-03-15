package projects.emailmanager.domain;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@RequiredArgsConstructor
public class Task {
    private String text;

    public Task(String text) {
        this.text  = text;
    }
}
