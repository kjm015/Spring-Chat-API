package group.chat.api.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
public class Message implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private User user;

	private String message;

	@JsonInclude()
	@Transient
	private String host;

	@Override
	public String toString() {
		return "Message{" +
				"id=" + id +
				", user=" + user +
				", message='" + message + '\'' +
				'}';
	}
}