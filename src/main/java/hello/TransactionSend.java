package hello;

import java.util.Calendar;

public class TransactionSend {
    private User userFrom;
    private User userTo;
    private String context;
    private Calendar time;


    public TransactionSend(Transaction transaction,UserRepository userRepository){
        this.userFrom=userRepository.findById(transaction.getUserFrom().intValue());
        this.userTo=userRepository.findById(transaction.getUserTo().intValue());
        int amount=transaction.getAmount();
        if(amount>=0){
            this.context=" sent "+amount+" to you";
        }
        else{
            this.context=" took "+amount+" from you";
        }
        this.time=transaction.getTime();
    }
    @Override
    public String toString() {
        return "TransactionSend{" +
                "userFrom=" + userFrom +
                ", userTo=" + userTo +
                ", context='" + context + '\'' +
                ", time=" + time +
                '}';
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
