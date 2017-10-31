package group.chat.api.domain;


import java.io.Serializable;

public class Message implements Serializable{

    private final long id;
    private final User user;
    private final String message;
    private final String time;

    public Message(long id, User user, String message, String time) {
        this.id = id;
        this.user = user;
        this.message = message;
        this.time = time;
    }

    public long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", user=" + user +
                ", message='" + message + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}