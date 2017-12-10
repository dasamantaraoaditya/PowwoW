package com.myapp2.app.web.rest;

import com.myapp2.app.Myapp2App;

import com.myapp2.app.domain.Contacts;
import com.myapp2.app.domain.User;
import com.myapp2.app.domain.User;
import com.myapp2.app.repository.ContactsRepository;
import com.myapp2.app.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.myapp2.app.web.rest.TestUtil.sameInstant;
import static com.myapp2.app.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.myapp2.app.domain.enumeration.ContactStatus;
/**
 * Test class for the ContactsResource REST controller.
 *
 * @see ContactsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Myapp2App.class)
public class ContactsResourceIntTest {

    private static final ZonedDateTime DEFAULT_CREATED_ON = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_ON = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ContactStatus DEFAULT_STATUS = ContactStatus.REQUEST_SENT;
    private static final ContactStatus UPDATED_STATUS = ContactStatus.ACCEPT_REQUEST;

    @Autowired
    private ContactsRepository contactsRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restContactsMockMvc;

    private Contacts contacts;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ContactsResource contactsResource = new ContactsResource(contactsRepository);
        this.restContactsMockMvc = MockMvcBuilders.standaloneSetup(contactsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Contacts createEntity(EntityManager em) {
        Contacts contacts = new Contacts()
            .created_on(DEFAULT_CREATED_ON)
            .status(DEFAULT_STATUS);
        // Add required entity
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        contacts.setUser(user);
        // Add required entity
        User contact = UserResourceIntTest.createEntity(em);
        em.persist(contact);
        em.flush();
        contacts.setContact(contact);
        return contacts;
    }

    @Before
    public void initTest() {
        contacts = createEntity(em);
    }

    @Test
    @Transactional
    public void createContacts() throws Exception {
        int databaseSizeBeforeCreate = contactsRepository.findAll().size();

        // Create the Contacts
        restContactsMockMvc.perform(post("/api/contacts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contacts)))
            .andExpect(status().isCreated());

        // Validate the Contacts in the database
        List<Contacts> contactsList = contactsRepository.findAll();
        assertThat(contactsList).hasSize(databaseSizeBeforeCreate + 1);
        Contacts testContacts = contactsList.get(contactsList.size() - 1);
        assertThat(testContacts.getCreated_on()).isEqualTo(DEFAULT_CREATED_ON);
        assertThat(testContacts.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createContactsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = contactsRepository.findAll().size();

        // Create the Contacts with an existing ID
        contacts.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restContactsMockMvc.perform(post("/api/contacts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contacts)))
            .andExpect(status().isBadRequest());

        // Validate the Contacts in the database
        List<Contacts> contactsList = contactsRepository.findAll();
        assertThat(contactsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCreated_onIsRequired() throws Exception {
        int databaseSizeBeforeTest = contactsRepository.findAll().size();
        // set the field null
        contacts.setCreated_on(null);

        // Create the Contacts, which fails.

        restContactsMockMvc.perform(post("/api/contacts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contacts)))
            .andExpect(status().isBadRequest());

        List<Contacts> contactsList = contactsRepository.findAll();
        assertThat(contactsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = contactsRepository.findAll().size();
        // set the field null
        contacts.setStatus(null);

        // Create the Contacts, which fails.

        restContactsMockMvc.perform(post("/api/contacts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contacts)))
            .andExpect(status().isBadRequest());

        List<Contacts> contactsList = contactsRepository.findAll();
        assertThat(contactsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllContacts() throws Exception {
        // Initialize the database
        contactsRepository.saveAndFlush(contacts);

        // Get all the contactsList
        restContactsMockMvc.perform(get("/api/contacts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contacts.getId().intValue())))
            .andExpect(jsonPath("$.[*].created_on").value(hasItem(sameInstant(DEFAULT_CREATED_ON))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getContacts() throws Exception {
        // Initialize the database
        contactsRepository.saveAndFlush(contacts);

        // Get the contacts
        restContactsMockMvc.perform(get("/api/contacts/{id}", contacts.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(contacts.getId().intValue()))
            .andExpect(jsonPath("$.created_on").value(sameInstant(DEFAULT_CREATED_ON)))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingContacts() throws Exception {
        // Get the contacts
        restContactsMockMvc.perform(get("/api/contacts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateContacts() throws Exception {
        // Initialize the database
        contactsRepository.saveAndFlush(contacts);
        int databaseSizeBeforeUpdate = contactsRepository.findAll().size();

        // Update the contacts
        Contacts updatedContacts = contactsRepository.findOne(contacts.getId());
        updatedContacts
            .created_on(UPDATED_CREATED_ON)
            .status(UPDATED_STATUS);

        restContactsMockMvc.perform(put("/api/contacts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedContacts)))
            .andExpect(status().isOk());

        // Validate the Contacts in the database
        List<Contacts> contactsList = contactsRepository.findAll();
        assertThat(contactsList).hasSize(databaseSizeBeforeUpdate);
        Contacts testContacts = contactsList.get(contactsList.size() - 1);
        assertThat(testContacts.getCreated_on()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testContacts.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingContacts() throws Exception {
        int databaseSizeBeforeUpdate = contactsRepository.findAll().size();

        // Create the Contacts

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restContactsMockMvc.perform(put("/api/contacts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contacts)))
            .andExpect(status().isCreated());

        // Validate the Contacts in the database
        List<Contacts> contactsList = contactsRepository.findAll();
        assertThat(contactsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteContacts() throws Exception {
        // Initialize the database
        contactsRepository.saveAndFlush(contacts);
        int databaseSizeBeforeDelete = contactsRepository.findAll().size();

        // Get the contacts
        restContactsMockMvc.perform(delete("/api/contacts/{id}", contacts.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Contacts> contactsList = contactsRepository.findAll();
        assertThat(contactsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Contacts.class);
        Contacts contacts1 = new Contacts();
        contacts1.setId(1L);
        Contacts contacts2 = new Contacts();
        contacts2.setId(contacts1.getId());
        assertThat(contacts1).isEqualTo(contacts2);
        contacts2.setId(2L);
        assertThat(contacts1).isNotEqualTo(contacts2);
        contacts1.setId(null);
        assertThat(contacts1).isNotEqualTo(contacts2);
    }
}
