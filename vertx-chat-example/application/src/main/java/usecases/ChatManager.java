package usecases;

import adapters.MessageRepository;

public class ChatManager {
	private MessageRepository repository;

	public ChatManager(MessageRepository repository) {
		this.repository = repository;
	}

}
