package com.synectiks.procurement.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A VendorRequisitionBucket.
 */
@Entity
@Table(name = "vendor_requisition_bucket")
public class VendorRequisitionBucket implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
  @SequenceGenerator(name = "sequenceGenerator")
  private Long id;

  @Column(name = "status")
  private String status;

  @Column(name = "stages")
  private String stages;

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

  @ManyToOne
  @JsonIgnoreProperties(value = { "department", "currency", "requisitionLineItemLists", "requisitions" }, allowSetters = true)
  private Requisition requisition;

  @ManyToOne
  @JsonIgnoreProperties(value = { "vendors" }, allowSetters = true)
  private Vendor vendor;

  // jhipster-needle-entity-add-field - JHipster will add fields here
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public VendorRequisitionBucket id(Long id) {
    this.id = id;
    return this;
  }

  public String getStatus() {
    return this.status;
  }

  public VendorRequisitionBucket status(String status) {
    this.status = status;
    return this;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getStages() {
    return this.stages;
  }

  public VendorRequisitionBucket stages(String stages) {
    this.stages = stages;
    return this;
  }

  public void setStages(String stages) {
    this.stages = stages;
  }

  public Instant getCreatedOn() {
    return this.createdOn;
  }

  public VendorRequisitionBucket createdOn(Instant createdOn) {
    this.createdOn = createdOn;
    return this;
  }

  public void setCreatedOn(Instant createdOn) {
    this.createdOn = createdOn;
  }

  public String getCreatedBy() {
    return this.createdBy;
  }

  public VendorRequisitionBucket createdBy(String createdBy) {
    this.createdBy = createdBy;
    return this;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  public Instant getUpdatedOn() {
    return this.updatedOn;
  }

  public VendorRequisitionBucket updatedOn(Instant updatedOn) {
    this.updatedOn = updatedOn;
    return this;
  }

  public void setUpdatedOn(Instant updatedOn) {
    this.updatedOn = updatedOn;
  }

  public String getUpdatedBy() {
    return this.updatedBy;
  }

  public VendorRequisitionBucket updatedBy(String updatedBy) {
    this.updatedBy = updatedBy;
    return this;
  }

  public void setUpdatedBy(String updatedBy) {
    this.updatedBy = updatedBy;
  }

  public String getNotes() {
    return this.notes;
  }

  public VendorRequisitionBucket notes(String notes) {
    this.notes = notes;
    return this;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public Requisition getRequisition() {
    return this.requisition;
  }

  public VendorRequisitionBucket requisition(Requisition requisition) {
    this.setRequisition(requisition);
    return this;
  }

  public void setRequisition(Requisition requisition) {
    this.requisition = requisition;
  }

  public Vendor getVendor() {
    return this.vendor;
  }

  public VendorRequisitionBucket vendor(Vendor vendor) {
    this.setVendor(vendor);
    return this;
  }

  public void setVendor(Vendor vendor) {
    this.vendor = vendor;
  }

  // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof VendorRequisitionBucket)) {
      return false;
    }
    return id != null && id.equals(((VendorRequisitionBucket) o).id);
  }

  @Override
  public int hashCode() {
    // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
    return getClass().hashCode();
  }

  // prettier-ignore
    @Override
    public String toString() {
        return "VendorRequisitionBucket{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", stages='" + getStages() + "'" +
            ", createdOn='" + getCreatedOn() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", updatedOn='" + getUpdatedOn() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            ", notes='" + getNotes() + "'" +
            "}";
    }
}
