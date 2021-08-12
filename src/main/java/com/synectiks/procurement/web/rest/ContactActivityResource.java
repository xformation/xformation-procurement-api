package com.synectiks.procurement.web.rest;

import com.synectiks.procurement.domain.ContactActivity;
import com.synectiks.procurement.repository.ContactActivityRepository;
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
 * REST controller for managing {@link com.synectiks.procurement.domain.ContactActivity}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ContactActivityResource {

    private final Logger log = LoggerFactory.getLogger(ContactActivityResource.class);

    private static final String ENTITY_NAME = "procurementContactActivity";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ContactActivityRepository contactActivityRepository;

    public ContactActivityResource(ContactActivityRepository contactActivityRepository) {
        this.contactActivityRepository = contactActivityRepository;
    }

    /**
     * {@code POST  /contact-activities} : Create a new contactActivity.
     *
     * @param contactActivity the contactActivity to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new contactActivity, or with status {@code 400 (Bad Request)} if the contactActivity has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/contact-activities")
    public ResponseEntity<ContactActivity> createContactActivity(@Valid @RequestBody ContactActivity contactActivity) throws URISyntaxException {
        log.debug("REST request to save ContactActivity : {}", contactActivity);
        if (contactActivity.getId() != null) {
            throw new BadRequestAlertException("A new contactActivity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ContactActivity result = contactActivityRepository.save(contactActivity);
        return ResponseEntity.created(new URI("/api/contact-activities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /contact-activities} : Updates an existing contactActivity.
     *
     * @param contactActivity the contactActivity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contactActivity,
     * or with status {@code 400 (Bad Request)} if the contactActivity is not valid,
     * or with status {@code 500 (Internal Server Error)} if the contactActivity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/contact-activities")
    public ResponseEntity<ContactActivity> updateContactActivity(@Valid @RequestBody ContactActivity contactActivity) throws URISyntaxException {
        log.debug("REST request to update ContactActivity : {}", contactActivity);
        if (contactActivity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ContactActivity result = contactActivityRepository.save(contactActivity);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, contactActivity.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /contact-activities} : get all the contactActivities.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of contactActivities in body.
     */
    @GetMapping("/contact-activities")
    public ResponseEntity<List<ContactActivity>> getAllContactActivities(Pageable pageable) {
        log.debug("REST request to get a page of ContactActivities");
        Page<ContactActivity> page = contactActivityRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /contact-activities/:id} : get the "id" contactActivity.
     *
     * @param id the id of the contactActivity to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contactActivity, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/contact-activities/{id}")
    public ResponseEntity<ContactActivity> getContactActivity(@PathVariable Long id) {
        log.debug("REST request to get ContactActivity : {}", id);
        Optional<ContactActivity> contactActivity = contactActivityRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(contactActivity);
    }

    /**
     * {@code DELETE  /contact-activities/:id} : delete the "id" contactActivity.
     *
     * @param id the id of the contactActivity to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/contact-activities/{id}")
    public ResponseEntity<Void> deleteContactActivity(@PathVariable Long id) {
        log.debug("REST request to delete ContactActivity : {}", id);
        contactActivityRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
