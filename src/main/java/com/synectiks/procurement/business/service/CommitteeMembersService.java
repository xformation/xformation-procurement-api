package com.synectiks.procurement.business.service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.synectiks.procurement.config.Constants;
import com.synectiks.procurement.domain.Committee;
import com.synectiks.procurement.domain.CommitteeActivity;
import com.synectiks.procurement.domain.CommitteeMembers;
import com.synectiks.procurement.domain.CommitteeMembersStatus;
import com.synectiks.procurement.domain.Roles;
import com.synectiks.procurement.domain.RolesGroup;
import com.synectiks.procurement.repository.CommitteeMembersRepository;
import com.synectiks.procurement.repository.CommitteeMembersStatusRepository;
import com.synectiks.procurement.repository.CommitteeRepository;

@Service
public class CommitteeMembersService {
	private static final Logger logger = LoggerFactory.getLogger(CommitteeMembersService.class);
	
	@Autowired
	private CommitteeMembersStatusRepository committeeMembersStatusRepository;
	
	@Autowired
	private CommitteeMembersRepository committeeMembersRepository;
	@Autowired
	private CommitteeRepository committeeRepository;

	public CommitteeMembers getCommitteeMembers(Long id) {
		logger.info("Getting committee members by id: " + id);
		Optional<CommitteeMembers> ovn = committeeMembersRepository.findById(id);
		if (ovn.isPresent()) {
			logger.info("Committee members: " + ovn.get().toString());
			return ovn.get();
		}
		logger.warn("Committee members not found");
		return null;
	}

	@Transactional
	public CommitteeMembers addCommitteeMembers(String obj, MultipartFile file) throws Exception {
		CommitteeMembers committeeMembers = new CommitteeMembers();

		if (file != null) {
			byte[] bytes = file.getBytes();
			String filename = StringUtils.cleanPath(file.getOriginalFilename());
			filename = filename.toLowerCase().replaceAll(" ", "-");
			String uniqueID = UUID.randomUUID().toString();
			filename = uniqueID.concat(filename);
			File localStorage = new File(Constants.LOCAL_REQUISITION_FILE_STORAGE_DIRECTORY);
			committeeMembers.setProfileImage(filename);
			if (!localStorage.exists()) {
				localStorage.mkdirs();
			}
			Path path = Paths
					.get(Constants.LOCAL_REQUISITION_FILE_STORAGE_DIRECTORY + File.pathSeparatorChar + filename);
			Files.write(path, bytes);
		}
		
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode json = (ObjectNode) mapper.readTree(obj);

		if (json.get("commiteeId") != null) {
			Optional<Committee> com = committeeRepository.findById(json.get("commiteeId").asLong());
			if (com.isPresent()) {
				committeeMembers.setCommittee(com.get());
			}
		}

		if (json.get("name") != null) {
			committeeMembers.setName(json.get("name").asText());
		}
		if (json.get("company") != null) {
			committeeMembers.setCompany(json.get("company").asText());
		}

		if (json.get("department") != null) {
			committeeMembers.setDepartment(json.get("department").asText());
		}
		if (json.get("degradation") != null) {
			committeeMembers.setDegradation(json.get("degradation").asText());
		}
//		if (json.get("status") != null) {
//			committeeMembers.setStatus(json.get("status").asText());
//		}

		if (json.get("phone_number") != null) {
			committeeMembers.setPhoneNumber(json.get("phone_number").asText());
		}
		if (json.get("email") != null) {
			committeeMembers.setEmail(json.get("email").asText());
		}

		if (json.get("user") != null) {
			committeeMembers.setCreatedBy(json.get("user").asText());
			committeeMembers.setUpdatedBy(json.get("user").asText());
		} else {
			committeeMembers.setCreatedBy(Constants.SYSTEM_ACCOUNT);
			committeeMembers.setUpdatedBy(Constants.SYSTEM_ACCOUNT);
		}

		Instant now = Instant.now();
		committeeMembers.setCreatedOn(now);
		committeeMembers.setUpdatedOn(now);
		committeeMembers = committeeMembersRepository.save(committeeMembers);
		logger.info("Committee members added successfully");
	
		
		if (committeeMembers != null) {
			CommitteeMembersStatus committeeMembersStatus = new CommitteeMembersStatus();
			committeeMembersStatus.setCommitteeMembers(committeeMembers);
//			committeeMembersStatus.setCommitteeMembers(committeeMembers);
			if (json.get("commiteeId") != null) {
				Optional<Committee> com = committeeRepository.findById(json.get("commiteeId").asLong());
				if (com.isPresent()) {
					committeeMembersStatus.setCommittee(com.get());
				}
			}
			
			if (json.get("status") != null) {
				committeeMembersStatus.setStatus(json.get("status").asText());
			}
			logger.info("Committee members status added successfully" + committeeMembersStatus.toString());
			committeeMembersStatus = committeeMembersStatusRepository.save(committeeMembersStatus);
		}
		return committeeMembers;

	}

