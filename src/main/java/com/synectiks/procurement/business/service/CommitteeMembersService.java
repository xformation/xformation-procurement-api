package com.synectiks.procurement.business.service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.synectiks.procurement.domain.CommitteeAndMemberLink;
import com.synectiks.procurement.domain.CommitteeMember;
import com.synectiks.procurement.domain.Document;
import com.synectiks.procurement.repository.CommitteeAndMemberLinkRepository;
import com.synectiks.procurement.repository.CommitteeMemberRepository;
import com.synectiks.procurement.repository.CommitteeRepository;

@Service
public class CommitteeMembersService {
	private static final Logger logger = LoggerFactory.getLogger(CommitteeMembersService.class);
	
	@Autowired
	private CommitteeAndMemberLinkRepository committeeAndMemberLinkRepository;
	
	@Autowired
	private CommitteeMemberRepository committeeMemberRepository;
	
	@Autowired
	private CommitteeRepository committeeRepository;
	
	@Autowired
	private CommitteeService committeeService;
	
	@Autowired
	private DocumentService documentService;

	public CommitteeMember getCommitteeMember(Long id) {
		logger.info("Getting committee member by id: " + id);
		Optional<CommitteeMember> ovn = committeeMemberRepository.findById(id);
		if (ovn.isPresent()) {
			logger.info("Committee member: " + ovn.get().toString());
			return ovn.get();
		}
		logger.warn("Committee member not found");
		return null;
	}

	@Transactional
	public CommitteeMember addCommitteeMember(String obj, MultipartFile file) throws Exception {
		CommitteeMember committeeMember = new CommitteeMember();

		ObjectMapper mapper = new ObjectMapper();
		ObjectNode json = (ObjectNode) mapper.readTree(obj);
		Committee committee = null; 
		if (json.get("committeeId") != null) {
			committee = committeeService.getCommittee(json.get("committeeId").asLong());
			if (committee == null) {
				logger.warn("Committee not found. Cannot add the member.");
				return null;
			}
		}
		
		if (json.get("name") != null) {
			committeeMember.setName(json.get("name").asText());
		}
		if (json.get("company") != null) {
			committeeMember.setCompany(json.get("company").asText());
		}

		if (json.get("department") != null) {
			committeeMember.setDepartment(json.get("department").asText());
		}
		if (json.get("designation") != null) {
			committeeMember.setDesignation(json.get("designation").asText());
		}
		if (json.get("phoneNumber") != null) {
			committeeMember.setPhoneNumber(json.get("phoneNumber").asText());
		}
		if (json.get("email") != null) {
			committeeMember.setEmail(json.get("email").asText());
		}

		if (json.get("user") != null) {
			committeeMember.setCreatedBy(json.get("user").asText());
			committeeMember.setUpdatedBy(json.get("user").asText());
		} else {
			committeeMember.setCreatedBy(Constants.SYSTEM_ACCOUNT);
			committeeMember.setUpdatedBy(Constants.SYSTEM_ACCOUNT);
		}

		Instant now = Instant.now();
		committeeMember.setCreatedOn(now);
		committeeMember.setUpdatedOn(now);
		committeeMember = committeeMemberRepository.save(committeeMember);
		logger.debug("Committee member saved successfully. "+committeeMember.toString());
		
		if (committeeMember != null) {
			CommitteeAndMemberLink committeeAndMemberLink = new CommitteeAndMemberLink();
			committeeAndMemberLink.setCommitteeMember(committeeMember);
			committeeAndMemberLink.setCommittee(committee);
			if (json.get("status") != null) {
				committeeAndMemberLink.setStatus(json.get("status").asText());
			}
			committeeAndMemberLink = committeeAndMemberLinkRepository.save(committeeAndMemberLink);
			logger.debug("Committee member and its associated committee saved successfully" + committeeAndMemberLink.toString());
		}
		
		if (file != null) {
			byte[] bytes = file.getBytes();
			String orgFileName = StringUtils.cleanPath(file.getOriginalFilename());
			String ext = "";
			if(orgFileName.lastIndexOf(".") != -1) {
				ext = orgFileName.substring(orgFileName.lastIndexOf(".")+1);
			}
			String filename = "";
			if (json.get("name") != null) {
				filename = json.get("name").asText();
			}
			filename = filename.toLowerCase().replaceAll(" ", "-")+"_"+System.currentTimeMillis()+"."+ext;
			File localStorage = new File(Constants.LOCAL_PROFILE_IMAGE_STORAGE_DIRECTORY);
			if (!localStorage.exists()) {
				localStorage.mkdirs();
			}
			Path path = Paths.get(localStorage.getAbsolutePath() + File.pathSeparatorChar + filename);
			Files.write(path, bytes);
			
			Document document = new Document();
			document.setFileName(filename);
			document.setFileExt(ext);
			document.setFileType(Constants.FILE_TYPE_IMAGE);
			document.setFileSize(file.getSize());
			document.setStorageLocation(Constants.FILE_STORAGE_LOCATION_LOCAL);
			document.setLocalFilePath(localStorage.getAbsolutePath() + File.pathSeparatorChar + filename);
			document.setSourceOfOrigin(this.getClass().getSimpleName());
			document.setSourceId(committeeMember.getId());
			document.setIdentifier(Constants.IDENTIFIER_PROFILE_IMAGE);
			document.setCreatedBy(committeeMember.getCreatedBy());
			document.updatedBy(committeeMember.getCreatedBy());
			document.setCreatedOn(now);
			document.setUpdatedOn(now);
			documentService.saveDocument(document);
			logger.debug("Committee member\'s profile image saved successfully");
		}
		logger.debug("Committee member added successfully. ");
		return committeeMember;

	}

