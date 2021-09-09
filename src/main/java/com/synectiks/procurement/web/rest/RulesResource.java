package com.synectiks.procurement.web.rest;

import com.synectiks.procurement.domain.Rules;
import com.synectiks.procurement.repository.RulesRepository;
import com.synectiks.procurement.web.rest.errors.BadRequestAlertException;
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
import org.springframework.web.bind.annotation.*;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.synectiks.procurement.domain.Rules}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class RulesResource {

  private final Logger log = LoggerFactory.getLogger(RulesResource.class);

  private static final String ENTITY_NAME = "procurementRules";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final RulesRepository rulesRepository;

  public RulesResource(RulesRepository rulesRepository) {
    this.rulesRepository = rulesRepository;
  }

  /**
   * {@code POST  /rules} : Create a new rules.
   *
   * @param rules the rules to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rules, or with status {@code 400 (Bad Request)} if the rules has already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/rules")
  public ResponseEntity<Rules> createRules(@Valid @RequestBody Rules rules) throws URISyntaxException {
    log.debug("REST request to save Rules : {}", rules);
    if (rules.getId() != null) {
      throw new BadRequestAlertException("A new rules cannot already have an ID", ENTITY_NAME, "idexists");
    }
    Rules result = rulesRepository.save(rules);
    return ResponseEntity
      .created(new URI("/api/rules/" + result.getId()))
      .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
      .body(result);
  }

  /**
   * {@code PUT  /rules/:id} : Updates an existing rules.
   *
   * @param id the id of the rules to save.
   * @param rules the rules to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rules,
   * or with status {@code 400 (Bad Request)} if the rules is not valid,
   * or with status {@code 500 (Internal Server Error)} if the rules couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/rules/{id}")
  public ResponseEntity<Rules> updateRules(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Rules rules)
    throws URISyntaxException {
    log.debug("REST request to update Rules : {}, {}", id, rules);
    if (rules.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    if (!Objects.equals(id, rules.getId())) {
      throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
    }

    if (!rulesRepository.existsById(id)) {
      throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
    }

    Rules result = rulesRepository.save(rules);
    return ResponseEntity
      .ok()
      .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, rules.getId().toString()))
      .body(result);
  }

  /**
   * {@code PATCH  /rules/:id} : Partial updates given fields of an existing rules, field will ignore if it is null
   *
   * @param id the id of the rules to save.
   * @param rules the rules to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rules,
   * or with status {@code 400 (Bad Request)} if the rules is not valid,
   * or with status {@code 404 (Not Found)} if the rules is not found,
   * or with status {@code 500 (Internal Server Error)} if the rules couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PatchMapping(value = "/rules/{id}", consumes = "application/merge-patch+json")
  public ResponseEntity<Rules> partialUpdateRules(
    @PathVariable(value = "id", required = false) final Long id,
    @NotNull @RequestBody Rules rules
  ) throws URISyntaxException {
    log.debug("REST request to partial update Rules partially : {}, {}", id, rules);
    if (rules.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    if (!Objects.equals(id, rules.getId())) {
      throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
    }

    if (!rulesRepository.existsById(id)) {
      throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
    }

    Optional<Rules> result = rulesRepository
      .findById(rules.getId())
      .map(
        existingRules -> {
          if (rules.getName() != null) {
            existingRules.setName(rules.getName());
          }
          if (rules.getDescription() != null) {
            existingRules.setDescription(rules.getDescription());
          }
          if (rules.getStatus() != null) {
            existingRules.setStatus(rules.getStatus());
          }
          if (rules.getRule() != null) {
            existingRules.setRule(rules.getRule());
          }
          if (rules.getCreatedOn() != null) {
            existingRules.setCreatedOn(rules.getCreatedOn());
          }
          if (rules.getCreatedBy() != null) {
            existingRules.setCreatedBy(rules.getCreatedBy());
          }
          if (rules.getUpdatedOn() != null) {
            existingRules.setUpdatedOn(rules.getUpdatedOn());
          }
          if (rules.getUpdatedBy() != null) {
            existingRules.setUpdatedBy(rules.getUpdatedBy());
          }

          return existingRules;
        }
      )
      .map(rulesRepository::save);

    return ResponseUtil.wrapOrNotFound(
      result,
      HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, rules.getId().toString())
    );
  }

  /**
   * {@code GET  /rules} : get all the rules.
   *
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rules in body.
   */
  @GetMapping("/rules")
  public List<Rules> getAllRules() {
    log.debug("REST request to get all Rules");
    return rulesRepository.findAll();
  }

  /**
   * {@code GET  /rules/:id} : get the "id" rules.
   *
   * @param id the id of the rules to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rules, or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/rules/{id}")
  public ResponseEntity<Rules> getRules(@PathVariable Long id) {
    log.debug("REST request to get Rules : {}", id);
    Optional<Rules> rules = rulesRepository.findById(id);
    return ResponseUtil.wrapOrNotFound(rules);
  }

  /**
   * {@code DELETE  /rules/:id} : delete the "id" rules.
   *
   * @param id the id of the rules to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/rules/{id}")
  public ResponseEntity<Void> deleteRules(@PathVariable Long id) {
    log.debug("REST request to delete Rules : {}", id);
    rulesRepository.deleteById(id);
    return ResponseEntity
      .noContent()
      .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
      .build();
  }
}
