package com.synectiks.procurement.web.rest;

import com.synectiks.procurement.domain.Roles;
import com.synectiks.procurement.repository.RolesRepository;
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
 * REST controller for managing {@link com.synectiks.procurement.domain.Roles}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class RolesResource {

  private final Logger log = LoggerFactory.getLogger(RolesResource.class);

  private static final String ENTITY_NAME = "procurementRoles";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final RolesRepository rolesRepository;

  public RolesResource(RolesRepository rolesRepository) {
    this.rolesRepository = rolesRepository;
  }

  /**
   * {@code POST  /roles} : Create a new roles.
   *
   * @param roles the roles to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new roles, or with status {@code 400 (Bad Request)} if the roles has already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/roles")
  public ResponseEntity<Roles> createRoles(@RequestBody Roles roles) throws URISyntaxException {
    log.debug("REST request to save Roles : {}", roles);
    if (roles.getId() != null) {
      throw new BadRequestAlertException("A new roles cannot already have an ID", ENTITY_NAME, "idexists");
    }
    Roles result = rolesRepository.save(roles);
    return ResponseEntity
      .created(new URI("/api/roles/" + result.getId()))
      .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
      .body(result);
  }

  /**
   * {@code PUT  /roles/:id} : Updates an existing roles.
   *
   * @param id the id of the roles to save.
   * @param roles the roles to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated roles,
   * or with status {@code 400 (Bad Request)} if the roles is not valid,
   * or with status {@code 500 (Internal Server Error)} if the roles couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/roles/{id}")
  public ResponseEntity<Roles> updateRoles(@PathVariable(value = "id", required = false) final Long id, @RequestBody Roles roles)
    throws URISyntaxException {
    log.debug("REST request to update Roles : {}, {}", id, roles);
    if (roles.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    if (!Objects.equals(id, roles.getId())) {
      throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
    }

    if (!rolesRepository.existsById(id)) {
      throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
    }

    Roles result = rolesRepository.save(roles);
    return ResponseEntity
      .ok()
      .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, roles.getId().toString()))
      .body(result);
  }

  /**
   * {@code PATCH  /roles/:id} : Partial updates given fields of an existing roles, field will ignore if it is null
   *
   * @param id the id of the roles to save.
   * @param roles the roles to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated roles,
   * or with status {@code 400 (Bad Request)} if the roles is not valid,
   * or with status {@code 404 (Not Found)} if the roles is not found,
   * or with status {@code 500 (Internal Server Error)} if the roles couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PatchMapping(value = "/roles/{id}", consumes = "application/merge-patch+json")
  public ResponseEntity<Roles> partialUpdateRoles(@PathVariable(value = "id", required = false) final Long id, @RequestBody Roles roles)
    throws URISyntaxException {
    log.debug("REST request to partial update Roles partially : {}, {}", id, roles);
    if (roles.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    if (!Objects.equals(id, roles.getId())) {
      throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
    }

    if (!rolesRepository.existsById(id)) {
      throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
    }

    Optional<Roles> result = rolesRepository
      .findById(roles.getId())
      .map(
        existingRoles -> {
          if (roles.getName() != null) {
            existingRoles.setName(roles.getName());
          }
          if (roles.getDescription() != null) {
            existingRoles.setDescription(roles.getDescription());
          }
          if (roles.getStatus() != null) {
            existingRoles.setStatus(roles.getStatus());
          }
          if (roles.getCreatedOn() != null) {
            existingRoles.setCreatedOn(roles.getCreatedOn());
          }
          if (roles.getCreatedBy() != null) {
            existingRoles.setCreatedBy(roles.getCreatedBy());
          }
          if (roles.getUpdatedOn() != null) {
            existingRoles.setUpdatedOn(roles.getUpdatedOn());
          }
          if (roles.getUpdatedBy() != null) {
            existingRoles.setUpdatedBy(roles.getUpdatedBy());
          }
          if (roles.getSecurityserviceId() != null) {
            existingRoles.setSecurityserviceId(roles.getSecurityserviceId());
          }

          return existingRoles;
        }
      )
      .map(rolesRepository::save);

    return ResponseUtil.wrapOrNotFound(
      result,
      HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, roles.getId().toString())
    );
  }

  /**
   * {@code GET  /roles} : get all the roles.
   *
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of roles in body.
   */
  @GetMapping("/roles")
  public List<Roles> getAllRoles() {
    log.debug("REST request to get all Roles");
    return rolesRepository.findAll();
  }

  /**
   * {@code GET  /roles/:id} : get the "id" roles.
   *
   * @param id the id of the roles to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the roles, or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/roles/{id}")
  public ResponseEntity<Roles> getRoles(@PathVariable Long id) {
    log.debug("REST request to get Roles : {}", id);
    Optional<Roles> roles = rolesRepository.findById(id);
    return ResponseUtil.wrapOrNotFound(roles);
  }

  /**
   * {@code DELETE  /roles/:id} : delete the "id" roles.
   *
   * @param id the id of the roles to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/roles/{id}")
  public ResponseEntity<Void> deleteRoles(@PathVariable Long id) {
    log.debug("REST request to delete Roles : {}", id);
    rolesRepository.deleteById(id);
    return ResponseEntity
      .noContent()
      .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
      .build();
  }
}
