package hello;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Friend {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;
    private User userA;
    private User userB;

    @Override
    public String toString() {
        return "Friend{" +
                "id=" + id +
                ", userA=" + userA +
                ", userB=" + userB +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUserA() {
        return userA;
    }

    public void setUserA(User userA) {
        this.userA = userA;
    }

    public User getUserB() {
        return userB;
    }

    public void setUserB(User userB) {
        this.userB = userB;
    }
}
