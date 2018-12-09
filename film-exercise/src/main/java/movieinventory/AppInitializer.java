package movieinventory;

import movieinventory.dataproviders.CustomerRepository;
import movieinventory.dataproviders.FilmRepository;
import movieinventory.domain.usecases.CalculateBonusPoints;
import movieinventory.domain.usecases.CalculateRenting;
import movieinventory.domain.usecases.ListInventory;
import movieinventory.domain.usecases.RentFilms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class AppInitializer {

    public static void main(String[] args) {
        SpringApplication.run(AppInitializer.class, args);
    }


    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    FilmRepository filmRepository;

    @Bean
    public CalculateBonusPoints calculateBonusPoints() {
        return new CalculateBonusPoints(customerRepository);
    }

    @Bean
    public CalculateRenting calculateRenting() {
        return new CalculateRenting(filmRepository);
    }

    @Bean
    public ListInventory listInventory() {
        return new ListInventory(filmRepository);
    }

    @Bean
    public RentFilms rentFilms() {
        return new RentFilms(customerRepository, filmRepository);
    }

    /*for debugging process */
/*
    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            Film film1 = new Film("title", 1, Film.CategoryMovie.NEW_RELEASES);
            Film film2 = new Film("title2", 1, Film.CategoryMovie.REGULAR);
            Film film3 = new Film("title3", 1, Film.CategoryMovie.OLD);
            this.filmRepository.save(Arrays.asList(film1, film2, film3));
            Customer customer = new Customer("jack1", "jack1");
            customer.setRents(Arrays.asList(
                    new Rent(Calendar.getInstance().getTime(), 1, customer, film1)
                    , new Rent(Calendar.getInstance().getTime(), 1, customer, film2)
                    , new Rent(Calendar.getInstance().getTime(), 1, customer, film3)));

            this.customerRepository.save(customer);
            System.out.println("Customer available: " + customer.getId());
            System.out.println("Let's inspect the beans provided by Spring Boot:");
            String[] beanNames = ctx.getBeanDefinitionNames();
            Arrays.sort(beanNames);
            for (String beanName : beanNames) {
                System.out.println(beanName);
            }

        };
    }
    */


}

