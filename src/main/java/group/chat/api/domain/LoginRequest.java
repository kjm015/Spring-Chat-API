package group.chat.api.domain;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LoginRequest {

	@NotBlank
	private String name;

	@NotBlank
	private String password;

}
