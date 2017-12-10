package com.myapp2.app.web.rest;

import com.myapp2.app.Myapp2App;

import com.myapp2.app.domain.ChatRoom;
import com.myapp2.app.domain.User;
import com.myapp2.app.domain.User;
import com.myapp2.app.repository.ChatRoomRepository;
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

/**
 * Test class for the ChatRoomResource REST controller.
 *
 * @see ChatRoomResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Myapp2App.class)
public class ChatRoomResourceIntTest {

    private static final String DEFAULT_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_MESSAGE = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_SENT_ON = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_SENT_ON = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Boolean DEFAULT_IS_VISIBLE_TO_SENDER = false;
    private static final Boolean UPDATED_IS_VISIBLE_TO_SENDER = true;

    private static final Boolean DEFAULT_IS_VISIBLE_TO_RECIVER = false;
    private static final Boolean UPDATED_IS_VISIBLE_TO_RECIVER = true;

    private static final Boolean DEFAULT_IS_READ = false;
    private static final Boolean UPDATED_IS_READ = true;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restChatRoomMockMvc;

    private ChatRoom chatRoom;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ChatRoomResource chatRoomResource = new ChatRoomResource(chatRoomRepository);
        this.restChatRoomMockMvc = MockMvcBuilders.standaloneSetup(chatRoomResource)
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
    public static ChatRoom createEntity(EntityManager em) {
        ChatRoom chatRoom = new ChatRoom()
            .message(DEFAULT_MESSAGE)
            .sent_on(DEFAULT_SENT_ON)
            .is_visible_to_sender(DEFAULT_IS_VISIBLE_TO_SENDER)
            .is_visible_to_reciver(DEFAULT_IS_VISIBLE_TO_RECIVER)
            .is_read(DEFAULT_IS_READ);
        // Add required entity
        User sent_from = UserResourceIntTest.createEntity(em);
        em.persist(sent_from);
        em.flush();
        chatRoom.setSent_from(sent_from);
        // Add required entity
        User sent_to = UserResourceIntTest.createEntity(em);
        em.persist(sent_to);
        em.flush();
        chatRoom.setSent_to(sent_to);
        return chatRoom;
    }

    @Before
    public void initTest() {
        chatRoom = createEntity(em);
    }

    @Test
    @Transactional
    public void createChatRoom() throws Exception {
        int databaseSizeBeforeCreate = chatRoomRepository.findAll().size();

        // Create the ChatRoom
        restChatRoomMockMvc.perform(post("/api/chat-rooms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chatRoom)))
            .andExpect(status().isCreated());

        // Validate the ChatRoom in the database
        List<ChatRoom> chatRoomList = chatRoomRepository.findAll();
        assertThat(chatRoomList).hasSize(databaseSizeBeforeCreate + 1);
        ChatRoom testChatRoom = chatRoomList.get(chatRoomList.size() - 1);
        assertThat(testChatRoom.getMessage()).isEqualTo(DEFAULT_MESSAGE);
        assertThat(testChatRoom.getSent_on()).isEqualTo(DEFAULT_SENT_ON);
        assertThat(testChatRoom.isIs_visible_to_sender()).isEqualTo(DEFAULT_IS_VISIBLE_TO_SENDER);
        assertThat(testChatRoom.isIs_visible_to_reciver()).isEqualTo(DEFAULT_IS_VISIBLE_TO_RECIVER);
        assertThat(testChatRoom.isIs_read()).isEqualTo(DEFAULT_IS_READ);
    }

    @Test
    @Transactional
    public void createChatRoomWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = chatRoomRepository.findAll().size();

        // Create the ChatRoom with an existing ID
        chatRoom.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restChatRoomMockMvc.perform(post("/api/chat-rooms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chatRoom)))
            .andExpect(status().isBadRequest());

        // Validate the ChatRoom in the database
        List<ChatRoom> chatRoomList = chatRoomRepository.findAll();
        assertThat(chatRoomList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkMessageIsRequired() throws Exception {
        int databaseSizeBeforeTest = chatRoomRepository.findAll().size();
        // set the field null
        chatRoom.setMessage(null);

        // Create the ChatRoom, which fails.

        restChatRoomMockMvc.perform(post("/api/chat-rooms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chatRoom)))
            .andExpect(status().isBadRequest());

        List<ChatRoom> chatRoomList = chatRoomRepository.findAll();
        assertThat(chatRoomList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSent_onIsRequired() throws Exception {
        int databaseSizeBeforeTest = chatRoomRepository.findAll().size();
        // set the field null
        chatRoom.setSent_on(null);

        // Create the ChatRoom, which fails.

        restChatRoomMockMvc.perform(post("/api/chat-rooms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chatRoom)))
            .andExpect(status().isBadRequest());

        List<ChatRoom> chatRoomList = chatRoomRepository.findAll();
        assertThat(chatRoomList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIs_visible_to_senderIsRequired() throws Exception {
        int databaseSizeBeforeTest = chatRoomRepository.findAll().size();
        // set the field null
        chatRoom.setIs_visible_to_sender(null);

        // Create the ChatRoom, which fails.

        restChatRoomMockMvc.perform(post("/api/chat-rooms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chatRoom)))
            .andExpect(status().isBadRequest());

        List<ChatRoom> chatRoomList = chatRoomRepository.findAll();
        assertThat(chatRoomList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIs_visible_to_reciverIsRequired() throws Exception {
        int databaseSizeBeforeTest = chatRoomRepository.findAll().size();
        // set the field null
        chatRoom.setIs_visible_to_reciver(null);

        // Create the ChatRoom, which fails.

        restChatRoomMockMvc.perform(post("/api/chat-rooms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chatRoom)))
            .andExpect(status().isBadRequest());

        List<ChatRoom> chatRoomList = chatRoomRepository.findAll();
        assertThat(chatRoomList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIs_readIsRequired() throws Exception {
        int databaseSizeBeforeTest = chatRoomRepository.findAll().size();
        // set the field null
        chatRoom.setIs_read(null);

        // Create the ChatRoom, which fails.

        restChatRoomMockMvc.perform(post("/api/chat-rooms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chatRoom)))
            .andExpect(status().isBadRequest());

        List<ChatRoom> chatRoomList = chatRoomRepository.findAll();
        assertThat(chatRoomList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllChatRooms() throws Exception {
        // Initialize the database
        chatRoomRepository.saveAndFlush(chatRoom);

        // Get all the chatRoomList
        restChatRoomMockMvc.perform(get("/api/chat-rooms?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(chatRoom.getId().intValue())))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE.toString())))
            .andExpect(jsonPath("$.[*].sent_on").value(hasItem(sameInstant(DEFAULT_SENT_ON))))
            .andExpect(jsonPath("$.[*].is_visible_to_sender").value(hasItem(DEFAULT_IS_VISIBLE_TO_SENDER.booleanValue())))
            .andExpect(jsonPath("$.[*].is_visible_to_reciver").value(hasItem(DEFAULT_IS_VISIBLE_TO_RECIVER.booleanValue())))
            .andExpect(jsonPath("$.[*].is_read").value(hasItem(DEFAULT_IS_READ.booleanValue())));
    }

    @Test
    @Transactional
    public void getChatRoom() throws Exception {
        // Initialize the database
        chatRoomRepository.saveAndFlush(chatRoom);

        // Get the chatRoom
        restChatRoomMockMvc.perform(get("/api/chat-rooms/{id}", chatRoom.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(chatRoom.getId().intValue()))
            .andExpect(jsonPath("$.message").value(DEFAULT_MESSAGE.toString()))
            .andExpect(jsonPath("$.sent_on").value(sameInstant(DEFAULT_SENT_ON)))
            .andExpect(jsonPath("$.is_visible_to_sender").value(DEFAULT_IS_VISIBLE_TO_SENDER.booleanValue()))
            .andExpect(jsonPath("$.is_visible_to_reciver").value(DEFAULT_IS_VISIBLE_TO_RECIVER.booleanValue()))
            .andExpect(jsonPath("$.is_read").value(DEFAULT_IS_READ.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingChatRoom() throws Exception {
        // Get the chatRoom
        restChatRoomMockMvc.perform(get("/api/chat-rooms/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateChatRoom() throws Exception {
        // Initialize the database
        chatRoomRepository.saveAndFlush(chatRoom);
        int databaseSizeBeforeUpdate = chatRoomRepository.findAll().size();

        // Update the chatRoom
        ChatRoom updatedChatRoom = chatRoomRepository.findOne(chatRoom.getId());
        updatedChatRoom
            .message(UPDATED_MESSAGE)
            .sent_on(UPDATED_SENT_ON)
            .is_visible_to_sender(UPDATED_IS_VISIBLE_TO_SENDER)
            .is_visible_to_reciver(UPDATED_IS_VISIBLE_TO_RECIVER)
            .is_read(UPDATED_IS_READ);

        restChatRoomMockMvc.perform(put("/api/chat-rooms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedChatRoom)))
            .andExpect(status().isOk());

        // Validate the ChatRoom in the database
        List<ChatRoom> chatRoomList = chatRoomRepository.findAll();
        assertThat(chatRoomList).hasSize(databaseSizeBeforeUpdate);
        ChatRoom testChatRoom = chatRoomList.get(chatRoomList.size() - 1);
        assertThat(testChatRoom.getMessage()).isEqualTo(UPDATED_MESSAGE);
        assertThat(testChatRoom.getSent_on()).isEqualTo(UPDATED_SENT_ON);
        assertThat(testChatRoom.isIs_visible_to_sender()).isEqualTo(UPDATED_IS_VISIBLE_TO_SENDER);
        assertThat(testChatRoom.isIs_visible_to_reciver()).isEqualTo(UPDATED_IS_VISIBLE_TO_RECIVER);
        assertThat(testChatRoom.isIs_read()).isEqualTo(UPDATED_IS_READ);
    }

    @Test
    @Transactional
    public void updateNonExistingChatRoom() throws Exception {
        int databaseSizeBeforeUpdate = chatRoomRepository.findAll().size();

        // Create the ChatRoom

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restChatRoomMockMvc.perform(put("/api/chat-rooms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chatRoom)))
            .andExpect(status().isCreated());

        // Validate the ChatRoom in the database
        List<ChatRoom> chatRoomList = chatRoomRepository.findAll();
        assertThat(chatRoomList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteChatRoom() throws Exception {
        // Initialize the database
        chatRoomRepository.saveAndFlush(chatRoom);
        int databaseSizeBeforeDelete = chatRoomRepository.findAll().size();

        // Get the chatRoom
        restChatRoomMockMvc.perform(delete("/api/chat-rooms/{id}", chatRoom.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ChatRoom> chatRoomList = chatRoomRepository.findAll();
        assertThat(chatRoomList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ChatRoom.class);
        ChatRoom chatRoom1 = new ChatRoom();
        chatRoom1.setId(1L);
        ChatRoom chatRoom2 = new ChatRoom();
        chatRoom2.setId(chatRoom1.getId());
        assertThat(chatRoom1).isEqualTo(chatRoom2);
        chatRoom2.setId(2L);
        assertThat(chatRoom1).isNotEqualTo(chatRoom2);
        chatRoom1.setId(null);
        assertThat(chatRoom1).isNotEqualTo(chatRoom2);
    }
}
