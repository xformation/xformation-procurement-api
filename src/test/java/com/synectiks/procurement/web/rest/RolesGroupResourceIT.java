package com.synectiks.procurement.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.synectiks.procurement.domain.RolesGroup;
import com.synectiks.procurement.repository.RolesGroupRepository;

/**
 * Integration tests for the {@link RolesGroupResource} REST controller.
 */

@AutoConfigureMockMvc
@WithMockUser
class RolesGroupResourceIT {

  private static final Boolean DEFAULT_GROUP = false;
  private static final Boolean UPDATED_GROUP = true;

  private static final String ENTITY_API_URL = "/api/roles-groups";
  private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

  private static Random random = new Random();
  private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

  @Autowired
  private RolesGroupRepository rolesGroupRepository;

  @Autowired
  private EntityManager em;

  @Autowired
  private MockMvc restRolesGroupMockMvc;

  private RolesGroup rolesGroup;

  /**
   * Create an entity for this test.
   *
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static RolesGroup createEntity(EntityManager em) {
    RolesGroup rolesGroup = new RolesGroup().group(DEFAULT_GROUP);
    return rolesGroup;
  }

  /**
   * Create an updated entity for this test.
   *
   * This is a static method, as tests for other entities might also need it,
   * if they test an entity which requires the current entity.
   */
  public static RolesGroup createUpdatedEntity(EntityManager em) {
    RolesGroup rolesGroup = new RolesGroup().group(UPDATED_GROUP);
    return rolesGroup;
  }

  @BeforeEach
  public void initTest() {
    rolesGroup = createEntity(em);
  }

