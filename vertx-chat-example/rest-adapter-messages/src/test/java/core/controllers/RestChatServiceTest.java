package core.controllers;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import adapters.MessageRepository;
import adapters.MessagesInMemory;
import entities.ChatMessage;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClientResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;

@RunWith(VertxUnitRunner.class)
public class RestChatServiceTest {

	private Vertx vertx;
	private Integer port;
	private RestChatService chatService;
	private MessageRepository messageService;

	@Before
	public void setUp(TestContext context) throws IOException {
		vertx = Vertx.vertx();
		ServerSocket socket = new ServerSocket(0);
		port = socket.getLocalPort();
		socket.close();

		messageService = new MessagesInMemory();
		chatService = new RestChatService(messageService);

		DeploymentOptions options = new DeploymentOptions().setConfig(new JsonObject().put("http.port", port));
		vertx.deployVerticle(chatService, options, context.asyncAssertSuccess());
	}

	@After
	public void tearDown(TestContext context) {
		vertx.close(context.asyncAssertSuccess());
	}

	@Test
	public void getCorrectly(TestContext context) {
		final Async async = context.async();

		ChatMessage message = new ChatMessage("jose", "soccer", "Hello world");
		messageService.put(message);

		Handler<HttpClientResponse> handler = response -> {
			checkTypeMessage(context, response, RestChatService.OK);
			response.handler(body -> {
				context.assertEquals(message.getAuthor(), "jose");
				context.assertEquals(message.getChannel(), "soccer");
				context.assertEquals(message.getText(), "Hello world");
				async.complete();
			});
		};

		getOneChatMessage(message.getId(), handler);
	}

	@Test
	public void getNotExistId(TestContext context) {
		final Async async = context.async();
		Handler<HttpClientResponse> handler = response -> {
			context.assertEquals(response.statusCode(), RestChatService.ERROR_NOT_FOUND);
			async.complete();
		};

		getOneChatMessage(0, handler);
	}

	@Test
	public void deleteCorrectly(TestContext context) {
		final Async async = context.async();

		ChatMessage message = new ChatMessage("jose", "soccer", "Hello world");
		messageService.put(message);

		Handler<HttpClientResponse> handler = response -> {
			context.assertEquals(response.statusCode(), RestChatService.DELETE_CORRECTLY);
			async.complete();
		};

		deleteOneChatMessage(message.getId(), handler);
	}

	@Test
	public void deleteNotExist(TestContext context) {
		final Async async = context.async();
		Handler<HttpClientResponse> handler = response -> {
			context.assertEquals(response.statusCode(), RestChatService.DELETE_CORRECTLY);
			async.complete();
		};

		deleteOneChatMessage(0, handler);
	}

	@Test
	public void deleteWithoutId(TestContext context) {
		final Async async = context.async();
		Handler<HttpClientResponse> handler = response -> {
			context.assertEquals(response.statusCode(), RestChatService.ERROR_NOT_FOUND);
			async.complete();
		};
		vertx.createHttpClient().delete(port, "localhost", RestChatService.API_MESSAGES, handler).end();
	}

	@Test
	public void createOneCorrectly(TestContext context) {
		final Async async = context.async();
		HashMap<String, String> values = createChatMessage("jose", "soccer", "Hello world");

		final String message = Json.encodePrettily(values);
		final String length = Integer.toString(message.length());

		Handler<HttpClientResponse> handler = response -> {
			checkTypeMessage(context, response, RestChatService.CREATED_CORRECTLY);
			response.bodyHandler(body -> {
				final ChatMessage responseMessage = Json.decodeValue(body.toString(), ChatMessage.class);
				context.assertEquals(responseMessage.getAuthor(), "jose");
				context.assertEquals(responseMessage.getChannel(), "soccer");
				context.assertEquals(responseMessage.getText(), "Hello world");
				async.complete();
			});
		};
		postChatMessage(message, length, handler);
	}

