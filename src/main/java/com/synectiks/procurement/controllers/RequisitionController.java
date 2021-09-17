package com.synectiks.procurement.controllers;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.synectiks.procurement.business.service.RequisitionService;
import com.synectiks.procurement.domain.Requisition;
import com.synectiks.procurement.domain.Status;

@RestController
@RequestMapping("/api")
public class RequisitionController {
	private static final Logger logger = LoggerFactory.getLogger(RequisitionController.class);

	@Autowired
	private RequisitionService requisitionService;

//	@Autowired
//	private RequisitionRepository requisitionRepository;

//	@Autowired
//	private RulesRepository rulesRepository;

	@RequestMapping(value = "/addRequisition", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Status> addRequisition(@RequestParam("file") MultipartFile file,
			@RequestParam("obj") String obj) throws IOException {
		logger.info("Request to add a requsition");
		Status st = new Status();
		try {
			Requisition requisition = requisitionService.addRequisition(file, obj);
			if (requisition == null) {
				logger.error("Add requisition failed");
				st.setCode(HttpStatus.EXPECTATION_FAILED.value());
				st.setType("ERROR");
				st.setMessage("Add requisition failed");
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
			}
			st.setCode(HttpStatus.OK.value());
			st.setType("SUCCESS");
			st.setMessage("Add requisition successful");
			st.setObject(requisition);
			return ResponseEntity.status(HttpStatus.OK).body(st);
		} catch (Exception e) {
			logger.error("Add requisition failed. Exception: ", e);
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("Add requisition failed");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}
	}

	@PostMapping("/updateRequisition")
	public ResponseEntity<Status> updateRequisition(@RequestBody ObjectNode obj)
			throws JSONException, URISyntaxException {
		logger.info("Request to update a requsition");
		Status st = new Status();
		try {
			Requisition requisition = requisitionService.updateRequisition(obj);
			if (requisition == null) {
				logger.error("Update requisition failed");
				st.setCode(HttpStatus.EXPECTATION_FAILED.value());
				st.setType("ERROR");
				st.setMessage("Update requisition failed");
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
			}
			st.setCode(HttpStatus.OK.value());
			st.setType("SUCCESS");
			st.setMessage("Update requisition successful");
			st.setObject(requisition);
			return ResponseEntity.status(HttpStatus.OK).body(st);
		} catch (Exception e) {
			logger.error("Update requisition failed. Exception: ", e);
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("Update requisition failed");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}
	}

	@GetMapping("/searchRequisition")
	public ResponseEntity<Status> searchRequisition(@RequestParam Map<String, String> requestObj) {
		logger.info("Request to search requsitions");
		Status st = new Status();
		try {
			List<Requisition> list = requisitionService.searchRequisition(requestObj);
			if (list == null) {
				logger.error("Search requisition failed");
				st.setCode(HttpStatus.EXPECTATION_FAILED.value());
				st.setType("ERROR");
				st.setMessage("Search requisition failed");
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
			}
			st.setCode(HttpStatus.OK.value());
			st.setType("SUCCESS");
			st.setMessage("Search requisition successful");
			st.setObject(list);
			return ResponseEntity.status(HttpStatus.OK).body(st);
		} catch (Exception e) {
			logger.error("Search requisition failed. Exception: ", e);
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("Search requisition failed");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}

	}

	@DeleteMapping("/deleteRequisition/{id}")
	public ResponseEntity<Status> deleteRequisition(@PathVariable Long id) {
		Status st = new Status();
		try {
			requisitionService.deleteRequisition(id);
			st.setCode(HttpStatus.OK.value());
			st.setType("SUCCESS");
			st.setMessage("Requisition deleted successfully");
			return ResponseEntity.status(HttpStatus.OK).body(st);
		} catch (Throwable th) {
			st.setCode(HttpStatus.OK.value());
			st.setType("ERROR");
			st.setMessage("Delete Requisition failed");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}
	}

//	@GetMapping("/getAllRequisitions")
//	private ResponseEntity<Status> getAllRequisitions() {
//		logger.info("Request to get all requsitions");
//		Status st = new Status();
//		try {
//			List<Requisition> list = requisitionService.getAllRequisitions();
//			if (list == null) {
//				logger.error("Search all requisition failed");
//				st.setCode(HttpStatus.EXPECTATION_FAILED.value());
//				st.setType("ERROR");
//				st.setMessage("Search all requisition failed");
//				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
//			}
//			st.setCode(HttpStatus.OK.value());
//			st.setType("SUCCESS");
//			st.setMessage("Search all requisition successfully");
//			st.setObject(list);
//			return ResponseEntity.status(HttpStatus.OK).body(st);
//		} catch (Exception e) {
//			logger.error("Search all requisition failed. Exception: ", e);
//			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
//			st.setType("ERROR");
//			st.setMessage("Search all requisition failed");
//			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
//		}
//	}

	@GetMapping("/getRequisition/{id}")
	public ResponseEntity<Status> getRequisition(@PathVariable Long id) {
		logger.info("Getting requisition by id: " + id);
		Status st = new Status();
		try {
			Requisition requisition = requisitionService.getRequisition(id);
			if (requisition == null) {
				logger.warn("Requisition not found.");
				st.setCode(HttpStatus.EXPECTATION_FAILED.value());
				st.setType("ERROR");
				st.setMessage("Requisition found suses");
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
			}
			st.setCode(HttpStatus.OK.value());
			st.setType("SUCCESS");
			st.setMessage("Requisition found");
			st.setObject(requisition);
			return ResponseEntity.status(HttpStatus.OK).body(st);
		} catch (Exception e) {
			logger.error("Requisition not found. Exception: ", e);
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("Requisition not found");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}
	}

	@PostMapping("/sendRequisitionToVendor")
	public ResponseEntity<Status> sendRequisitionToVendor(@RequestBody List<ObjectNode> list) {
		logger.info("Assigning requisitions to vendors ");
		Status st = new Status();
		try {
			requisitionService.sendRequisitionToVendor(list);
			st.setCode(HttpStatus.OK.value());
			st.setType("SUCCESS");
			st.setMessage("Requisition to vendor found");
			return ResponseEntity.status(HttpStatus.OK).body(st);
		} catch (Exception e) {
			logger.error("Requisition not found. Exception: ", e);
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("Requisition not found");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}
	}

//	@PostMapping("/updateRequisitionToVendor")
//	public ResponseEntity<Status> updateRequisitionToVendor(@RequestBody List<ObjectNode> list) {
//		logger.info("Assigning requisitions to vendors ");
//		Status st = new Status();
//		try {
//			requisitionService.updateRequisitionToVendor(list);
//			st.setCode(HttpStatus.OK.value());
//			st.setType("SUCCESS");
//			st.setMessage("Requisition to vendor  found");
//			st.setObject("All the requisition to vendor  updated  successfully");
//			return ResponseEntity.status(HttpStatus.OK).body(st);
//		} catch (Exception e) {
//			logger.error(" update of  failed");
//			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
//			st.setType("ERROR");
//			st.setMessage("Requisition to vendor update  failed");
//			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
//		}
//	}

	@PostMapping("/approveRequisition")
	public ResponseEntity<Status> approveRequisition(@RequestBody ObjectNode obj)
			throws JSONException {
		logger.info("Request to approve a requsition");
		
		Status st = new Status();
		try {
			boolean updateFlag = requisitionService.approveRequisition(obj);
			if (updateFlag) {
				st.setCode(HttpStatus.OK.value());
				st.setType("SUCCESS");
				st.setMessage("Requisition approved successfully");
			} else {
				st.setCode(HttpStatus.EXPECTATION_FAILED.value());
				st.setType("ERROR");
				st.setMessage("Requestion could not be approved");
			}
			return ResponseEntity.status(HttpStatus.OK).body(st);
		} catch (Exception e) {
			logger.error("Approve requisition failed");
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("Approve Requisition failed");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}
	}

}