	@Transactional
	public CommitteeMember updateCommitteeMembers(String obj, MultipartFile file) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode json = (ObjectNode) mapper.readTree(obj);
		
		Optional<CommitteeMember> ur = committeeMemberRepository.findById(Long.parseLong(json.get("id").asText()));
		if (!ur.isPresent()) {
			logger.warn("Committee member not found. Cannot update the committee member. Given committee member id: "+json.get("id").asText());
			return null;
		}
		
		CommitteeMember committeeMember = ur.get();
		
		Committee committee = null; 
		if (json.get("committeeId") != null) {
			committee = committeeService.getCommittee(json.get("committeeId").asLong());
		}
		
		if (json.get("name") != null) {
			committeeMember.setName(json.get("name").asText());
		}
		if (json.get("company") != null) {
			committeeMember.setCompany(json.get("company").asText());
		}

		if (json.get("department") != null) {
			committeeMember.setDepartment(json.get("department").asText());
		}
		if (json.get("designation") != null) {
			committeeMember.setDesignation(json.get("designation").asText());
		}
		if (json.get("phoneNumber") != null) {
			committeeMember.setPhoneNumber(json.get("phoneNumber").asText());
		}
		if (json.get("email") != null) {
			committeeMember.setEmail(json.get("email").asText());
		}

		if (json.get("user") != null) {
			committeeMember.setUpdatedBy(json.get("user").asText());
		} else {
			committeeMember.setUpdatedBy(Constants.SYSTEM_ACCOUNT);
		}

		Instant now = Instant.now();
		committeeMember.setUpdatedOn(now);
		committeeMember = committeeMemberRepository.save(committeeMember);
		logger.debug("Committee member updated successfully. "+committeeMember.toString());
		
		if (committee != null) {
			CommitteeAndMemberLink committeeAndMemberLink = new CommitteeAndMemberLink();
			committeeAndMemberLink.setCommitteeMember(committeeMember);
			committeeAndMemberLink.setCommittee(committee);
			Optional<CommitteeAndMemberLink> ocl = committeeAndMemberLinkRepository.findOne(Example.of(committeeAndMemberLink));
			
			if(ocl.isPresent()) {
				committeeAndMemberLink = ocl.get();
			}
			
			if (json.get("status") != null) {
				committeeAndMemberLink.setStatus(json.get("status").asText());
			}
			committeeAndMemberLink = committeeAndMemberLinkRepository.save(committeeAndMemberLink);
			logger.debug("Committee member and its associated committee updated successfully" + committeeAndMemberLink.toString());
		}
		
