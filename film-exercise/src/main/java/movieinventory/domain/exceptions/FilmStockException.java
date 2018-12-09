package movieinventory.domain.exceptions;

public class FilmStockException extends Throwable {
    public FilmStockException(String title, Integer stock) {
        super("Not enough stock " + stock + " for title: " + title);
    }
}
