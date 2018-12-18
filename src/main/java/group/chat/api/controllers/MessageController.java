package group.chat.api.controllers;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import group.chat.api.domain.Message;
import group.chat.api.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import group.chat.api.repository.MessageRepository;
import group.chat.api.repository.UserRepository;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;


@RestController
@RequestMapping("/message")
@CrossOrigin
@Slf4j
public class MessageController {

	@Autowired
	private MessageRepository messageRepository;

	@Autowired
	private UserRepository userRepository;

	ObjectMapper mapper = new ObjectMapper();

	@PutMapping("/send")
	public ResponseEntity<?> postMessage(@RequestParam(value = "text") String text, @RequestParam(value = "id") int id, @RequestParam(value = "password") String password) throws JsonProcessingException {
		User user = userRepository.findById(id).get();
		if (user == null) {
			return new ResponseEntity<>(mapper.writeValueAsString("User doesn't exist"), HttpStatus.INTERNAL_SERVER_ERROR);
		} else if (user.getPassword().equals(password)) {
			if (text.length() != 0) {
				Message temp = new Message();
				temp.setMessage(text);
				temp.setUser(userRepository.findById(id).get());
				temp.setHost(getHostName());
				messageRepository.save(temp);
				log.info("New Message: " + temp.toString());
				return new ResponseEntity<>(mapper.writeValueAsString(temp), HttpStatus.OK);
			} else {
				return new ResponseEntity<>(mapper.writeValueAsString("Message cannot be blank"), HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} else {
			return new ResponseEntity<>(mapper.writeValueAsString("Invalid Password"), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping(value = "/getMessages", produces = "text/json")
	public ResponseEntity<?> getMessages(@RequestParam("id") int id) throws JsonProcessingException {
		List<Message> tempMsg = messageRepository.findByIdGreaterThanEqualOrderByIdAsc(id);

		if (tempMsg.size() > 0) {
			tempMsg.get(0).setHost(getHostName());
		}

		return new ResponseEntity<>(mapper.writeValueAsString(tempMsg), HttpStatus.OK);
	}

	private String getHostName() {
		try {
			return InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException uhe) {
			return "unknown";
		}
	}

}
