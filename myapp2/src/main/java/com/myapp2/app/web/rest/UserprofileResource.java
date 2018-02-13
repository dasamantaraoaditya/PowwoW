package com.myapp2.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.myapp2.app.domain.Userprofile;

import com.myapp2.app.repository.UserprofileRepository;
import com.myapp2.app.web.rest.errors.BadRequestAlertException;
import com.myapp2.app.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Userprofile.
 */
@RestController
@RequestMapping("/api")
public class UserprofileResource {

    private final Logger log = LoggerFactory.getLogger(UserprofileResource.class);

    private static final String ENTITY_NAME = "userprofile";

    private final UserprofileRepository userprofileRepository;

    public UserprofileResource(UserprofileRepository userprofileRepository) {
        this.userprofileRepository = userprofileRepository;
    }

    /**
     * POST  /userprofiles : Create a new userprofile.
     *
     * @param userprofile the userprofile to create
     * @return the ResponseEntity with status 201 (Created) and with body the new userprofile, or with status 400 (Bad Request) if the userprofile has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/userprofiles")
    @Timed
    public ResponseEntity<Userprofile> createUserprofile(@Valid @RequestBody Userprofile userprofile) throws URISyntaxException {
        log.debug("REST request to save Userprofile : {}", userprofile);
        if (userprofile.getId() != null) {
            throw new BadRequestAlertException("A new userprofile cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Userprofile result = userprofileRepository.save(userprofile);
        return ResponseEntity.created(new URI("/api/userprofiles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /userprofiles : Updates an existing userprofile.
     *
     * @param userprofile the userprofile to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated userprofile,
     * or with status 400 (Bad Request) if the userprofile is not valid,
     * or with status 500 (Internal Server Error) if the userprofile couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/userprofiles")
    @Timed
    public ResponseEntity<Userprofile> updateUserprofile(@Valid @RequestBody Userprofile userprofile) throws URISyntaxException {
        log.debug("REST request to update Userprofile : {}", userprofile);
        if (userprofile.getId() == null) {
            return createUserprofile(userprofile);
        }
        Userprofile result = userprofileRepository.save(userprofile);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, userprofile.getId().toString()))
            .body(result);
    }

    /**
     * GET  /userprofiles : get all the userprofiles.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of userprofiles in body
     */
    @GetMapping("/userprofiles")
    @Timed
    public List<Userprofile> getAllUserprofiles() {
        log.debug("REST request to get all Userprofiles");
        return userprofileRepository.findAll();
        }

    /**
     * GET  /userprofiles/:id : get the "id" userprofile.
     *
     * @param id the id of the userprofile to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the userprofile, or with status 404 (Not Found)
     */
    @GetMapping("/userprofiles/{id}")
    @Timed
    public ResponseEntity<Userprofile> getUserprofile(@PathVariable Long id) {
        log.debug("REST request to get Userprofile : {}", id);
        Userprofile userprofile = userprofileRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(userprofile));
    }

    /**
     * DELETE  /userprofiles/:id : delete the "id" userprofile.
     *
     * @param id the id of the userprofile to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/userprofiles/{id}")
    @Timed
    public ResponseEntity<Void> deleteUserprofile(@PathVariable Long id) {
        log.debug("REST request to delete Userprofile : {}", id);
        userprofileRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
