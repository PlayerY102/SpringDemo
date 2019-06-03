package hello;

import org.springframework.data.repository.CrudRepository;

import hello.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;


public interface UserRepository extends CrudRepository<User, Integer> {
    List<User> findByEmail(String email);
    List<User> findByIdIn(Collection<Integer> C);
    User findById(int id);
    void deleteByEmail(String email);
}
