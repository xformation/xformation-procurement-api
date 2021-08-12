package com.synectiks.procurement.web.rest;

import com.synectiks.procurement.domain.QuotationActivity;
import com.synectiks.procurement.repository.QuotationActivityRepository;
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
 * REST controller for managing {@link com.synectiks.procurement.domain.QuotationActivity}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class QuotationActivityResource {

    private final Logger log = LoggerFactory.getLogger(QuotationActivityResource.class);

    private static final String ENTITY_NAME = "procurementQuotationActivity";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final QuotationActivityRepository quotationActivityRepository;

    public QuotationActivityResource(QuotationActivityRepository quotationActivityRepository) {
        this.quotationActivityRepository = quotationActivityRepository;
    }

    /**
     * {@code POST  /quotation-activities} : Create a new quotationActivity.
     *
     * @param quotationActivity the quotationActivity to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new quotationActivity, or with status {@code 400 (Bad Request)} if the quotationActivity has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/quotation-activities")
    public ResponseEntity<QuotationActivity> createQuotationActivity(@Valid @RequestBody QuotationActivity quotationActivity) throws URISyntaxException {
        log.debug("REST request to save QuotationActivity : {}", quotationActivity);
        if (quotationActivity.getId() != null) {
            throw new BadRequestAlertException("A new quotationActivity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        QuotationActivity result = quotationActivityRepository.save(quotationActivity);
        return ResponseEntity.created(new URI("/api/quotation-activities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /quotation-activities} : Updates an existing quotationActivity.
     *
     * @param quotationActivity the quotationActivity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated quotationActivity,
     * or with status {@code 400 (Bad Request)} if the quotationActivity is not valid,
     * or with status {@code 500 (Internal Server Error)} if the quotationActivity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/quotation-activities")
    public ResponseEntity<QuotationActivity> updateQuotationActivity(@Valid @RequestBody QuotationActivity quotationActivity) throws URISyntaxException {
        log.debug("REST request to update QuotationActivity : {}", quotationActivity);
        if (quotationActivity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        QuotationActivity result = quotationActivityRepository.save(quotationActivity);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, quotationActivity.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /quotation-activities} : get all the quotationActivities.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of quotationActivities in body.
     */
    @GetMapping("/quotation-activities")
    public ResponseEntity<List<QuotationActivity>> getAllQuotationActivities(Pageable pageable) {
        log.debug("REST request to get a page of QuotationActivities");
        Page<QuotationActivity> page = quotationActivityRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /quotation-activities/:id} : get the "id" quotationActivity.
     *
     * @param id the id of the quotationActivity to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the quotationActivity, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/quotation-activities/{id}")
    public ResponseEntity<QuotationActivity> getQuotationActivity(@PathVariable Long id) {
        log.debug("REST request to get QuotationActivity : {}", id);
        Optional<QuotationActivity> quotationActivity = quotationActivityRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(quotationActivity);
    }

    /**
     * {@code DELETE  /quotation-activities/:id} : delete the "id" quotationActivity.
     *
     * @param id the id of the quotationActivity to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/quotation-activities/{id}")
    public ResponseEntity<Void> deleteQuotationActivity(@PathVariable Long id) {
        log.debug("REST request to delete QuotationActivity : {}", id);
        quotationActivityRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
