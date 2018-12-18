package group.chat.api.domain;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class MessageRequest {

	@NotBlank
	private String text;

	@NotNull
	private Integer id;

	@NotBlank
	private String password;

}
