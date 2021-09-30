package com.synectiks.procurement.web.rest;

import com.synectiks.procurement.ProcurementApp;
import com.synectiks.procurement.domain.CommitteeMembers;
import com.synectiks.procurement.repository.CommitteeMembersRepository;

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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link CommitteeMembersResource} REST controller.
 */
@SpringBootTest(classes = ProcurementApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class CommitteeMembersResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_COMPANY = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY = "BBBBBBBBBB";

    private static final String DEFAULT_DEPARTMENT = "AAAAAAAAAA";
    private static final String UPDATED_DEPARTMENT = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_DEGRADATION = "AAAAAAAAAA";
    private static final String UPDATED_DEGRADATION = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_UPDATED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_UPDATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_UPDATED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_PROFILE_IMAGE = "AAAAAAAAAA";
    private static final String UPDATED_PROFILE_IMAGE = "BBBBBBBBBB";

    @Autowired
    private CommitteeMembersRepository committeeMembersRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCommitteeMembersMockMvc;

    private CommitteeMembers committeeMembers;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CommitteeMembers createEntity(EntityManager em) {
        CommitteeMembers committeeMembers = new CommitteeMembers()
            .name(DEFAULT_NAME)
            .company(DEFAULT_COMPANY)
            .department(DEFAULT_DEPARTMENT)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .email(DEFAULT_EMAIL)
            .degradation(DEFAULT_DEGRADATION)
            .createdOn(DEFAULT_CREATED_ON)
            .createdBy(DEFAULT_CREATED_BY)
            .updatedOn(DEFAULT_UPDATED_ON)
            .updatedBy(DEFAULT_UPDATED_BY)
            .profileImage(DEFAULT_PROFILE_IMAGE);
        return committeeMembers;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CommitteeMembers createUpdatedEntity(EntityManager em) {
        CommitteeMembers committeeMembers = new CommitteeMembers()
            .name(UPDATED_NAME)
            .company(UPDATED_COMPANY)
            .department(UPDATED_DEPARTMENT)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .email(UPDATED_EMAIL)
            .degradation(UPDATED_DEGRADATION)
            .createdOn(UPDATED_CREATED_ON)
            .createdBy(UPDATED_CREATED_BY)
            .updatedOn(UPDATED_UPDATED_ON)
            .updatedBy(UPDATED_UPDATED_BY)
            .profileImage(UPDATED_PROFILE_IMAGE);
        return committeeMembers;
    }

    @BeforeEach
    public void initTest() {
        committeeMembers = createEntity(em);
    }

    @Test
    @Transactional
    public void createCommitteeMembers() throws Exception {
        int databaseSizeBeforeCreate = committeeMembersRepository.findAll().size();
        // Create the CommitteeMembers
        restCommitteeMembersMockMvc.perform(post("/api/committee-members")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(committeeMembers)))
            .andExpect(status().isCreated());

        // Validate the CommitteeMembers in the database
        List<CommitteeMembers> committeeMembersList = committeeMembersRepository.findAll();
        assertThat(committeeMembersList).hasSize(databaseSizeBeforeCreate + 1);
        CommitteeMembers testCommitteeMembers = committeeMembersList.get(committeeMembersList.size() - 1);
        assertThat(testCommitteeMembers.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCommitteeMembers.getCompany()).isEqualTo(DEFAULT_COMPANY);
        assertThat(testCommitteeMembers.getDepartment()).isEqualTo(DEFAULT_DEPARTMENT);
        assertThat(testCommitteeMembers.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testCommitteeMembers.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testCommitteeMembers.getDegradation()).isEqualTo(DEFAULT_DEGRADATION);
        assertThat(testCommitteeMembers.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
        assertThat(testCommitteeMembers.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testCommitteeMembers.getUpdatedOn()).isEqualTo(DEFAULT_UPDATED_ON);
        assertThat(testCommitteeMembers.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        assertThat(testCommitteeMembers.getProfileImage()).isEqualTo(DEFAULT_PROFILE_IMAGE);
    }

    @Test
    @Transactional
    public void createCommitteeMembersWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = committeeMembersRepository.findAll().size();

        // Create the CommitteeMembers with an existing ID
        committeeMembers.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCommitteeMembersMockMvc.perform(post("/api/committee-members")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(committeeMembers)))
            .andExpect(status().isBadRequest());

        // Validate the CommitteeMembers in the database
        List<CommitteeMembers> committeeMembersList = committeeMembersRepository.findAll();
        assertThat(committeeMembersList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllCommitteeMembers() throws Exception {
        // Initialize the database
        committeeMembersRepository.saveAndFlush(committeeMembers);

        // Get all the committeeMembersList
        restCommitteeMembersMockMvc.perform(get("/api/committee-members?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(committeeMembers.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].company").value(hasItem(DEFAULT_COMPANY)))
            .andExpect(jsonPath("$.[*].department").value(hasItem(DEFAULT_DEPARTMENT)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].degradation").value(hasItem(DEFAULT_DEGRADATION)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].updatedOn").value(hasItem(DEFAULT_UPDATED_ON.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].profileImage").value(hasItem(DEFAULT_PROFILE_IMAGE)));
    }
    
    @Test
    @Transactional
    public void getCommitteeMembers() throws Exception {
        // Initialize the database
        committeeMembersRepository.saveAndFlush(committeeMembers);

        // Get the committeeMembers
        restCommitteeMembersMockMvc.perform(get("/api/committee-members/{id}", committeeMembers.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(committeeMembers.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.company").value(DEFAULT_COMPANY))
            .andExpect(jsonPath("$.department").value(DEFAULT_DEPARTMENT))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.degradation").value(DEFAULT_DEGRADATION))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.updatedOn").value(DEFAULT_UPDATED_ON.toString()))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY))
            .andExpect(jsonPath("$.profileImage").value(DEFAULT_PROFILE_IMAGE));
    }
    @Test
    @Transactional
    public void getNonExistingCommitteeMembers() throws Exception {
        // Get the committeeMembers
        restCommitteeMembersMockMvc.perform(get("/api/committee-members/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCommitteeMembers() throws Exception {
        // Initialize the database
        committeeMembersRepository.saveAndFlush(committeeMembers);

        int databaseSizeBeforeUpdate = committeeMembersRepository.findAll().size();

        // Update the committeeMembers
        CommitteeMembers updatedCommitteeMembers = committeeMembersRepository.findById(committeeMembers.getId()).get();
        // Disconnect from session so that the updates on updatedCommitteeMembers are not directly saved in db
        em.detach(updatedCommitteeMembers);
        updatedCommitteeMembers
            .name(UPDATED_NAME)
            .company(UPDATED_COMPANY)
            .department(UPDATED_DEPARTMENT)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .email(UPDATED_EMAIL)
            .degradation(UPDATED_DEGRADATION)
            .createdOn(UPDATED_CREATED_ON)
            .createdBy(UPDATED_CREATED_BY)
            .updatedOn(UPDATED_UPDATED_ON)
            .updatedBy(UPDATED_UPDATED_BY)
            .profileImage(UPDATED_PROFILE_IMAGE);

        restCommitteeMembersMockMvc.perform(put("/api/committee-members")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedCommitteeMembers)))
            .andExpect(status().isOk());

        // Validate the CommitteeMembers in the database
        List<CommitteeMembers> committeeMembersList = committeeMembersRepository.findAll();
        assertThat(committeeMembersList).hasSize(databaseSizeBeforeUpdate);
        CommitteeMembers testCommitteeMembers = committeeMembersList.get(committeeMembersList.size() - 1);
        assertThat(testCommitteeMembers.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCommitteeMembers.getCompany()).isEqualTo(UPDATED_COMPANY);
        assertThat(testCommitteeMembers.getDepartment()).isEqualTo(UPDATED_DEPARTMENT);
        assertThat(testCommitteeMembers.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testCommitteeMembers.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testCommitteeMembers.getDegradation()).isEqualTo(UPDATED_DEGRADATION);
        assertThat(testCommitteeMembers.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testCommitteeMembers.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testCommitteeMembers.getUpdatedOn()).isEqualTo(UPDATED_UPDATED_ON);
        assertThat(testCommitteeMembers.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testCommitteeMembers.getProfileImage()).isEqualTo(UPDATED_PROFILE_IMAGE);
    }

    @Test
    @Transactional
    public void updateNonExistingCommitteeMembers() throws Exception {
        int databaseSizeBeforeUpdate = committeeMembersRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommitteeMembersMockMvc.perform(put("/api/committee-members")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(committeeMembers)))
            .andExpect(status().isBadRequest());

        // Validate the CommitteeMembers in the database
        List<CommitteeMembers> committeeMembersList = committeeMembersRepository.findAll();
        assertThat(committeeMembersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCommitteeMembers() throws Exception {
        // Initialize the database
        committeeMembersRepository.saveAndFlush(committeeMembers);

        int databaseSizeBeforeDelete = committeeMembersRepository.findAll().size();

        // Delete the committeeMembers
        restCommitteeMembersMockMvc.perform(delete("/api/committee-members/{id}", committeeMembers.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CommitteeMembers> committeeMembersList = committeeMembersRepository.findAll();
        assertThat(committeeMembersList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
