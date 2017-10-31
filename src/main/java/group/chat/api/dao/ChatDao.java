package group.chat.api.dao;


import group.chat.api.domain.Message;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ChatDao {
    private ArrayList<Message> messages = new ArrayList<>();


    public List<Message> getAllMessagesFrom(int id) {
        if(messages.size() > 0 && id >= messages.size()-1) {
            return messages.subList(id, messages.size());
        }
        return messages;
    }

    public boolean addMessage(Message m) {
        messages.add(m);
        return true;
    }

}
