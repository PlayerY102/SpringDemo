package hello;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FriendRepository extends CrudRepository {
    List<Friend> findByUserA(User userA);
}
