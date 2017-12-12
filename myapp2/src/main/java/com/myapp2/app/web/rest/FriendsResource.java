/**
 * 
 */
package com.myapp2.app.web.rest;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.myapp2.app.domain.Contacts;
import com.myapp2.app.domain.User;
import com.myapp2.app.repository.ChatRoomRepository;
import com.myapp2.app.repository.ContactsRepository;
import com.myapp2.app.repository.UserRepository;

/**
 * @author aditya REST Controller for managing Friends
 */
@RestController
@RequestMapping("/api")
public class FriendsResource {
	private final Logger log = LoggerFactory.getLogger(FriendsResource.class);

	private static final String ENTITY_NAME = "friends";

	private final ContactsRepository contactsRepository;

	private final UserRepository usersRepository;

	public FriendsResource(ChatRoomRepository chatRoomRepository, ContactsRepository contactsRepository,
			UserRepository usersRepository) {
		this.contactsRepository = contactsRepository;
		this.usersRepository = usersRepository;
	}

	/**
	 * GET /friends: get all the contacts of FRIENDS.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of contacts in
	 *         body
	 */
	@GetMapping("/friends")
	@Timed
	public List<Contacts> getAllFriends() {
		log.debug("REST request to get a all contacts of FRIENDS");
		return contactsRepository.findByUserIsCurrentUser();
	}

	/**
	 * GET /friends/search: get all the contacts+users and construct a contacts
	 * object with existing user search result to send to ui.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of contacts in
	 *         body
	 */
	@GetMapping("/friends/search/{searchtext}")
	@Timed
	public List<Contacts> getAllContactsAndUsers(@PathVariable String searchtext) {
		log.debug("REST request to get a all contacts in search " + searchtext);
		String aSearchText = searchtext+"%";
		List<Contacts> contacts = contactsRepository.findByUserIsCurrentUserAndNameLike(aSearchText);
		List<User> users = usersRepository.findByFirstNameOrLastName(aSearchText);
		List<User> uniqueUsers = users
				.stream().filter(user -> (contacts.stream()
						.filter(contact -> contact.getContact().getId().equals(user.getId())).count()) < 1)
				.collect(Collectors.toList());
		for (User user : uniqueUsers) {
			Contacts c = new Contacts();
			c.setContact(user);
			contacts.add(c);
		}
		return contacts;
	}
}
