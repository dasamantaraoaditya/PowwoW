package com.myapp2.app.web.rest;

import com.myapp2.app.Myapp2App;

import com.myapp2.app.domain.Userprofile;
import com.myapp2.app.domain.User;
import com.myapp2.app.repository.UserprofileRepository;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static com.myapp2.app.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the UserprofileResource REST controller.
 *
 * @see UserprofileResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Myapp2App.class)
public class UserprofileResourceIntTest {

    private static final String DEFAULT_PROFILEPICTURE = "AAAAAAAAAA";
    private static final String UPDATED_PROFILEPICTURE = "BBBBBBBBBB";

    private static final String DEFAULT_ABOUT = "AAAAAAAAAA";
    private static final String UPDATED_ABOUT = "BBBBBBBBBB";

    private static final String DEFAULT_NICNAME = "AAAAAAAAAA";
    private static final String UPDATED_NICNAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATEOFBIRTH = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATEOFBIRTH = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACTNO = "AAAAAAAAAA";
    private static final String UPDATED_CONTACTNO = "BBBBBBBBBB";

    private static final String DEFAULT_FACEBOOKLINK = "AAAAAAAAAA";
    private static final String UPDATED_FACEBOOKLINK = "BBBBBBBBBB";

    private static final String DEFAULT_GOOGLEPLUSLINK = "AAAAAAAAAA";
    private static final String UPDATED_GOOGLEPLUSLINK = "BBBBBBBBBB";

    private static final String DEFAULT_LINKEDINLINK = "AAAAAAAAAA";
    private static final String UPDATED_LINKEDINLINK = "BBBBBBBBBB";

    private static final String DEFAULT_TWITTERLINK = "AAAAAAAAAA";
    private static final String UPDATED_TWITTERLINK = "BBBBBBBBBB";

    private static final String DEFAULT_INSTAGRAMLINK = "AAAAAAAAAA";
    private static final String UPDATED_INSTAGRAMLINK = "BBBBBBBBBB";

    @Autowired
    private UserprofileRepository userprofileRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restUserprofileMockMvc;

    private Userprofile userprofile;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final UserprofileResource userprofileResource = new UserprofileResource(userprofileRepository);
        this.restUserprofileMockMvc = MockMvcBuilders.standaloneSetup(userprofileResource)
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
    public static Userprofile createEntity(EntityManager em) {
        Userprofile userprofile = new Userprofile()
            .profilepicture(DEFAULT_PROFILEPICTURE)
            .about(DEFAULT_ABOUT)
            .nicname(DEFAULT_NICNAME)
            .dateofbirth(DEFAULT_DATEOFBIRTH)
            .address(DEFAULT_ADDRESS)
            .contactno(DEFAULT_CONTACTNO)
            .facebooklink(DEFAULT_FACEBOOKLINK)
            .googlepluslink(DEFAULT_GOOGLEPLUSLINK)
            .linkedinlink(DEFAULT_LINKEDINLINK)
            .twitterlink(DEFAULT_TWITTERLINK)
            .instagramlink(DEFAULT_INSTAGRAMLINK);
        // Add required entity
        User userid = UserResourceIntTest.createEntity(em);
        em.persist(userid);
        em.flush();
        userprofile.setUserid(userid);
        return userprofile;
    }

    @Before
    public void initTest() {
        userprofile = createEntity(em);
    }

