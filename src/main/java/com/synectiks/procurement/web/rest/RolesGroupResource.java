package com.synectiks.procurement.web.rest;

import com.synectiks.procurement.domain.RolesGroup;
import com.synectiks.procurement.repository.RolesGroupRepository;
import com.synectiks.procurement.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.synectiks.procurement.domain.RolesGroup}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class RolesGroupResource {

  private final Logger log = LoggerFactory.getLogger(RolesGroupResource.class);

  private static final String ENTITY_NAME = "procurementRolesGroup";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final RolesGroupRepository rolesGroupRepository;

  public RolesGroupResource(RolesGroupRepository rolesGroupRepository) {
    this.rolesGroupRepository = rolesGroupRepository;
  }

  /**
   * {@code POST  /roles-groups} : Create a new rolesGroup.
   *
   * @param rolesGroup the rolesGroup to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rolesGroup, or with status {@code 400 (Bad Request)} if the rolesGroup has already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/roles-groups")
  public ResponseEntity<RolesGroup> createRolesGroup(@RequestBody RolesGroup rolesGroup) throws URISyntaxException {
    log.debug("REST request to save RolesGroup : {}", rolesGroup);
    if (rolesGroup.getId() != null) {
      throw new BadRequestAlertException("A new rolesGroup cannot already have an ID", ENTITY_NAME, "idexists");
    }
    RolesGroup result = rolesGroupRepository.save(rolesGroup);
    return ResponseEntity
      .created(new URI("/api/roles-groups/" + result.getId()))
      .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
      .body(result);
  }

  /**
   * {@code PUT  /roles-groups/:id} : Updates an existing rolesGroup.
   *
   * @param id the id of the rolesGroup to save.
   * @param rolesGroup the rolesGroup to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rolesGroup,
   * or with status {@code 400 (Bad Request)} if the rolesGroup is not valid,
   * or with status {@code 500 (Internal Server Error)} if the rolesGroup couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/roles-groups/{id}")
  public ResponseEntity<RolesGroup> updateRolesGroup(
    @PathVariable(value = "id", required = false) final Long id,
    @RequestBody RolesGroup rolesGroup
  ) throws URISyntaxException {
    log.debug("REST request to update RolesGroup : {}, {}", id, rolesGroup);
    if (rolesGroup.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    if (!Objects.equals(id, rolesGroup.getId())) {
      throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
    }

    if (!rolesGroupRepository.existsById(id)) {
      throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
    }

    RolesGroup result = rolesGroupRepository.save(rolesGroup);
    return ResponseEntity
      .ok()
      .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, rolesGroup.getId().toString()))
      .body(result);
  }

  /**
   * {@code PATCH  /roles-groups/:id} : Partial updates given fields of an existing rolesGroup, field will ignore if it is null
   *
   * @param id the id of the rolesGroup to save.
   * @param rolesGroup the rolesGroup to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rolesGroup,
   * or with status {@code 400 (Bad Request)} if the rolesGroup is not valid,
   * or with status {@code 404 (Not Found)} if the rolesGroup is not found,
   * or with status {@code 500 (Internal Server Error)} if the rolesGroup couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PatchMapping(value = "/roles-groups/{id}", consumes = "application/merge-patch+json")
  public ResponseEntity<RolesGroup> partialUpdateRolesGroup(
    @PathVariable(value = "id", required = false) final Long id,
    @RequestBody RolesGroup rolesGroup
  ) throws URISyntaxException {
    log.debug("REST request to partial update RolesGroup partially : {}, {}", id, rolesGroup);
    if (rolesGroup.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    if (!Objects.equals(id, rolesGroup.getId())) {
      throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
    }

    if (!rolesGroupRepository.existsById(id)) {
      throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
    }

    Optional<RolesGroup> result = rolesGroupRepository
      .findById(rolesGroup.getId())
      .map(
        existingRolesGroup -> {
          if (rolesGroup.getGroup() != null) {
            existingRolesGroup.setGroup(rolesGroup.getGroup());
          }

          return existingRolesGroup;
        }
      )
      .map(rolesGroupRepository::save);

    return ResponseUtil.wrapOrNotFound(
      result,
      HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, rolesGroup.getId().toString())
    );
  }

  /**
   * {@code GET  /roles-groups} : get all the rolesGroups.
   *
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rolesGroups in body.
   */
  @GetMapping("/roles-groups")
  public List<RolesGroup> getAllRolesGroups() {
    log.debug("REST request to get all RolesGroups");
    return rolesGroupRepository.findAll();
  }

  /**
   * {@code GET  /roles-groups/:id} : get the "id" rolesGroup.
   *
   * @param id the id of the rolesGroup to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rolesGroup, or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/roles-groups/{id}")
  public ResponseEntity<RolesGroup> getRolesGroup(@PathVariable Long id) {
    log.debug("REST request to get RolesGroup : {}", id);
    Optional<RolesGroup> rolesGroup = rolesGroupRepository.findById(id);
    return ResponseUtil.wrapOrNotFound(rolesGroup);
  }

  /**
   * {@code DELETE  /roles-groups/:id} : delete the "id" rolesGroup.
   *
   * @param id the id of the rolesGroup to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/roles-groups/{id}")
  public ResponseEntity<Void> deleteRolesGroup(@PathVariable Long id) {
    log.debug("REST request to delete RolesGroup : {}", id);
    rolesGroupRepository.deleteById(id);
    return ResponseEntity
      .noContent()
      .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
      .build();
  }
}
