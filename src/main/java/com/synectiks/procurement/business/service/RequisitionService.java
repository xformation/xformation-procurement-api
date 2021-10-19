package com.synectiks.procurement.business.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.synectiks.procurement.config.Constants;
import com.synectiks.procurement.domain.Currency;
import com.synectiks.procurement.domain.DataFile;
import com.synectiks.procurement.domain.Department;
import com.synectiks.procurement.domain.Document;
import com.synectiks.procurement.domain.Requisition;
import com.synectiks.procurement.domain.RequisitionActivity;
import com.synectiks.procurement.domain.RequisitionLineItem;
import com.synectiks.procurement.domain.RequisitionLineItemActivity;
import com.synectiks.procurement.domain.Roles;
import com.synectiks.procurement.domain.Rules;
import com.synectiks.procurement.domain.Vendor;
import com.synectiks.procurement.domain.VendorRequisitionBucket;
import com.synectiks.procurement.repository.DataFileRepository;
import com.synectiks.procurement.repository.RequisitionActivityRepository;
import com.synectiks.procurement.repository.RequisitionRepository;
import com.synectiks.procurement.repository.VendorRequisitionBucketRepository;
import com.synectiks.procurement.util.DateFormatUtil;

@Service
public class RequisitionService {
	private static final Logger logger = LoggerFactory.getLogger(RequisitionService.class);

	@Autowired
	private RequisitionRepository requisitionRepository;

	@Autowired
	private RequisitionActivityRepository requisitionActivityRepository;

	@Autowired
	private RequisitionActivityService requisitionActivityService;

	@Autowired
	private DepartmentService departmentService;

	@Autowired
	private CurrencyService currencyService;

	@Autowired
	private RequisitionLineItemService requisitionLineItemService;

	@Autowired
	private VendorRequisitionBucketRepository vendorRequisitionBucketRepository;
	
	@Autowired
	private DataFileRepository dataFileRepository;

	@Autowired
	private VendorService vendorService;

	@Autowired
	private RolesService rolesService;

	@Autowired
	private RulesService rulesService;
	
	@Autowired
	private DocumentService documentService;
	
	@Autowired
	private RequisitionLineItemActivityService requisitionLineItemActivityService;

	public Requisition getRequisition(Long id) {
		logger.info("Getting requisition by id: " + id);
		Optional<Requisition> ovn = requisitionRepository.findById(id);
		if (ovn.isPresent()) {
			logger.info("Requisition: " + ovn.get().toString());
			return ovn.get();
		}
		logger.warn("Requisition not found");
		return null;
	}

	@Transactional
	public Requisition addRequisition(MultipartFile extraBudgetoryFile, MultipartFile requisitionLineItemFile, String obj) throws Exception {
		logger.info("Adding requistion");
		Requisition requisition = new Requisition();

		ObjectMapper mapper = new ObjectMapper();
		ObjectNode json = (ObjectNode) mapper.readTree(obj);

		if(json.get("departmentId") != null) {
			Department department = departmentService.getDepartment(Long.parseLong(json.get("departmentId").asText()));
			if (department != null) {
				requisition.setDepartment(department);
			}
		}
		
		if(json.get("currencyId") != null) {
			Currency currency = currencyService.getCurrency(Long.parseLong(json.get("currencyId").asText()));
			if (currency != null) {
				requisition.setCurrency(currency);
			}
		}

//		if (json.get("requisitionNo") != null) {
//			requisition.setRequisitionNo(json.get("requisitionNo").asText());
//		}

		requisition.setProgressStage(Constants.PROGRESS_STAGE_NEW);

		if (json.get("financialYear") != null) {
			requisition.setFinancialYear(json.get("financialYear").asInt());
		}
		
		Rules rule = rulesService.getRulesByName(Constants.RULE_REQUISITION_TYPE); 
////		if (json.get("roleName").asText() != null) {
////			Roles role = rolesService.getRolesByName(json.get("roleName").asText());
////			rule = rulesService.getRulesByRoleAndRuleName(role, Constants.RULE_REQUISITION_TYPE);
////		} else {
////			logger.error("Requistion could not be added. User's role missing");
////			return null;
////		}
     	  if(rule != null) {
			JSONObject ruleJson = new JSONObject(rule.getRule());
			JSONObject nonStandardRule = ruleJson.getJSONObject(Constants.REQUISITION_TYPE_NON_STANDARD);
			if (json.get("totalPrice") != null) {
				int price = json.get("totalPrice").asInt();
				if (price >= nonStandardRule.getInt("min") && price <= nonStandardRule.getInt("max")) {
					requisition.setType(Constants.REQUISITION_TYPE_NON_STANDARD);
				} else {
					requisition.setType(Constants.REQUISITION_TYPE_STANDARD);
				}
			} else {
				requisition.setType(Constants.REQUISITION_TYPE_NON_STANDARD);
			}
		}else {
			requisition.setType(Constants.REQUISITION_TYPE_NON_STANDARD);
		}
		
//		}

		if (json.get("totalPrice") != null) {
			requisition.setTotalPrice(json.get("totalPrice").asInt());
		}

		if (json.get("notes") != null) {
			requisition.setNotes(json.get("notes").asText());
		}
		
		if (json.get("status") != null) {
			requisition.setStatus(json.get("status").asText());
		}
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DEFAULT_DATE_FORMAT);
		if (json.get("dueDate") != null) {
			try {
				LocalDate localDate = LocalDate.parse(json.get("dueDate").asText(), formatter);
				requisition.setDueDate(localDate);
			}catch(Exception e) {
				logger.error("Cannot read due date. Exception: "+e.getMessage());
				long millis = System.currentTimeMillis();
				java.sql.Date date = new java.sql.Date(millis);

				LocalDate datew = LocalDate.parse(date.toString());
				LocalDate localDate = datew.plusDays(Constants.DEFAULT_DUE_DAYS);
				requisition.setDueDate(localDate);
			}
		} else {
			long millis = System.currentTimeMillis();
			java.sql.Date date = new java.sql.Date(millis);

			LocalDate datew = LocalDate.parse(date.toString());
			LocalDate localDate = datew.plusDays(Constants.DEFAULT_DUE_DAYS);
			requisition.setDueDate(localDate);
		}
		