		if (file != null) {
			byte[] bytes = file.getBytes();
			String orgFileName = StringUtils.cleanPath(file.getOriginalFilename());
			String ext = "";
			if(orgFileName.lastIndexOf(".") != -1) {
				ext = orgFileName.substring(orgFileName.lastIndexOf(".")+1);
			}
			String filename = "";
			if (json.get("name") != null) {
				filename = json.get("name").asText();
			}
			filename = filename.toLowerCase().replaceAll(" ", "-")+"_"+System.currentTimeMillis()+"."+ext;
			File localStorage = new File(Constants.LOCAL_PROFILE_IMAGE_STORAGE_DIRECTORY);
			if (!localStorage.exists()) {
				localStorage.mkdirs();
			}
			Path path = Paths.get(localStorage.getAbsolutePath() + File.pathSeparatorChar + filename);
			Files.write(path, bytes);
			
			Document document = null;
			
			Map<String, String> requestObj = new HashMap<>();
			requestObj.put("sourceId", String.valueOf(committeeMember.getId()));
			requestObj.put("identifier", Constants.IDENTIFIER_PROFILE_IMAGE);
			List<Document> cdList = documentService.searchDocument(requestObj); 
			
			if(cdList.size() > 0) {
				document = cdList.get(0);
			}else {
				document = new Document();
			}
			document.setSourceId(committeeMember.getId());
			document.setIdentifier(Constants.IDENTIFIER_PROFILE_IMAGE);
			
			document.setFileName(filename);
			document.setFileExt(ext);
			document.setFileType(Constants.FILE_TYPE_IMAGE);
			document.setFileSize(file.getSize());
			document.setStorageLocation(Constants.FILE_STORAGE_LOCATION_LOCAL);
			document.setLocalFilePath(localStorage.getAbsolutePath() + File.pathSeparatorChar + filename);
			document.setSourceOfOrigin(this.getClass().getSimpleName());
			
			if(cdList.size() > 0) {
				document.updatedBy(committeeMember.getUpdatedBy());
				document.setUpdatedOn(now);
			}else {
				document.setCreatedBy(committeeMember.getCreatedBy());
				document.updatedBy(committeeMember.getCreatedBy());
				document.setCreatedOn(now);
				document.setUpdatedOn(now);
			}
			
			documentService.saveDocument(document);
			logger.debug("Committee member\'s profile image updated successfully");
		}
		logger.debug("Committee member updated successfully. ");
		return committeeMember;

	}

	public List<CommitteeMember> searchCommitteeMembers(Map<String, String> requestObj) {
		logger.info("Request to search committee members on given filter criteria");
		CommitteeMember committeeMember = new CommitteeMember();
		boolean isFilter = false;
		
		if (requestObj.get("id") != null) {
			committeeMember.setId(Long.parseLong(requestObj.get("id")));
			isFilter = true;
		}
		if (requestObj.get("name") != null) {
			committeeMember.setName(requestObj.get("name"));
			isFilter = true;
		}
		if (requestObj.get("company") != null) {
			committeeMember.setCompany(requestObj.get("company"));
			isFilter = true;
		}
		if (requestObj.get("department") != null) {
			committeeMember.setDepartment(requestObj.get("department"));
			isFilter = true;
		}
		if (requestObj.get("phoneNumber") != null) {
			committeeMember.setPhoneNumber(requestObj.get("phoneNumber"));
			isFilter = true;
		}
		if (requestObj.get("email") != null) {
			committeeMember.setEmail(requestObj.get("email"));
			isFilter = true;
		}
		if (requestObj.get("designation") != null) {
			committeeMember.setDesignation(requestObj.get("designation"));
			isFilter = true;
		}
		
		if (requestObj.get("createdOn") != null) {
			Instant inst = Instant.parse(requestObj.get("createdOn"));
			committeeMember.setCreatedOn(inst);
			isFilter = true;
		}
		
		if (requestObj.get("createdBy") != null) {
			committeeMember.setCreatedBy(requestObj.get("createdBy"));
			isFilter = true;
		}
		if (requestObj.get("updatedOn") != null) {
			Instant inst = Instant.parse(requestObj.get("updatedOn"));
			committeeMember.setUpdatedOn(inst);
			isFilter = true;
		}
		if (requestObj.get("updatedBy") != null) {
			committeeMember.setUpdatedBy(requestObj.get("updatedBy"));
			isFilter = true;
		}
		
		List<CommitteeMember> list = null;
		if (requestObj.get("committeeId") != null) {
			Committee com = committeeService.getCommittee(Long.parseLong(requestObj.get("committeeId")));
			if (com == null) {
				logger.info("No committee found");
				return java.util.Collections.emptyList();
			}
			if (isFilter) {
				list = this.committeeMemberRepository.findAll(Example.of(committeeMember), Sort.by(Direction.ASC, "name"));
			} else {
				list = this.committeeMemberRepository.findAll(Sort.by(Direction.ASC, "name"));
			}
			for (CommitteeMember cmt : list) {
				CommitteeAndMemberLink ca = new CommitteeAndMemberLink();
				ca.setCommitteeMember(committeeMember);
				ca.setCommittee(com);
				if (requestObj.get("status") != null) {
					ca.setStatus(requestObj.get("status"));
				}
				List<CommitteeAndMemberLink> caList = committeeAndMemberLinkRepository.findAll(Example.of(ca));
				cmt.setCommitteeList(caList);
			}
		}else {
			if (isFilter) {
				list = this.committeeMemberRepository.findAll(Example.of(committeeMember), Sort.by(Direction.ASC, "name"));
			} else {
				list = this.committeeMemberRepository.findAll(Sort.by(Direction.ASC, "name"));
			}
			for (CommitteeMember cmt : list) {
				CommitteeAndMemberLink ca = new CommitteeAndMemberLink();
				ca.setCommitteeMember(committeeMember);
				List<CommitteeAndMemberLink> caList = committeeAndMemberLinkRepository.findAll(Example.of(ca));
				cmt.setCommitteeList(caList);
			}
		}
		logger.info("Committee members search completed. Total records: " + list.size());
		return list;
	}
	
}
