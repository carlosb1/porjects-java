package core.models;

import java.util.List;

import entities.ChatMessage;

public class ResponseGet {

	public final class Pagination {
		public final String next_url;
		public final long count;
		public final long per_page;
		public final long total;
		public final long current_page;
		public final long total_pages;

		public static final long PAGE = 1;
		public static final long SIZE_PAGE = 5;
		public static final long NUMBER = 5;

		public Pagination(String next_url, long count, long per_page, long total, long current_page, long total_pages) {
			super();
			this.total = total;
			this.count = count;
			this.per_page = per_page;
			this.current_page = current_page;
			this.total_pages = total_pages;
			this.next_url = next_url;

		}

	}

	public final List<ChatMessage> data;
	public final Pagination pagination;

	public ResponseGet(List<ChatMessage> data, String next_url, long count, long per_page, long total, long current_page) {
		this.data = data;
		long restPage = total % per_page;
		long totalPages = (restPage == 0 ? total / per_page : (total / per_page) + 1);

		this.pagination = new Pagination(next_url, count, per_page, total, current_page, totalPages);
	}

}