		if (json.get("user") != null) {
			requisition.setCreatedBy(json.get("user").asText());
			requisition.setUpdatedBy(json.get("user").asText());
		} else {
			requisition.setCreatedBy(Constants.SYSTEM_ACCOUNT);
			requisition.setUpdatedBy(Constants.SYSTEM_ACCOUNT);
		}

		Instant now = Instant.now();
		requisition.setCreatedOn(now);
		requisition.setUpdatedOn(now);

		requisition = requisitionRepository.save(requisition);
		logger.info("Requisition added successfully");
		
		saveExtraBudgetoryFile(extraBudgetoryFile, requisition, now);
		saveRequisitionActivity(requisition);
		
		List<RequisitionLineItem> liteItemList = getLineItemFromFile(requisitionLineItemFile);
		List<RequisitionLineItem> liteItemList2 = getLineItemFromJson(obj);
		liteItemList.addAll(liteItemList2);

		saveRequisitionLineItem(requisition, liteItemList);
		saveRequisitionLineItemFile( requisitionLineItemFile, now);
		logger.info("Requisition added successfully");
		return requisition;
	}

	private void saveRequisitionLineItem(Requisition requisition, List<RequisitionLineItem> liteItemList) {
		logger.info("Saving requisition line items");
		for(RequisitionLineItem reqLineItem: liteItemList) {
			logger.debug("Requisition line item: " + reqLineItem.toString());
			reqLineItem.setRequisition(requisition);
			reqLineItem.setCreatedBy(requisition.getCreatedBy());
			reqLineItem.setCreatedOn(requisition.getCreatedOn());
			reqLineItem.setUpdatedBy(requisition.getUpdatedBy());
			reqLineItem.setUpdatedOn(requisition.getUpdatedOn());
			reqLineItem.setStatus(requisition.getStatus());
			reqLineItem = requisitionLineItemService.addRequisitionLineItem(reqLineItem);
			requisition.getRequisitionLineItemLists().add(reqLineItem);
		}
		logger.info("Requisition line items saved successfully");
	}

	private List<RequisitionLineItem> getLineItemFromJson(String obj) throws Exception {
		List<RequisitionLineItem> liteItemList = new ArrayList<>();
		JSONObject jsonObj = new JSONObject(obj);
		JSONArray reqLineItemArray = jsonObj.getJSONArray("requisitionLineItemLists");
		if (reqLineItemArray != null && reqLineItemArray.length() > 0) {
			for (int j = 0; j < reqLineItemArray.length(); j++) {
				JSONObject json = reqLineItemArray.getJSONObject(j);
				RequisitionLineItem reqLineItem = new RequisitionLineItem(); 
				
				if (json.get("orderQuantity") != null) {
					try {
						String q = (String)json.get("orderQuantity");
						reqLineItem.setOrderQuantity(Integer.parseInt(q));
					}catch(Exception e) {
						try {
							Integer q = (Integer)json.get("orderQuantity");
							reqLineItem.setOrderQuantity(q);
						}catch(Exception ee) {
							logger.error("Cannot read quantity. Exception ", e);
							throw ee;
						}
					}
				}
					
				if (json.get("itemDescription") != null) {
					reqLineItem.setItemDescription((String)json.get("itemDescription"));
				}
				if (json.get("ratePerItem") != null) {
					try {
						String q = (String)json.get("ratePerItem");
						reqLineItem.setRatePerItem(Integer.parseInt(q));
					}catch(Exception e) {
						try {
							Integer q = (Integer)json.get("ratePerItem");
							reqLineItem.setRatePerItem(q);
						}catch(Exception ee) {
							logger.error("Cannot read per item rate. Exception ", e);
							throw ee;
						}
					}				
				}
				
				if (json.get("price") != null) {
					try {
						String q = (String)json.get("price");
						reqLineItem.setPrice(Integer.parseInt(q));
					}catch(Exception e) {
						try {
							Integer q = (Integer)json.get("price");
							reqLineItem.setPrice(q);
						}catch(Exception ee) {
							logger.error("Cannot read per item rate. Exception ", e);
							throw ee;
						}
					}	
				}
				
				liteItemList.add(reqLineItem);
			}
		}
		return liteItemList;
	}
	
	public List<RequisitionLineItem> getLineItemFromFile(MultipartFile requisitionLineItemFile) throws IOException{
		List<RequisitionLineItem> lineItemList = new ArrayList<>();
		if(requisitionLineItemFile == null) {
			return lineItemList;
		}
		
		XSSFWorkbook workbook = new XSSFWorkbook(requisitionLineItemFile.getInputStream());
		XSSFSheet worksheet = workbook.getSheetAt(0);
		
		//skiping first row, that is row index 0. Starting loop with 1
		for (int i = 1; i <worksheet.getPhysicalNumberOfRows(); i++) {
			XSSFRow row = worksheet.getRow(i);
			RequisitionLineItem item = new RequisitionLineItem();
			
			if (row.getCell(0) != null) {
				item.setItemDescription(row.getCell(0).getStringCellValue());
			}
			
			try {
				if (row.getCell(1) != null) {
					item.setOrderQuantity((int) row.getCell(1).getNumericCellValue());
				} 
			}catch(Exception e) {
				if (row.getCell(1) != null) {
					item.setOrderQuantity(Integer.parseInt(row.getCell(1).getStringCellValue()));
				}
			}

			try {
				if (row.getCell(2) != null) {
					item.setRatePerItem((int) row.getCell(2).getNumericCellValue());
				} 
			}catch(Exception e) {
				if (row.getCell(2) != null) {
					item.setRatePerItem(Integer.parseInt(row.getCell(2).getStringCellValue()));
				}
			}
			
			try {
				if (row.getCell(3) != null) {
					item.setPrice((int) row.getCell(3).getNumericCellValue());
				} 
			}catch(Exception e) {
				if (row.getCell(3) != null) {
					item.setPrice(Integer.parseInt(row.getCell(3).getStringCellValue()));
				}
			}
			
			lineItemList.add(item);

		}
		return lineItemList;
	}
	
	
	@Transactional
	public Requisition updateRequisition(MultipartFile extraBudgetoryFile, MultipartFile requisitionLineItemFile, String obj) throws Exception {
		logger.info("Update requisition");

		ObjectMapper mapper = new ObjectMapper();
		ObjectNode json = (ObjectNode) mapper.readTree(obj);
		
		Optional<Requisition> orq = requisitionRepository.findById(Long.parseLong(json.get("id").asText()));
		if (!orq.isPresent()) {
			logger.error("Requisition could not be updated. Requisition not found");
			return null;
		}

		Requisition requisition = orq.get();

		if (json.get("financialYear") != null) {
			requisition.setFinancialYear(json.get("financialYear").asInt());
		}
		
		if(json.get("departmentId") != null) {
			Department department = departmentService.getDepartment(Long.parseLong(json.get("departmentId").asText()));
			if (department != null) {
				requisition.setDepartment(department);
			}
		}
		
		if(json.get("currencyId") != null) {
			Currency currency = currencyService.getCurrency(Long.parseLong(json.get("currencyId").asText()));
			if (currency != null) {
				requisition.setCurrency(currency);
			}
		}
		
		if (json.get("notes") != null) {
			requisition.setNotes(json.get("notes").asText());
		}
		
		if (json.get("progressStage") != null) {
			requisition.setProgressStage(json.get("progressStage").asText());
		}

		
//		Rules rule = null; 
//		if (obj.get("roleName").asText() != null) {
//			Roles role = rolesService.getRolesByName(obj.get("roleName").asText());
//			rule = rulesService.getRulesByRoleAndRuleName(role, Constants.RULE_REQUISITION_TYPE);
//		} else {
//			logger.error("Requistion could not be added. User's role missing");
//			return null;
//		}
		
//		JSONObject jsonObject = new JSONObject(rule.getRule());
//		JSONObject nonStandardRule = jsonObject.getJSONObject(Constants.REQUISITION_TYPE_NON_STANDARD);
//		if (obj.get("totalPrice") != null) {
//			int price = obj.get("totalPrice").asInt();
//			if (price >= nonStandardRule.getInt("min") && price <= nonStandardRule.getInt("max")) {
//				requisition.setType(Constants.REQUISITION_TYPE_NON_STANDARD);
//			} else {
//				requisition.setType(Constants.REQUISITION_TYPE_STANDARD);
//			}
//		}

		if (json.get("totalPrice") != null) {
			requisition.setTotalPrice(json.get("totalPrice").asInt());
		}

		
		if (json.get("status") != null) {
			requisition.setStatus(json.get("status").asText());
		}
		
//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DEFAULT_DATE_FORMAT);
//		if (obj.get("dueDate") != null) {
//			LocalDate localDate = LocalDate.parse(obj.get("dueDate").asText(), formatter);
//			requisition.setDueDate(localDate);
//		}

		if (json.get("user") != null) {
			requisition.setUpdatedBy(json.get("user").asText());
		} else {
			requisition.setUpdatedBy(Constants.SYSTEM_ACCOUNT);
		}
		
		Instant now = Instant.now();
		requisition.setUpdatedOn(now);
		
		requisition = requisitionRepository.save(requisition);
		logger.info("Requisition updated successfully");

		saveExtraBudgetoryFile(extraBudgetoryFile, requisition, now);
		saveRequisitionActivity(requisition);

		List<RequisitionLineItem> liteItemList = getLineItemFromFile(requisitionLineItemFile);
		List<RequisitionLineItem> liteItemList2 = getLineItemFromJson(obj);
		liteItemList.addAll(liteItemList2);

		saveRequisitionLineItem(requisition, liteItemList);
		saveRequisitionLineItemFile( requisitionLineItemFile, now);
		
		return requisition;
	}

