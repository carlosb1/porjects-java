package devices.dataprovider;

import core.models.News;
import org.junit.Test;

import java.util.Date;
import java.util.List;

import static junit.framework.TestCase.assertTrue;

/**
 * Created by carlos on 5/21/17.
 */
public class NewsMongoStoreTest {
    @Test
    public void givenAnEmptyMongoStoreWhenAddOneNewThenOk() {
        NewsMongoStore mongoStore = new NewsMongoStore("localhost");
        News news = new News("title1","description1","status", new Date(1,1,1));
        mongoStore.insertNews(news);
        List<News> savedNews = mongoStore.listNews();
        assertTrue(savedNews.size()> 0 );
    }
}