  @Test
  @Transactional
  void createRolesGroup() throws Exception {
    int databaseSizeBeforeCreate = rolesGroupRepository.findAll().size();
    // Create the RolesGroup
    restRolesGroupMockMvc
      .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rolesGroup)))
      .andExpect(status().isCreated());

    // Validate the RolesGroup in the database
    List<RolesGroup> rolesGroupList = rolesGroupRepository.findAll();
    assertThat(rolesGroupList).hasSize(databaseSizeBeforeCreate + 1);
    RolesGroup testRolesGroup = rolesGroupList.get(rolesGroupList.size() - 1);
    assertThat(testRolesGroup.getGroup()).isEqualTo(DEFAULT_GROUP);
  }

  @Test
  @Transactional
  void createRolesGroupWithExistingId() throws Exception {
    // Create the RolesGroup with an existing ID
    rolesGroup.setId(1L);

    int databaseSizeBeforeCreate = rolesGroupRepository.findAll().size();

    // An entity with an existing ID cannot be created, so this API call must fail
    restRolesGroupMockMvc
      .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rolesGroup)))
      .andExpect(status().isBadRequest());

    // Validate the RolesGroup in the database
    List<RolesGroup> rolesGroupList = rolesGroupRepository.findAll();
    assertThat(rolesGroupList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  void getAllRolesGroups() throws Exception {
    // Initialize the database
    rolesGroupRepository.saveAndFlush(rolesGroup);

    // Get all the rolesGroupList
    restRolesGroupMockMvc
      .perform(get(ENTITY_API_URL + "?sort=id,desc"))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
      .andExpect(jsonPath("$.[*].id").value(hasItem(rolesGroup.getId().intValue())))
      .andExpect(jsonPath("$.[*].group").value(hasItem(DEFAULT_GROUP.booleanValue())));
  }

  @Test
  @Transactional
  void getRolesGroup() throws Exception {
    // Initialize the database
    rolesGroupRepository.saveAndFlush(rolesGroup);

    // Get the rolesGroup
    restRolesGroupMockMvc
      .perform(get(ENTITY_API_URL_ID, rolesGroup.getId()))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
      .andExpect(jsonPath("$.id").value(rolesGroup.getId().intValue()))
      .andExpect(jsonPath("$.group").value(DEFAULT_GROUP.booleanValue()));
  }

  @Test
  @Transactional
  void getNonExistingRolesGroup() throws Exception {
    // Get the rolesGroup
    restRolesGroupMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  void putNewRolesGroup() throws Exception {
    // Initialize the database
    rolesGroupRepository.saveAndFlush(rolesGroup);

    int databaseSizeBeforeUpdate = rolesGroupRepository.findAll().size();

    // Update the rolesGroup
    RolesGroup updatedRolesGroup = rolesGroupRepository.findById(rolesGroup.getId()).get();
    // Disconnect from session so that the updates on updatedRolesGroup are not directly saved in db
    em.detach(updatedRolesGroup);
    updatedRolesGroup.group(UPDATED_GROUP);

    restRolesGroupMockMvc
      .perform(
        put(ENTITY_API_URL_ID, updatedRolesGroup.getId())
          .contentType(MediaType.APPLICATION_JSON)
          .content(TestUtil.convertObjectToJsonBytes(updatedRolesGroup))
      )
      .andExpect(status().isOk());

    // Validate the RolesGroup in the database
    List<RolesGroup> rolesGroupList = rolesGroupRepository.findAll();
    assertThat(rolesGroupList).hasSize(databaseSizeBeforeUpdate);
    RolesGroup testRolesGroup = rolesGroupList.get(rolesGroupList.size() - 1);
    assertThat(testRolesGroup.getGroup()).isEqualTo(UPDATED_GROUP);
  }

  @Test
  @Transactional
  void putNonExistingRolesGroup() throws Exception {
    int databaseSizeBeforeUpdate = rolesGroupRepository.findAll().size();
    rolesGroup.setId(count.incrementAndGet());

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restRolesGroupMockMvc
      .perform(
        put(ENTITY_API_URL_ID, rolesGroup.getId())
          .contentType(MediaType.APPLICATION_JSON)
          .content(TestUtil.convertObjectToJsonBytes(rolesGroup))
      )
      .andExpect(status().isBadRequest());

    // Validate the RolesGroup in the database
    List<RolesGroup> rolesGroupList = rolesGroupRepository.findAll();
    assertThat(rolesGroupList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void putWithIdMismatchRolesGroup() throws Exception {
    int databaseSizeBeforeUpdate = rolesGroupRepository.findAll().size();
    rolesGroup.setId(count.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restRolesGroupMockMvc
      .perform(
        put(ENTITY_API_URL_ID, count.incrementAndGet())
          .contentType(MediaType.APPLICATION_JSON)
          .content(TestUtil.convertObjectToJsonBytes(rolesGroup))
      )
      .andExpect(status().isBadRequest());

    // Validate the RolesGroup in the database
    List<RolesGroup> rolesGroupList = rolesGroupRepository.findAll();
    assertThat(rolesGroupList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void putWithMissingIdPathParamRolesGroup() throws Exception {
    int databaseSizeBeforeUpdate = rolesGroupRepository.findAll().size();
    rolesGroup.setId(count.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restRolesGroupMockMvc
      .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rolesGroup)))
      .andExpect(status().isMethodNotAllowed());

    // Validate the RolesGroup in the database
    List<RolesGroup> rolesGroupList = rolesGroupRepository.findAll();
    assertThat(rolesGroupList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void partialUpdateRolesGroupWithPatch() throws Exception {
    // Initialize the database
    rolesGroupRepository.saveAndFlush(rolesGroup);

    int databaseSizeBeforeUpdate = rolesGroupRepository.findAll().size();

    // Update the rolesGroup using partial update
    RolesGroup partialUpdatedRolesGroup = new RolesGroup();
    partialUpdatedRolesGroup.setId(rolesGroup.getId());

    partialUpdatedRolesGroup.group(UPDATED_GROUP);

    restRolesGroupMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, partialUpdatedRolesGroup.getId())
          .contentType("application/merge-patch+json")
          .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRolesGroup))
      )
      .andExpect(status().isOk());

    // Validate the RolesGroup in the database
    List<RolesGroup> rolesGroupList = rolesGroupRepository.findAll();
    assertThat(rolesGroupList).hasSize(databaseSizeBeforeUpdate);
    RolesGroup testRolesGroup = rolesGroupList.get(rolesGroupList.size() - 1);
    assertThat(testRolesGroup.getGroup()).isEqualTo(UPDATED_GROUP);
  }

  @Test
  @Transactional
  void fullUpdateRolesGroupWithPatch() throws Exception {
    // Initialize the database
    rolesGroupRepository.saveAndFlush(rolesGroup);

    int databaseSizeBeforeUpdate = rolesGroupRepository.findAll().size();

    // Update the rolesGroup using partial update
    RolesGroup partialUpdatedRolesGroup = new RolesGroup();
    partialUpdatedRolesGroup.setId(rolesGroup.getId());

    partialUpdatedRolesGroup.group(UPDATED_GROUP);

    restRolesGroupMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, partialUpdatedRolesGroup.getId())
          .contentType("application/merge-patch+json")
          .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRolesGroup))
      )
      .andExpect(status().isOk());

    // Validate the RolesGroup in the database
    List<RolesGroup> rolesGroupList = rolesGroupRepository.findAll();
    assertThat(rolesGroupList).hasSize(databaseSizeBeforeUpdate);
    RolesGroup testRolesGroup = rolesGroupList.get(rolesGroupList.size() - 1);
    assertThat(testRolesGroup.getGroup()).isEqualTo(UPDATED_GROUP);
  }

  @Test
  @Transactional
  void patchNonExistingRolesGroup() throws Exception {
    int databaseSizeBeforeUpdate = rolesGroupRepository.findAll().size();
    rolesGroup.setId(count.incrementAndGet());

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restRolesGroupMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, rolesGroup.getId())
          .contentType("application/merge-patch+json")
          .content(TestUtil.convertObjectToJsonBytes(rolesGroup))
      )
      .andExpect(status().isBadRequest());

    // Validate the RolesGroup in the database
    List<RolesGroup> rolesGroupList = rolesGroupRepository.findAll();
    assertThat(rolesGroupList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void patchWithIdMismatchRolesGroup() throws Exception {
    int databaseSizeBeforeUpdate = rolesGroupRepository.findAll().size();
    rolesGroup.setId(count.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restRolesGroupMockMvc
      .perform(
        patch(ENTITY_API_URL_ID, count.incrementAndGet())
          .contentType("application/merge-patch+json")
          .content(TestUtil.convertObjectToJsonBytes(rolesGroup))
      )
      .andExpect(status().isBadRequest());

    // Validate the RolesGroup in the database
    List<RolesGroup> rolesGroupList = rolesGroupRepository.findAll();
    assertThat(rolesGroupList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void patchWithMissingIdPathParamRolesGroup() throws Exception {
    int databaseSizeBeforeUpdate = rolesGroupRepository.findAll().size();
    rolesGroup.setId(count.incrementAndGet());

    // If url ID doesn't match entity ID, it will throw BadRequestAlertException
    restRolesGroupMockMvc
      .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(rolesGroup)))
      .andExpect(status().isMethodNotAllowed());

    // Validate the RolesGroup in the database
    List<RolesGroup> rolesGroupList = rolesGroupRepository.findAll();
    assertThat(rolesGroupList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  void deleteRolesGroup() throws Exception {
    // Initialize the database
    rolesGroupRepository.saveAndFlush(rolesGroup);

    int databaseSizeBeforeDelete = rolesGroupRepository.findAll().size();

    // Delete the rolesGroup
    restRolesGroupMockMvc
      .perform(delete(ENTITY_API_URL_ID, rolesGroup.getId()).accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isNoContent());

    // Validate the database contains one less item
    List<RolesGroup> rolesGroupList = rolesGroupRepository.findAll();
    assertThat(rolesGroupList).hasSize(databaseSizeBeforeDelete - 1);
  }
}
