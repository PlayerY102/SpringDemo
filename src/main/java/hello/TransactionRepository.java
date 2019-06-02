package hello;

import org.springframework.data.repository.CrudRepository;

import java.util.Calendar;
import java.util.List;

public interface TransactionRepository extends CrudRepository<Transaction,Integer> {
    List<Transaction> findByUserFrom(Integer userFrom);
    List<Transaction> findByUserTo(Integer userTo);
}
