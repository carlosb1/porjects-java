package repositories.mongodb.functionals;

import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import entities.ChatMessage;
import repositories.mongodb.MongoDBMessageRepository;

public class MongoDBMessageRepositoryTest {
	private final static String NAME_TEST_DATABASE = "test";
	private final static String ADDRESS_DATABASE = "localhost";
	private final static int PORT_DATABASE = 27017;

	public MongoDBMessageRepository repository;

	@Before
	public void setUp() {
		repository = new MongoDBMessageRepository(ADDRESS_DATABASE, PORT_DATABASE, NAME_TEST_DATABASE);
		repository.create();
	}

	@After
	public void tearDown() {
		// TODO error
		repository.delete();
	}

	@Test
	public void addAndGetMessageCorrectly() {
		// TODO Delete this identifier in the constructor
		ChatMessage message = new ChatMessage("usertest", "channeltest", "messagetest");
		repository.put(message);
		ChatMessage foundChatMessage = repository.get(message.getId());
		assertTrue(foundChatMessage.getAuthor().equals("usertest"));
		assertTrue(foundChatMessage.getChannel().equals("channeltest"));
		assertTrue(foundChatMessage.getText().equals("messagetest"));
	}

	@Test
	public void addAndRemoveMessageCorrectly() {
		ChatMessage message = new ChatMessage("usertest", "channeltest", "messagetest");
		repository.put(message);
		repository.remove(message.getId());
		ChatMessage foundChatMessage = repository.get(message.getId());
		assertTrue(foundChatMessage.isNull());
	}

	@Test
	public void count() {
		// TODO add messages
		ChatMessage message = new ChatMessage("usertest", "channeltest", "messagetest");
		ChatMessage message2 = new ChatMessage("usertest2", "channeltest", "messagetest2");
		ChatMessage message3 = new ChatMessage("usertest3", "channeltest", "messagetest3");
		repository.put(message);
		repository.put(message2);
		repository.put(message3);
		assertTrue(repository.count() == 3);
	}

	@Test
	public void getMessagesFromChannel() {
		// TODO add messages
		ChatMessage message = new ChatMessage("usertest", "channeltest", "messagetest");
		ChatMessage message2 = new ChatMessage("usertest2", "channeltest", "messagetest2");
		ChatMessage message3 = new ChatMessage("usertest3", "channeltest1", "messagetest3");
		repository.put(message);
		repository.put(message2);
		repository.put(message3);
		List<ChatMessage> messagesByChannel = repository.getMessagesByChannel("channeltest");
		assertTrue(repository.getMessagesByChannel("channeltest").size() == 2);
		assertTrue(messagesByChannel.iterator().next().getAuthor().equals("usertest"));
		assertTrue(messagesByChannel.iterator().next().getText().equals("messagetest"));
		Iterator<ChatMessage> iterMessages = messagesByChannel.iterator();
		iterMessages.next();
		ChatMessage receivedMessage = iterMessages.next();
		assertTrue(receivedMessage.getAuthor().equals("usertest2"));
		assertTrue(receivedMessage.getText().equals("messagetest2"));

	}

	/*
	 * private boolean existsDatabase(String databaseName) { List<String>
	 * nameDatabases = new MongoClient("localhost", 27017).getDatabaseNames();
	 * return nameDatabases.contains(databaseName); }
	 */

}
