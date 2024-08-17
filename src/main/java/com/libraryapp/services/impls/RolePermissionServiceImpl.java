package com.libraryapp.services.impls;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.libraryapp.dtos.requests.RolePermissionRequest;
import com.libraryapp.entities.RolePermission;
import com.libraryapp.repositories.RolePermissionRepository;
import com.libraryapp.services.RolePermissionService;

@Service
public class RolePermissionServiceImpl implements RolePermissionService {

    RolePermissionRepository rolePermissionRepository;

    RolePermissionServiceImpl(RolePermissionRepository rolePermissionRepository) {
        this.rolePermissionRepository = rolePermissionRepository;
    }

    @Override
    public List<RolePermission> getRolePermissionByPermissionId(int permissionId) {

        return rolePermissionRepository.findByPermissionId(permissionId);
    }

    @Override
    public List<RolePermission> getRolePermissionByRoleId(int roleId) {
        return rolePermissionRepository.findByRoleId(roleId);
    }

    @Override
    public RolePermission editRolePermission(RolePermissionRequest rolePermission) {
        boolean existsById = rolePermissionRepository.existsById(rolePermission.getId());
        if (existsById) {
            Optional<RolePermission> existingRolePermission = rolePermissionRepository.findById(rolePermission.getId());
            if (existingRolePermission.isPresent()) {
                return rolePermissionRepository
                        .save(buildRolePermissionRequest(rolePermission, rolePermission.getId()));
            }
            return null;
        }
        return null;
    }

    @Override
    public RolePermission registerRolePermission(RolePermissionRequest rolePermission) {

        return rolePermissionRepository.save(buildRolePermissionRequest(rolePermission, 0));

    }

    @Override
    public boolean removeRolePermissionById(int rolePermissionId) {
        boolean isDeleted = false;
        try {
            boolean existById = rolePermissionRepository.existsById(rolePermissionId);
            if (existById) {
                Optional<RolePermission> rolePermission = rolePermissionRepository.findById(rolePermissionId);
                if (rolePermission.isPresent()) {
                    rolePermissionRepository.delete(rolePermission.get());
                    isDeleted = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isDeleted;
    }

    private RolePermission buildRolePermissionRequest(RolePermissionRequest rolePermissionRequest, int id) {
        RolePermission newRolePermission = new RolePermission();
        if (id > 0) {
            newRolePermission.setId(id);
        }

        newRolePermission.setIdRole(rolePermissionRequest.getIdRole());
        newRolePermission.setIdPermission(rolePermissionRequest.getIdPermission());

        return newRolePermission;
    }

}
