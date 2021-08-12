package com.synectiks.procurement.web.rest;

import com.synectiks.procurement.domain.CommitteeActivity;
import com.synectiks.procurement.repository.CommitteeActivityRepository;
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
 * REST controller for managing {@link com.synectiks.procurement.domain.CommitteeActivity}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CommitteeActivityResource {

    private final Logger log = LoggerFactory.getLogger(CommitteeActivityResource.class);

    private static final String ENTITY_NAME = "procurementCommitteeActivity";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CommitteeActivityRepository committeeActivityRepository;

    public CommitteeActivityResource(CommitteeActivityRepository committeeActivityRepository) {
        this.committeeActivityRepository = committeeActivityRepository;
    }

    /**
     * {@code POST  /committee-activities} : Create a new committeeActivity.
     *
     * @param committeeActivity the committeeActivity to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new committeeActivity, or with status {@code 400 (Bad Request)} if the committeeActivity has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/committee-activities")
    public ResponseEntity<CommitteeActivity> createCommitteeActivity(@Valid @RequestBody CommitteeActivity committeeActivity) throws URISyntaxException {
        log.debug("REST request to save CommitteeActivity : {}", committeeActivity);
        if (committeeActivity.getId() != null) {
            throw new BadRequestAlertException("A new committeeActivity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CommitteeActivity result = committeeActivityRepository.save(committeeActivity);
        return ResponseEntity.created(new URI("/api/committee-activities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /committee-activities} : Updates an existing committeeActivity.
     *
     * @param committeeActivity the committeeActivity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated committeeActivity,
     * or with status {@code 400 (Bad Request)} if the committeeActivity is not valid,
     * or with status {@code 500 (Internal Server Error)} if the committeeActivity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/committee-activities")
    public ResponseEntity<CommitteeActivity> updateCommitteeActivity(@Valid @RequestBody CommitteeActivity committeeActivity) throws URISyntaxException {
        log.debug("REST request to update CommitteeActivity : {}", committeeActivity);
        if (committeeActivity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CommitteeActivity result = committeeActivityRepository.save(committeeActivity);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, committeeActivity.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /committee-activities} : get all the committeeActivities.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of committeeActivities in body.
     */
    @GetMapping("/committee-activities")
    public ResponseEntity<List<CommitteeActivity>> getAllCommitteeActivities(Pageable pageable) {
        log.debug("REST request to get a page of CommitteeActivities");
        Page<CommitteeActivity> page = committeeActivityRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /committee-activities/:id} : get the "id" committeeActivity.
     *
     * @param id the id of the committeeActivity to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the committeeActivity, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/committee-activities/{id}")
    public ResponseEntity<CommitteeActivity> getCommitteeActivity(@PathVariable Long id) {
        log.debug("REST request to get CommitteeActivity : {}", id);
        Optional<CommitteeActivity> committeeActivity = committeeActivityRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(committeeActivity);
    }

    /**
     * {@code DELETE  /committee-activities/:id} : delete the "id" committeeActivity.
     *
     * @param id the id of the committeeActivity to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/committee-activities/{id}")
    public ResponseEntity<Void> deleteCommitteeActivity(@PathVariable Long id) {
        log.debug("REST request to delete CommitteeActivity : {}", id);
        committeeActivityRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
