package group.chat.api.controllers;

import group.chat.api.domain.Message;
import group.chat.api.domain.MessageRequest;
import group.chat.api.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import group.chat.api.repository.MessageRepository;
import group.chat.api.repository.UserRepository;

import javax.validation.Valid;
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

	@Autowired
	private PasswordEncoder encoder;

	@PostMapping(path = "/send", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> postMessage(@RequestBody @Valid MessageRequest request) {
		ResponseEntity entity;

		Integer id = request.getId();
		String text = request.getText();
		String password = request.getPassword();

		if (userRepository.findById(id).isPresent()) {
			User user = userRepository.findById(id).get();

			if (encoder.matches(password, user.getPassword())) {
				Message message = new Message();
				message.setMessage(text);
				message.setUser(userRepository.findById(id).get());
				message.setHost(getHostName());
				messageRepository.save(message);

				log.info("New Message: " + message.toString());
				entity = ResponseEntity.ok("Message created: " + message.toString());
			} else {
				entity = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password!");
			}
		} else {
			entity = ResponseEntity.status(HttpStatus.NOT_FOUND).body("That user does not exist!");
		}

		return entity;
	}

	@GetMapping(value = "/getMessages", produces = "application/json")
	public ResponseEntity<?> getMessages(@RequestParam("id") int id) {
		List<Message> messages = messageRepository.findByIdGreaterThanEqualOrderByIdAsc(id);

		if (messages.size() > 0) {
			messages.get(0).setHost(getHostName());
		}

		return ResponseEntity.ok(messages);
	}

	private String getHostName() {
		try {
			return InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			log.warn("Could not retrieve hostname");
			return "unknown";
		}
	}

}
