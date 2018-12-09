package movieinventory.integration.dataproviders;

import movieinventory.dataproviders.CustomerRepository;
import movieinventory.domain.entities.Customer;
import movieinventory.domain.entities.Film;
import movieinventory.domain.entities.Rent;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
@ComponentScan(basePackages = "movieinventory")
public class CustomerRepositoryTest {

    @Autowired
    CustomerRepository customerRepository;


    @Before
    public void setUp() {
        customerRepository.deleteAll();
    }

    @Test
    public void should_add_customer_correctly() {
        Customer added = customerRepository.save(new Customer("jack1", "jack2"));
        Assert.assertNotNull(added.getId());
    }

    @Test
    public void should_return_customer_when_is_added_correctly() {
        Customer added = customerRepository.save(new Customer("jack1", "jack2"));
        Assert.assertNotNull(customerRepository.findOne(added.getId()));
    }


    @Test
    public void should_add_customer_with_rents_correctly() {
        Customer customer = new Customer("jack1", "jack2");
        Rent rent1 = new Rent(Calendar.getInstance().getTime(), 2, customer, new Film("a", 1, Film.CategoryMovie.REGULAR));
        Rent rent2 = new Rent(Calendar.getInstance().getTime(), 1, customer, new Film("b", 1, Film.CategoryMovie.REGULAR));
        customer.setRents(Arrays.asList(rent1, rent2));
        Customer added = customerRepository.save(customer);
        List<Rent> rents = customerRepository.findOne(added.getId()).getRents();
        Assert.assertNotEquals(0, rents.size());
        assertRent(rent1, rents.get(0));
        assertRent(rent2, rents.get(1));

    }

    private void assertRent(Rent expectedRent, Rent rent) {
        Assert.assertEquals(expectedRent, rent);
        Assert.assertNotNull(rent.getCustomer());
        Assert.assertNotNull(rent.getFilm());
    }


}