	@Transactional
	public CommitteeMembers updateCommitteeMembers(String obj, MultipartFile file) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode json = (ObjectNode) mapper.readTree(obj);
		Optional<CommitteeMembers> ur = committeeMembersRepository.findById(Long.parseLong(json.get("id").asText()));
		if (!ur.isPresent()) {
			logger.info("Committee members  id not found");
			return null;
		}
		CommitteeMembers committeeMembers = ur.get();
		if (file != null) {
			byte[] bytes = file.getBytes();
			String filename = StringUtils.cleanPath(file.getOriginalFilename());
			filename = filename.toLowerCase().replaceAll(" ", "-");
			String uniqueID = UUID.randomUUID().toString();
			filename = uniqueID.concat(filename);
			committeeMembers.setProfileImage(filename);
			File localStorage = new File(Constants.LOCAL_REQUISITION_FILE_STORAGE_DIRECTORY);
			if (!localStorage.exists()) {
				localStorage.mkdirs();
			}
			Path path = Paths
					.get(Constants.LOCAL_REQUISITION_FILE_STORAGE_DIRECTORY + File.pathSeparatorChar + filename);
			Files.write(path, bytes);
		}

		if (json.get("name") != null) {
			committeeMembers.setName(json.get("name").asText());
		}
		if (json.get("company") != null) {
			committeeMembers.setCompany(json.get("company").asText());
		}

		if (json.get("department") != null) {
			committeeMembers.setDepartment(json.get("department").asText());
		}
		if (json.get("degradation") != null) {
			committeeMembers.setDegradation(json.get("degradation").asText());
		}
		if (json.get("status") != null) {
			committeeMembers.setStatus(json.get("status").asText());
		}

		if (json.get("phone_number") != null) {
			committeeMembers.setPhoneNumber(json.get("phone_number").asText());
		}
		if (json.get("email") != null) {
			committeeMembers.setEmail(json.get("email").asText());
		}

		if (json.get("user") != null) {
			committeeMembers.setCreatedBy(json.get("user").asText());
			committeeMembers.setUpdatedBy(json.get("user").asText());
		} else {
			committeeMembers.setCreatedBy(Constants.SYSTEM_ACCOUNT);
			committeeMembers.setUpdatedBy(Constants.SYSTEM_ACCOUNT);
		}

		Instant now = Instant.now();
		committeeMembers.setCreatedOn(now);
		committeeMembers.setUpdatedOn(now);
		committeeMembers = committeeMembersRepository.save(committeeMembers);
		logger.info("Committee members updated successfully");
		
		if (committeeMembers != null) {
			CommitteeMembersStatus committeeMembersStatus = new CommitteeMembersStatus();
			BeanUtils.copyProperties(committeeMembers, committeeMembersStatus);
			committeeMembersStatus.setCommitteeMembers(committeeMembers);
			if (json.get("commiteeId") != null) {
				Optional<Committee> com = committeeRepository.findById(json.get("commiteeId").asLong());
				if (com.isPresent()) {
					committeeMembersStatus.setCommittee(com.get());
				}
			}
			if (json.get("status") != null) {
				committeeMembersStatus.setStatus(json.get("status").asText());
			}
			logger.info("Committee members status update successfully" + committeeMembersStatus.toString());
			committeeMembersStatus = committeeMembersStatusRepository.save(committeeMembersStatus);
		}
		return committeeMembers;

	}

	public List<CommitteeMembers> searchCommitteeMembers(Map<String, String> requestObj) {
		logger.info("Request to get committee members on given filter criteria");
		CommitteeMembers committeeMembers = new CommitteeMembers();
		boolean isFilter = false;
		if (requestObj.get("id") != null) {
			committeeMembers.setId(Long.parseLong(requestObj.get("id")));
			isFilter = true;
		}
//		if (requestObj.get("departmentId") != null) {
//			requisition.setDepartment(requestObj.get("departmentId"));
//			isFilter = true;
//		}

		if (requestObj.get("name") != null) {
			committeeMembers.setName(requestObj.get("name"));
			isFilter = true;
		}
		if (requestObj.get("company") != null) {
			committeeMembers.setCompany(requestObj.get("company"));
			isFilter = true;
		}
		if (requestObj.get("department") != null) {
			committeeMembers.setDepartment(requestObj.get("department"));
			isFilter = true;
		}
		if (requestObj.get("degradation") != null) {
			committeeMembers.setDegradation(requestObj.get("degradation"));
			isFilter = true;
		}
		if (requestObj.get("status") != null) {
			committeeMembers.setStatus(requestObj.get("status"));
			isFilter = true;
		}
		if (requestObj.get("phone_number") != null) {
			committeeMembers.setPhoneNumber(requestObj.get("phone_number"));
			isFilter = true;
		}
		if (requestObj.get("email") != null) {
			committeeMembers.setEmail(requestObj.get("email"));
			isFilter = true;
		}
		if (requestObj.get("email") != null) {
			committeeMembers.setEmail(requestObj.get("email"));
			isFilter = true;
		}
		List<CommitteeMembers> list = null;
		if (isFilter) {
			list = this.committeeMembersRepository.findAll(Example.of(committeeMembers), Sort.by(Direction.DESC, "id"));
		} else {
			list = this.committeeMembersRepository.findAll(Sort.by(Direction.DESC, "id"));
		}
//		for (CommitteeMembers cmt : list) {
//			CommitteeMembersStatus ca = new CommitteeMembersStatus();
//			ca.setCommitteeMembers(committeeMembers);
////			List<CommitteeMembersStatus> caList = committeeMembersStatusRepository.findAll(Example.of(ca));
//	
//			
//		}
	

//		logger.info("Committee members search completed. Total records: " + list.size());
		return list;
	}
}
