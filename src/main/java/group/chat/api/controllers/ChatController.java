package group.chat.api.controllers;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import group.chat.api.dao.ChatDao;
import group.chat.api.dao.UserDao;
import group.chat.api.domain.Message;
import group.chat.api.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/message")
@CrossOrigin
    public class ChatController {
    @Autowired
        ChatDao chatDao;
    @Autowired
        UserDao userDao;

    ObjectMapper mapper = new ObjectMapper();
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final AtomicInteger counter = new AtomicInteger();

    @RequestMapping(value = "/send", method = RequestMethod.PUT, produces = "text/json")
    public ResponseEntity<?> postMessage(@RequestParam(value="text") String text, @RequestParam(value="id")int id) throws JsonProcessingException{
        Message temp  = new Message(counter.incrementAndGet(), userDao.getUserById(id), text, "12:15");
        chatDao.addMessage(temp);
        logger.info("New Message: " + temp.toString());
        return new ResponseEntity<>(mapper.writeValueAsString(temp),HttpStatus.OK);
    }
    @RequestMapping(value = "/getMessages", method = RequestMethod.GET, produces = "text/json")
    public ResponseEntity<?> getMessages(@RequestParam(value="id") int id) throws JsonProcessingException {
        //logger.info("sent messages");
        return new ResponseEntity<>(mapper.writeValueAsString(chatDao.getAllMessagesFrom(id)), HttpStatus.OK);
    }
}
