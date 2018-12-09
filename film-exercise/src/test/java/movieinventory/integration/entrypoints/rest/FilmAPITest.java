package movieinventory.integration.entrypoints.rest;

import movieinventory.AppInitializer;
import movieinventory.dataproviders.CustomerRepository;
import movieinventory.dataproviders.FilmRepository;
import movieinventory.domain.entities.Film;
import movieinventory.entrypoints.rest.dto.RequestCheckRent;
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
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppInitializer.class)
@WebAppConfiguration
public class FilmAPITest {
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

    private List<Film> films;
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
        films = setUpFilms();
        filmRepository.save(films);

    }


    @Test
    public void should_list_films() throws Exception {
        this.mockMvc.perform(get("/v1.0/films").contentType(contentType).accept(contentType)).andExpect(status().isOk()).andReturn();
    }

    @Test
    public void should_get_checkrent_for_regular() throws Exception {
        String requestCheckRentJSON = json(new RequestCheckRent(1));
        this.mockMvc.perform(put("/v1.0/films/" + films.get(0).getId().toString() + "/checkrent")
                .contentType(contentType)
                .content(requestCheckRentJSON)
                .accept(contentType)).andExpect(status().isOk()).andReturn();
    }

    private List<Film> setUpFilms() {
        List<Film> films = new ArrayList();
        for (int index = 0; index < 10; index++) {
            films.add(new Film("movie" + index, index, Film.CategoryMovie.REGULAR));
        }
        return films;
    }
}

