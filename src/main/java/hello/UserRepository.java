package hello;

import org.springframework.data.repository.CrudRepository;

import hello.User;

import java.util.Collection;
import java.util.List;



public interface UserRepository extends CrudRepository<User, Integer> {
    List<User> findByEmail(String email);
    List<User> findByIdIn(Collection<Integer> C);
    void deleteByEmail(String email);
}