//	private List<RequisitionLineItem> getLineItemFromJson(ObjectNode obj) throws Exception {
//		return getLineItemFromJson(obj.toPrettyString());
//	}
	
	private RequisitionActivity saveRequisitionActivity(Requisition requisition) {
		RequisitionActivity requisitionActivity = null;
		if (requisition != null) {
			logger.info("Adding requisition activity");
			requisitionActivity = new RequisitionActivity();
			BeanUtils.copyProperties(requisition, requisitionActivity);
			requisitionActivity.setRequisitionId(requisition.getId());
			
			requisitionActivity.setUpdatedBy(requisition.getUpdatedBy());
			requisitionActivity.setUpdatedOn(requisition.getUpdatedOn());
			
			requisitionActivity = requisitionActivityService.addRequisitionActivity(requisitionActivity);
			logger.info("Requisition activity added successfully");
		}
		return requisitionActivity;
	}

	public List<Requisition> searchRequisition(Map<String, String> requestObj) throws Exception {
		logger.info("Request to search requisition on given filter criteria");
		Requisition requisition = new Requisition();

		boolean isFilter = false;
		if (!org.apache.commons.lang3.StringUtils.isBlank(requestObj.get("id"))) {
			requisition.setId(Long.parseLong(requestObj.get("id")));
			isFilter = true;
		}

		if (!org.apache.commons.lang3.StringUtils.isBlank(requestObj.get("reqNo"))) {
			requisition.setId(Long.parseLong(requestObj.get("reqNo").toLowerCase()));
			isFilter = true;
		}
		
		if (!org.apache.commons.lang3.StringUtils.isBlank(requestObj.get("reqestNo"))) {
			requisition.setId(Long.parseLong(requestObj.get("reqestNo").toLowerCase()));
			isFilter = true;
		}
		
		if (!org.apache.commons.lang3.StringUtils.isBlank(requestObj.get("departmentId"))) {
			Department department = departmentService.getDepartment(Long.parseLong(requestObj.get("departmentId")));
			if (department != null) {
				requisition.setDepartment(department);
			}
			isFilter = true;
		}

		if (!org.apache.commons.lang3.StringUtils.isBlank(requestObj.get("currencyId"))) {
			Currency currency = currencyService.getCurrency(Long.parseLong(requestObj.get("currencyId")));
			if (currency != null) {
				requisition.setCurrency(currency);
			}
			isFilter = true;
		}

//		if (!org.apache.commons.lang3.StringUtils.isBlank(requestObj.get("requisitionNo"))) {
//			requisition.setRequisitionNo(requestObj.get("requisitionNo"));
//			isFilter = true;
//		}

		if (!org.apache.commons.lang3.StringUtils.isBlank(requestObj.get("status"))) {
			requisition.setStatus(requestObj.get("status"));
			isFilter = true;
		}

		if (!org.apache.commons.lang3.StringUtils.isBlank(requestObj.get("progressStage"))) {
			requisition.setProgressStage(requestObj.get("progressStage"));
			isFilter = true;
		}

		if (!org.apache.commons.lang3.StringUtils.isBlank(requestObj.get("financialYear"))) {
			requisition.setFinancialYear(Integer.parseInt(requestObj.get("financialYear")));
			isFilter = true;
		}
		if (!org.apache.commons.lang3.StringUtils.isBlank(requestObj.get("type"))) {
			requisition.setType(requestObj.get("type"));
			isFilter = true;
		}
		if (!org.apache.commons.lang3.StringUtils.isBlank(requestObj.get("totalPrice"))) {
			requisition.setTotalPrice(Integer.parseInt(requestObj.get("totalPrice")));
			isFilter = true;
		}
		if (!org.apache.commons.lang3.StringUtils.isBlank(requestObj.get("notes"))) {
			requisition.setNotes(requestObj.get("notes"));
			isFilter = true;
		}
		if (requestObj.get("createdOn") != null) {
			Instant instant = Instant.parse(requestObj.get("createdOn"));
			requisition.setCreatedOn(instant);
			isFilter = true;
		}
		if (requestObj.get("createdBy") != null) {
			requisition.setCreatedBy(requestObj.get("createdBy"));
			isFilter = true;
		}
		if (requestObj.get("updatedOn") != null) {
			Instant instant = Instant.parse(requestObj.get("updatedOn"));
			requisition.setUpdatedOn(instant);
			isFilter = true;
		}
	    if (requestObj.get("updatedBy") != null) {
	    	requisition.setUpdatedBy(requestObj.get("updatedBy"));
			isFilter = true;
		}

		if (!org.apache.commons.lang3.StringUtils.isBlank(requestObj.get("dueDate"))) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DEFAULT_DATE_FORMAT);
			LocalDate localDate = LocalDate.parse(requestObj.get("dueDate"), formatter);
			requisition.setDueDate(localDate);
			isFilter = true;
		}
		
		List<Requisition> list = null;
		if (isFilter) {
			list = this.requisitionRepository.findAll(Example.of(requisition), Sort.by(Direction.DESC, "id"));
		} else {
			list = this.requisitionRepository.findAll(Sort.by(Direction.DESC, "id"));
		}

		Date fromDate = null;
		boolean isDateFilter = false;
		if(requestObj.get("fromDate") != null){
			fromDate = DateFormatUtil.convertStringToUtilDate(Constants.DEFAULT_DATE_FORMAT,requestObj.get("fromDate"));
			isDateFilter = true;
		}
		
		Date toDate = null;
		if(requestObj.get("toDate") != null){
			toDate = DateFormatUtil.convertStringToUtilDate(Constants.DEFAULT_DATE_FORMAT,requestObj.get("toDate"));
			isDateFilter = true;
		}
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.from(ZoneOffset.UTC));
		List<Requisition> filteredList = new ArrayList<>();
		for (Requisition req : list) {
			if(fromDate != null && toDate != null && fromDate.equals(toDate)) {
				Date reqDate = DateFormatUtil.convertInstantToUtilDate(formatter, req.getCreatedOn());
				if(reqDate.equals(fromDate)) {
					filteredList.add(req);
				}
			}else if(fromDate != null && toDate != null && !fromDate.equals(toDate)) {
				Date reqDate = DateFormatUtil.convertInstantToUtilDate(formatter, req.getCreatedOn());
		        if(reqDate.getTime() >= fromDate.getTime() && reqDate.getTime() <= toDate.getTime()) {
					filteredList.add(req);
				}
			}else if(fromDate != null && toDate == null ) {
				Date reqDate = DateFormatUtil.convertInstantToUtilDate(formatter, req.getCreatedOn());
				if(reqDate.getTime() >= fromDate.getTime()) {
					filteredList.add(req);
				}
			}else if(fromDate == null && toDate != null ) {
				Date reqDate = DateFormatUtil.convertInstantToUtilDate(formatter, req.getCreatedOn());
				if(reqDate.getTime() <= toDate.getTime()) {
					filteredList.add(req);
				}
			}
		}
		
		if(isDateFilter) {
			list = filteredList;
		}
		
		for (Requisition req : list) {
			RequisitionActivity ca = new RequisitionActivity();
			ca.setRequisitionId(req.getId());
			List<RequisitionActivity> caList = requisitionActivityRepository.findAll(Example.of(ca));
			req.setActivityList(caList);
			
			Map<String, String> searchDoc = new HashMap<>();
			searchDoc.put("sourceOfOrigin", this.getClass().getSimpleName());
			searchDoc.put("sourceId", String.valueOf(req.getId()));
			searchDoc.put("identifier", Constants.IDENTIFIER_REQUISITION_EXTRA_BUDGETORY_FILE);
			List<Document> docList = documentService.searchDocument(searchDoc);
			req.setDocumentList(docList);
		}

		logger.info("Requisition search completed. Total records: " + list.size());

		return list;

	}

