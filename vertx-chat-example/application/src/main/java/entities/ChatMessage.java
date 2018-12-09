package entities;

import java.util.concurrent.atomic.AtomicInteger;

public class ChatMessage {
	private static final AtomicInteger COUNTER = new AtomicInteger();
	private final int id;
	private String author;
	private String channel;
	private String text;

	private static final ChatMessage NULLOBJECT = new ChatMessage(-1, new String(), new String(), new String());

	public static ChatMessage makeNullObject() {
		return NULLOBJECT;
	}

	public boolean isNull() {
		return (id == -1);
	}

	public ChatMessage(String author, String channel, String text) {
		this.id = COUNTER.getAndIncrement();
		this.author = author;
		this.channel = channel;
		this.text = text;
	}

	public ChatMessage(Integer id, String author, String channel, String text) {
		this.id = id;
		this.author = author;
		this.channel = channel;
		this.text = text;
	}

	// It is necessary this constructor for the JSON parsing
	public ChatMessage() {
		this.id = COUNTER.getAndIncrement();
		this.author = new String();
		this.channel = new String();
		this.text = new String();
	}

	public String getAuthor() {
		return author;
	}

	public String getChannel() {
		return channel;
	}

	public int getId() {
		return id;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
