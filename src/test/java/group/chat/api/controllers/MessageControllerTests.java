package group.chat.api.controllers;

import group.chat.api.domain.Message;
import group.chat.api.domain.MessageRequest;
import group.chat.api.domain.User;
import group.chat.api.repository.MessageRepository;
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
import java.util.Optional;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(SpringRunner.class)
public class MessageControllerTests {

	@Mock
	private PasswordEncoder encoder;

	@Mock
	private MessageRepository messageRepository;

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private MessageController controller;

	private MessageRequest messageRequest;
	private Message message;
	private List<Message> messages;
	private User user;

	@Before
	public void goGetEm() {
		initMocks(MessageControllerTests.class);

		messageRequest = new MessageRequest();
		messageRequest.setPassword("password1");
		messageRequest.setId(1);
		messageRequest.setText("hey");

		user = new User();
		user.setHost("http://cnn.com");
		user.setId(1);
		user.setName("Adam");
		user.setPassword("password1");

		message = new Message();
		message.setMessage("hey");
		message.setHost("http://cnn.com");
		message.setId(1);
		message.setUser(user);

		messages = new ArrayList<>();
		messages.add(message);
	}

	@Test
	public void testMessagePost_andSucceed() {
		when(userRepository.findById(messageRequest.getId())).thenReturn(Optional.of(user));
		when(encoder.matches(anyString(), anyString())).thenReturn(true);

		assertTrue(controller.postMessage(messageRequest).getStatusCode().is2xxSuccessful());
	}

	@Test
	public void testMessagePost_andPasswordIncorrect() {
		when(userRepository.findById(messageRequest.getId())).thenReturn(Optional.of(user));
		when(encoder.matches(anyString(), anyString())).thenReturn(false);

		assertTrue(controller.postMessage(messageRequest).getStatusCode().is4xxClientError());
	}

	@Test
	public void testMessagePost_andUserNotFound() {
		when(userRepository.findById(messageRequest.getId())).thenReturn(Optional.empty());

		assertTrue(controller.postMessage(messageRequest).getStatusCode().is4xxClientError());
	}

	@Test
	public void testMessageRetrieval() {
		when(messageRepository.findByIdGreaterThanEqualOrderByIdAsc(messageRequest.getId())).thenReturn(messages);
		assertTrue(controller.getMessages(messageRequest.getId()).getStatusCode().is2xxSuccessful());
	}

}
