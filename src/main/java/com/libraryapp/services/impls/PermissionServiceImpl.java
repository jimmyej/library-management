package com.libraryapp.services.impls;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.libraryapp.dtos.requests.PermissionRequest;
import com.libraryapp.entities.Permission;
import com.libraryapp.enums.CommonConstants;
import com.libraryapp.repositories.PermissionRepository;
import com.libraryapp.services.PermissionService;

@SuppressWarnings("unused")
@Service
public class PermissionServiceImpl implements PermissionService {

    PermissionRepository permissionRepository;

    @Autowired
    PermissionServiceImpl(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public Permission getPermissionByName(String permissionName) {
        Optional<Permission> permission = permissionRepository.findByPermissionName(permissionName);
        return permission.orElse(null);
    }

    public Permission editPermission(PermissionRequest permission) {

        boolean existsById = permissionRepository.existsById(permission.getPermissionId());
        if (existsById) {
            Optional<Permission> existingPermission = permissionRepository.findById(permission.getPermissionId());
            if (existingPermission.isPresent()) {
                return permissionRepository.save(buildPermissionRequest(permission, permission.getPermissionId()));
            }
            return null;
        }
        return null;
    }

    public Permission registerPermission(PermissionRequest permission) {
        boolean existsByName = permissionRepository.existsByPermissionName(permission.getPermissionName());
        if (!existsByName) {
            return permissionRepository.save(buildPermissionRequest(permission, 0));
        }
        return null;
    }

    public Boolean removePermissionById(int permissionId) {

        boolean isDeleted = false;
        try {
            boolean existById = permissionRepository.existsById(permissionId);
            if (existById) {
                Optional<Permission> permission = permissionRepository.findById(permissionId);
                if (permission.isPresent()) {
                    permission.get().setActivated(false);
                    permissionRepository.save(permission.get());
                    isDeleted = true;
                }
            }
        } catch (NullPointerException e) {
            e.getCause();
        }
        return isDeleted;
    }

    public List<Permission> getPermissionsByIsActivated(String activated) {
        if (activated != null) {
            boolean status = activated.equals(CommonConstants.ACTIVATED.name());
            return permissionRepository.findByActivated(status);
        } else {
            return (List<Permission>) permissionRepository.findAll();
        }
    }

    private Permission buildPermissionRequest(PermissionRequest permission, int id) {
        Permission newPermission = new Permission();
        if (id > 0) {
            newPermission.setPermissionId(id);
        }
        newPermission.setPermissionName(permission.getPermissionName());
        newPermission.setDescription(permission.getDescription());
        newPermission.setActivated(permission.getActivated());
        return newPermission;
    }

}
