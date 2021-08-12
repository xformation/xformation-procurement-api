package com.synectiks.procurement.controllers;

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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.synectiks.procurement.config.Constants;
import com.synectiks.procurement.domain.Requisition;
import com.synectiks.procurement.domain.RequisitionActivity;
import com.synectiks.procurement.domain.RequisitionLineItem;
import com.synectiks.procurement.domain.RequisitionLineItemActivity;
import com.synectiks.procurement.domain.Status;
import com.synectiks.procurement.repository.RequisitionLineItemActivityRepository;
import com.synectiks.procurement.repository.RequisitionLineItemRepository;
import com.synectiks.procurement.repository.RequisitionRepository;

import io.github.jhipster.web.util.HeaderUtil;

@RestController
@RequestMapping("/api")
public class RequisitionLineItemController {
	private static final Logger logger = LoggerFactory.getLogger(RequisitionLineItemController.class);
	private static final String ENTITY_NAME = "requisitionLineItem";
	
	@Value("${jhipster.clientApp.name}")
	private String applicationName;
	@Autowired
	private RequisitionLineItemRepository requisitionLineItemRepository;
	@Autowired
	private RequisitionLineItemActivityRepository requisitionLineItemActivityRepository;
	@Autowired
	private RequisitionRepository requisitionRepository;
	@PostMapping("/addRequisitionLineItem")
	public  ResponseEntity<Object> addRequisitionLineItem(@RequestBody ObjectNode obj) throws JSONException {
				Status st = new Status();
		//		 String id =(obj.get("id")).asText();
		 Optional<Requisition> ur = requisitionRepository.findById(Long.parseLong(obj.get("id").asText()));
		 if(!ur.isPresent()) {
				st.setCode(HttpStatus.EXPECTATION_FAILED.value());
				st.setType("ERROR");
				st.setMessage("Requisition id not found");
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		 }
			 
				try {
					RequisitionLineItem requisitionLineItem = new RequisitionLineItem();
					
				    requisitionLineItem.setRequisition(ur.get());
				if (obj.get("progressStage") != null) {
					requisitionLineItem.setProgressStage(obj.get("progressStage").asText());
				}
				if (obj.get("itemDescription") != null) {
					requisitionLineItem.setItemDescription(obj.get("itemDescription").asText());
				}
				if (obj.get("orderQuantity") != null) {
					requisitionLineItem.setOrderQuantity(obj.get("orderQuantity").asInt());
				}
				if (obj.get("priority") != null) {
					requisitionLineItem.setPriority(obj.get("priority").asText());
				}
				if (obj.get("price") != null) {
					requisitionLineItem.setPrice(obj.get("price").asInt());
				}
				if (obj.get("notes") != null) {
					requisitionLineItem.setNotes(obj.get("notes").asText());
				}
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
				LocalDate localDate = LocalDate.parse(obj.get("dueDate").asText(), formatter);
				requisitionLineItem.setDueDate(localDate);
				
				requisitionLineItem.setCreatedBy(Constants.SYSTEM_ACCOUNT);
				requisitionLineItem.setUpdatedBy(Constants.SYSTEM_ACCOUNT);
				Instant now = Instant.now();
				requisitionLineItem.setCreatedOn(now);
				requisitionLineItem.setUpdatedOn(now);
				requisitionLineItem = requisitionLineItemRepository.save(requisitionLineItem);
				logger.info("Add New requisitionLineItem SUCCESS");
				if(requisitionLineItem.getId() != null) {
					RequisitionLineItemActivity requisitionLineItemActivity = new RequisitionLineItemActivity();
						BeanUtils.copyProperties(requisitionLineItem, requisitionLineItemActivity);	
						requisitionLineItemActivity.setRequisitionLineItem(requisitionLineItem);
						requisitionLineItemActivity = requisitionLineItemActivityRepository.save(requisitionLineItemActivity);
						logger.info("Add New RequisitionLineItemActivity");
					}
				st.setCode(HttpStatus.OK.value());
				st.setType("SUCCESS");
				st.setMessage("Add New requisitionLineItem SUCCESS : "+requisitionLineItem);
				st.setObject(requisitionLineItem);
				return ResponseEntity.status(HttpStatus.OK).body(st);
				
				}catch (Exception e) {
					logger.error("Add New requisitionLineItem failed. Exception: ", e);
					st.setCode(HttpStatus.EXPECTATION_FAILED.value());
					st.setType("ERROR");
					st.setMessage("Add New requisitionLineItem failed");
					return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
				}
		 }

	
	@PutMapping("/updateRequisitionLineItem")
	public  ResponseEntity<Object> updateRequisitionLineItem(@RequestBody ObjectNode obj) throws JSONException, URISyntaxException {
//		 String id =(obj.get("id")).asText();
		Status st = new Status();
		 Optional<RequisitionLineItem> ur = requisitionLineItemRepository.findById(Long.parseLong(obj.get("id").asText()));
		 if(!ur.isPresent()) {
				return ResponseEntity.created(new URI("/api/updateRequisitionLineItem/")).headers(HeaderUtil
						.createEntityCreationAlert(applicationName, false, ENTITY_NAME, ""))
						.body(null);
			 }
		try {
			RequisitionLineItem requisitionLineItem = new RequisitionLineItem();

			if (obj.get("progressStage") != null) {
				requisitionLineItem.setProgressStage(obj.get("progressStage").asText());
			}
			if (obj.get("itemDescription") != null) {
				requisitionLineItem.setItemDescription(obj.get("itemDescription").asText());
			}
			if (obj.get("orderQuantity") != null) {
				requisitionLineItem.setOrderQuantity(obj.get("orderQuantity").asInt());
			}
			if (obj.get("priority") != null) {
				requisitionLineItem.setPriority(obj.get("priority").asText());
			}
			if (obj.get("price") != null) {
				requisitionLineItem.setPrice(obj.get("price").asInt());
			}
			if (obj.get("notes") != null) {
				requisitionLineItem.setNotes(obj.get("notes").asText());
			}
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
			LocalDate localDate = LocalDate.parse(obj.get("dueDate").asText(), formatter);
			requisitionLineItem.setDueDate(localDate);
			
		
		 if (obj.get("user") != null) {
			 requisitionLineItem.setUpdatedBy(obj.get("user").asText());
			 } else {
				 requisitionLineItem.setUpdatedBy(Constants.SYSTEM_ACCOUNT);
			 }
		 
			 Instant now = Instant.now();
			 requisitionLineItem.setUpdatedOn(now);
			 requisitionLineItem = requisitionLineItemRepository.save(requisitionLineItem);
		logger.info("Updating requisition completed");
		st.setCode(HttpStatus.OK.value());
		st.setType("SUCCESS");
		st.setMessage("Updating requisitionLineItem SUCCESS : "+requisitionLineItem);
		st.setObject(requisitionLineItem);
		return ResponseEntity.status(HttpStatus.OK).body(st);
		
		}catch (Exception e) {
			logger.error("Updating requisitionLineItem failed. Exception: ", e);			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("Updating requisitionLineItem failed");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}
		
	}
	
