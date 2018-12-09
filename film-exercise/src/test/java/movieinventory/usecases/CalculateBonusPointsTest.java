package movieinventory.usecases;

import movieinventory.dataproviders.CustomerRepository;
import movieinventory.domain.entities.Customer;
import movieinventory.domain.entities.Film;
import movieinventory.domain.entities.Rent;
import movieinventory.domain.exceptions.NotExistsCustomerException;
import movieinventory.domain.usecases.CalculateBonusPoints;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Calendar;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CalculateBonusPointsTest {

    private CalculateBonusPoints calculateBonusPoints;

    private final CustomerRepository customerRepository = mock(CustomerRepository.class);

    @Before
    public void setUp() {
        calculateBonusPoints = new CalculateBonusPoints(customerRepository);
    }

    @Test
    public void should_give_four_points_for_three_movies() throws NotExistsCustomerException {
        Customer customer = new Customer("jack", "jones");
        customer.setRents(Arrays.asList(
                new Rent(Calendar.getInstance().getTime(), 1, customer, new Film("title", 1, Film.CategoryMovie.NEW_RELEASES))
                , new Rent(Calendar.getInstance().getTime(), 1, customer, new Film("title", 1, Film.CategoryMovie.REGULAR))
                , new Rent(Calendar.getInstance().getTime(), 1, customer, new Film("title", 1, Film.CategoryMovie.OLD))
        ));
        when(customerRepository.findOne(any())).thenReturn(customer);
        when(customerRepository.exists(any())).thenReturn(true);
        Double points = calculateBonusPoints.run(1L);
        Assert.assertEquals(4., points.doubleValue(), 0.1);


    }

}
