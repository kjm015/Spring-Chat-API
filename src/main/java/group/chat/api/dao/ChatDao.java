package group.chat.api.dao;


import group.chat.api.domain.Message;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public class ChatDao {
    private ArrayList<Message> messages = new ArrayList<>();


    public ArrayList<Message> getAllMessages() {
        return messages;
    }

    public boolean addMessage(Message m) {
        messages.add(m);
        return true;
    }

}
