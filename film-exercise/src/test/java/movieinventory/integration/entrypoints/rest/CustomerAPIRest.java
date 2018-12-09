package movieinventory.integration.entrypoints.rest;

import movieinventory.AppInitializer;
import movieinventory.dataproviders.CustomerRepository;
import movieinventory.dataproviders.FilmRepository;
import movieinventory.domain.entities.Customer;
import movieinventory.domain.entities.Film;
import movieinventory.domain.entities.Rent;
import movieinventory.entrypoints.rest.dto.RequestRent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppInitializer.class)
@WebAppConfiguration
public class CustomerAPIRest {
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    FilmRepository filmRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
                .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
                .findAny()
                .orElse(null);

        assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }

    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void should_rent_a_set_of_films_when_customer_exists() throws Exception {
        this.filmRepository.save(setUpFilms());
        Customer customer = new Customer("jack1", "jack1");
        this.customerRepository.save(customer);
        List<RequestRent> requestRents = Arrays.asList(new RequestRent(1L, 1));
        String requestRentJSON = json(requestRents);
        this.mockMvc.perform(put("/v1.0/customers/" + customer.getId() + "/rent").contentType(contentType)
                .content(requestRentJSON)
                .accept(contentType)).andExpect(status().isOk()).andReturn();
    }

    @Test
    public void should_check_points_when_customer_exists() throws Exception {
        Film film1 = new Film("title", 1, Film.CategoryMovie.NEW_RELEASES);
        Film film2 = new Film("title", 1, Film.CategoryMovie.REGULAR);
        Film film3 = new Film("title", 1, Film.CategoryMovie.OLD);
        this.filmRepository.save(Arrays.asList(film1, film2, film3));
        Customer customer = new Customer("jack1", "jack1");
        customer.setRents(Arrays.asList(
                new Rent(Calendar.getInstance().getTime(), 1, customer, film1)
                , new Rent(Calendar.getInstance().getTime(), 1, customer, film2)
                , new Rent(Calendar.getInstance().getTime(), 1, customer, film3)));

        this.customerRepository.save(customer);
        this.mockMvc.perform(get("/v1.0/customers/" + customer.getId() + "/points").contentType(contentType).accept(contentType)).andExpect(status().isOk()).andReturn();
    }

    private List<Film> setUpFilms() {
        List<Film> films = new ArrayList();
        for (int index = 0; index < 10; index++) {
            films.add(new Film("movie" + index, index + 1, Film.CategoryMovie.REGULAR));
        }
        return films;
    }
}

