package adapters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import entities.ChatMessage;

//TODO add tests for this messageInMemory class
public class MessagesInMemory implements MessageRepository {
	private Map<Integer, ChatMessage> messages;

	public MessagesInMemory() {
		messages = new LinkedHashMap<Integer, ChatMessage>();
	}

	public void put(ChatMessage message) {
		messages.put(message.getId(), message);
	}

	public ChatMessage get(Integer identifier) {
		return messages.get(identifier);
	}

	public void remove(Integer identifier) {
		messages.remove(identifier);

	}

	@Override
	public List<ChatMessage> getMessagesByChannel(String channel) {
		List<ChatMessage> messagesFromChannel = new LinkedList<ChatMessage>();
		for (Integer key : messages.keySet()) {
			ChatMessage chatMessage = messages.get(key);
			if (chatMessage.getChannel().equals(channel)) {
				messagesFromChannel.add(chatMessage);
			}

		}
		return messagesFromChannel;
	}

	@Override
	public List<ChatMessage> getAll() {
		List<ChatMessage> messagesAll = new LinkedList<ChatMessage>();
		for (Integer key : messages.keySet()) {
			ChatMessage chatMessage = messages.get(key);
			messagesAll.add(chatMessage);
		}
		return messagesAll;
	}

	@Override
	public long count() {
		return messages.keySet().size();
	}

	private List<ChatMessage> getRangeValues(long number, long offset, Set<Integer> keys, int total) {
		Integer arrayKeys[] = new Integer[total];
		arrayKeys = (Integer[]) keys.toArray(new Integer[total]);
		Integer[] rangeValues = Arrays.copyOfRange(arrayKeys, (int) offset, (int) number);
		List<ChatMessage> responseMessages = new ArrayList<ChatMessage>();
		for (int i = 0; i < rangeValues.length; i++) {
			responseMessages.add(messages.get(rangeValues[i]));
		}
		return responseMessages;
	}

	@Override
	public List<ChatMessage> getAll(long number, long offset) {
		return getRangeValues(number, offset, messages.keySet(), messages.size());
	}

	@Override
	public List<ChatMessage> getMessagesByChannel(String channel, long number, long offset) {
		List<ChatMessage> messagesFromChannel = new LinkedList<ChatMessage>();
		// TODO check offset!
		Set<Integer> keys = messages.keySet();
		getRangeValues(number, offset, keys, keys.size());
		for (Integer key : keys) {
			ChatMessage chatMessage = messages.get(key);
			if (chatMessage.getChannel().equals(channel)) {
				messagesFromChannel.add(chatMessage);
			}

		}
		return messagesFromChannel;
	}

}
