package com.synectiks.procurement.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Rules.
 */
@Entity
@Table(name = "rules")
public class Rules implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
  @SequenceGenerator(name = "sequenceGenerator")
  private Long id;

  @Column(name = "name")
  private String name;

  @Size(max = 5000)
  @Column(name = "description", length = 5000)
  private String description;

  @Column(name = "status")
  private String status;

  @Size(max = 10000)
  @Column(name = "rule", length = 10000)
  private String rule;

  @Column(name = "created_on")
  private Instant createdOn;

  @Column(name = "created_by")
  private String createdBy;

  @Column(name = "updated_on")
  private Instant updatedOn;

  @Column(name = "updated_by")
  private String updatedBy;

  @ManyToOne
  @JsonIgnoreProperties(value = { "roles" }, allowSetters = true)
  private Roles roles;

  // jhipster-needle-entity-add-field - JHipster will add fields here
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Rules id(Long id) {
    this.id = id;
    return this;
  }

  public String getName() {
    return this.name;
  }

  public Rules name(String name) {
    this.name = name;
    return this;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return this.description;
  }

  public Rules description(String description) {
    this.description = description;
    return this;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getStatus() {
    return this.status;
  }

  public Rules status(String status) {
    this.status = status;
    return this;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getRule() {
    return this.rule;
  }

  public Rules rule(String rule) {
    this.rule = rule;
    return this;
  }

  public void setRule(String rule) {
    this.rule = rule;
  }

  public Instant getCreatedOn() {
    return this.createdOn;
  }

  public Rules createdOn(Instant createdOn) {
    this.createdOn = createdOn;
    return this;
  }

  public void setCreatedOn(Instant createdOn) {
    this.createdOn = createdOn;
  }

  public String getCreatedBy() {
    return this.createdBy;
  }

  public Rules createdBy(String createdBy) {
    this.createdBy = createdBy;
    return this;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  public Instant getUpdatedOn() {
    return this.updatedOn;
  }

  public Rules updatedOn(Instant updatedOn) {
    this.updatedOn = updatedOn;
    return this;
  }

  public void setUpdatedOn(Instant updatedOn) {
    this.updatedOn = updatedOn;
  }

  public String getUpdatedBy() {
    return this.updatedBy;
  }

  public Rules updatedBy(String updatedBy) {
    this.updatedBy = updatedBy;
    return this;
  }

  public void setUpdatedBy(String updatedBy) {
    this.updatedBy = updatedBy;
  }

  public Roles getRoles() {
    return this.roles;
  }

  public Rules roles(Roles roles) {
    this.setRoles(roles);
    return this;
  }

  public void setRoles(Roles roles) {
    this.roles = roles;
  }

  // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Rules)) {
      return false;
    }
    return id != null && id.equals(((Rules) o).id);
  }

  @Override
  public int hashCode() {
    // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
    return getClass().hashCode();
  }

  // prettier-ignore
    @Override
    public String toString() {
        return "Rules{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", status='" + getStatus() + "'" +
            ", rule='" + getRule() + "'" +
            ", createdOn='" + getCreatedOn() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", updatedOn='" + getUpdatedOn() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            "}";
    }
}
