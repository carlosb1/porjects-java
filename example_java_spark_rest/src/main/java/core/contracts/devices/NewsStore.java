package core.contracts.devices;

import core.models.News;

import java.util.List;

/**
 * Created by carlos on 5/21/17.
 */
public interface NewsStore {

    void insertNews(News news);

    List<News> listNews();
}
