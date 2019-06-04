package hello;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MessageRepository extends CrudRepository<Message,Integer> {
    List<Message> findByUserTo(int userTo);
    Message findById(int id);
}
