package group.chat.api.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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


@Repository
@RequestMapping("/user")
@CrossOrigin
public class UserController {
    @Autowired
    UserRepository userRepository;
    ObjectMapper mapper = new ObjectMapper();
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @RequestMapping(value = "/register", method = RequestMethod.PUT, produces = "text/json")
    public ResponseEntity<?> createUser(@RequestParam(value="name") String name) throws JsonProcessingException {
        User temp  = new User();
        temp.setName(name);
        userRepository.save(temp);
        logger.info("New User: " + temp.toString());
        return new ResponseEntity<>(mapper.writeValueAsString(temp), HttpStatus.OK);
    }
}
