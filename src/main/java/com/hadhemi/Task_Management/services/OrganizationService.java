package com.hadhemi.Task_Management.services;
import com.hadhemi.Task_Management.dto.OrganizationDTO;
import com.hadhemi.Task_Management.dto.UserDTO;
import com.hadhemi.Task_Management.enums.Role;
import com.hadhemi.Task_Management.exception.NotFoundException;
import com.hadhemi.Task_Management.exception.ResourceNotFoundException;
import com.hadhemi.Task_Management.models.Organization;
import com.hadhemi.Task_Management.models.User;
import com.hadhemi.Task_Management.repository.OrganizationRepository;
import com.hadhemi.Task_Management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrganizationService {
    @Autowired
    private OrganizationRepository organizationRepository;
    @Autowired
    private UserRepository userRepository;


    public List<Organization> getAllOrganizations() {
        return organizationRepository.findAll();
    }

    public Organization createOrganization(Organization request, String adminUserId) {
        Organization organization = new Organization();
        organization.setName(request.getName());
        organization.setDescription(request.getDescription());
        organization.setCreationDateTime(LocalDateTime.now());

        // Trouver l'utilisateur admin par son rôle et l'attribuer en tant qu'admin de l'organisation
        User adminUser = userRepository.findById(adminUserId)
                .orElseThrow(() -> new NotFoundException("Admin user not found with id: " + adminUserId));
        organization.setAdmin(adminUser);

        // Enregistrer l'organisation
        Organization createdOrganization = organizationRepository.save(organization);

        // Mettre à jour l'admin avec l'organisation
        adminUser.setOrganizationId(organization.getId());
        userRepository.save(adminUser);

        return createdOrganization;
    }





    public OrganizationDTO getOrganization(String orgId) {
        Organization organization = organizationRepository.findById(orgId)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found with id: " + orgId));
        return convertToOrganizationDTO(organization);
    }

    public Organization updateOrganization(String orgId, Organization request, String adminUserId) {
        Organization existingOrganization = organizationRepository.findById(orgId)
                .orElseThrow(() -> new NotFoundException("Organization not found with id: " + orgId));


        existingOrganization.setName(request.getName());
        existingOrganization.setDescription(request.getDescription());

        User adminUser = userRepository.findById(adminUserId)
                .orElseThrow(() -> new NotFoundException("Admin user not found with id: " + adminUserId));
        existingOrganization.setAdmin(adminUser);

        return organizationRepository.save(existingOrganization);
    }

    public void deleteOrganization(String orgId) {
        organizationRepository.deleteById(orgId);
    }

    private OrganizationDTO convertToOrganizationDTO(Organization organization) {
        OrganizationDTO organizationDTO = new OrganizationDTO();
        organizationDTO.setName(organization.getName());
        organizationDTO.setDescription(organization.getDescription());
//        organizationDTO.setAdmin(UserService.convertToUserDTO(organization.getAdmin()));
//        organizationDTO.setMembers(UserService.convertToUserDTOList(organization.getMembers()));
//        organizationDTO.setTasks(TaskService.convertToTaskDTOList(organization.getTasks()));

        return organizationDTO;
    }

    private Organization convertToOrganization(OrganizationDTO organizationDTO) {
        Organization organization = new Organization();
        organization.setName(organizationDTO.getName());
        organization.setDescription(organizationDTO.getDescription());
//        organization.setAdmin(UserService.convertToUser(organizationDTO.getAdmin()));
//        organization.setMembers(UserService.convertToUserList(organizationDTO.getMembers()));
//        organization.setTasks(TaskService.convertToTaskList(organizationDTO.getTasks()));

        return organization;
    }

    private void updateOrganizationFromDTO(Organization organization, OrganizationDTO organizationDTO) {
        organization.setName(organizationDTO.getName());
        organization.setDescription(organizationDTO.getDescription());
        //organization.setAdmin(UserService.convertToUser(organizationDTO.getAdmin()));
//        organization.setMembers(UserService.convertToUserList(organizationDTO.getMembers()));
//        organization.setTasks(TaskService.convertToTaskList(organizationDTO.getTasks()));

    }
}
