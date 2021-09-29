package com.synectiks.procurement.web.rest;

import com.synectiks.procurement.ProcurementApp;
import com.synectiks.procurement.domain.CommitteeMembersStatus;
import com.synectiks.procurement.repository.CommitteeMembersStatusRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link CommitteeMembersStatusResource} REST controller.
 */
@SpringBootTest(classes = ProcurementApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class CommitteeMembersStatusResourceIT {

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    @Autowired
    private CommitteeMembersStatusRepository committeeMembersStatusRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCommitteeMembersStatusMockMvc;

    private CommitteeMembersStatus committeeMembersStatus;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CommitteeMembersStatus createEntity(EntityManager em) {
        CommitteeMembersStatus committeeMembersStatus = new CommitteeMembersStatus()
            .status(DEFAULT_STATUS);
        return committeeMembersStatus;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CommitteeMembersStatus createUpdatedEntity(EntityManager em) {
        CommitteeMembersStatus committeeMembersStatus = new CommitteeMembersStatus()
            .status(UPDATED_STATUS);
        return committeeMembersStatus;
    }

    @BeforeEach
    public void initTest() {
        committeeMembersStatus = createEntity(em);
    }

    @Test
    @Transactional
    public void createCommitteeMembersStatus() throws Exception {
        int databaseSizeBeforeCreate = committeeMembersStatusRepository.findAll().size();
        // Create the CommitteeMembersStatus
        restCommitteeMembersStatusMockMvc.perform(post("/api/committee-members-statuses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(committeeMembersStatus)))
            .andExpect(status().isCreated());

        // Validate the CommitteeMembersStatus in the database
        List<CommitteeMembersStatus> committeeMembersStatusList = committeeMembersStatusRepository.findAll();
        assertThat(committeeMembersStatusList).hasSize(databaseSizeBeforeCreate + 1);
        CommitteeMembersStatus testCommitteeMembersStatus = committeeMembersStatusList.get(committeeMembersStatusList.size() - 1);
        assertThat(testCommitteeMembersStatus.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createCommitteeMembersStatusWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = committeeMembersStatusRepository.findAll().size();

        // Create the CommitteeMembersStatus with an existing ID
        committeeMembersStatus.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCommitteeMembersStatusMockMvc.perform(post("/api/committee-members-statuses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(committeeMembersStatus)))
            .andExpect(status().isBadRequest());

        // Validate the CommitteeMembersStatus in the database
        List<CommitteeMembersStatus> committeeMembersStatusList = committeeMembersStatusRepository.findAll();
        assertThat(committeeMembersStatusList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllCommitteeMembersStatuses() throws Exception {
        // Initialize the database
        committeeMembersStatusRepository.saveAndFlush(committeeMembersStatus);

        // Get all the committeeMembersStatusList
        restCommitteeMembersStatusMockMvc.perform(get("/api/committee-members-statuses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(committeeMembersStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
    }
    
    @Test
    @Transactional
    public void getCommitteeMembersStatus() throws Exception {
        // Initialize the database
        committeeMembersStatusRepository.saveAndFlush(committeeMembersStatus);

        // Get the committeeMembersStatus
        restCommitteeMembersStatusMockMvc.perform(get("/api/committee-members-statuses/{id}", committeeMembersStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(committeeMembersStatus.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS));
    }
    @Test
    @Transactional
    public void getNonExistingCommitteeMembersStatus() throws Exception {
        // Get the committeeMembersStatus
        restCommitteeMembersStatusMockMvc.perform(get("/api/committee-members-statuses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCommitteeMembersStatus() throws Exception {
        // Initialize the database
        committeeMembersStatusRepository.saveAndFlush(committeeMembersStatus);

        int databaseSizeBeforeUpdate = committeeMembersStatusRepository.findAll().size();

        // Update the committeeMembersStatus
        CommitteeMembersStatus updatedCommitteeMembersStatus = committeeMembersStatusRepository.findById(committeeMembersStatus.getId()).get();
        // Disconnect from session so that the updates on updatedCommitteeMembersStatus are not directly saved in db
        em.detach(updatedCommitteeMembersStatus);
        updatedCommitteeMembersStatus
            .status(UPDATED_STATUS);

        restCommitteeMembersStatusMockMvc.perform(put("/api/committee-members-statuses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedCommitteeMembersStatus)))
            .andExpect(status().isOk());

        // Validate the CommitteeMembersStatus in the database
        List<CommitteeMembersStatus> committeeMembersStatusList = committeeMembersStatusRepository.findAll();
        assertThat(committeeMembersStatusList).hasSize(databaseSizeBeforeUpdate);
        CommitteeMembersStatus testCommitteeMembersStatus = committeeMembersStatusList.get(committeeMembersStatusList.size() - 1);
        assertThat(testCommitteeMembersStatus.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingCommitteeMembersStatus() throws Exception {
        int databaseSizeBeforeUpdate = committeeMembersStatusRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommitteeMembersStatusMockMvc.perform(put("/api/committee-members-statuses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(committeeMembersStatus)))
            .andExpect(status().isBadRequest());

        // Validate the CommitteeMembersStatus in the database
        List<CommitteeMembersStatus> committeeMembersStatusList = committeeMembersStatusRepository.findAll();
        assertThat(committeeMembersStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCommitteeMembersStatus() throws Exception {
        // Initialize the database
        committeeMembersStatusRepository.saveAndFlush(committeeMembersStatus);

        int databaseSizeBeforeDelete = committeeMembersStatusRepository.findAll().size();

        // Delete the committeeMembersStatus
        restCommitteeMembersStatusMockMvc.perform(delete("/api/committee-members-statuses/{id}", committeeMembersStatus.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CommitteeMembersStatus> committeeMembersStatusList = committeeMembersStatusRepository.findAll();
        assertThat(committeeMembersStatusList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
