package movieinventory.dataproviders;

import movieinventory.domain.entities.Customer;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface CustomerRepository extends PagingAndSortingRepository<Customer, Long> {
    @Override
    Customer findOne(Long id);

    @Override
    boolean exists(Long id);

    @Override
    void deleteAll();

}
