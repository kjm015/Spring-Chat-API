package group.chat.api.domain;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class Message implements Serializable{

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private int id;
    private User user;
    private String message;

    public int getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getMessage() {
        return message;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", user=" + user +
                ", message='" + message + '\'' +
                '}';
    }
}