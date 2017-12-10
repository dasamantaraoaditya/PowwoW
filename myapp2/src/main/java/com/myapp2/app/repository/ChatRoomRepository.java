package com.myapp2.app.repository;

import com.myapp2.app.domain.ChatRoom;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the ChatRoom entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

	@Query("select chat_room from ChatRoom chat_room where chat_room.sent_from.login = ?#{principal.username}")
	List<ChatRoom> findBySent_fromIsCurrentUser();

	@Query("select chat_room from ChatRoom chat_room where chat_room.sent_to.login = ?#{principal.username}")
	List<ChatRoom> findBySent_toIsCurrentUser();

	@Query("select chat_room from ChatRoom chat_room where chat_room.sent_from.login = ?#{principal.username} and chat_room.sent_to.id = (:sentToId))")
	List<ChatRoom> findBySent_toAndCurrentUser(@Param("sentToId") Long id);

	@Query("select chat_room from ChatRoom chat_room where chat_room.sent_to.login = ?#{principal.username} and chat_room.sent_from.id = (:sentFromId))")
	List<ChatRoom> findBySent_fromAndCurrentUser(@Param("sentFromId") Long id);

}