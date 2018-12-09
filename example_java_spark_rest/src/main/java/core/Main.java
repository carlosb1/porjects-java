package core;

import core.contracts.devices.NewsStore;
import core.contracts.usecases.InsertNewNews;
import core.contracts.usecases.ListAllNews;
import devices.dataprovider.NewsMongoStore;
import entrypoints.chat.ChatWebSocketHandler;
import entrypoints.images.ImagesREST;
import entrypoints.news.NewsREST;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONObject;
import org.eclipse.jetty.websocket.api.Session;

import static j2html.TagCreator.*;
import static spark.Spark.*;


/**
 * Created by carlos on 5/21/17.
 */
/*

    private static final String MYSQL_USERNAME = "expertuser";
    private static final String MYSQL_PWD = "expertuser123";
    private static final String MYSQL_CONNECTION_URL =
            "jdbc:mysql://localhost:3306/employees?user=" + MYSQL_USERNAME + "&password=" + MYSQL_PWD;
 */



public class Main {
    // this map is shared between sessions and threads, so it needs to be thread-safe (http://stackoverflow.com/a/2688817)
    public static Map<Session, String> userUsernameMap = new ConcurrentHashMap<>();
    public static int nextUserNumber = 1; //Used for creating the next username

    public static void main(String args[]) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
         /* start chat */
        executor.submit(() -> {
            staticFileLocation("/public"); //index.html is served at localhost:4567 (default port)
            webSocket("/chat", ChatWebSocketHandler.class);
            init();
        });
        /* start rest service news */
        executor.submit(() -> {
            NewsStore newsStore = new NewsMongoStore("localhost");
            new NewsREST(new InsertNewNews(newsStore), new ListAllNews(newsStore));
        });

        /* start rest service images*/
        executor.submit(() -> {
            new ImagesREST();
        });
    }

    //Sends a message from one user to all users, along with a list of current usernames
    public static void broadcastMessage(String sender, String message) {
        userUsernameMap.keySet().stream().filter(Session::isOpen).forEach(session -> {
            try {
                session.getRemote().sendString(String.valueOf(new JSONObject()
                        .put("userMessage", createHtmlMessageFromSender(sender, message))
                        .put("userlist", userUsernameMap.values())
                ));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    //Builds a HTML element with a sender-name, a message, and a timestamp,
    private static String createHtmlMessageFromSender(String sender, String message) {
        return article(
                b(sender + " says:"),
                span(attrs(".timestamp"), new SimpleDateFormat("HH:mm:ss").format(new Date())),
                p(message)
        ).render();
    }
}
