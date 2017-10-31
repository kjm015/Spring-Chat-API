package group.chat.api.dao;

import group.chat.api.domain.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public class UserDao {
    private ArrayList<User> users = new ArrayList<>();


    public User getUserById(int id) {
        for(User x : users) {
            if(x.getId() == id) return x;
        }
        return null;
    }

    public boolean addUser(User user) {
        users.add(user);
        return true;
    }
}
