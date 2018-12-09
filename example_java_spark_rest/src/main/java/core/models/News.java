package core.models;

import java.util.Date;

public class News {
    private String title;
    private String description;
    private String status;
    private Date timestamp;
    public News(String title, String description, String status, Date timestamp) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.timestamp = timestamp;
    }


    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public Object getTimestamp() {
        return timestamp;
    }
}
