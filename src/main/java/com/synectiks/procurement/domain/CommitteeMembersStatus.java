package com.synectiks.procurement.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A CommitteeMembersStatus.
 */
@Entity
@Table(name = "committee_members_status")
public class CommitteeMembersStatus implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "status")
    private String status;

    @ManyToOne
    @JsonIgnoreProperties(value = "committeeMembersStatuses", allowSetters = true)
    private Committee committee;

    @ManyToOne
    @JsonIgnoreProperties(value = "committeeMembersStatuses", allowSetters = true)
    private CommitteeMembers committeeMembers;

  

	// jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public CommitteeMembersStatus status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Committee getCommittee() {
        return committee;
    }

    public CommitteeMembersStatus committee(Committee committee) {
        this.committee = committee;
        return this;
    }

    public void setCommittee(Committee committee) {
        this.committee = committee;
    }

    public CommitteeMembers getCommitteeMembers() {
        return committeeMembers;
    }

    public CommitteeMembersStatus committeeMembers(CommitteeMembers committeeMembers) {
        this.committeeMembers = committeeMembers;
        return this;
    }

    public void setCommitteeMembers(CommitteeMembers committeeMembers) {
        this.committeeMembers = committeeMembers;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CommitteeMembersStatus)) {
            return false;
        }
        return id != null && id.equals(((CommitteeMembersStatus) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommitteeMembersStatus{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
