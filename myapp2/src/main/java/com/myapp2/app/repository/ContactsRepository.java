package com.myapp2.app.repository;

import com.myapp2.app.domain.Contacts;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
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

}
