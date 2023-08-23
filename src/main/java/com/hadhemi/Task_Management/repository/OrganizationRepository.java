package com.hadhemi.Task_Management.repository;

import com.hadhemi.Task_Management.models.Organization;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrganizationRepository extends MongoRepository<Organization, String> {
    Organization findByName(String name);
}
