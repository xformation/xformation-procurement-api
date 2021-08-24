package com.synectiks.procurement.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A Contact.
 */
@Entity
@Table(name = "contact")

public class Contact implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "is_active")
    private String isActive;

    @Column(name = "invite_status")
    private String inviteStatus;

    @Column(name = "invitation_link")
    private String invitationLink;

    @Column(name = "invite_sent_on")
    private Instant inviteSentOn;

    @Column(name = "designation")
    private String designation;

    @Column(name = "job_type")
    private String jobType;

    @Column(name = "created_on")
    private Instant createdOn;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_on")
    private Instant updatedOn;

    @Column(name = "updated_by")
    private String updatedBy;

    @Size(max = 5000)
    @Column(name = "notes", length = 5000)
    private String notes;

    @OneToOne
    @JoinColumn(unique = true)
    private Contact owner;

    @OneToMany(mappedBy = "contact")
    private Set<Document> documentLists = new HashSet<>();

    @OneToMany(mappedBy = "contact")
    private Set<ContactActivity> contactActivityLists = new HashSet<>();

    @ManyToMany(mappedBy = "contacts")
    @JsonIgnore
    private Set<Committee> committees = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public Contact firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public Contact middleName(String middleName) {
        this.middleName = middleName;
        return this;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public Contact lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Contact phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public Contact email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIsActive() {
        return isActive;
    }

    public Contact isActive(String isActive) {
        this.isActive = isActive;
        return this;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getInviteStatus() {
        return inviteStatus;
    }

    public Contact inviteStatus(String inviteStatus) {
        this.inviteStatus = inviteStatus;
        return this;
    }

    public void setInviteStatus(String inviteStatus) {
        this.inviteStatus = inviteStatus;
    }

    public String getInvitationLink() {
        return invitationLink;
    }

    public Contact invitationLink(String invitationLink) {
        this.invitationLink = invitationLink;
        return this;
    }

    public void setInvitationLink(String invitationLink) {
        this.invitationLink = invitationLink;
    }

    public Instant getInviteSentOn() {
        return inviteSentOn;
    }

    public Contact inviteSentOn(Instant inviteSentOn) {
        this.inviteSentOn = inviteSentOn;
        return this;
    }

    public void setInviteSentOn(Instant inviteSentOn) {
        this.inviteSentOn = inviteSentOn;
    }

    public String getDesignation() {
        return designation;
    }

    public Contact designation(String designation) {
        this.designation = designation;
        return this;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getJobType() {
        return jobType;
    }

    public Contact jobType(String jobType) {
        this.jobType = jobType;
        return this;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public Instant getCreatedOn() {
        return createdOn;
    }

    public Contact createdOn(Instant createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public void setCreatedOn(Instant createdOn) {
        this.createdOn = createdOn;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public Contact createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getUpdatedOn() {
        return updatedOn;
    }

    public Contact updatedOn(Instant updatedOn) {
        this.updatedOn = updatedOn;
        return this;
    }

    public void setUpdatedOn(Instant updatedOn) {
        this.updatedOn = updatedOn;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public Contact updatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getNotes() {
        return notes;
    }

    public Contact notes(String notes) {
        this.notes = notes;
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Contact getOwner() {
        return owner;
    }

    public Contact owner(Contact contact) {
        this.owner = contact;
        return this;
    }

    public void setOwner(Contact contact) {
        this.owner = contact;
    }

    public Set<Document> getDocumentLists() {
        return documentLists;
    }

    public Contact documentLists(Set<Document> documents) {
        this.documentLists = documents;
        return this;
    }

    public Contact addDocumentList(Document document) {
        this.documentLists.add(document);
        document.setContact(this);
        return this;
    }

    public Contact removeDocumentList(Document document) {
        this.documentLists.remove(document);
        document.setContact(null);
        return this;
    }

    public void setDocumentLists(Set<Document> documents) {
        this.documentLists = documents;
    }

    public Set<ContactActivity> getContactActivityLists() {
        return contactActivityLists;
    }

    public Contact contactActivityLists(Set<ContactActivity> contactActivities) {
        this.contactActivityLists = contactActivities;
        return this;
    }

    public Contact addContactActivityList(ContactActivity contactActivity) {
        this.contactActivityLists.add(contactActivity);
        contactActivity.setContact(this);
        return this;
    }

    public Contact removeContactActivityList(ContactActivity contactActivity) {
        this.contactActivityLists.remove(contactActivity);
        contactActivity.setContact(null);
        return this;
    }

    public void setContactActivityLists(Set<ContactActivity> contactActivities) {
        this.contactActivityLists = contactActivities;
    }

    public Set<Committee> getCommittees() {
        return committees;
    }

    public Contact committees(Set<Committee> committees) {
        this.committees = committees;
        return this;
    }

    public Contact addCommittee(Committee committee) {
        this.committees.add(committee);
        committee.getContacts().add(this);
        return this;
    }

    public Contact removeCommittee(Committee committee) {
        this.committees.remove(committee);
        committee.getContacts().remove(this);
        return this;
    }

    public void setCommittees(Set<Committee> committees) {
        this.committees = committees;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Contact)) {
            return false;
        }
        return id != null && id.equals(((Contact) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Contact{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", middleName='" + getMiddleName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", email='" + getEmail() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", inviteStatus='" + getInviteStatus() + "'" +
            ", invitationLink='" + getInvitationLink() + "'" +
            ", inviteSentOn='" + getInviteSentOn() + "'" +
            ", designation='" + getDesignation() + "'" +
            ", jobType='" + getJobType() + "'" +
            ", createdOn='" + getCreatedOn() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", updatedOn='" + getUpdatedOn() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            ", notes='" + getNotes() + "'" +
            "}";
    }
}