	private HashMap<String, String> createChatMessage(String author, String channel, String text) {
		HashMap<String, String> values = new HashMap<String, String>();
		values.put(RestChatService.AUTHOR, author);
		values.put(RestChatService.CHANNEL, channel);
		values.put(RestChatService.TEXT, text);
		return values;
	}

	@Test
	public void createOneWithEmptyMessageGivesError(TestContext context) {
		final Async async = context.async();
		Handler<HttpClientResponse> handler = response -> {
			context.assertEquals(response.statusCode(), RestChatService.ERROR_BAD_REQUEST);
			async.complete();
		};
		postChatMessage("", "0", handler);
	}

	@Test
	public void createOneWithIncorrectMessageGivesError(TestContext context) {
		HashMap<String, String> values = new HashMap<String, String>();
		values.put("incorrect", "incorrect");

		final Async async = context.async();
		Handler<HttpClientResponse> handler = response -> {
			context.assertEquals(response.statusCode(), RestChatService.ERROR_BAD_REQUEST);
			async.complete();
		};
		String jsonMessage = Json.encodePrettily(values);
		postChatMessage(jsonMessage, Integer.toString(jsonMessage.length()), handler);
	}

	@Test
	public void createOneMessageIncorrectTypeOfMessageGivesError(TestContext context) {
		final Async async = context.async();

		Handler<HttpClientResponse> handler = response -> {
			context.assertEquals(response.statusCode(), RestChatService.ERROR_BAD_REQUEST);
			async.complete();
		};
		String jsonMessage = "not json";
		postChatMessage(jsonMessage, Integer.toString(jsonMessage.length()), handler);
	}

	@Test
	public void updateOneCorrectly(TestContext context) {
		final Async async = context.async();

		ChatMessage previousMessage = new ChatMessage("jose", "soccer", "Helloworld");
		messageService.put(previousMessage);

		final String message = Json.encodePrettily(createChatMessage("jose", "soccer", "Hello world2"));
		final String length = Integer.toString(message.length());

		Handler<HttpClientResponse> handler = response -> {
			checkTypeMessage(context, response, RestChatService.OK);
			response.bodyHandler(body -> {
				final ChatMessage responseMessage = Json.decodeValue(body.toString(), ChatMessage.class);
				context.assertEquals(responseMessage.getAuthor(), "jose");
				context.assertEquals(responseMessage.getChannel(), "soccer");
				context.assertEquals(responseMessage.getText(), "Hello world2");
				async.complete();
			});
		};
		putChatMessage(message, previousMessage.getId(), length, handler);
	}

	@Test
	public void updateOneNotExistIdGivesError(TestContext context) {
		final Async async = context.async();

		ChatMessage previousMessage = new ChatMessage("jose", "soccer", "Hello world");
		messageService.put(previousMessage);

		final String message = Json.encodePrettily(new ChatMessage("jose", "soccer", "Hello world2"));
		final String length = Integer.toString(message.length());

		Handler<HttpClientResponse> handler = response -> {
			context.assertEquals(response.statusCode(), RestChatService.ERROR_NOT_FOUND);
			async.complete();
		};
		putChatMessage(message, 2, length, handler);
	}

	@Test
	public void updateOneMessageNullGivesError(TestContext context) {
		final Async async = context.async();

		ChatMessage previousMessage = new ChatMessage("jose", "soccer", "Hello world");
		messageService.put(previousMessage);

		Handler<HttpClientResponse> handler = response -> {
			context.assertEquals(response.statusCode(), RestChatService.ERROR_BAD_REQUEST);
			async.complete();
		};
		putChatMessage("", 2, "0", handler);
	}

	@Test
	public void updateIncorrectTypeOFMessageGivesError(TestContext context) {
		final Async async = context.async();

		ChatMessage previousMessage = new ChatMessage("jose", "soccer", "Hello world");
		messageService.put(previousMessage);

		Handler<HttpClientResponse> handler = response -> {
			context.assertEquals(response.statusCode(), RestChatService.ERROR_BAD_REQUEST);
			async.complete();
		};
		String jsonMessage = "not json";
		putChatMessage(jsonMessage, previousMessage.getId(), Integer.toString(jsonMessage.length()), handler);
	}

