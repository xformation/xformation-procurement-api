package com.synectiks.procurement.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.synectiks.procurement.domain.VendorRequisitionBucket;
import com.synectiks.procurement.repository.VendorRequisitionBucketRepository;
import com.synectiks.procurement.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.synectiks.procurement.domain.VendorRequisitionBucket}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class VendorRequisitionBucketResource {

  private final Logger log = LoggerFactory.getLogger(VendorRequisitionBucketResource.class);

  private static final String ENTITY_NAME = "procurementVendorRequisitionBucket";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final VendorRequisitionBucketRepository vendorRequisitionBucketRepository;

  public VendorRequisitionBucketResource(VendorRequisitionBucketRepository vendorRequisitionBucketRepository) {
    this.vendorRequisitionBucketRepository = vendorRequisitionBucketRepository;
  }

  /**
   * {@code POST  /vendor-requisition-buckets} : Create a new vendorRequisitionBucket.
   *
   * @param vendorRequisitionBucket the vendorRequisitionBucket to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vendorRequisitionBucket, or with status {@code 400 (Bad Request)} if the vendorRequisitionBucket has already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/vendor-requisition-buckets")
  public ResponseEntity<VendorRequisitionBucket> createVendorRequisitionBucket(
    @Valid @RequestBody VendorRequisitionBucket vendorRequisitionBucket
  ) throws URISyntaxException {
    log.debug("REST request to save VendorRequisitionBucket : {}", vendorRequisitionBucket);
    if (vendorRequisitionBucket.getId() != null) {
      throw new BadRequestAlertException("A new vendorRequisitionBucket cannot already have an ID", ENTITY_NAME, "idexists");
    }
    VendorRequisitionBucket result = vendorRequisitionBucketRepository.save(vendorRequisitionBucket);
    return ResponseEntity
      .created(new URI("/api/vendor-requisition-buckets/" + result.getId()))
      .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
      .body(result);
  }

  /**
   * {@code PUT  /vendor-requisition-buckets/:id} : Updates an existing vendorRequisitionBucket.
   *
   * @param id the id of the vendorRequisitionBucket to save.
   * @param vendorRequisitionBucket the vendorRequisitionBucket to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vendorRequisitionBucket,
   * or with status {@code 400 (Bad Request)} if the vendorRequisitionBucket is not valid,
   * or with status {@code 500 (Internal Server Error)} if the vendorRequisitionBucket couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/vendor-requisition-buckets/{id}")
  public ResponseEntity<VendorRequisitionBucket> updateVendorRequisitionBucket(
    @PathVariable(value = "id", required = false) final Long id,
    @Valid @RequestBody VendorRequisitionBucket vendorRequisitionBucket
  ) throws URISyntaxException {
    log.debug("REST request to update VendorRequisitionBucket : {}, {}", id, vendorRequisitionBucket);
    if (vendorRequisitionBucket.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    if (!Objects.equals(id, vendorRequisitionBucket.getId())) {
      throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
    }

    if (!vendorRequisitionBucketRepository.existsById(id)) {
      throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
    }

    VendorRequisitionBucket result = vendorRequisitionBucketRepository.save(vendorRequisitionBucket);
    return ResponseEntity
      .ok()
      .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, vendorRequisitionBucket.getId().toString()))
      .body(result);
  }

  /**
   * {@code PATCH  /vendor-requisition-buckets/:id} : Partial updates given fields of an existing vendorRequisitionBucket, field will ignore if it is null
   *
   * @param id the id of the vendorRequisitionBucket to save.
   * @param vendorRequisitionBucket the vendorRequisitionBucket to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vendorRequisitionBucket,
   * or with status {@code 400 (Bad Request)} if the vendorRequisitionBucket is not valid,
   * or with status {@code 404 (Not Found)} if the vendorRequisitionBucket is not found,
   * or with status {@code 500 (Internal Server Error)} if the vendorRequisitionBucket couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PatchMapping(value = "/vendor-requisition-buckets/{id}", consumes = "application/merge-patch+json")
  public ResponseEntity<VendorRequisitionBucket> partialUpdateVendorRequisitionBucket(
    @PathVariable(value = "id", required = false) final Long id,
    @NotNull @RequestBody VendorRequisitionBucket vendorRequisitionBucket
  ) throws URISyntaxException {
    log.debug("REST request to partial update VendorRequisitionBucket partially : {}, {}", id, vendorRequisitionBucket);
    if (vendorRequisitionBucket.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    if (!Objects.equals(id, vendorRequisitionBucket.getId())) {
      throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
    }

    if (!vendorRequisitionBucketRepository.existsById(id)) {
      throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
    }

    Optional<VendorRequisitionBucket> result = vendorRequisitionBucketRepository
      .findById(vendorRequisitionBucket.getId())
      .map(
        existingVendorRequisitionBucket -> {
          if (vendorRequisitionBucket.getStatus() != null) {
            existingVendorRequisitionBucket.setStatus(vendorRequisitionBucket.getStatus());
          }
          if (vendorRequisitionBucket.getStages() != null) {
            existingVendorRequisitionBucket.setStages(vendorRequisitionBucket.getStages());
          }
          if (vendorRequisitionBucket.getCreatedOn() != null) {
            existingVendorRequisitionBucket.setCreatedOn(vendorRequisitionBucket.getCreatedOn());
          }
          if (vendorRequisitionBucket.getCreatedBy() != null) {
            existingVendorRequisitionBucket.setCreatedBy(vendorRequisitionBucket.getCreatedBy());
          }
          if (vendorRequisitionBucket.getUpdatedOn() != null) {
            existingVendorRequisitionBucket.setUpdatedOn(vendorRequisitionBucket.getUpdatedOn());
          }
          if (vendorRequisitionBucket.getUpdatedBy() != null) {
            existingVendorRequisitionBucket.setUpdatedBy(vendorRequisitionBucket.getUpdatedBy());
          }
          if (vendorRequisitionBucket.getNotes() != null) {
            existingVendorRequisitionBucket.setNotes(vendorRequisitionBucket.getNotes());
          }

          return existingVendorRequisitionBucket;
        }
      )
      .map(vendorRequisitionBucketRepository::save);

    return ResponseUtil.wrapOrNotFound(
      result,
      HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, vendorRequisitionBucket.getId().toString())
    );
  }

  /**
   * {@code GET  /vendor-requisition-buckets} : get all the vendorRequisitionBuckets.
   *
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vendorRequisitionBuckets in body.
   */
  @GetMapping("/vendor-requisition-buckets")
  public List<VendorRequisitionBucket> getAllVendorRequisitionBuckets() {
    log.debug("REST request to get all VendorRequisitionBuckets");
    return vendorRequisitionBucketRepository.findAll();
  }

  /**
   * {@code GET  /vendor-requisition-buckets/:id} : get the "id" vendorRequisitionBucket.
   *
   * @param id the id of the vendorRequisitionBucket to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vendorRequisitionBucket, or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/vendor-requisition-buckets/{id}")
  public ResponseEntity<VendorRequisitionBucket> getVendorRequisitionBucket(@PathVariable Long id) {
    log.debug("REST request to get VendorRequisitionBucket : {}", id);
    Optional<VendorRequisitionBucket> vendorRequisitionBucket = vendorRequisitionBucketRepository.findById(id);
    return ResponseUtil.wrapOrNotFound(vendorRequisitionBucket);
  }

  /**
   * {@code DELETE  /vendor-requisition-buckets/:id} : delete the "id" vendorRequisitionBucket.
   *
   * @param id the id of the vendorRequisitionBucket to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/vendor-requisition-buckets/{id}")
  public ResponseEntity<Void> deleteVendorRequisitionBucket(@PathVariable Long id) {
    log.debug("REST request to delete VendorRequisitionBucket : {}", id);
    vendorRequisitionBucketRepository.deleteById(id);
    return ResponseEntity
      .noContent()
      .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
      .build();
  }
}
