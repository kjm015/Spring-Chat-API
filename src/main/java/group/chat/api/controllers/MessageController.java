package group.chat.api.controllers;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import group.chat.api.domain.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import group.chat.api.repository.MessageRepository;
import group.chat.api.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/message")
@CrossOrigin
    public class MessageController {

    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private UserRepository userRepository;

    ObjectMapper mapper = new ObjectMapper();
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value = "/send", method = RequestMethod.PUT, produces = "text/json")
    public ResponseEntity<?> postMessage(@RequestParam(value="text") String text, @RequestParam(value="id")Integer id) throws JsonProcessingException{
        if(text.length() !=0) {
            Message temp = new Message();
            temp.setMessage(text);
            temp.setUser(userRepository.findOne(id));
            messageRepository.save(temp);
            logger.info("New Message: " + temp.toString());
            return new ResponseEntity<>(mapper.writeValueAsString(temp), HttpStatus.OK);
        }
        return null;
    }
    @RequestMapping(value = "/getMessages", method = RequestMethod.GET, produces = "text/json")
    public ResponseEntity<?> getMessages(@RequestParam(value="id") int id) throws JsonProcessingException {
        List<Message> tempMsg = messageRepository.findByIdGreaterThanEqualOrderByIdAsc(id);
        return new ResponseEntity<>(mapper.writeValueAsString(tempMsg),HttpStatus.OK);
    }
}