	@Test
	public void updateIncorrectMessageGivesError(TestContext context) {
		final Async async = context.async();

		ChatMessage previousMessage = new ChatMessage("jose", "soccer", "Hello world");
		messageService.put(previousMessage);

		HashMap<String, String> values = new HashMap<String, String>();
		values.put("incorrect", "incorrect");

		Handler<HttpClientResponse> handler = response -> {
			context.assertEquals(response.statusCode(), RestChatService.ERROR_BAD_REQUEST);
			async.complete();
		};
		String jsonMessage = Json.encodePrettily(values);
		putChatMessage(jsonMessage, previousMessage.getId(), Integer.toString(jsonMessage.length()), handler);
	}

	@Test
	public void getAllCorrectly(TestContext context) {
		final Async async = context.async();
		// TODO add new messages
		ChatMessage previousMessage = new ChatMessage("jose", "soccer", "Hello world");
		messageService.put(previousMessage);
		ChatMessage previousMessage2 = new ChatMessage("jose2", "soccer", "Hello world");
		messageService.put(previousMessage2);
		Handler<HttpClientResponse> handler = response -> {
			response.handler(body -> {
				HashMap responseMessage = Json.decodeValue(body.toString(), HashMap.class);
				List<HashMap> messages = (List<HashMap>) responseMessage.get("data");

				context.assertEquals(messages.size(), 2);
				context.assertEquals(messages.get(0).get(RestChatService.AUTHOR), "jose");
				context.assertEquals(messages.get(1).get(RestChatService.AUTHOR), "jose2");
				context.assertEquals(messages.get(0).get(RestChatService.CHANNEL), "soccer");
				context.assertEquals(messages.get(1).get(RestChatService.CHANNEL), "soccer");
				context.assertEquals(messages.get(0).get(RestChatService.TEXT), "Hello world");
				context.assertEquals(messages.get(1).get(RestChatService.TEXT), "Hello world");

				async.complete();
			});
		};
		getAllChatMessages(async, handler);
	}

	@Test
	public void getFirstFiveCorrectly(TestContext context) {
		final Async async = context.async();
		createSixMessages();

		Handler<HttpClientResponse> handler = response -> {
			response.handler(body -> {
				HashMap responseMessage = Json.decodeValue(body.toString(), HashMap.class);
				List<HashMap> messages = (List<HashMap>) responseMessage.get("data");
				context.assertEquals(messages.size(), 5);
			});
			async.complete();
		};
		getPagesChatMessages(async, handler, 1, 5);
	}

	@Test
	public void getFirstFiveCheckPaginationCorrectly(TestContext context) {
		final Async async = context.async();
		createSixMessages();

		Handler<HttpClientResponse> handler = response -> {
			response.handler(body -> {
				HashMap responseMessage = Json.decodeValue(body.toString(), HashMap.class);
				HashMap pagination = (HashMap) responseMessage.get("pagination");
				context.assertEquals(pagination.get("next_url"), "/api/messages?next_page=2&number=1");
				context.assertEquals(pagination.get("count"), 5);
				context.assertEquals(pagination.get("per_page"), 5);
				context.assertEquals(pagination.get("total"), 6);
				context.assertEquals(pagination.get("current_page"), 1);
				context.assertEquals(pagination.get("total_pages"), 2);
			});
			async.complete();
		};
		getPagesChatMessages(async, handler, 1, 5);
	}