//	public void deleteRequisition(Long id) {
//		requisitionRepository.deleteById(id);
//		logger.info("Requisition deleted successfully");
//	}

	public List<Requisition> getAllRequisitions() {
		List<Requisition> list = requisitionRepository.findAll(Sort.by(Direction.ASC, "id"));
		logger.info("All requisitions. Total records: " + list.size());
		return list;
	}

	@Transactional
	public void sendRequisitionToVendor(List<ObjectNode> list) {
		for (ObjectNode obj : list) {
			VendorRequisitionBucket bucket = new VendorRequisitionBucket();
			logger.debug("Send new requisition to vendor: " + obj.toString());
			if (obj.get("requisitionId") != null) {
				Optional<Requisition> or = requisitionRepository.findById(obj.get("requisitionId").asLong());
				if (or.isPresent()) {
					bucket.setRequisition(or.get());
				}
			}
			if (obj.get("vendorId") != null) {
				Vendor vendor = vendorService.getVendor(obj.get("vendorId").asLong());

				if (vendor != null) {
					bucket.setVendor(vendor);
				}
			}
			if (obj.get("stages") != null) {
				bucket.setStages(obj.get("stages").asText());
			}
			if (obj.get("notes") != null) {
				bucket.setNotes(obj.get("notes").asText());
			}
			if (obj.get("status") != null) {
				bucket.setStatus(obj.get("status").asText());
			}
			if (obj.get("user") != null) {
				bucket.setCreatedBy(obj.get("user").asText());
				bucket.setUpdatedBy(obj.get("user").asText());
			} else {
				bucket.setCreatedBy(Constants.SYSTEM_ACCOUNT);
				bucket.setUpdatedBy(Constants.SYSTEM_ACCOUNT);
			}

			Instant now = Instant.now();
			bucket.setCreatedOn(now);
			bucket.setUpdatedOn(now);
			bucket = vendorRequisitionBucketRepository.save(bucket);
			logger.info("Bucket added successfully");
		}
	}

	@Transactional
	public void updateRequisitionToVendor(List<ObjectNode> list) {
		for (ObjectNode obj : list) {
			logger.debug("Update requisition to vendor: " + obj.toString());
			Optional<VendorRequisitionBucket> ur = vendorRequisitionBucketRepository
					.findById(Long.parseLong(obj.get("id").asText()));
			if (!ur.isPresent()) {
				logger.error("Vendor requisition bucket not found");
			}
			VendorRequisitionBucket bucket = ur.get();
			if (obj.get("stages") != null) {
				bucket.setStages(obj.get("stages").asText());
			}
			if (obj.get("notes") != null) {
				bucket.setNotes(obj.get("notes").asText());
			}
			if (obj.get("status") != null) {
				bucket.setStatus(obj.get("status").asText());
			}

			if (obj.get("user") != null) {
				bucket.setCreatedBy(obj.get("user").asText());
				bucket.setUpdatedBy(obj.get("user").asText());
			} else {
				bucket.setCreatedBy(Constants.SYSTEM_ACCOUNT);
				bucket.setUpdatedBy(Constants.SYSTEM_ACCOUNT);
			}
			Instant now = Instant.now();
			bucket.setCreatedOn(now);
			bucket.setUpdatedOn(now);
			bucket = vendorRequisitionBucketRepository.save(bucket);
			logger.info("Bucket update successfully");
		}
	}

