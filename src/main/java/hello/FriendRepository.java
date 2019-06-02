package hello;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FriendRepository extends CrudRepository <Friend,Integer>{
    List<Friend> findByUserA(Integer userA);
}
