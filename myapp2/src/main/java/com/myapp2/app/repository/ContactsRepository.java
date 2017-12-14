package com.myapp2.app.repository;

import com.myapp2.app.domain.Contacts;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Contacts entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContactsRepository extends JpaRepository<Contacts, Long> {

    @Query("select contacts from Contacts contacts where contacts.user.login = ?#{principal.username}")
    List<Contacts> findByUserIsCurrentUser();
    
    @Query("select contacts from Contacts contacts where contacts.user.login = ?#{principal.username} and contacts.status = 'FRIEND'")
    List<Contacts> findByUserAndFriendStatusIsCurrentUser();


    @Query("select contacts from Contacts contacts where contacts.contact.login = ?#{principal.username}")
    List<Contacts> findByContactIsCurrentUser();

    @Query("select contacts from Contacts contacts where contacts.user.login = ?#{principal.username} and CONCAT(contacts.contact.firstName ,' ', contacts.contact.lastName) LIKE (:searchtext))")
	List<Contacts> findByUserIsCurrentUserAndNameLike(@Param("searchtext") String searchtext);

    @Query("select contacts from Contacts contacts where (contacts.user.login = ?#{principal.username} AND contacts.contact.id = (:id)) OR (contacts.contact.login = ?#{principal.username} AND contacts.user.id = (:id))")
	List<Contacts> getFriendContacts(@Param("id") Long id);

}
