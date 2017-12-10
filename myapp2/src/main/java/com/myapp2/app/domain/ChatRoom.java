package com.myapp2.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A ChatRoom.
 */
@Entity
@Table(name = "chat_room")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ChatRoom implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 1)
    @Column(name = "message", nullable = false)
    private String message;

    @NotNull
    @Column(name = "sent_on", nullable = false)
    private ZonedDateTime sent_on;

    @NotNull
    @Column(name = "is_visible_to_sender", nullable = false)
    private Boolean is_visible_to_sender;

    @NotNull
    @Column(name = "is_visible_to_reciver", nullable = false)
    private Boolean is_visible_to_reciver;

    @NotNull
    @Column(name = "is_read", nullable = false)
    private Boolean is_read;

    @ManyToOne(optional = false)
    @NotNull
    private User sent_from;

    @ManyToOne(optional = false)
    @NotNull
    private User sent_to;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public ChatRoom message(String message) {
        this.message = message;
        return this;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ZonedDateTime getSent_on() {
        return sent_on;
    }

    public ChatRoom sent_on(ZonedDateTime sent_on) {
        this.sent_on = sent_on;
        return this;
    }

    public void setSent_on(ZonedDateTime sent_on) {
        this.sent_on = sent_on;
    }

    public Boolean isIs_visible_to_sender() {
        return is_visible_to_sender;
    }

    public ChatRoom is_visible_to_sender(Boolean is_visible_to_sender) {
        this.is_visible_to_sender = is_visible_to_sender;
        return this;
    }

    public void setIs_visible_to_sender(Boolean is_visible_to_sender) {
        this.is_visible_to_sender = is_visible_to_sender;
    }

    public Boolean isIs_visible_to_reciver() {
        return is_visible_to_reciver;
    }

    public ChatRoom is_visible_to_reciver(Boolean is_visible_to_reciver) {
        this.is_visible_to_reciver = is_visible_to_reciver;
        return this;
    }

    public void setIs_visible_to_reciver(Boolean is_visible_to_reciver) {
        this.is_visible_to_reciver = is_visible_to_reciver;
    }

    public Boolean isIs_read() {
        return is_read;
    }

    public ChatRoom is_read(Boolean is_read) {
        this.is_read = is_read;
        return this;
    }

    public void setIs_read(Boolean is_read) {
        this.is_read = is_read;
    }

    public User getSent_from() {
        return sent_from;
    }

    public ChatRoom sent_from(User user) {
        this.sent_from = user;
        return this;
    }

    public void setSent_from(User user) {
        this.sent_from = user;
    }

    public User getSent_to() {
        return sent_to;
    }

    public ChatRoom sent_to(User user) {
        this.sent_to = user;
        return this;
    }

    public void setSent_to(User user) {
        this.sent_to = user;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChatRoom chatRoom = (ChatRoom) o;
        if (chatRoom.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), chatRoom.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ChatRoom{" +
            "id=" + getId() +
            ", message='" + getMessage() + "'" +
            ", sent_on='" + getSent_on() + "'" +
            ", is_visible_to_sender='" + isIs_visible_to_sender() + "'" +
            ", is_visible_to_reciver='" + isIs_visible_to_reciver() + "'" +
            ", is_read='" + isIs_read() + "'" +
            "}";
    }
}
