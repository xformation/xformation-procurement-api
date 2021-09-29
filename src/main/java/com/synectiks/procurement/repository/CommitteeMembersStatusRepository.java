package com.synectiks.procurement.repository;

import com.synectiks.procurement.domain.CommitteeMembersStatus;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the CommitteeMembersStatus entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CommitteeMembersStatusRepository extends JpaRepository<CommitteeMembersStatus, Long> {
}
