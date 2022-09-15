package com.example.shorturl.dao;

import com.example.shorturl.model.roles.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    /**
     * This method is used to look for a RoleEntity with a specific
     * roleName from the database
     *
     * @param roleName This is the role name to look for
     * @return Optional<RoleEntity> This returns the found role entity
     */
    Optional<RoleEntity> findByRoleName(String roleName);

}
