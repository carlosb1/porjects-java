package adapters;

import java.util.List;

import entities.ChatMessage;

public interface MessageRepository {

	public void put(ChatMessage message);

	public ChatMessage get(Integer idenfifier);

	public void remove(Integer identifier);

	public long count();

	public List<ChatMessage> getAll();

	public List<ChatMessage> getMessagesByChannel(String channel);

	public List<ChatMessage> getAll(long number, long offset);

	public List<ChatMessage> getMessagesByChannel(String channel, long number, long offset);
}
