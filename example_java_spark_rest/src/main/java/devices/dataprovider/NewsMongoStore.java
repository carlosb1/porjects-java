package devices.dataprovider;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import core.contracts.devices.NewsStore;
import core.models.News;
import org.bson.Document;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class NewsMongoStore implements NewsStore {

    private static class NewsRepository {
        private static String COLLECTION_NAME = "news";
        private MongoDatabase database;
        private MongoCollection<Document> collection;
        private NewsRepository(MongoDatabase database) {
            collection = database.getCollection(COLLECTION_NAME);
        }

        private final String TITLE = "title";
        private final String DESCRIPTION = "description";
        private final String STATUS = "status";
        private final String TIMESTAMP = "timestamp";

        private final DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");

        public void create(News news) {
            Document doc = new Document();
            doc.append(TITLE,news.getTitle());
            doc.append(STATUS,news.getStatus());
            doc.append(DESCRIPTION,news.getDescription());
            doc.append(TIMESTAMP, news.getTimestamp());
            collection.insertOne(doc);
        }

        public List<News> list() {
            List<News> news = new ArrayList<News>();
            FindIterable<Document> cursor = collection.find();
            for (Document doc: cursor) {
                Date date = (Date)doc.get(TIMESTAMP);
                news.add(new News((String)doc.get(TITLE),(String)doc.get(DESCRIPTION),(String)doc.get(STATUS),date));
            }
          return news;
        }


    }


    private final MongoClient client;
    private final MongoDatabase database;
    private String NAME_DATABASE = "mydb";
    private final NewsRepository newsRepository;

    public NewsMongoStore(String addressServer ) {
            client = new MongoClient(addressServer);
            database = client.getDatabase(NAME_DATABASE);
            newsRepository = new NewsRepository(database);
    }

    @Override
    public void insertNews(News news) {
        newsRepository.create(news);
    }

    @Override
    public List<News> listNews() {
        return newsRepository.list();
    }

}
