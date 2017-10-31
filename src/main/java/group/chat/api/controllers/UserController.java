package group.chat.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import group.chat.api.dao.UserDao;
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

import java.util.concurrent.atomic.AtomicInteger;

@Repository
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:63343")
public class UserController {
    @Autowired
    UserDao userDao;
    ObjectMapper mapper = new ObjectMapper();
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final AtomicInteger counter = new AtomicInteger();
    @RequestMapping(value = "/register", method = RequestMethod.PUT, produces = "text/plain")
    public ResponseEntity<?> createUser(@RequestParam(value="name") String name) {
        User temp  = new User(counter.incrementAndGet(),name);
        userDao.addUser(temp);
        logger.info("New User: " + temp.toString());
        return new ResponseEntity<>("User created", HttpStatus.OK);
    }
}
