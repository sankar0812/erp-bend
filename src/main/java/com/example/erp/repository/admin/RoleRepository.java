package com.example.erp.repository.admin;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.erp.entity.admin.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

}
