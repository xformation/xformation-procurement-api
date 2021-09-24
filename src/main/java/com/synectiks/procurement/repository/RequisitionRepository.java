package com.synectiks.procurement.repository;

import com.synectiks.procurement.domain.Requisition;



import java.util.List;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Requisition entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RequisitionRepository extends JpaRepository<Requisition, Long> {

	  
	  @Query("select r FROM Requisition r WHERE UPPER(r.progressStage) != UPPER(?1) and UPPER(r.progressStage) != UPPER(?2) ")
	  public List<Requisition> getAllRequisition(String rejected, String completed );

}
