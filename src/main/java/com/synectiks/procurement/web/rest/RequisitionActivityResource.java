package com.synectiks.procurement.web.rest;

import com.synectiks.procurement.domain.RequisitionActivity;
import com.synectiks.procurement.repository.RequisitionActivityRepository;
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
 * REST controller for managing {@link com.synectiks.procurement.domain.RequisitionActivity}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class RequisitionActivityResource {

    private final Logger log = LoggerFactory.getLogger(RequisitionActivityResource.class);

    private static final String ENTITY_NAME = "procurementRequisitionActivity";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RequisitionActivityRepository requisitionActivityRepository;

    public RequisitionActivityResource(RequisitionActivityRepository requisitionActivityRepository) {
        this.requisitionActivityRepository = requisitionActivityRepository;
    }

    /**
     * {@code POST  /requisition-activities} : Create a new requisitionActivity.
     *
     * @param requisitionActivity the requisitionActivity to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new requisitionActivity, or with status {@code 400 (Bad Request)} if the requisitionActivity has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/requisition-activities")
    public ResponseEntity<RequisitionActivity> createRequisitionActivity(@Valid @RequestBody RequisitionActivity requisitionActivity) throws URISyntaxException {
        log.debug("REST request to save RequisitionActivity : {}", requisitionActivity);
        if (requisitionActivity.getId() != null) {
            throw new BadRequestAlertException("A new requisitionActivity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RequisitionActivity result = requisitionActivityRepository.save(requisitionActivity);
        return ResponseEntity.created(new URI("/api/requisition-activities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /requisition-activities} : Updates an existing requisitionActivity.
     *
     * @param requisitionActivity the requisitionActivity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated requisitionActivity,
     * or with status {@code 400 (Bad Request)} if the requisitionActivity is not valid,
     * or with status {@code 500 (Internal Server Error)} if the requisitionActivity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/requisition-activities")
    public ResponseEntity<RequisitionActivity> updateRequisitionActivity(@Valid @RequestBody RequisitionActivity requisitionActivity) throws URISyntaxException {
        log.debug("REST request to update RequisitionActivity : {}", requisitionActivity);
        if (requisitionActivity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RequisitionActivity result = requisitionActivityRepository.save(requisitionActivity);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, requisitionActivity.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /requisition-activities} : get all the requisitionActivities.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of requisitionActivities in body.
     */
    @GetMapping("/requisition-activities")
    public ResponseEntity<List<RequisitionActivity>> getAllRequisitionActivities(Pageable pageable) {
        log.debug("REST request to get a page of RequisitionActivities");
        Page<RequisitionActivity> page = requisitionActivityRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /requisition-activities/:id} : get the "id" requisitionActivity.
     *
     * @param id the id of the requisitionActivity to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the requisitionActivity, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/requisition-activities/{id}")
    public ResponseEntity<RequisitionActivity> getRequisitionActivity(@PathVariable Long id) {
        log.debug("REST request to get RequisitionActivity : {}", id);
        Optional<RequisitionActivity> requisitionActivity = requisitionActivityRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(requisitionActivity);
    }

    /**
     * {@code DELETE  /requisition-activities/:id} : delete the "id" requisitionActivity.
     *
     * @param id the id of the requisitionActivity to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/requisition-activities/{id}")
    public ResponseEntity<Void> deleteRequisitionActivity(@PathVariable Long id) {
        log.debug("REST request to delete RequisitionActivity : {}", id);
        requisitionActivityRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
