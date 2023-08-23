package com.hadhemi.Task_Management.controllers;

import com.hadhemi.Task_Management.dto.OrganizationDTO;
import com.hadhemi.Task_Management.dto.UserDTO;
import com.hadhemi.Task_Management.enums.Role;
import com.hadhemi.Task_Management.models.Organization;
import com.hadhemi.Task_Management.models.User;
import com.hadhemi.Task_Management.services.OrganizationService;
import com.hadhemi.Task_Management.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.time.LocalDateTime;
import java.util.List;
@CrossOrigin(origins = "*")
@RestController
@Api("/organization")
@RequestMapping("/organizations")
public class OrganizationController {
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private UserService userService;
    @GetMapping
    public List<Organization> getAllOrganizations() {
        return organizationService.getAllOrganizations();
    }
    @PostMapping
    public ResponseEntity<Organization> createOrganization(@RequestBody Organization request,
                                                           @RequestParam String adminUserId) {
        Organization createdOrganization = organizationService.createOrganization(request, adminUserId);
        return ResponseEntity.ok(createdOrganization);
    }



    @GetMapping("/{orgId}")
    public ResponseEntity<OrganizationDTO> getOrganization(@PathVariable String orgId) {
        OrganizationDTO organizationDTO = organizationService.getOrganization(orgId);
        return ResponseEntity.ok(organizationDTO);
    }

    @PutMapping("/{orgId}")
    public ResponseEntity<Organization> updateOrganization(@PathVariable String orgId, @RequestBody Organization request,
                                                           @RequestParam String adminUserId) {
        Organization updatedOrganization = organizationService.updateOrganization(orgId, request, adminUserId);
        return ResponseEntity.ok(updatedOrganization);
    }

    @DeleteMapping("/{orgId}")
    public ResponseEntity<Void> deleteOrganization(@PathVariable String orgId) {
        organizationService.deleteOrganization(orgId);
        return ResponseEntity.noContent().build();
    }
}


