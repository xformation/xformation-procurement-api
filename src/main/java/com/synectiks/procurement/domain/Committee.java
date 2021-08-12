package com.synectiks.procurement.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A Committee.
 */
@Entity
@Table(name = "committee")
public class Committee implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String type;

    @Column(name = "status")
    private String status;

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

    @OneToMany(mappedBy = "committee")
    private Set<CommitteeActivity> committeeActivityLists = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "committee_contact",
               joinColumns = @JoinColumn(name = "committee_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "contact_id", referencedColumnName = "id"))
    private Set<Contact> contacts = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Committee name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public Committee type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public Committee status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Instant getCreatedOn() {
        return createdOn;
    }

    public Committee createdOn(Instant createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public void setCreatedOn(Instant createdOn) {
        this.createdOn = createdOn;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public Committee createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getUpdatedOn() {
        return updatedOn;
    }

    public Committee updatedOn(Instant updatedOn) {
        this.updatedOn = updatedOn;
        return this;
    }

    public void setUpdatedOn(Instant updatedOn) {
        this.updatedOn = updatedOn;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public Committee updatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getNotes() {
        return notes;
    }

    public Committee notes(String notes) {
        this.notes = notes;
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Set<CommitteeActivity> getCommitteeActivityLists() {
        return committeeActivityLists;
    }

    public Committee committeeActivityLists(Set<CommitteeActivity> committeeActivities) {
        this.committeeActivityLists = committeeActivities;
        return this;
    }

    public Committee addCommitteeActivityList(CommitteeActivity committeeActivity) {
        this.committeeActivityLists.add(committeeActivity);
        committeeActivity.setCommittee(this);
        return this;
    }

    public Committee removeCommitteeActivityList(CommitteeActivity committeeActivity) {
        this.committeeActivityLists.remove(committeeActivity);
        committeeActivity.setCommittee(null);
        return this;
    }

    public void setCommitteeActivityLists(Set<CommitteeActivity> committeeActivities) {
        this.committeeActivityLists = committeeActivities;
    }

    public Set<Contact> getContacts() {
        return contacts;
    }

    public Committee contacts(Set<Contact> contacts) {
        this.contacts = contacts;
        return this;
    }

    public Committee addContact(Contact contact) {
        this.contacts.add(contact);
        contact.getCommittees().add(this);
        return this;
    }

    public Committee removeContact(Contact contact) {
        this.contacts.remove(contact);
        contact.getCommittees().remove(this);
        return this;
    }

    public void setContacts(Set<Contact> contacts) {
        this.contacts = contacts;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Committee)) {
            return false;
        }
        return id != null && id.equals(((Committee) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Committee{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", type='" + getType() + "'" +
            ", status='" + getStatus() + "'" +
            ", createdOn='" + getCreatedOn() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", updatedOn='" + getUpdatedOn() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            ", notes='" + getNotes() + "'" +
            "}";
    }
}
