package com.synectiks.procurement.business.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

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
import com.synectiks.procurement.domain.Department;
import com.synectiks.procurement.domain.Requisition;
import com.synectiks.procurement.domain.RequisitionActivity;
import com.synectiks.procurement.domain.RequisitionLineItem;
import com.synectiks.procurement.domain.Roles;
import com.synectiks.procurement.domain.Rules;
import com.synectiks.procurement.domain.Vendor;
import com.synectiks.procurement.domain.VendorRequisitionBucket;
import com.synectiks.procurement.repository.RequisitionActivityRepository;
import com.synectiks.procurement.repository.RequisitionRepository;
import com.synectiks.procurement.repository.VendorRequisitionBucketRepository;

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
	private VendorService vendorService;

	@Autowired
	private RolesService rolesService;

	@Autowired
	private RulesService rulesService;

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
	public Requisition addRequisition(MultipartFile file, String obj) throws IOException, JSONException {
		logger.info("Adding requistion");
		Requisition requisition = new Requisition();
		if (file != null) {
			byte[] bytes = file.getBytes();
			String filename = StringUtils.cleanPath(file.getOriginalFilename());
			filename = filename.toLowerCase().replaceAll(" ", "-");
			String uniqueID = UUID.randomUUID().toString();
			filename = uniqueID.concat(filename);
			File localStorage = new File(Constants.LOCAL_REQUISITION_FILE_STORAGE_DIRECTORY);
			if (!localStorage.exists()) {
				localStorage.mkdirs();
			}
			Path path = Paths
					.get(Constants.LOCAL_REQUISITION_FILE_STORAGE_DIRECTORY + File.pathSeparatorChar + filename);
			Files.write(path, bytes);
		}
		ObjectMapper mapper = new ObjectMapper();

//	ObjectNode json = (ObjectNode) new ObjectMapper().readTree(obj);
		ObjectNode json = (ObjectNode) mapper.readTree(obj);

		Department department = departmentService.getDepartment(Long.parseLong(json.get("departmentId").asText()));
		if (department == null) {
			logger.error("Requistion could not be added. Department missing");
			return null;
		}

		Currency currency = currencyService.getCurrency(Long.parseLong(json.get("currencyId").asText()));
		if (currency == null) {
			logger.error("Requistion could not be added. Currency missing");
			return null;
		}

		requisition.setDepartment(department);
		requisition.setCurrency(currency);

		if (json.get("requisitionNo") != null) {
			requisition.setRequisitionNo(json.get("requisitionNo").asText());
		}

		requisition.setProgressStage(Constants.PROGRESS_STAGE_NEW);

		if (json.get("financialYear") != null) {
			requisition.setFinancialYear(json.get("financialYear").asInt());
		}
		Rules rule = null; 
		if (json.get("roleName").asText() != null) {
			Roles role = rolesService.getRolesByName(json.get("roleName").asText());
			rule = rulesService.getRulesByRoleAndRuleName(role, Constants.RULE_REQUISITION_TYPE);
		} else {
			logger.error("Requistion could not be added. User's role missing");
			return null;
		}
		JSONObject jsonObject = new JSONObject(rule.getRule());
		JSONObject nonStandardRule = jsonObject.getJSONObject(Constants.REQUISITION_TYPE_NON_STANDARD);
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
			LocalDate localDate = LocalDate.parse(json.get("dueDate").asText(), formatter);
			requisition.setDueDate(localDate);
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
		logger.debug("Requisition saved");
		saveRequisitionActivity(requisition);

		JSONObject jsonObj = new JSONObject(obj);
		JSONArray reqLineItemArray = jsonObj.getJSONArray("requisitionLineItemLists");
		if (reqLineItemArray != null && reqLineItemArray.length() > 0) {
			logger.info("Saving requisition line items");
//			int length = reqLineItemArray.length(); 
//			ObjectMapper mapper=new ObjectMapper();
			for (int j = 0; j < reqLineItemArray.length(); j++) {
				JSONObject json1 = reqLineItemArray.getJSONObject(j);
				RequisitionLineItem result = mapper.readValue(json1.toString(j), RequisitionLineItem.class);
				logger.debug("Requisition line item: " + result.toString());
				result.setRequisition(requisition);
				result = requisitionLineItemService.addRequisitionLineItem(result);
				requisition.getRequisitionLineItemLists().add(result);
			}
		}

		logger.info("Requisition added successfully");
		return requisition;
	}

	@Transactional
	public Requisition updateRequisition(ObjectNode obj) throws JSONException {
		logger.info("Update requisition");

		Optional<Requisition> orq = requisitionRepository.findById(Long.parseLong(obj.get("RequisitionId").asText()));
		if (!orq.isPresent()) {
			logger.error("Requisition could not be updated. Requisition not found");
			return null;
		}

		Requisition requisition = orq.get();

		if (obj.get("departmentId") != null) {
			Department department = departmentService.getDepartment(Long.parseLong(obj.get("departmentId").asText()));
			if (department == null) {
				requisition.setDepartment(department);
			}
		}

		if (obj.get("currencyId") != null) {
			Currency currency = currencyService.getCurrency(Long.parseLong(obj.get("currencyId").asText()));
			if (currency == null) {
				requisition.setCurrency(currency);
			}
		}

		if (obj.get("requisitionNo") != null) {
			requisition.setRequisitionNo(obj.get("requisitionNo").asText());
		}

		if (obj.get("status") != null) {
			requisition.setStatus(obj.get("status").asText());
		}

		if (obj.get("progressStage") != null) {
			requisition.setProgressStage(obj.get("progressStage").asText());
		}

		if (obj.get("financialYear") != null) {
			requisition.setFinancialYear(obj.get("financialYear").asInt());
		}
		Rules rule = null; 
		if (obj.get("roleName").asText() != null) {
			Roles role = rolesService.getRolesByName(obj.get("roleName").asText());
			rule = rulesService.getRulesByRoleAndRuleName(role, Constants.RULE_REQUISITION_TYPE);
		} else {
			logger.error("Requistion could not be added. User's role missing");
			return null;
		}
		JSONObject jsonObject = new JSONObject(rule.getRule());
//       		JSONObject standardRule = jsonObject.getJSONObject(Constants.REQUISITION_TYPE_STANDARD);
		JSONObject nonStandardRule = jsonObject.getJSONObject(Constants.REQUISITION_TYPE_NON_STANDARD);
		if (obj.get("totalPrice") != null) {
			int price = obj.get("totalPrice").asInt();
			if (price >= nonStandardRule.getInt("min") && price <= nonStandardRule.getInt("max")) {
				requisition.setType(Constants.REQUISITION_TYPE_NON_STANDARD);
			} else {
				requisition.setType(Constants.REQUISITION_TYPE_STANDARD);
			}
		}
//		}

		if (obj.get("totalPrice") != null) {
			requisition.setTotalPrice(obj.get("totalPrice").asInt());
		}

		if (obj.get("notes") != null) {
			requisition.setNotes(obj.get("notes").asText());
		}
		if (obj.get("status") != null) {
			requisition.setStatus(obj.get("status").asText());
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DEFAULT_DATE_FORMAT);
		if (obj.get("dueDate") != null) {
			LocalDate localDate = LocalDate.parse(obj.get("dueDate").asText(), formatter);
			requisition.setDueDate(localDate);
		}

		if (obj.get("user") != null) {
			requisition.setUpdatedBy(obj.get("user").asText());
		} else {
			requisition.setUpdatedBy(Constants.SYSTEM_ACCOUNT);
		}
		requisition.setUpdatedOn(Instant.now());
		requisition = requisitionRepository.save(requisition);
		logger.info("Requisition updated successfully");

		saveRequisitionActivity(requisition);

		return requisition;
	}

	private RequisitionActivity saveRequisitionActivity(Requisition requisition) {
		RequisitionActivity requisitionActivity = null;
		if (requisition != null) {
			logger.info("Adding requisition activity");
			requisitionActivity = new RequisitionActivity();
			BeanUtils.copyProperties(requisition, requisitionActivity);
			requisitionActivity.setRequisitionId(requisition.getId());
			requisitionActivity = requisitionActivityService.addRequisitionActivity(requisitionActivity);
			logger.info("Requisition activity added successfully");
		}
		return requisitionActivity;
	}

	public List<Requisition> searchRequisition(Map<String, String> requestObj) {
		logger.info("Request to search requisition on given filter criteria");
		Requisition requisition = new Requisition();

		boolean isFilter = false;
		if (!org.apache.commons.lang3.StringUtils.isBlank(requestObj.get("id"))) {
			requisition.setId(Long.parseLong(requestObj.get("id")));
			isFilter = true;
		}

		if (!org.apache.commons.lang3.StringUtils.isBlank(requestObj.get("departmentId"))) {
			Department department = departmentService.getDepartment(Long.parseLong(requestObj.get("departmentId")));
			if (department == null) {
				requisition.setDepartment(department);
			}
			isFilter = true;
		}

		if (!org.apache.commons.lang3.StringUtils.isBlank(requestObj.get("currencyId"))) {
			Currency currency = currencyService.getCurrency(Long.parseLong(requestObj.get("currencyId")));
			if (currency == null) {
				requisition.setCurrency(currency);
			}
			isFilter = true;
		}

		if (!org.apache.commons.lang3.StringUtils.isBlank(requestObj.get("requisitionNo"))) {
			requisition.setRequisitionNo(requestObj.get("requisitionNo"));
			isFilter = true;
		}

		if (!org.apache.commons.lang3.StringUtils.isBlank(requestObj.get("status"))) {
			requisition.setStatus(requestObj.get("status"));
			isFilter = true;
		}

		if (!org.apache.commons.lang3.StringUtils.isBlank(requestObj.get("progressStage"))) {
			requisition.setProgressStage(requestObj.get("progressStage"));
			isFilter = true;
		}

		if (!org.apache.commons.lang3.StringUtils.isBlank(requestObj.get("financialYear"))) {
			requisition.setFinancialYear(Integer.parseInt(requestObj.get("progressStage")));
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

		if (!org.apache.commons.lang3.StringUtils.isBlank(requestObj.get("dueDate"))) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DEFAULT_DATE_FORMAT);
			LocalDate localDate = LocalDate.parse(requestObj.get("dueDate"), formatter);
			requisition.setDueDate(localDate);
		}

		List<Requisition> list = null;
		if (isFilter) {
			list = this.requisitionRepository.findAll(Example.of(requisition), Sort.by(Direction.DESC, "id"));
		} else {
			list = this.requisitionRepository.findAll(Sort.by(Direction.DESC, "id"));
		}
		for (Requisition req : list) {
			RequisitionActivity ca = new RequisitionActivity();
			ca.setRequisitionId(req.getId());
			List<RequisitionActivity> caList = requisitionActivityRepository.findAll(Example.of(ca));
			req.setActivityList(caList);
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

	public boolean approveRequisition(ObjectNode obj) throws JSONException {
		logger.info("Getting requisition by id: " + obj);

		try {
			if (obj.get("requisitionId") == null) {
				logger.error("Requision id not found. Cannot approve requisition.");
				return false;
			}

			if (obj.get("roleName") == null) {
				logger.error("Role not found. Cannot approve requisition.");
				return false;
			}

			Optional<Requisition> req = requisitionRepository.findById(obj.get("requisitionId").asLong());
			if (!req.isPresent()) {
				logger.error("Requision not found. Cannot approve requisition.");
				return false;
			}
//			Roles role = rolesService.getRolesByName(json.get("roleName").asText());
//			Roles role = rolesService.getRolesByName(obj.get("roleName").asText());
//			if (role == null) {
//				logger.error("Given role " + obj.get("roleName").asText() + "Role not found. Cannot approve requisition.");
//				return false;
//			}

			Requisition requisition = req.get();
			
			Rules rule = null; 
			if (obj.get("roleName").asText() != null) {
				Roles role = rolesService.getRolesByName(obj.get("roleName").asText());
				rule = rulesService.getRulesByRoleAndRuleName(role, Constants.RULE_APPROVE_REQUISITION);
			} else {
				logger.error("Requistion could not be added. User's role missing");
				return false;
			}
//			
//			Rules rule = rulesService.getRulesByRoleAndRuleName(role, Constants.RULE_APPROVE_REQUISITION);
//			if (rule == null) {
//				logger.error("Approval rule not found. Cannot approve requisition.");
//				return false;
//			}

			JSONObject jsonObject = new JSONObject(rule.getRule());

			int price = 0;
			if (requisition.getTotalPrice() != null) {
				price = requisition.getTotalPrice().intValue();
			}

			int minRulePrice = 0;
			int maxRulePrice = 0;

			try {
				minRulePrice = jsonObject.getInt("min");
			} catch (Exception e) {
				logger.error("Minimum price rule not found. Cannot approve requisiont. Exception: ", e);
				return false;
			}

			if (jsonObject.get("max") != null) {
				try {
					maxRulePrice = jsonObject.getInt("max");
				} catch (Exception e) {
					logger.error("Incorrect maximum price rule. Cannot approve requisiont. Exception: ", e);
					return false;
				}
			}

			boolean isRuleApplied = false;
			if (price >= minRulePrice && jsonObject.get("max") == null) {
				requisition.setStatus(Constants.PROGRESS_STAGE_APPROVED);
				isRuleApplied = true;
			}

			if (price >= minRulePrice && jsonObject.get("max") != null && price <= maxRulePrice) {
				requisition.setStatus(Constants.PROGRESS_STAGE_APPROVED);
				isRuleApplied = true;
			}

			if (isRuleApplied) {
				requisition = requisitionRepository.save(requisition);
				saveRequisitionActivity(requisition);
				return true;
			} else {
				logger.warn("Approve requisition failed. No rule applied");
				return false;
			}

		} catch (Exception e) {
			logger.error("Approve requisition failed. Exception: ", e);
			return false;
		}

	}
	

}
