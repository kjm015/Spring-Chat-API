package group.chat.api.repository;

import group.chat.api.domain.Message;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends CrudRepository<Message, Integer> {
	List<Message> findByIdGreaterThanEqualOrderByIdAsc(Integer id);
}
