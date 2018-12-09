package entrypoints.images;

import entrypoints.news.ResponseError;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.Part;

import static entrypoints.news.JsonUtil.json;
import static entrypoints.news.JsonUtil.toJson;
import static spark.Spark.*;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;

/**
 * Created by carlos on 6/11/17.
 */
public class ImagesREST {
    public static final String OK = "ok";
    private static String LOCATION = "image";          // the directory LOCATION where files will be stored
    private static long MAXFILESIZE = 100000000;       // the maximum size allowed for uploaded files
    private static long MAXREQUESTSIZE = 100000000;    // the maximum size allowed for multipart/form-data requests
    private static int FILESIZETHRESHOLD = 1024;       // the size threshold after which files will be written to disk

    public ImagesREST() {
        post("/upload", "multipart/form-data", (request, response) -> {



            MultipartConfigElement multipartConfigElement = new MultipartConfigElement(
                    LOCATION, MAXFILESIZE, MAXREQUESTSIZE, FILESIZETHRESHOLD);
            request.raw().setAttribute("org.eclipse.jetty.multipartConfig",
                    multipartConfigElement);

            Collection<Part> parts = request.raw().getParts();

            String fName = request.raw().getPart("upfile").getSubmittedFileName();
            Part uploadedFile = request.raw().getPart("upfile");
            if (!Paths.get("image/").toFile().exists())  {
                Paths.get("image/").toFile().mkdirs();
            }

            Path out = Paths.get("image/" + fName);
            if (out.toFile().exists())  {
                out.toFile().delete();
            }
            try (final InputStream in = uploadedFile.getInputStream()) {
                Files.copy(in, out);
                uploadedFile.delete();
            }

            return Arrays.asList(OK);

        },json());

        after((req, res) -> {
            res.type("application/json");
        });

        exception(IllegalArgumentException.class, (e, req, res) -> {
            res.status(400);
            res.body(toJson(new ResponseError(e)));
        });
    }
}

