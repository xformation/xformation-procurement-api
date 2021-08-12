package com.synectiks.procurement.web.rest;

import com.synectiks.procurement.domain.Requisition;
import com.synectiks.procurement.repository.RequisitionRepository;
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
 * REST controller for managing {@link com.synectiks.procurement.domain.Requisition}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class RequisitionResource {

    private final Logger log = LoggerFactory.getLogger(RequisitionResource.class);

    private static final String ENTITY_NAME = "procurementRequisition";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RequisitionRepository requisitionRepository;

    public RequisitionResource(RequisitionRepository requisitionRepository) {
        this.requisitionRepository = requisitionRepository;
    }

    /**
     * {@code POST  /requisitions} : Create a new requisition.
     *
     * @param requisition the requisition to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new requisition, or with status {@code 400 (Bad Request)} if the requisition has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/requisitions")
    public ResponseEntity<Requisition> createRequisition(@Valid @RequestBody Requisition requisition) throws URISyntaxException {
        log.debug("REST request to save Requisition : {}", requisition);
        if (requisition.getId() != null) {
            throw new BadRequestAlertException("A new requisition cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Requisition result = requisitionRepository.save(requisition);
        return ResponseEntity.created(new URI("/api/requisitions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /requisitions} : Updates an existing requisition.
     *
     * @param requisition the requisition to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated requisition,
     * or with status {@code 400 (Bad Request)} if the requisition is not valid,
     * or with status {@code 500 (Internal Server Error)} if the requisition couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/requisitions")
    public ResponseEntity<Requisition> updateRequisition(@Valid @RequestBody Requisition requisition) throws URISyntaxException {
        log.debug("REST request to update Requisition : {}", requisition);
        if (requisition.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Requisition result = requisitionRepository.save(requisition);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, requisition.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /requisitions} : get all the requisitions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of requisitions in body.
     */
    @GetMapping("/requisitions")
    public ResponseEntity<List<Requisition>> getAllRequisitions(Pageable pageable) {
        log.debug("REST request to get a page of Requisitions");
        Page<Requisition> page = requisitionRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /requisitions/:id} : get the "id" requisition.
     *
     * @param id the id of the requisition to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the requisition, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/requisitions/{id}")
    public ResponseEntity<Requisition> getRequisition(@PathVariable Long id) {
        log.debug("REST request to get Requisition : {}", id);
        Optional<Requisition> requisition = requisitionRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(requisition);
    }

    /**
     * {@code DELETE  /requisitions/:id} : delete the "id" requisition.
     *
     * @param id the id of the requisition to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/requisitions/{id}")
    public ResponseEntity<Void> deleteRequisition(@PathVariable Long id) {
        log.debug("REST request to delete Requisition : {}", id);
        requisitionRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
