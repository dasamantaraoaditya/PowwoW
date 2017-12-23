/**
 * 
 */
package com.myapp2.app.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.myapp2.app.domain.Contacts;
import com.myapp2.app.domain.User;
import com.myapp2.app.domain.enumeration.ContactStatus;
import com.myapp2.app.repository.ChatRoomRepository;
import com.myapp2.app.repository.ContactsRepository;
import com.myapp2.app.repository.UserRepository;
import com.myapp2.app.web.rest.errors.BadRequestAlertException;
import com.myapp2.app.web.rest.util.HeaderUtil;

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
		String aSearchText = searchtext + "%";
		List<Contacts> contacts = contactsRepository.findByUserIsCurrentUserAndNameLike(aSearchText);
		User currentUser = usersRepository.findByCurrentLoggedIn();
		List<User> users = usersRepository.findByFirstNameOrLastName(aSearchText);
		users.remove(currentUser);
		List<User> uniqueUsers = users
				.stream().filter(user -> (contacts.stream()
						.filter(contact -> contact.getContact().getId().equals(user.getId())).count()) < 1)
				.collect(Collectors.toList());
		for (User user : uniqueUsers) {
			Contacts c = new Contacts();
			c.setContact(user);
			c.setUser(currentUser);
			contacts.add(c);
		}
		return contacts;
	}

	/**
	 * POST /friends/addFriend: Create a new contacts.
	 *
	 * @param contacts
	 *            the contacts to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         contacts, or with status 400 (Bad Request) if the contacts has
	 *         already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/friends/addFriend")
	@Timed
	public ResponseEntity<Contacts> addFriendCreateContacts(@Valid @RequestBody Contacts contacts)
			throws URISyntaxException {
		log.debug("REST request to save Contacts : {}", contacts);
		if (contacts.getId() != null) {
			throw new BadRequestAlertException("A new contacts cannot already have an ID", ENTITY_NAME, "idexists");
		}
		Contacts result = contactsRepository.save(contacts);
		User user = contacts.getUser();
		contacts.setUser(contacts.getContact());
		contacts.setContact(user);
		contacts.setStatus(ContactStatus.ACCEPT_REQUEST);
		contacts.setId(null);
		contactsRepository.save(contacts);
		return ResponseEntity.created(new URI("/api/friends/addFriend/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
	}

	/**
	 * DELETE /friends/cancelRequest/:id : delete the "id" contacts.
	 *
	 * @param id
	 *            the id of the contacts to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/friends/cancelRequest/{id}")
	@Timed
	public ResponseEntity<Void> deleteFriendRequest(@PathVariable Long id) {
		log.debug("REST request to delete Contacts : {}", id);
		List<Contacts> contacts = contactsRepository.getFriendContacts(id);
		contactsRepository.delete(contacts);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

	/**
	 * Get /friends/acceptFriend: update new contacts.
	 *
	 * @param id:contact
	 *            the contacts to update
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         contacts, or with status 400 (Bad Request) if the contacts has
	 *         already an ID
	 */
	@GetMapping("/friends/acceptRequest/{id}")
	@Timed
	public void acceptFriendRequest(@PathVariable Long id) {
		log.debug("REST request to modfiy Contacts : {}", id);
		List<Contacts> contacts = contactsRepository.getFriendContacts(id);
		contacts.get(0).setStatus(ContactStatus.FRIEND);
		contacts.get(1).setStatus(ContactStatus.FRIEND);
		contacts = contactsRepository.save(contacts);
	}

}