    @Test
    @Transactional
    public void createUserprofile() throws Exception {
        int databaseSizeBeforeCreate = userprofileRepository.findAll().size();

        // Create the Userprofile
        restUserprofileMockMvc.perform(post("/api/userprofiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userprofile)))
            .andExpect(status().isCreated());

        // Validate the Userprofile in the database
        List<Userprofile> userprofileList = userprofileRepository.findAll();
        assertThat(userprofileList).hasSize(databaseSizeBeforeCreate + 1);
        Userprofile testUserprofile = userprofileList.get(userprofileList.size() - 1);
        assertThat(testUserprofile.getProfilepicture()).isEqualTo(DEFAULT_PROFILEPICTURE);
        assertThat(testUserprofile.getAbout()).isEqualTo(DEFAULT_ABOUT);
        assertThat(testUserprofile.getNicname()).isEqualTo(DEFAULT_NICNAME);
        assertThat(testUserprofile.getDateofbirth()).isEqualTo(DEFAULT_DATEOFBIRTH);
        assertThat(testUserprofile.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testUserprofile.getContactno()).isEqualTo(DEFAULT_CONTACTNO);
        assertThat(testUserprofile.getFacebooklink()).isEqualTo(DEFAULT_FACEBOOKLINK);
        assertThat(testUserprofile.getGooglepluslink()).isEqualTo(DEFAULT_GOOGLEPLUSLINK);
        assertThat(testUserprofile.getLinkedinlink()).isEqualTo(DEFAULT_LINKEDINLINK);
        assertThat(testUserprofile.getTwitterlink()).isEqualTo(DEFAULT_TWITTERLINK);
        assertThat(testUserprofile.getInstagramlink()).isEqualTo(DEFAULT_INSTAGRAMLINK);
    }

    @Test
    @Transactional
    public void createUserprofileWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userprofileRepository.findAll().size();

        // Create the Userprofile with an existing ID
        userprofile.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserprofileMockMvc.perform(post("/api/userprofiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userprofile)))
            .andExpect(status().isBadRequest());

        // Validate the Userprofile in the database
        List<Userprofile> userprofileList = userprofileRepository.findAll();
        assertThat(userprofileList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllUserprofiles() throws Exception {
        // Initialize the database
        userprofileRepository.saveAndFlush(userprofile);

        // Get all the userprofileList
        restUserprofileMockMvc.perform(get("/api/userprofiles?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userprofile.getId().intValue())))
            .andExpect(jsonPath("$.[*].profilepicture").value(hasItem(DEFAULT_PROFILEPICTURE.toString())))
            .andExpect(jsonPath("$.[*].about").value(hasItem(DEFAULT_ABOUT.toString())))
            .andExpect(jsonPath("$.[*].nicname").value(hasItem(DEFAULT_NICNAME.toString())))
            .andExpect(jsonPath("$.[*].dateofbirth").value(hasItem(DEFAULT_DATEOFBIRTH.toString())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].contactno").value(hasItem(DEFAULT_CONTACTNO.toString())))
            .andExpect(jsonPath("$.[*].facebooklink").value(hasItem(DEFAULT_FACEBOOKLINK.toString())))
            .andExpect(jsonPath("$.[*].googlepluslink").value(hasItem(DEFAULT_GOOGLEPLUSLINK.toString())))
            .andExpect(jsonPath("$.[*].linkedinlink").value(hasItem(DEFAULT_LINKEDINLINK.toString())))
            .andExpect(jsonPath("$.[*].twitterlink").value(hasItem(DEFAULT_TWITTERLINK.toString())))
            .andExpect(jsonPath("$.[*].instagramlink").value(hasItem(DEFAULT_INSTAGRAMLINK.toString())));
    }

    @Test
    @Transactional
    public void getUserprofile() throws Exception {
        // Initialize the database
        userprofileRepository.saveAndFlush(userprofile);

        // Get the userprofile
        restUserprofileMockMvc.perform(get("/api/userprofiles/{id}", userprofile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(userprofile.getId().intValue()))
            .andExpect(jsonPath("$.profilepicture").value(DEFAULT_PROFILEPICTURE.toString()))
            .andExpect(jsonPath("$.about").value(DEFAULT_ABOUT.toString()))
            .andExpect(jsonPath("$.nicname").value(DEFAULT_NICNAME.toString()))
            .andExpect(jsonPath("$.dateofbirth").value(DEFAULT_DATEOFBIRTH.toString()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS.toString()))
            .andExpect(jsonPath("$.contactno").value(DEFAULT_CONTACTNO.toString()))
            .andExpect(jsonPath("$.facebooklink").value(DEFAULT_FACEBOOKLINK.toString()))
            .andExpect(jsonPath("$.googlepluslink").value(DEFAULT_GOOGLEPLUSLINK.toString()))
            .andExpect(jsonPath("$.linkedinlink").value(DEFAULT_LINKEDINLINK.toString()))
            .andExpect(jsonPath("$.twitterlink").value(DEFAULT_TWITTERLINK.toString()))
            .andExpect(jsonPath("$.instagramlink").value(DEFAULT_INSTAGRAMLINK.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingUserprofile() throws Exception {
        // Get the userprofile
        restUserprofileMockMvc.perform(get("/api/userprofiles/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserprofile() throws Exception {
        // Initialize the database
        userprofileRepository.saveAndFlush(userprofile);
        int databaseSizeBeforeUpdate = userprofileRepository.findAll().size();

        // Update the userprofile
        Userprofile updatedUserprofile = userprofileRepository.findOne(userprofile.getId());
        updatedUserprofile
            .profilepicture(UPDATED_PROFILEPICTURE)
            .about(UPDATED_ABOUT)
            .nicname(UPDATED_NICNAME)
            .dateofbirth(UPDATED_DATEOFBIRTH)
            .address(UPDATED_ADDRESS)
            .contactno(UPDATED_CONTACTNO)
            .facebooklink(UPDATED_FACEBOOKLINK)
            .googlepluslink(UPDATED_GOOGLEPLUSLINK)
            .linkedinlink(UPDATED_LINKEDINLINK)
            .twitterlink(UPDATED_TWITTERLINK)
            .instagramlink(UPDATED_INSTAGRAMLINK);

        restUserprofileMockMvc.perform(put("/api/userprofiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedUserprofile)))
            .andExpect(status().isOk());

        // Validate the Userprofile in the database
        List<Userprofile> userprofileList = userprofileRepository.findAll();
        assertThat(userprofileList).hasSize(databaseSizeBeforeUpdate);
        Userprofile testUserprofile = userprofileList.get(userprofileList.size() - 1);
        assertThat(testUserprofile.getProfilepicture()).isEqualTo(UPDATED_PROFILEPICTURE);
        assertThat(testUserprofile.getAbout()).isEqualTo(UPDATED_ABOUT);
        assertThat(testUserprofile.getNicname()).isEqualTo(UPDATED_NICNAME);
        assertThat(testUserprofile.getDateofbirth()).isEqualTo(UPDATED_DATEOFBIRTH);
        assertThat(testUserprofile.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testUserprofile.getContactno()).isEqualTo(UPDATED_CONTACTNO);
        assertThat(testUserprofile.getFacebooklink()).isEqualTo(UPDATED_FACEBOOKLINK);
        assertThat(testUserprofile.getGooglepluslink()).isEqualTo(UPDATED_GOOGLEPLUSLINK);
        assertThat(testUserprofile.getLinkedinlink()).isEqualTo(UPDATED_LINKEDINLINK);
        assertThat(testUserprofile.getTwitterlink()).isEqualTo(UPDATED_TWITTERLINK);
        assertThat(testUserprofile.getInstagramlink()).isEqualTo(UPDATED_INSTAGRAMLINK);
    }

    @Test
    @Transactional
    public void updateNonExistingUserprofile() throws Exception {
        int databaseSizeBeforeUpdate = userprofileRepository.findAll().size();

        // Create the Userprofile

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restUserprofileMockMvc.perform(put("/api/userprofiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userprofile)))
            .andExpect(status().isCreated());

        // Validate the Userprofile in the database
        List<Userprofile> userprofileList = userprofileRepository.findAll();
        assertThat(userprofileList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteUserprofile() throws Exception {
        // Initialize the database
        userprofileRepository.saveAndFlush(userprofile);
        int databaseSizeBeforeDelete = userprofileRepository.findAll().size();

        // Get the userprofile
        restUserprofileMockMvc.perform(delete("/api/userprofiles/{id}", userprofile.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Userprofile> userprofileList = userprofileRepository.findAll();
        assertThat(userprofileList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Userprofile.class);
        Userprofile userprofile1 = new Userprofile();
        userprofile1.setId(1L);
        Userprofile userprofile2 = new Userprofile();
        userprofile2.setId(userprofile1.getId());
        assertThat(userprofile1).isEqualTo(userprofile2);
        userprofile2.setId(2L);
        assertThat(userprofile1).isNotEqualTo(userprofile2);
        userprofile1.setId(null);
        assertThat(userprofile1).isNotEqualTo(userprofile2);
    }
}
