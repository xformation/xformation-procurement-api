package com.synectiks.procurement.controllers;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
//import com.synectiks.asset.domain.Asset;
//import com.synectiks.asset.domain.OrganizationalUnit;
import com.synectiks.procurement.config.Constants;
import com.synectiks.procurement.domain.Currency;
import com.synectiks.procurement.domain.Department;
import com.synectiks.procurement.domain.Requisition;
import com.synectiks.procurement.domain.RequisitionActivity;
import com.synectiks.procurement.domain.RequisitionLineItem;
import com.synectiks.procurement.domain.Status;
import com.synectiks.procurement.repository.CurrencyRepository;
import com.synectiks.procurement.repository.DepartmentRepository;
import com.synectiks.procurement.repository.RequisitionActivityRepository;
import com.synectiks.procurement.repository.RequisitionLineItemRepository;
import com.synectiks.procurement.repository.RequisitionRepository;

import io.github.jhipster.web.util.HeaderUtil;

@RestController
@RequestMapping("/api")
public class RequisitionController {
	private static final Logger logger = LoggerFactory.getLogger(RequisitionController.class);
	private static final String ENTITY_NAME = "requisition";
	
	@Value("${jhipster.clientApp.name}")
	private String applicationName;
	@Autowired
	private RequisitionActivityRepository requisitionActivityRepository;
	@Autowired
	private RequisitionRepository requisitionRepository;
	@Autowired
	private DepartmentRepository departmentRepository;
	@Autowired
	private CurrencyRepository currencyRepository;
	@Autowired
	private RequisitionLineItemRepository requisitionLineItemRepository;
	
	 @RequestMapping(value = "/addRequisition", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<Object> addRequisition(@RequestParam("file") MultipartFile file, @RequestParam("obj") String obj) throws IOException {
			
			if(file != null) {
				 byte[] bytes = file.getBytes();
				 String filename = StringUtils.cleanPath(file.getOriginalFilename());
				 filename = filename.toLowerCase().replaceAll(" ", "-");
				 String uniqueID  = UUID.randomUUID().toString();
				 filename= uniqueID.concat(filename);   
		            Path path = Paths.get("Reqimages/" + filename);
		            Files.write(path, bytes);
				}
	
	           
				ObjectNode json = (ObjectNode) new ObjectMapper().readTree(obj);
				Status st = new Status();
				Optional<Department> oDpr = departmentRepository.findById(Long.parseLong(json.get("departmentId").asText()));
				if (!oDpr.isPresent()) {
					st.setCode(HttpStatus.EXPECTATION_FAILED.value());
					st.setType("ERROR");
					st.setMessage("Department not found");
					return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st); 
				} 
				Optional<Currency> oCr = currencyRepository.findById(Long.parseLong(json.get("currencyId").asText()));
				if (!oCr.isPresent()) {
					st.setCode(HttpStatus.EXPECTATION_FAILED.value());
					st.setType("ERROR");
					st.setMessage("Currency not found");
					return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st); 
				} 
						 
