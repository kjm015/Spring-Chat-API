package group.chat.api.controllers;

import lombok.Data;

@Data
public class MessageRequest {

	private String text;
	private String password;
	private Long id;

}
