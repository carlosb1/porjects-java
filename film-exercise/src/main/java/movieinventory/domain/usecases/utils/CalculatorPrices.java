package movieinventory.domain.usecases.utils;

import movieinventory.domain.entities.Film;

public class CalculatorPrices {
    public Double calculate(Film.CategoryMovie category, Integer days) {
        if (category == Film.CategoryMovie.NEW_RELEASES) {
            return days * ConstantFilms.PREMIUM_PRICE;
        } else if (category == Film.CategoryMovie.REGULAR) {
            Double price = 0.;
            price += ConstantFilms.BASIC_PRICE;
            if (days <= 3) {
                return price;
            }
            price += (ConstantFilms.BASIC_PRICE * (days - 3));
            return price;

        } else {
            Double price = 0.;
            price += ConstantFilms.BASIC_PRICE;
            if (days <= 5) {
                return price;
            }
            price += (ConstantFilms.BASIC_PRICE * (days - 5));
            return price;
        }

    }
}