	@GetMapping("/searchRequisitionLineItem")
	public List<RequisitionLineItem> searchRequisition(@RequestParam Map<String, String> requestObj) {
		logger.info("Request to get requisitionLineItem on given filter criteria");
		RequisitionLineItem requisitionLineItem = new RequisitionLineItem();
		
		try {
		boolean isFilter = false;
		if (requestObj.get("id") != null) {
			requisitionLineItem.setId(Long.parseLong(requestObj.get("id")));
			isFilter = true;
		}
//		if (requestObj.get("departmentId") != null) {
//			requisition.setDepartment(requestObj.get("departmentId"));
//			isFilter = true;
//		}
		
		if (requestObj.get("progressStage") != null) {
			requisitionLineItem.setProgressStage(requestObj.get("progressStage"));
			isFilter = true;
		}
		if (requestObj.get("itemDescription") != null) {
			requisitionLineItem.setItemDescription(requestObj.get("itemDescription"));
			isFilter = true;
		}
		if (requestObj.get("orderQuantity") != null) {
			requisitionLineItem.setOrderQuantity(Integer.valueOf(requestObj.get("progressStage")));
			isFilter = true;
		}
		if (requestObj.get("priority") != null) {
			requisitionLineItem.setPriority(requestObj.get("priority"));
			isFilter = true;
		}
		if (requestObj.get("price") != null) {
			requisitionLineItem.setPrice(Integer.valueOf(requestObj.get("price")));
			isFilter = true;
		}
		if (requestObj.get("notes") != null) {
			requisitionLineItem.setNotes(requestObj.get("notes"));
			isFilter = true;
		}
		List<RequisitionLineItem> list = null;
		if (isFilter) {
			list = this.requisitionLineItemRepository.findAll(Example.of(requisitionLineItem), Sort.by(Direction.DESC, "id"));
		} else {
			list = this.requisitionLineItemRepository.findAll(Sort.by(Direction.DESC, "id"));
		}
		
        logger.info("search data ");
		
		Status st = new Status();
		st.setCode(HttpStatus.OK.value());
		st.setType("SUCCESS");
		st.setMessage("search data get SUCCESS : "+list);
		st.setObject(list);
		return (List<RequisitionLineItem>) ResponseEntity.status(HttpStatus.OK).body(st);
		
		}catch (Exception e) {
			logger.error("Updating RequisitionLineItem failed. Exception: ", e);
			Status st = new Status();
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("search data get failed");
			return (List<RequisitionLineItem>) ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}
		
	}
	 @DeleteMapping("/requisitionLineItem/{id}")
	    public ResponseEntity<Object> deletePost(@PathVariable Long id) {
		 try {
			 requisitionLineItemRepository.deleteById(id);
			} catch (Throwable th) {
				logger.error(th.getMessage(), th);
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(th);
			}
			return ResponseEntity.status(HttpStatus.OK)
					.body("User removed Successfully");
		}
}
