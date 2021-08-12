package com.synectiks.procurement.web.rest;

import com.synectiks.procurement.domain.RequisitionLineItemActivity;
import com.synectiks.procurement.repository.RequisitionLineItemActivityRepository;
import com.synectiks.procurement.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.synectiks.procurement.domain.RequisitionLineItemActivity}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class RequisitionLineItemActivityResource {

    private final Logger log = LoggerFactory.getLogger(RequisitionLineItemActivityResource.class);

    private static final String ENTITY_NAME = "procurementRequisitionLineItemActivity";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RequisitionLineItemActivityRepository requisitionLineItemActivityRepository;

    public RequisitionLineItemActivityResource(RequisitionLineItemActivityRepository requisitionLineItemActivityRepository) {
        this.requisitionLineItemActivityRepository = requisitionLineItemActivityRepository;
    }

    /**
     * {@code POST  /requisition-line-item-activities} : Create a new requisitionLineItemActivity.
     *
     * @param requisitionLineItemActivity the requisitionLineItemActivity to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new requisitionLineItemActivity, or with status {@code 400 (Bad Request)} if the requisitionLineItemActivity has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/requisition-line-item-activities")
    public ResponseEntity<RequisitionLineItemActivity> createRequisitionLineItemActivity(@Valid @RequestBody RequisitionLineItemActivity requisitionLineItemActivity) throws URISyntaxException {
        log.debug("REST request to save RequisitionLineItemActivity : {}", requisitionLineItemActivity);
        if (requisitionLineItemActivity.getId() != null) {
            throw new BadRequestAlertException("A new requisitionLineItemActivity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RequisitionLineItemActivity result = requisitionLineItemActivityRepository.save(requisitionLineItemActivity);
        return ResponseEntity.created(new URI("/api/requisition-line-item-activities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /requisition-line-item-activities} : Updates an existing requisitionLineItemActivity.
     *
     * @param requisitionLineItemActivity the requisitionLineItemActivity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated requisitionLineItemActivity,
     * or with status {@code 400 (Bad Request)} if the requisitionLineItemActivity is not valid,
     * or with status {@code 500 (Internal Server Error)} if the requisitionLineItemActivity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/requisition-line-item-activities")
    public ResponseEntity<RequisitionLineItemActivity> updateRequisitionLineItemActivity(@Valid @RequestBody RequisitionLineItemActivity requisitionLineItemActivity) throws URISyntaxException {
        log.debug("REST request to update RequisitionLineItemActivity : {}", requisitionLineItemActivity);
        if (requisitionLineItemActivity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RequisitionLineItemActivity result = requisitionLineItemActivityRepository.save(requisitionLineItemActivity);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, requisitionLineItemActivity.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /requisition-line-item-activities} : get all the requisitionLineItemActivities.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of requisitionLineItemActivities in body.
     */
    @GetMapping("/requisition-line-item-activities")
    public ResponseEntity<List<RequisitionLineItemActivity>> getAllRequisitionLineItemActivities(Pageable pageable) {
        log.debug("REST request to get a page of RequisitionLineItemActivities");
        Page<RequisitionLineItemActivity> page = requisitionLineItemActivityRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /requisition-line-item-activities/:id} : get the "id" requisitionLineItemActivity.
     *
     * @param id the id of the requisitionLineItemActivity to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the requisitionLineItemActivity, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/requisition-line-item-activities/{id}")
    public ResponseEntity<RequisitionLineItemActivity> getRequisitionLineItemActivity(@PathVariable Long id) {
        log.debug("REST request to get RequisitionLineItemActivity : {}", id);
        Optional<RequisitionLineItemActivity> requisitionLineItemActivity = requisitionLineItemActivityRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(requisitionLineItemActivity);
    }

    /**
     * {@code DELETE  /requisition-line-item-activities/:id} : delete the "id" requisitionLineItemActivity.
     *
     * @param id the id of the requisitionLineItemActivity to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/requisition-line-item-activities/{id}")
    public ResponseEntity<Void> deleteRequisitionLineItemActivity(@PathVariable Long id) {
        log.debug("REST request to delete RequisitionLineItemActivity : {}", id);
        requisitionLineItemActivityRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
