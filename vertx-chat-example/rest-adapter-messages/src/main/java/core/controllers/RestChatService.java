package core.controllers;

import java.util.List;

import adapters.MessageRepository;
import core.models.ResponseGet;
import core.models.ResponseGet.Pagination;
import entities.ChatMessage;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;

//TODO add  integration tests or unit tests for each method
public class RestChatService extends AbstractVerticle {

	public static final String TEXT = "text";
	public static final String CHANNEL = "channel";
	public static final String AUTHOR = "author";
	public static final int PORT = 8080;
	public static final int CREATED_CORRECTLY = 201;
	public static final int ERROR_NOT_FOUND = 404;
	public static final int ERROR_BAD_REQUEST = 400;
	public static final int DELETE_CORRECTLY = 204;
	public static final int OK = 200;

	public static final String API_MESSAGES = "/api/messages";
	private static final String API_MESSAGES_ID = RestChatService.API_MESSAGES + "/:id";
	public static final String API_MESSAGES_ROUTE = RestChatService.API_MESSAGES + "*";
	private MessageRepository messagesService;

	public RestChatService(MessageRepository messagesService) {
		this.messagesService = messagesService;
	}

	@Override
	public void start(Future<Void> fut) {
		Router router = Router.router(vertx);

		// TODO add pagination
		router.route("/assets/*").handler(StaticHandler.create("assets"));

		router.get(RestChatService.API_MESSAGES).handler(this::getAll);
		router.route(API_MESSAGES_ROUTE).handler(BodyHandler.create());
		router.post(RestChatService.API_MESSAGES).handler(this::addOne);
		router.get(API_MESSAGES_ID).handler(this::getOne);
		router.put(API_MESSAGES_ID).handler(this::updateOne);
		router.delete(API_MESSAGES_ID).handler(this::deleteOne);

		vertx.createHttpServer().requestHandler(router::accept).listen(config().getInteger("http.port", PORT), result -> {
			if (result.succeeded()) {
				fut.complete();
			} else {
				fut.fail(result.cause());
			}
		});
	}

	private void addOne(RoutingContext routingContext) {
		try {
			JsonObject json = routingContext.getBodyAsJson();
			if (!itIsAChatMessage(json)) {
				routingContext.response().setStatusCode(ERROR_BAD_REQUEST).end();
				return;
			}
			ChatMessage message = new ChatMessage(json.getString(AUTHOR), json.getString(CHANNEL), json.getString(TEXT));

			messagesService.put(message);

			routingContext.response().setStatusCode(CREATED_CORRECTLY).putHeader("content-type", "application/json; charset=utf-8")
					.end(Json.encodePrettily(message));
		} catch (Exception ex) {
			routingContext.response().setStatusCode(ERROR_BAD_REQUEST).end();
			return;
		}
	}

	private boolean itIsAChatMessage(JsonObject json) {
		return (json.containsKey(AUTHOR) && json.containsKey(CHANNEL) && json.containsKey(TEXT));
	}

	private void getOne(RoutingContext routingContext) {
		final String id = routingContext.request().getParam("id");
		final Integer idAsInteger = Integer.valueOf(id);

		ChatMessage message = messagesService.get(idAsInteger);

		if (message == null) {
			routingContext.response().setStatusCode(ERROR_NOT_FOUND).end();
			return;
		}

		routingContext.response().putHeader("content-type", "application/json; charset=utf-8").end(Json.encodePrettily(message));
	}

	private void updateOne(RoutingContext routingContext) {
		final String id = routingContext.request().getParam("id");
		try {
			JsonObject json = routingContext.getBodyAsJson();
			if (!itIsAChatMessage(json)) {
				routingContext.response().setStatusCode(ERROR_BAD_REQUEST).end();
				return;
			}

			final Integer idAsInteger = Integer.valueOf(id);
			ChatMessage message = messagesService.get(idAsInteger);
			if (message == null) {
				routingContext.response().setStatusCode(ERROR_NOT_FOUND).end();
				return;
			}

			message.setAuthor(json.getString(AUTHOR));
			message.setChannel(json.getString(CHANNEL));
			message.setText(json.getString(TEXT));
			routingContext.response().putHeader("content-type", "application/json; charset=utf-8").end(Json.encodePrettily(message));

		} catch (Exception ex) {
			routingContext.response().setStatusCode(ERROR_BAD_REQUEST).end();
			return;
		}

	}

	private void deleteOne(RoutingContext routingContext) {
		String id = routingContext.request().getParam("id");
		Integer idAsInteger = Integer.valueOf(id);
		messagesService.remove(idAsInteger);
		routingContext.response().setStatusCode(DELETE_CORRECTLY).end();
	}

	private void getAll(RoutingContext routingContext) {
		String valuePage = routingContext.request().getParam("per_page");
		String valueNumber = routingContext.request().getParam("number");

		long page = Pagination.PAGE;
		page = paramToLong(valuePage, page);
		long number = Pagination.NUMBER;
		number = paramToLong(valueNumber, number);

		long offset = calculateOffset(page);

		long total = messagesService.count();

		boolean lastPage = isLastPage(number, total);
		if (!lastPage) {
			number = total;
		}

		List<ChatMessage> responseMessages = messagesService.getAll(number, offset);

		int count = responseMessages.size();
		String urlResponse = setUpNextPageURL(page, number, offset, total);
		ResponseGet responseGet = new ResponseGet(responseMessages, urlResponse, count, number, total, page);
		routingContext.response().putHeader("content-type", "application/json; charset=utf-8").end(Json.encodePrettily(responseGet));
	}

	// TODO add tests for these cases
	private String setUpNextPageURL(long page, long number, long offset, long total) {
		String urlResponse = null;
		if ((number + offset) != total) {
			long newNumber = total - number - offset;
			if (newNumber > number) {
				newNumber = number;
			}
			urlResponse = API_MESSAGES + "?next_page=" + String.valueOf(page + 1) + "&number=" + String.valueOf(newNumber);
		}
		return urlResponse;
	}

	private long calculateOffset(long page) {
		long offset = (page - 1) * Pagination.SIZE_PAGE;
		return offset;
	}

	private boolean isLastPage(long number, long total) {
		return number <= total;
	}

	private long paramToLong(String valuePage, long page) {
		if (valuePage != null) {
			try {
				page = Long.parseLong(valuePage);
			} catch (Exception e) {
				// TODO add logger exceptions
			}
		}
		return page;
	}

}
