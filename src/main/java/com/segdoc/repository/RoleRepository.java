package com.segdoc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.segdoc.model.Role;

@Repository("role")
public interface RoleRepository extends JpaRepository<Role, Integer>{
	Role findByRole (String Role);
}
