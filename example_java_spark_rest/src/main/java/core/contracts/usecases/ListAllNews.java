package core.contracts.usecases;

import core.contracts.devices.NewsStore;
import core.models.News;

import java.util.List;

/**
 * Created by carlos on 5/21/17.
 */
public class ListAllNews {
    private NewsStore newsStore;
    public ListAllNews(NewsStore newsStore) {
        this.newsStore = newsStore;
    }

    public List<News> execute() {
        return this.newsStore.listNews();
    }

}
