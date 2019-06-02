package hello;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TransactionRepository extends CrudRepository {
    List<Transaction> findByUserFrom(User userFrom);
    List<Transaction> findByUserTo(User userTo);
}
