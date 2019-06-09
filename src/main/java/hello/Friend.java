package hello;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
//朋友关系的类
@Entity
public class Friend {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;
    private Integer userA;
    private Integer userB;

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

    public Integer getUserA() {
        return userA;
    }

    public void setUserA(Integer userA) {
        this.userA = userA;
    }

    public Integer getUserB() {
        return userB;
    }

    public void setUserB(Integer userB) {
        this.userB = userB;
    }
}
