package repositories.mongodb;

import org.bson.Document;

import entities.ChatMessage;

public final class JSONChatMessage {

	private final ChatMessage chatMessage;

	// TODO add tests for this class
	public JSONChatMessage(ChatMessage chatMessage) {
		this.chatMessage = chatMessage;
	}

	public JSONChatMessage(Document document) {
		// TODO add move to constants

		if (document == null) {
			this.chatMessage = ChatMessage.makeNullObject();
			return;
		}
		Integer id = (Integer) document.get("id");
		if (id == null) {
			id = new Integer(-1);

		}
		String author = (String) document.get("author");
		if (author == null) {
			author = new String();
		}

		String channel = (String) document.get("channel");
		if (channel == null) {
			channel = new String();
		}

		String text = (String) document.get("text");
		if (text == null) {
			text = new String();
		}

		this.chatMessage = new ChatMessage(id, author, channel, text);
	}

	public Document toJSON() {
		Document document = new Document();
		document.append("id", chatMessage.getId());
		document.append("author", chatMessage.getAuthor());
		document.append("channel", chatMessage.getChannel());
		document.append("text", chatMessage.getText());
		return document;
	}

	public ChatMessage getChatMessage() {
		return this.chatMessage;
	}

}
