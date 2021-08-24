package com.synectiks.procurement.business.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.synectiks.procurement.config.Constants;
import com.synectiks.procurement.domain.Requisition;
import com.synectiks.procurement.domain.RequisitionLineItem;
import com.synectiks.procurement.domain.RequisitionLineItemActivity;
import com.synectiks.procurement.domain.Status;
import com.synectiks.procurement.repository.RequisitionLineItemActivityRepository;
import com.synectiks.procurement.repository.RequisitionLineItemRepository;
import com.synectiks.procurement.repository.RequisitionRepository;

import io.github.jhipster.web.util.HeaderUtil;

@Service
public class RequisitionLineItemService {
	private static final Logger logger = LoggerFactory.getLogger(RequisitionLineItemService.class);
	
	@Autowired
	private RequisitionLineItemRepository requisitionLineItemRepository;
	
	@Autowired
	private RequisitionLineItemActivityService requisitionLineItemActivityService;
	
	@Autowired
	private RequisitionRepository requisitionRepository;
	
	public RequisitionLineItem addRequisitionLineItem(RequisitionLineItem obj) {
		logger.info("Add Requisition line item");
		obj = requisitionLineItemRepository.save(obj);
		
		if(obj != null) {
			logger.info("Saving requisition line item acitivity");
			RequisitionLineItemActivity requisitionLineItemActivity = new RequisitionLineItemActivity();
			BeanUtils.copyProperties(obj, requisitionLineItemActivity);	
			requisitionLineItemActivity.setRequisitionLineItem(obj);
			requisitionLineItemActivity = requisitionLineItemActivityService.addRequisitionLineItemActivity(requisitionLineItemActivity);
			logger.info("Requisition line item acitivity saved successfully");
		}
		
		return obj;
	}
	
	public RequisitionLineItem addRequisitionLineItem(ObjectNode obj) throws JSONException {
		logger.info("Adding Requisition line item");
		RequisitionLineItem requisitionLineItem = new RequisitionLineItem();
		
		Optional<Requisition> oReq = requisitionRepository.findById(Long.parseLong(obj.get("id").asText()));
		if(!oReq.isPresent()) {
			logger.error("Requisition line item cannot be added. Requisition missing");
			return null;
		}
		requisitionLineItem.setRequisition(oReq.get());
		
		if (obj.get("status") != null) {
			requisitionLineItem.setStatus(obj.get("status").asText());
		}
		
		if (obj.get("progressStage") != null) {
			requisitionLineItem.setProgressStage(obj.get("progressStage").asText());
		}
		
		if (obj.get("itemDescription") != null) {
			requisitionLineItem.setItemDescription(obj.get("itemDescription").asText());
		}
		
		if (obj.get("orderQuantity") != null) {
			requisitionLineItem.setOrderQuantity(obj.get("orderQuantity").asInt());
		}
		
		if (obj.get("price") != null) {
			requisitionLineItem.setPrice(obj.get("price").asInt());
		}
		
		if (obj.get("priority") != null) {
			requisitionLineItem.setPriority(obj.get("priority").asText());
		}
		
		if (obj.get("notes") != null) {
			requisitionLineItem.setNotes(obj.get("notes").asText());
		}
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DEFAULT_DATE_FORMAT);
		if(obj.get("dueDate") != null) {
			LocalDate localDate = LocalDate.parse(obj.get("dueDate").asText(), formatter);
			requisitionLineItem.setDueDate(localDate);
		}
		
		if(obj.get("user") != null) {
			requisitionLineItem.setCreatedBy(obj.get("user").asText());
			requisitionLineItem.setUpdatedBy(obj.get("user").asText());
		}else {
			requisitionLineItem.setCreatedBy(Constants.SYSTEM_ACCOUNT);
			requisitionLineItem.setUpdatedBy(Constants.SYSTEM_ACCOUNT);
			
		}
		
		Instant now = Instant.now();
		requisitionLineItem.setCreatedOn(now);
		requisitionLineItem.setUpdatedOn(now);
		logger.debug("RequisitionLineItem object: "+requisitionLineItem.toString());
		requisitionLineItem = requisitionLineItemRepository.save(requisitionLineItem);
		logger.info("Requisition line item added successfully");
		
		if(requisitionLineItem != null) {
			logger.info("Adding requisition line item acitivity");
			RequisitionLineItemActivity requisitionLineItemActivity = new RequisitionLineItemActivity();
			BeanUtils.copyProperties(requisitionLineItem, requisitionLineItemActivity);	
			requisitionLineItemActivity.setRequisitionLineItem(requisitionLineItem);
			requisitionLineItemActivity = requisitionLineItemActivityService.addRequisitionLineItemActivity(requisitionLineItemActivity);
			logger.info("Requisition line item acitivity added successfully");
		}
		
		return requisitionLineItem;
	}

	
	
}
