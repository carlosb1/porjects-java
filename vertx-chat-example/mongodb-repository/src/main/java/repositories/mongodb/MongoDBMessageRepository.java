package repositories.mongodb;

import static com.mongodb.client.model.Filters.eq;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bson.Document;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.MongoCommandException;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import adapters.MessageRepository;
import entities.ChatMessage;

//TODO add close feature
public class MongoDBMessageRepository implements MessageRepository {
	private final String databaseName;
	private final MongoClient client;
	private MongoDatabase database;
	private MongoCollection<Document> collection;

	private final static String NAME_COLLECTION = "messages";

	private final static Logger LOGGER = Logger.getLogger(MongoDBMessageRepository.class.getName());

	public MongoDBMessageRepository(String address, int port, String databaseName) {
		this.databaseName = databaseName;
		this.client = new MongoClient(new ServerAddress(address, port));
	}

	public void create() {
		database = this.client.getDatabase(databaseName);
		try {
			database.createCollection(NAME_COLLECTION);
		} catch (MongoCommandException exception) {
			exception.addSuppressed(new Exception("It was not possible create the Message collection"));
			// TODO add new logger from JAVA
			LOGGER.log(Level.INFO, exception.getErrorMessage());

		}

		collection = database.getCollection(NAME_COLLECTION);
	}

	public void delete() {
		this.client.dropDatabase(databaseName);
	}

	@Override
	public void put(ChatMessage message) {
		collection.insertOne(new JSONChatMessage(message).toJSON());
	}

	@Override
	public ChatMessage get(Integer idenfifier) {
		// TODO it is possible to create abstraction
		// TODO test if it is null the object
		Document document = collection.find(eq("id", idenfifier)).first();
		return new JSONChatMessage(document).getChatMessage();
	}

	@Override
	public void remove(Integer identifier) {
		collection.deleteMany(eq("id", identifier));

	}

	@Override
	public List<ChatMessage> getAll() {
		List<ChatMessage> values = new LinkedList<ChatMessage>();
		collection.find().forEach(new Block<Document>() {
			@Override
			public void apply(final Document document) {
				values.add(new JSONChatMessage(document).getChatMessage());
			}
		});
		return values;
	}

	@Override
	public List<ChatMessage> getMessagesByChannel(String channel) {
		List<ChatMessage> messages = new LinkedList<ChatMessage>();
		collection.find(eq("channel", channel)).forEach(new Block<Document>() {
			@Override
			public void apply(final Document document) {
				messages.add(new JSONChatMessage(document).getChatMessage());
			}
		});

		return messages;
	}

	@Override
	public List<ChatMessage> getAll(long number, long offset) {
		List<ChatMessage> values = new LinkedList<ChatMessage>();
		collection.find().limit((int) number).skip((int) offset).forEach(new Block<Document>() {
			@Override
			public void apply(final Document document) {
				values.add(new JSONChatMessage(document).getChatMessage());
			}
		});
		return values;
	}

	@Override
	public List<ChatMessage> getMessagesByChannel(String channel, long number, long offset) {
		List<ChatMessage> messages = new LinkedList<ChatMessage>();
		collection.find(eq("channel", channel)).limit((int) number).skip((int) offset).forEach(new Block<Document>() {
			@Override
			public void apply(final Document document) {
				messages.add(new JSONChatMessage(document).getChatMessage());
			}
		});

		return messages;
	}

	@Override
	public long count() {
		return collection.count();
	}

}
