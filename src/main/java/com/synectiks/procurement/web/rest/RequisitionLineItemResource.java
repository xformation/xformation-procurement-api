package com.synectiks.procurement.web.rest;

import com.synectiks.procurement.domain.RequisitionLineItem;
import com.synectiks.procurement.repository.RequisitionLineItemRepository;
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
 * REST controller for managing {@link com.synectiks.procurement.domain.RequisitionLineItem}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class RequisitionLineItemResource {

    private final Logger log = LoggerFactory.getLogger(RequisitionLineItemResource.class);

    private static final String ENTITY_NAME = "procurementRequisitionLineItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RequisitionLineItemRepository requisitionLineItemRepository;

    public RequisitionLineItemResource(RequisitionLineItemRepository requisitionLineItemRepository) {
        this.requisitionLineItemRepository = requisitionLineItemRepository;
    }

    /**
     * {@code POST  /requisition-line-items} : Create a new requisitionLineItem.
     *
     * @param requisitionLineItem the requisitionLineItem to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new requisitionLineItem, or with status {@code 400 (Bad Request)} if the requisitionLineItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/requisition-line-items")
    public ResponseEntity<RequisitionLineItem> createRequisitionLineItem(@Valid @RequestBody RequisitionLineItem requisitionLineItem) throws URISyntaxException {
        log.debug("REST request to save RequisitionLineItem : {}", requisitionLineItem);
        if (requisitionLineItem.getId() != null) {
            throw new BadRequestAlertException("A new requisitionLineItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RequisitionLineItem result = requisitionLineItemRepository.save(requisitionLineItem);
        return ResponseEntity.created(new URI("/api/requisition-line-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /requisition-line-items} : Updates an existing requisitionLineItem.
     *
     * @param requisitionLineItem the requisitionLineItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated requisitionLineItem,
     * or with status {@code 400 (Bad Request)} if the requisitionLineItem is not valid,
     * or with status {@code 500 (Internal Server Error)} if the requisitionLineItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/requisition-line-items")
    public ResponseEntity<RequisitionLineItem> updateRequisitionLineItem(@Valid @RequestBody RequisitionLineItem requisitionLineItem) throws URISyntaxException {
        log.debug("REST request to update RequisitionLineItem : {}", requisitionLineItem);
        if (requisitionLineItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RequisitionLineItem result = requisitionLineItemRepository.save(requisitionLineItem);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, requisitionLineItem.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /requisition-line-items} : get all the requisitionLineItems.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of requisitionLineItems in body.
     */
    @GetMapping("/requisition-line-items")
    public ResponseEntity<List<RequisitionLineItem>> getAllRequisitionLineItems(Pageable pageable) {
        log.debug("REST request to get a page of RequisitionLineItems");
        Page<RequisitionLineItem> page = requisitionLineItemRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /requisition-line-items/:id} : get the "id" requisitionLineItem.
     *
     * @param id the id of the requisitionLineItem to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the requisitionLineItem, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/requisition-line-items/{id}")
    public ResponseEntity<RequisitionLineItem> getRequisitionLineItem(@PathVariable Long id) {
        log.debug("REST request to get RequisitionLineItem : {}", id);
        Optional<RequisitionLineItem> requisitionLineItem = requisitionLineItemRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(requisitionLineItem);
    }

    /**
     * {@code DELETE  /requisition-line-items/:id} : delete the "id" requisitionLineItem.
     *
     * @param id the id of the requisitionLineItem to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/requisition-line-items/{id}")
    public ResponseEntity<Void> deleteRequisitionLineItem(@PathVariable Long id) {
        log.debug("REST request to delete RequisitionLineItem : {}", id);
        requisitionLineItemRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
