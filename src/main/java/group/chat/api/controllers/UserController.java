package group.chat.api.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import group.chat.api.domain.LoginRequest;
import group.chat.api.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;
import group.chat.api.repository.UserRepository;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

@Repository
@RequestMapping("/user")
@CrossOrigin
@Slf4j
public class UserController {

	@Autowired
	UserRepository userRepository;

	ObjectMapper mapper = new ObjectMapper();

	@PutMapping(value = "/register", produces = "text/json")
	public ResponseEntity<?> createUser(@RequestParam(value = "name") String name, @RequestParam(value = "password") String password) throws JsonProcessingException {
		name = name.trim();

		if (userRepository.findByName(name).size() != 0) {
			return new ResponseEntity<>(mapper.writeValueAsString("Username already exists."), HttpStatus.CONFLICT);
		}

		User temp = new User();
		temp.setPassword(password);
		temp.setName(name);
		temp.setHost(getHostName());
		userRepository.save(temp);

		log.info("New User: " + temp.toString());
		return new ResponseEntity<>(mapper.writeValueAsString(temp), HttpStatus.OK);
	}

	@PostMapping(path = "/login", produces = "text/json")
	public ResponseEntity<?> loginUser(@RequestBody LoginRequest request) {
		String name = request.getName();
		String password = request.getPassword();

		List<User> users = userRepository.findByName(name);

		ResponseEntity entity;

		if (users.size() == 0) {
			log.warn("Tried to login in as invalid user");
			entity = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found!");
		} else {
			User temp = users.get(0);

			// TODO: Encrypt stored passwords
			if (temp.getPassword().equals(password)) {
				log.info("Logged in: " + temp.toString());
				temp.setHost(getHostName());
				entity = ResponseEntity.ok("Login successful!");
			} else {
				entity = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password!");
			}
		}

		return entity;
	}

	private String getHostName() {
		try {
			return InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			log.warn("Could not find host location, ", e);
			return "unknown";
		}
	}

}
