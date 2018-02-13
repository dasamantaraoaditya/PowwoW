package com.myapp2.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Userprofile.
 */
@Entity
@Table(name = "userprofile")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Userprofile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "profilepicture")
    private String profilepicture;

    @Column(name = "about")
    private String about;

    @Column(name = "nicname")
    private String nicname;

    @Column(name = "dateofbirth")
    private LocalDate dateofbirth;

    @Column(name = "address")
    private String address;

    @Column(name = "contactno")
    private String contactno;

    @Column(name = "facebooklink")
    private String facebooklink;

    @Column(name = "googlepluslink")
    private String googlepluslink;

    @Column(name = "linkedinlink")
    private String linkedinlink;

    @Column(name = "twitterlink")
    private String twitterlink;

    @Column(name = "instagramlink")
    private String instagramlink;

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private User userid;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProfilepicture() {
        return profilepicture;
    }

    public Userprofile profilepicture(String profilepicture) {
        this.profilepicture = profilepicture;
        return this;
    }

    public void setProfilepicture(String profilepicture) {
        this.profilepicture = profilepicture;
    }

    public String getAbout() {
        return about;
    }

    public Userprofile about(String about) {
        this.about = about;
        return this;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getNicname() {
        return nicname;
    }

    public Userprofile nicname(String nicname) {
        this.nicname = nicname;
        return this;
    }

    public void setNicname(String nicname) {
        this.nicname = nicname;
    }

    public LocalDate getDateofbirth() {
        return dateofbirth;
    }

    public Userprofile dateofbirth(LocalDate dateofbirth) {
        this.dateofbirth = dateofbirth;
        return this;
    }

    public void setDateofbirth(LocalDate dateofbirth) {
        this.dateofbirth = dateofbirth;
    }

    public String getAddress() {
        return address;
    }

    public Userprofile address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactno() {
        return contactno;
    }

    public Userprofile contactno(String contactno) {
        this.contactno = contactno;
        return this;
    }

    public void setContactno(String contactno) {
        this.contactno = contactno;
    }

    public String getFacebooklink() {
        return facebooklink;
    }

    public Userprofile facebooklink(String facebooklink) {
        this.facebooklink = facebooklink;
        return this;
    }

    public void setFacebooklink(String facebooklink) {
        this.facebooklink = facebooklink;
    }

    public String getGooglepluslink() {
        return googlepluslink;
    }

    public Userprofile googlepluslink(String googlepluslink) {
        this.googlepluslink = googlepluslink;
        return this;
    }

    public void setGooglepluslink(String googlepluslink) {
        this.googlepluslink = googlepluslink;
    }

    public String getLinkedinlink() {
        return linkedinlink;
    }

    public Userprofile linkedinlink(String linkedinlink) {
        this.linkedinlink = linkedinlink;
        return this;
    }

    public void setLinkedinlink(String linkedinlink) {
        this.linkedinlink = linkedinlink;
    }

    public String getTwitterlink() {
        return twitterlink;
    }

    public Userprofile twitterlink(String twitterlink) {
        this.twitterlink = twitterlink;
        return this;
    }

    public void setTwitterlink(String twitterlink) {
        this.twitterlink = twitterlink;
    }

    public String getInstagramlink() {
        return instagramlink;
    }

    public Userprofile instagramlink(String instagramlink) {
        this.instagramlink = instagramlink;
        return this;
    }

    public void setInstagramlink(String instagramlink) {
        this.instagramlink = instagramlink;
    }

    public User getUserid() {
        return userid;
    }

    public Userprofile userid(User user) {
        this.userid = user;
        return this;
    }

    public void setUserid(User user) {
        this.userid = user;
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
        Userprofile userprofile = (Userprofile) o;
        if (userprofile.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), userprofile.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Userprofile{" +
            "id=" + getId() +
            ", profilepicture='" + getProfilepicture() + "'" +
            ", about='" + getAbout() + "'" +
            ", nicname='" + getNicname() + "'" +
            ", dateofbirth='" + getDateofbirth() + "'" +
            ", address='" + getAddress() + "'" +
            ", contactno='" + getContactno() + "'" +
            ", facebooklink='" + getFacebooklink() + "'" +
            ", googlepluslink='" + getGooglepluslink() + "'" +
            ", linkedinlink='" + getLinkedinlink() + "'" +
            ", twitterlink='" + getTwitterlink() + "'" +
            ", instagramlink='" + getInstagramlink() + "'" +
            "}";
    }
}
