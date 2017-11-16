package group.chat.api.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import group.chat.api.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import group.chat.api.repository.UserRepository;
import java.util.List;



@Repository
@RequestMapping("/user")
@CrossOrigin
public class UserController {
    @Autowired
    UserRepository userRepository;
    ObjectMapper mapper = new ObjectMapper();
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value = "/register", method = RequestMethod.PUT, produces="text/json")
    public ResponseEntity<?> createUser(@RequestParam(value = "name") String name, @RequestParam(value = "password") String password ) throws JsonProcessingException {
        name = name.trim();
        if (userRepository.findByName(name).size() != 0) {
            return new ResponseEntity<>(mapper.writeValueAsString("Username already exists."), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        User temp = new User();
        temp.setPassword(password);
        temp.setName(name);
        userRepository.save(temp);
        logger.info("New User: " + temp.toString());
        return new ResponseEntity<>(mapper.writeValueAsString(temp), HttpStatus.OK);
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET, produces="text/json")
    public ResponseEntity<?> loginUser(@RequestParam(value = "name") String name, @RequestParam(value = "password") String password) throws JsonProcessingException {
        List<User> users = userRepository.findByName(name);
        if (users.size() == 0) {
            logger.info("Tried to login in as invalid user");
            return new ResponseEntity<>(mapper.writeValueAsString("User name does not exist"), HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            User temp = users.get(0);
            if (temp.getPassword().equals(password)) {
                logger.info("Logged in: " + temp.toString());
                return new ResponseEntity<>(mapper.writeValueAsString(temp), HttpStatus.OK);
            }
            return new ResponseEntity<>(mapper.writeValueAsString("Invalid Password"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
