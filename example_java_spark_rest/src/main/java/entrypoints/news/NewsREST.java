package entrypoints.news;

import core.contracts.usecases.InsertNewNews;
import core.contracts.usecases.ListAllNews;
import entrypoints.news.dto.ContextResponse;

import static entrypoints.news.JsonUtil.json;
import static entrypoints.news.JsonUtil.toJson;
import static spark.Spark.*;

/**
 * Created by carlos on 5/16/17.
 */
public class NewsREST {
    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";
    private final InsertNewNews newNews;
    private final ListAllNews listAllNews;

    public NewsREST(final InsertNewNews newNews, final ListAllNews listAllNews) {
        this.newNews = newNews;
        this.listAllNews = listAllNews;

        post("/news", (req, res) -> {
            String title = req.queryParams(NewsREST.TITLE);
            String description = req.queryParams(NewsREST.DESCRIPTION);
            Long identifier = newNews.execute(title, description);
            return identifier.toString();
        }, json());

        get("/news", (req, res) -> {
            return ContextResponse.makeInstance(listAllNews.execute());
        }, json());

        after((req, res) -> {
            res.type("application/json");
        });

        exception(IllegalArgumentException.class, (e, req, res) -> {
            res.status(400);
            res.body(toJson(new ResponseError(e)));
        });
    }
}
