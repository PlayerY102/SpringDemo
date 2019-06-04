package hello;

import java.util.Calendar;
import java.util.List;

public class MessageSend {
    private Integer id;
    private User userFrom;
    private User userTo;
    private String context;
    private Calendar time;


    public MessageSend(Message message,UserRepository userRepository){
        this.id=message.getId();
        this.userFrom=userRepository.findById(message.getUserFrom().intValue());
        this.userTo=userRepository.findById(message.getUserTo().intValue());
        int amount=message.getAmount();
        if(amount>=0){
            this.context=" want to send "+amount+" to you";
        }
        else{
            this.context=" want to take "+amount+" from you";
        }
        this.time=message.getTime();
    }


    @Override
    public String toString() {
        return "MessageSend{" +
                "id=" + id +
                ", userFrom=" + userFrom +
                ", userTo=" + userTo +
                ", context='" + context + '\'' +
                ", time=" + time +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUserFrom() {
        return userFrom;
    }

    public void setUserFrom(User userFrom) {
        this.userFrom = userFrom;
    }

    public User getUserTo() {
        return userTo;
    }

    public void setUserTo(User userTo) {
        this.userTo = userTo;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public Calendar getTime() {
        return time;
    }

    public void setTime(Calendar time) {
        this.time = time;
    }
}