	@Test
	public void getFirstTwoCheckPaginationCorrectly(TestContext context) {
		final Async async = context.async();
		createSixMessages();

		Handler<HttpClientResponse> handler = response -> {
			response.handler(body -> {
				HashMap responseMessage = Json.decodeValue(body.toString(), HashMap.class);
				HashMap pagination = (HashMap) responseMessage.get("pagination");
				context.assertEquals(pagination.get("next_url"), "/api/messages?next_page=2&number=2");
				context.assertEquals(pagination.get("count"), 2);
				context.assertEquals(pagination.get("per_page"), 2);
				context.assertEquals(pagination.get("total"), 6);
				context.assertEquals(pagination.get("current_page"), 1);
				context.assertEquals(pagination.get("total_pages"), 3);
			});
			async.complete();
		};
		getPagesChatMessages(async, handler, 1, 2);
	}

	@Test
	public void getWithoutPaginationCorrectly(TestContext context) {
		final Async async = context.async();
		createSixMessages();

		Handler<HttpClientResponse> handler = response -> {
			response.handler(body -> {
				HashMap responseMessage = Json.decodeValue(body.toString(), HashMap.class);
				HashMap pagination = (HashMap) responseMessage.get("pagination");
				context.assertEquals(pagination.get("next_url"), "/api/messages?next_page=2&number=1");
				context.assertEquals(pagination.get("count"), 5);
				context.assertEquals(pagination.get("per_page"), 5);
				context.assertEquals(pagination.get("total"), 6);
				context.assertEquals(pagination.get("current_page"), 1);
				context.assertEquals(pagination.get("total_pages"), 2);

			});
			async.complete();
		};
		getAllChatMessages(async, handler);
	}

	private void createSixMessages() {
		ChatMessage previousMessage = new ChatMessage("jose", "soccer", "Hello world");
		messageService.put(previousMessage);
		ChatMessage previousMessage2 = new ChatMessage("jose2", "soccer", "Hello world");
		messageService.put(previousMessage2);
		ChatMessage previousMessage3 = new ChatMessage("jose3", "soccer", "Hello world");
		messageService.put(previousMessage3);
		ChatMessage previousMessage4 = new ChatMessage("jose4", "soccer", "Hello world");
		messageService.put(previousMessage4);
		ChatMessage previousMessage5 = new ChatMessage("jose5", "soccer", "Hello world");
		messageService.put(previousMessage5);
		ChatMessage previousMessage6 = new ChatMessage("jose6", "soccer", "Hello world");
		messageService.put(previousMessage6);
	}

	private void checkTypeMessage(TestContext context, HttpClientResponse response, int statusCode) {
		context.assertEquals(response.statusCode(), statusCode);
		context.assertTrue(response.headers().get("content-type").contains("application/json"));
	}

	private void postChatMessage(String message, String length, Handler<HttpClientResponse> handler) {
		vertx.createHttpClient().post(port, "localhost", RestChatService.API_MESSAGES).putHeader("content-type", "application/json")
				.putHeader("content-length", length).handler(handler).write(message).end();
	}

	private void putChatMessage(String message, int messageId, final String length, Handler<HttpClientResponse> handler) {
		vertx.createHttpClient().put(port, "localhost", RestChatService.API_MESSAGES + "/" + Integer.toString(messageId))
				.putHeader("content-type", "application/json").putHeader("content-length", length).handler(handler).write(message).end();
	}

	private void getAllChatMessages(final Async async, Handler<HttpClientResponse> handler) {
		vertx.createHttpClient().getNow(port, "localhost", RestChatService.API_MESSAGES, handler);
	}

	private void getPagesChatMessages(final Async async, Handler<HttpClientResponse> handler, int page, int count) {
		vertx.createHttpClient().getNow(port, "localhost", RestChatService.API_MESSAGES + "?page=" + String.valueOf(page) + "&number=" + String.valueOf(count),
				handler);
	}

	private void getOneChatMessage(Integer messageId, Handler<HttpClientResponse> handler) {
		vertx.createHttpClient().getNow(port, "localhost", RestChatService.API_MESSAGES + "/" + Integer.toString(messageId), handler);
	}

	private void deleteOneChatMessage(Integer messageId, Handler<HttpClientResponse> handler) {
		vertx.createHttpClient().delete(port, "localhost", RestChatService.API_MESSAGES + "/" + Integer.toString(messageId), handler).end();
	}

}
