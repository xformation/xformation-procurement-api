package com.synectiks.procurement.repository;

import com.synectiks.procurement.domain.Committee;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Committee entity.
 */
@Repository
public interface CommitteeRepository extends JpaRepository<Committee, Long> {

    @Query(value = "select distinct committee from Committee committee left join fetch committee.contacts",
        countQuery = "select count(distinct committee) from Committee committee")
    Page<Committee> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct committee from Committee committee left join fetch committee.contacts")
    List<Committee> findAllWithEagerRelationships();

    @Query("select committee from Committee committee left join fetch committee.contacts where committee.id =:id")
    Optional<Committee> findOneWithEagerRelationships(@Param("id") Long id);
}
