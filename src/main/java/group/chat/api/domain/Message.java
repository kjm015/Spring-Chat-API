package group.chat.api.domain;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Message implements Serializable{

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private int id;
    private User user;
    private String message;
    @JsonInclude()
    @Transient
    private String host;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

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