//	public boolean approveRequisition(ObjectNode obj) throws JSONException {
//		logger.info("Getting requisition by id: " + obj);
//
//		try {
//			if (obj.get("requisitionId") == null) {
//				logger.error("Requision id not found. Cannot approve requisition.");
//				return false;
//			}
//
////			if (obj.get("roleName") == null) {
////				logger.error("Role not found. Cannot approve requisition.");
////				return false;
////			}
//
//			Optional<Requisition> req = requisitionRepository.findById(obj.get("requisitionId").asLong());
//			if (!req.isPresent()) {
//				logger.error("Requision not found. Cannot approve requisition.");
//				return false;
//			}
//
//			Requisition requisition = req.get();
//			
//			Rules rule = rulesService.getRulesByName(Constants.RULE_APPROVE_REQUISITION); 
//			Roles role = rolesService.getRolesByName(obj.get("roleName").asText());
//			// TO DO. Find rule assigned to a role in roles_rules_link table 
//			JSONObject jsonObject = new JSONObject(rule.getRule());
//
//			int price = 0;
//			if (requisition.getTotalPrice() != null) {
//				price = requisition.getTotalPrice().intValue();
//			}
//
//			int minRulePrice = 0;
//			int maxRulePrice = 0;
//
//			try {
//				minRulePrice = jsonObject.getInt("min");
//			} catch (Exception e) {
//				logger.error("Minimum price rule not found. Cannot approve requisiont. Exception: ", e);
//				return false;
//			}
//
//			if (jsonObject.get("max") != null) {
//				try {
//					maxRulePrice = jsonObject.getInt("max");
//				} catch (Exception e) {
//					logger.error("Incorrect maximum price rule. Cannot approve requisiont. Exception: ", e);
//					return false;
//				}
//			}
//
//			boolean isRuleApplied = false;
//			if (price >= minRulePrice && jsonObject.get("max") == null) {
//				requisition.setStatus(Constants.PROGRESS_STAGE_APPROVED);
//				isRuleApplied = true;
//			}
//
//			if (price >= minRulePrice && jsonObject.get("max") != null && price <= maxRulePrice) {
//				requisition.setStatus(Constants.PROGRESS_STAGE_APPROVED);
//				isRuleApplied = true;
//			}
//
//			if (isRuleApplied) {
//				requisition = requisitionRepository.save(requisition);
//				saveRequisitionActivity(requisition);
//				return true;
//			} else {
//				logger.warn("Approve requisition failed. No rule applied");
//				return false;
//			}
//
//		} catch (Exception e) {
//			logger.error("Approve requisition failed. Exception: ", e);
//			return false;
//		}
//
//	}
	

	public boolean approveRequisition(ObjectNode obj) throws JSONException {
		logger.info("Getting requisition by id: " + obj);

		try {
			if (obj.get("requisitionId") == null) {
				logger.error("Requision id not found. Cannot approve requisition.");
				return false;
			}

			Optional<Requisition> req = requisitionRepository.findById(obj.get("requisitionId").asLong());
			if (!req.isPresent()) {
				logger.error("Requision not found. Cannot approve requisition.");
				return false;
			}

			Requisition requisition = req.get();
			requisition.setStatus(Constants.PROGRESS_STAGE_APPROVED);
			requisition = requisitionRepository.save(requisition);
			saveRequisitionActivity(requisition);
			logger.info("Requisition approved successfully");
			return true;

		} catch (Exception e) {
			logger.error("Approve requisition failed. Exception: ", e);
			return false;
		}

	}

	
	private void saveExtraBudgetoryFile(MultipartFile file, Requisition requisition, Instant now)
			throws IOException, JSONException {
		if (file != null) {
			logger.info("Saving extra budgetory file");
			byte[] bytes = file.getBytes();
			String orgFileName = StringUtils.cleanPath(file.getOriginalFilename());
			String ext = "";
			if (orgFileName.lastIndexOf(".") != -1) {
				ext = orgFileName.substring(orgFileName.lastIndexOf(".") + 1);
			}
			String filename = orgFileName;
			if (orgFileName.lastIndexOf(".") != -1) {
				filename = orgFileName.substring(0, orgFileName.lastIndexOf("."));
			}
			filename = filename.toLowerCase().replaceAll(" ", "-") + "_" + System.currentTimeMillis() + "." + ext;
			
			File localStorage = new File(Constants.LOCAL_REQUISITION_FILE_STORAGE_DIRECTORY);
			if (!localStorage.exists()) {
				localStorage.mkdirs();
			}
			Path path = Paths.get(localStorage.getAbsolutePath() + File.separatorChar + filename);
			Files.write(path, bytes);

			Document document = new Document();
			document.setFileName(filename);
			document.setFileExt(ext);
			document.setFileType(ext.toUpperCase());
			document.setFileSize(file.getSize());
			document.setStorageLocation(Constants.FILE_STORAGE_LOCATION_LOCAL);
			document.setLocalFilePath(localStorage.getAbsolutePath() + File.separatorChar + filename);
			document.setSourceOfOrigin(this.getClass().getSimpleName());
			document.setSourceId(requisition.getId());
			document.setIdentifier(Constants.IDENTIFIER_REQUISITION_EXTRA_BUDGETORY_FILE);
			document.setCreatedBy(requisition.getCreatedBy());
			document.updatedBy(requisition.getCreatedBy());
			document.setCreatedOn(now);
			document.setUpdatedOn(now);
			document = documentService.saveDocument(document);
			requisition.setExtraBudgetoryFile(bytes);
			logger.info("Extra budgetory file saved successfully");
		}else {
			logger.info("Requisition extra budgetory file not provided");
		}
	}
	
	private void  saveRequisitionLineItemFile(MultipartFile requisitionLineItemFile, Instant now)throws IOException, JSONException {
		if (requisitionLineItemFile != null) {
			logger.info("Saving requsition line items data file and its details");
			byte[] bytes = requisitionLineItemFile.getBytes();
			String orgFileName = StringUtils.cleanPath(requisitionLineItemFile.getOriginalFilename());
			String ext = "";
			if (orgFileName.lastIndexOf(".") != -1) {
				ext = orgFileName.substring(orgFileName.lastIndexOf(".") + 1);
			}
			
			String filename = orgFileName;
			if (orgFileName.lastIndexOf(".") != -1) {
				filename = orgFileName.substring(0, orgFileName.lastIndexOf("."));
			}
			filename = filename.toLowerCase().replaceAll(" ", "-") + "_" + System.currentTimeMillis() + "." + ext;
			
			File localStorage = new File(Constants.LOCAL_DATA_FILE_STORAGE_DIRECTORY);
			if (!localStorage.exists()) {
				localStorage.mkdirs();
			}
			Path path = Paths.get(localStorage.getAbsolutePath() + File.separatorChar + filename);
			Files.write(path, bytes);

			DataFile dataFile= new DataFile(); 
			dataFile.setFileName(filename);
			dataFile.setFileExt(ext);
			dataFile.setFileType(ext.toUpperCase());
			dataFile.setFileSize(requisitionLineItemFile.getSize());
			dataFile.setStorageLocation(Constants.FILE_STORAGE_LOCATION_LOCAL);
			dataFile.setSourceOfOrigin(this.getClass().getSimpleName());
//			dataFile.setCreatedBy(liteItemList.getCreatedBy());
			dataFile.setCreatedOn(now);
			
     		dataFile = dataFileRepository.save(dataFile);
     		logger.info("Requsition line items data file and its details saved successfully");
		}
	}
	
	@Transactional
	public void deleteRequisition(Long requisitionId) {
		logger.info("Deleting requisition");
		Requisition req = getRequisition(requisitionId);
		Instant now = Instant.now();
		if(req != null) {
			req.setStatus(Constants.STATUS_DELETED);
			req.setUpdatedOn(now);
			saveRequisitionActivity(req);
		}
		
		Map<String, String> reqLineItemMap = new HashMap<>();
		reqLineItemMap.put("requisitionId", String.valueOf(requisitionId));
		List<RequisitionLineItem> reqLineItemList = requisitionLineItemService.searchRequisitionLineItem(reqLineItemMap);
		for(RequisitionLineItem reqLineItem : reqLineItemList) {
			
			RequisitionLineItemActivity reqLiAct = new RequisitionLineItemActivity();
			BeanUtils.copyProperties(reqLineItem, reqLiAct);
			reqLiAct.setRequisitionLineItemId(reqLineItem.getId());
			reqLiAct.status(Constants.STATUS_DELETED);
			reqLiAct.setUpdatedOn(now);
			reqLiAct = requisitionLineItemActivityService.addRequisitionLineItemActivity(reqLiAct);
			requisitionLineItemService.deleteRequisitionLineItem(reqLineItem.getId());
			
		}
		requisitionRepository.deleteById(requisitionId);
		logger.info(" Requisition deleted successfully");
	}
	
	
}
