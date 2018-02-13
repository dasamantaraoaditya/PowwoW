package com.myapp2.app.repository;

import com.myapp2.app.domain.Userprofile;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Userprofile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserprofileRepository extends JpaRepository<Userprofile, Long> {

}
