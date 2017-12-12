/**
 * 
 */
package com.myapp2.app.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.myapp2.app.domain.ChatRoom;
import com.myapp2.app.domain.Contacts;
import com.myapp2.app.repository.ChatRoomRepository;
import com.myapp2.app.repository.ContactsRepository;
import com.myapp2.app.web.rest.errors.BadRequestAlertException;
import com.myapp2.app.web.rest.util.HeaderUtil;
import com.myapp2.app.web.rest.util.PaginationUtil;

import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;

/**
 * REST controller for managing ChatBox.
 */
@RestController
@RequestMapping("/api")
public class ChatBoxResource {
	
	private final Logger log = LoggerFactory.getLogger(ChatBoxResource.class);
	
	private static final String ENTITY_NAME = "chatRoom";
	
	private final ChatRoomRepository chatRoomRepository;
	
	private final ContactsRepository contactsRepository;
	
	public ChatBoxResource(ChatRoomRepository chatRoomRepository,ContactsRepository contactsRepository) {
        this.chatRoomRepository = chatRoomRepository;
        this.contactsRepository = contactsRepository;
    }
	
	/**
     * GET  /contacts: get all the contacts of FRIENDS.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of contacts in body
     */
    @GetMapping("/chat-box")
    @Timed
    public List<Contacts> getAllFriends() {
        log.debug("REST request to get a all contacts of FRIENDS");
        return contactsRepository.findByUserAndFriendStatusIsCurrentUser();
    }
    
    /**
     * GET  /ChatRoom : get all the chatRooms filter by Friend:USER ->Id
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of chatRooms in body
     */
    @GetMapping("/chat-box/{id}")
    @Timed
    public ResponseEntity<List<ChatRoom>> getFriendChatRooms(@PathVariable Long id) {
        log.debug("REST request to get All Friend ChatRoom : {}", id);
        List<ChatRoom> chatRooms = chatRoomRepository.findBySent_fromAndCurrentUser(id);
        chatRooms.forEach(chatRoom -> chatRoom.setIs_read(true));
        chatRoomRepository.save(chatRooms);
        chatRooms.addAll(chatRoomRepository.findBySent_toAndCurrentUser(id));
        Collections.sort(chatRooms, (o1, o2) -> o1.getSent_on().compareTo(o2.getSent_on()));
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(chatRooms));
    }
    
    /**
     * POST  /chat-rooms : Create a new chatRoom.
     *
     * @param chatRoom the chatRoom to create
     * @return the ResponseEntity with status 201 (Created) and with body the new chatRoom, or with status 400 (Bad Request) if the chatRoom has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/chat-box")
    @Timed
    public ResponseEntity<ChatRoom> createChatRoom(@Valid @RequestBody ChatRoom chatRoom) throws URISyntaxException {
    	log.debug("REST request to save ChatRoom : {}", chatRoom);
        if (chatRoom.getId() != null) {
            throw new BadRequestAlertException("A new chatRoom cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ChatRoom result = chatRoomRepository.save(chatRoom);
        return ResponseEntity.created(new URI("/api/chat-box/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
}
