package entrypoints.news.dto;

/**
 * Created by carlos on 5/21/17.
 */
public class DTONews {
    private String title;
    private String description;

    public DTONews(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }
}
