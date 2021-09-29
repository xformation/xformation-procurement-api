package com.synectiks.procurement.repository;

import com.synectiks.procurement.domain.CommitteeMembers;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the CommitteeMembers entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CommitteeMembersRepository extends JpaRepository<CommitteeMembers, Long> {
}
