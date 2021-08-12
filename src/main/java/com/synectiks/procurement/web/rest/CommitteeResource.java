package com.synectiks.procurement.web.rest;

import com.synectiks.procurement.domain.Committee;
import com.synectiks.procurement.repository.CommitteeRepository;
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
 * REST controller for managing {@link com.synectiks.procurement.domain.Committee}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CommitteeResource {

    private final Logger log = LoggerFactory.getLogger(CommitteeResource.class);

    private static final String ENTITY_NAME = "procurementCommittee";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CommitteeRepository committeeRepository;

    public CommitteeResource(CommitteeRepository committeeRepository) {
        this.committeeRepository = committeeRepository;
    }

    /**
     * {@code POST  /committees} : Create a new committee.
     *
     * @param committee the committee to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new committee, or with status {@code 400 (Bad Request)} if the committee has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/committees")
    public ResponseEntity<Committee> createCommittee(@Valid @RequestBody Committee committee) throws URISyntaxException {
        log.debug("REST request to save Committee : {}", committee);
        if (committee.getId() != null) {
            throw new BadRequestAlertException("A new committee cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Committee result = committeeRepository.save(committee);
        return ResponseEntity.created(new URI("/api/committees/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /committees} : Updates an existing committee.
     *
     * @param committee the committee to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated committee,
     * or with status {@code 400 (Bad Request)} if the committee is not valid,
     * or with status {@code 500 (Internal Server Error)} if the committee couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/committees")
    public ResponseEntity<Committee> updateCommittee(@Valid @RequestBody Committee committee) throws URISyntaxException {
        log.debug("REST request to update Committee : {}", committee);
        if (committee.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Committee result = committeeRepository.save(committee);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, committee.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /committees} : get all the committees.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of committees in body.
     */
    @GetMapping("/committees")
    public ResponseEntity<List<Committee>> getAllCommittees(Pageable pageable, @RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get a page of Committees");
        Page<Committee> page;
        if (eagerload) {
            page = committeeRepository.findAllWithEagerRelationships(pageable);
        } else {
            page = committeeRepository.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /committees/:id} : get the "id" committee.
     *
     * @param id the id of the committee to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the committee, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/committees/{id}")
    public ResponseEntity<Committee> getCommittee(@PathVariable Long id) {
        log.debug("REST request to get Committee : {}", id);
        Optional<Committee> committee = committeeRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(committee);
    }

    /**
     * {@code DELETE  /committees/:id} : delete the "id" committee.
     *
     * @param id the id of the committee to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/committees/{id}")
    public ResponseEntity<Void> deleteCommittee(@PathVariable Long id) {
        log.debug("REST request to delete Committee : {}", id);
        committeeRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
