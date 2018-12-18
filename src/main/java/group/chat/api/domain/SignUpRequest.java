package group.chat.api.domain;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class SignUpRequest {

	@Size(min = 3, max = 40)
	private String name;

	@Size(min = 3, max = 40)
	private String password;

}
