package com.myapp2.app.repository;

import com.myapp2.app.domain.User;
import com.myapp2.app.domain.Userprofile;
import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;


/**
 * Spring Data JPA repository for the Userprofile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserprofileRepository extends JpaRepository<Userprofile, Long> {
	
	@Query("select userprofile from Userprofile userprofile where userprofile.userid.id = :userId")
	List<Userprofile> findByUserId(@Param("userId") Long userId);
}
