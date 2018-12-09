package examples;

import org.junit.Before;
import org.junit.Test;

import adapters.MessagesInMemory;
import usecases.ChatManager;

public class ChatManagerTest {
	private ChatManager manager;

	@Before
	public void setUp() {
		manager = new ChatManager(new MessagesInMemory());
	}

	@Test
	public void addMessageOk() {
		/*
		 * manager.addMessage("user", "channel1", "text info");
		 * manager.getLastMessages()
		 */
	}

}
