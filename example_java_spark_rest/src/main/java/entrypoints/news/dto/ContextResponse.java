package entrypoints.news.dto;

import core.models.News;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by carlos on 5/21/17.
 */
public class ContextResponse<T> {
    private T data;

    private String next;
    private String before;
    private int index;

    public ContextResponse(T data, int index) {
        this.data = data;
        this.index = index;
        this.next = new String();
        this.before = new String();
    }


    public int getIndex() {
        return index;
    }

    public String getBefore() {
        return before;
    }

    public String getNext() {
        return next;
    }

    public static ContextResponse makeInstance(List<News> foundNews) {
        List<DTONews> dtoNews = new ArrayList<DTONews>();
        for (News news: foundNews) {
            dtoNews.add(new DTONews(news.getTitle(),news.getDescription()));
        }
        return new ContextResponse(foundNews,foundNews.size());
    }
}
