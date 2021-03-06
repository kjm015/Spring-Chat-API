package group.chat.api.controllers;

import group.chat.api.domain.LoginRequest;
import group.chat.api.domain.SignUpRequest;
import group.chat.api.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import group.chat.api.repository.UserRepository;

import javax.validation.Valid;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

@RestController
@RequestMapping("/user")
@CrossOrigin
@Slf4j
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder encoder;

	@PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createUser(@RequestBody @Valid SignUpRequest request) {
		String name = request.getName().trim();
		String password = encoder.encode(request.getPassword());

		ResponseEntity entity;

		if (userRepository.existsByName(name)) {
			entity = ResponseEntity.status(HttpStatus.CONFLICT)
					.body(String.format("User with name \"%s\" already exists.", name));
		} else {
			User user = new User();
			user.setPassword(password);
			user.setName(name);
			user.setHost(getHostName());
			userRepository.save(user);

			log.info("New User: " + user.toString());
			entity = ResponseEntity.status(HttpStatus.CREATED).body(user);
		}

		return entity;
	}

	@PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> loginUser(@RequestBody @Valid LoginRequest request) {
		String name = request.getName();
		String password = request.getPassword();

		List<User> users = userRepository.findByName(name);

		ResponseEntity entity;

		if (users.size() == 0) {
			log.warn("Tried to login in as invalid user");
			entity = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found!");
		} else {
			User user = users.get(0);

			if (encoder.matches(password, user.getPassword())) {
				log.info("Logged in: " + user.toString());
				user.setHost(getHostName());
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
