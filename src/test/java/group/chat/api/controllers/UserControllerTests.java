package group.chat.api.controllers;

import group.chat.api.domain.LoginRequest;
import group.chat.api.domain.SignUpRequest;
import group.chat.api.domain.User;
import group.chat.api.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(SpringRunner.class)
public class UserControllerTests {

	@Mock
	private PasswordEncoder encoder;

	@Mock
	private UserRepository repository;

	@InjectMocks
	private UserController controller;

	private User user;
	private SignUpRequest signUpRequest;
	private LoginRequest loginRequest;
	private List<User> users;

	@Before
	public void getItStarted() {
		initMocks(UserControllerTests.class);

		user = new User();
		user.setHost("http://cnn.com");
		user.setId(1);
		user.setName("Adam");
		user.setPassword("password1");

		signUpRequest = new SignUpRequest();
		signUpRequest.setName(user.getName());
		signUpRequest.setPassword(user.getPassword());

		loginRequest = new LoginRequest();
		loginRequest.setName(user.getName());
		loginRequest.setPassword(user.getPassword());

		users = new ArrayList<>();
	}

	@Test
	public void testUserCreation_andUserAlreadyExists() {
		when(repository.existsByName(user.getName())).thenReturn(true);
		assertTrue(controller.createUser(signUpRequest).getStatusCode().is4xxClientError());
	}

	@Test
	public void testUserCreation_andSuccess() {
		when(repository.existsByName(user.getName())).thenReturn(false);
		assertTrue(controller.createUser(signUpRequest).getStatusCode().is2xxSuccessful());
	}

	@Test
	public void testLogin_andSuccess() {
		users.add(user);

		when(repository.findByName(user.getName())).thenReturn(users);
		when(encoder.matches(anyString(), anyString())).thenReturn(true);

		assertTrue(controller.loginUser(loginRequest).getStatusCode().is2xxSuccessful());
	}

	@Test
	public void testLogin_andPasswordIncorrect() {
		users.add(user);
		when(repository.findByName(user.getName())).thenReturn(users);
		when(encoder.matches(anyString(), anyString())).thenReturn(false);

		assertTrue(controller.loginUser(loginRequest).getStatusCode().is4xxClientError());
	}

	@Test
	public void testLogin_andUsernameNotFound() {
		when(repository.findByName(user.getName())).thenReturn(users);
		assertTrue(controller.loginUser(loginRequest).getStatusCode().is4xxClientError());
	}

}
