package core.contracts.usecases;

import core.contracts.devices.NewsStore;
import core.models.News;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by carlos on 5/20/17.
 */
public final class InsertNewNews {

    private NewsStore newsStore;
    public InsertNewNews(NewsStore newsStore) {
        this.newsStore = newsStore;
    }


    public Long execute(String title, String description)  {
        Date date = Calendar.getInstance(TimeZone.getTimeZone("Europe/Madrid")).getTime();
        newsStore.insertNews(new News(title,description,"PENDING",date));
        long id = 1;
        return id;
    }
}