				try {
						Requisition requisition = new Requisition();
						requisition.setDepartment(oDpr.get());
						requisition.setCurrency(oCr.get());
						
						if (json.get("requisitionNo") != null) {
							requisition.setRequisitionNo(json.get("requisitionNo").asText());
						}
						
						requisition.setProgressStage(Constants.PROGRESS_STAGE_NEW);
						
						if (json.get("financialYear") != null) {
							requisition.setFinancialYear(json.get("financialYear").asInt());
						}
						if (json.get("type") != null) {
							requisition.setType(json.get("type").asText());
						}
//						if (obj.get("totalPrice") != null) {
//							requisition.setTotalPrice(obj.get("totalPrice").asInt());
//						}
						if (json.get("notes") != null) {
							requisition.setNotes(json.get("notes").asText());
						}
			//						DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
			//						LocalDate localDate = LocalDate.parse(obj.get("dueDate").asText(), formatter);
			//						requisition.setDueDate(localDate);
			//						
						requisition.setCreatedBy(Constants.SYSTEM_ACCOUNT);
						requisition.setUpdatedBy(Constants.SYSTEM_ACCOUNT);
						Instant now = Instant.now();
						requisition.setCreatedOn(now);
						requisition.setUpdatedOn(now);
						 requisition = requisitionRepository.save(requisition);
						JSONObject jsonObj = new JSONObject(obj);
					//	RequisitionLineItem Ingredients_names = new RequisitionLineItem();
						
						JSONArray ja_data = jsonObj.getJSONArray("requisitionLineItemLists");
						int length = ja_data.length(); 
						
						  for(int j=0; j<length; j++) {
						    JSONObject json1 = ja_data.getJSONObject(j);
						    ObjectMapper mapper=new ObjectMapper();
						    RequisitionLineItem result =mapper.readValue(json1.toString(j), RequisitionLineItem.class);

						    requisition.getRequisitionLineItemLists().add(result);
						    RequisitionLineItem requisitionLineItem = new RequisitionLineItem();
						    BeanUtils.copyProperties(result, requisitionLineItem);	
						    requisitionLineItem.setRequisition(requisition);
						    requisitionLineItem = requisitionLineItemRepository.save(requisitionLineItem);
						  }
						  
						//List<JsonNode> reqLineItems = json.findValues("requisitionLineItemLists");
						
//						for(JsonNode jn: reqLineItems) {
//							RequisitionLineItem requisitionLineItem = new RequisitionLineItem();
////						requisition.getRequisitionActivityLists().(jn);
//							ObjectMapper mapper = new ObjectMapper();
////							JsonNode studentList = mapper.readValue(jn, Requisition.class);
//						}
						
						
						
						if(requisition.getId() != null) {
						RequisitionActivity requisitionActivity = new RequisitionActivity();
							BeanUtils.copyProperties(requisition, requisitionActivity);	
							requisitionActivity.setRequisition(requisition);
							requisitionActivity = requisitionActivityRepository.save(requisitionActivity);
							logger.info("Add New Requisition Activity");
						}
						
						logger.info("Add New Requisition SUCCESS");
						
						st.setCode(HttpStatus.OK.value());
						st.setType("SUCCESS");
						st.setMessage("Add New Requisition SUCCESS : "+requisition);
						st.setObject(requisition);
						return ResponseEntity.status(HttpStatus.OK).body(st);
					}catch (Exception e) {
						logger.error("Add New Requisition failed. Exception: ", e);
						st.setCode(HttpStatus.EXPECTATION_FAILED.value());
						st.setType("ERROR");
						st.setMessage("Add New Requisition failed");
						return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
					}
			
		}
	@PutMapping("/updateRequisition")
	public  ResponseEntity<Object> updateRequisition(@RequestBody ObjectNode obj) throws JSONException, URISyntaxException {
//		 String id =(obj.get("id")).asText();
		Status st = new Status(); 
		Optional<Requisition> ur = requisitionRepository.findById(Long.parseLong(obj.get("id").asText()));
		 if(!ur.isPresent()) {
				return ResponseEntity.created(new URI("/api/updateRequisition/")).headers(HeaderUtil
						.createEntityCreationAlert(applicationName, false, ENTITY_NAME, ""))
						.body(null);
			 }
		try {
		Requisition requisition = new Requisition();
		if (obj.get("departmentId") != null) {
			requisition.setRequisitionNo(obj.get("departmentId").asText());
			}
		if (obj.get("currencyId") != null) {
			requisition.setRequisitionNo(obj.get("currencyId").asText());
			}
		if (obj.get("requisitionNo") != null) {
		requisition.setRequisitionNo(obj.get("requisitionNo").asText());
		}
		if (obj.get("progressStage") != null) {
		requisition.setProgressStage(obj.get("progressStage").asText());
		}
		if (obj.get("financialYear") != null) {
		requisition.setFinancialYear(obj.get("financialYear").asInt());
		}
		if (obj.get("type") != null) {
		requisition.setType(obj.get("type").asText());
		}
		if (obj.get("totalPrice") != null) {
		requisition.setTotalPrice(obj.get("totalPrice").asInt());
		}
		if (obj.get("notes") != null) {
		requisition.setNotes(obj.get("notes").asText());
		}
//		requisition.setDueDate(obj.get("dueDate").asText());
		
		 if (obj.get("user") != null) {
			 requisition.setUpdatedBy(obj.get("user").asText());
			 } else {
				 requisition.setUpdatedBy(Constants.SYSTEM_ACCOUNT);
			 }
		 
			 Instant now = Instant.now();
			 requisition.setUpdatedOn(now);
		requisition = requisitionRepository.save(requisition);
		logger.info("Updating requisition completed");
	
		st.setCode(HttpStatus.OK.value());
		st.setType("SUCCESS");
		st.setMessage("Updating requisition SUCCESS : "+requisition);
		st.setObject(requisition);
		return ResponseEntity.status(HttpStatus.OK).body(st);
		
		}catch (Exception e) {
			logger.error("Updating Requisition failed. Exception: ", e);
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("Updating requisition failed");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}
		
	}
	
	@GetMapping("/searchRequisition")
	public List<Requisition> searchRequisition(@RequestParam Map<String, String> requestObj) {
		logger.info("Request to get requisition on given filter criteria");
		Requisition requisition = new Requisition();
		
		try {
		boolean isFilter = false;
		if (requestObj.get("id") != null) {
			requisition.setId(Long.parseLong(requestObj.get("id")));
			isFilter = true;
		}
//		if (requestObj.get("departmentId") != null) {
//			requisition.setDepartment(requestObj.get("departmentId"));
//			isFilter = true;
//		}
		if (requestObj.get("currencyId") != null) {
			requisition.setId(Long.parseLong(requestObj.get("currencyId")));
			isFilter = true;
		}
		if (requestObj.get("requisitionNo") != null) {
			requisition.setRequisitionNo(requestObj.get("requisitionNo"));
			isFilter = true;
		}
		if (requestObj.get("progressStage") != null) {
			requisition.setProgressStage(requestObj.get("progressStage"));
			isFilter = true;
		}
		if (requestObj.get("financialYear") != null) {
			//requisition.setFinancialYear(requestObj.get("progressStage"));
			isFilter = true;
		}
		if (requestObj.get("type") != null) {
			requisition.setType(requestObj.get("type"));
			isFilter = true;
		}
		if (requestObj.get("totalPrice") != null) {
//			requisition.setTotalPrice(requestObj.get("totalPrice"));
			isFilter = true;
		}
		if (requestObj.get("notes") != null) {
			requisition.setNotes(requestObj.get("notes"));
			isFilter = true;
		}
		List<Requisition> list = null;
		if (isFilter) {
			list = this.requisitionRepository.findAll(Example.of(requisition), Sort.by(Direction.DESC, "id"));
		} else {
			list = this.requisitionRepository.findAll(Sort.by(Direction.DESC, "id"));
		}
		
        logger.info("search data ");
		
		Status st = new Status();
		st.setCode(HttpStatus.OK.value());
		st.setType("SUCCESS");
		st.setMessage("search data get SUCCESS : "+list);
		st.setObject(list);
		return (List<Requisition>) ResponseEntity.status(HttpStatus.OK).body(st);
		
		}catch (Exception e) {
			logger.error("Updating Requisition failed. Exception: ", e);
			Status st = new Status();
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("search data get failed");
			return (List<Requisition>) ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}
		
	}
	
	 @DeleteMapping("/requisition/{id}")
	    public ResponseEntity<Object> deletePost(@PathVariable Long id) {
		 try {
			 requisitionRepository.deleteById(id);
			} catch (Throwable th) {
				logger.error(th.getMessage(), th);
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(th);
			}
			return ResponseEntity.status(HttpStatus.OK)
					.body("User removed Successfully");
		}

		@GetMapping("/getAllRequisition")
		private List<Requisition> getAllRequisition() {
			List<Requisition> list = requisitionRepository.findAll(Sort.by(Direction.ASC, "id"));
			return list;
		}
}
