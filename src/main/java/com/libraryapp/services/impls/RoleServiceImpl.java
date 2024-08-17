package com.libraryapp.services.impls;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.libraryapp.dtos.requests.RoleRequest;
import com.libraryapp.entities.Role;
import com.libraryapp.enums.CommonConstants;
import com.libraryapp.repositories.RoleRepository;
import com.libraryapp.services.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

    RoleRepository roleRepository;

    @Autowired
    RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<Role> getRolesName(String roleName) {

        return roleRepository.findByRoleName(roleName);
    }

    @Override
    public Role registerRole(RoleRequest role) {
        boolean existByRoleName = roleRepository.existsByRoleName(role.getRoleName());
        if (!existByRoleName) {
            return roleRepository.save(buildRoleRequest(role, 0));
        }
        return null;
    }

    private Role buildRoleRequest(RoleRequest roleRequest, int id) {
        Role newRole = new Role();
        if (id > 0) {
            newRole.setId(id);
        }
        newRole.setRoleName(roleRequest.getRoleName());
        newRole.setActivated(roleRequest.getActivated());
        return newRole;
    }

    @Override
    public Role editRole(RoleRequest role) {

        boolean existById = roleRepository.existsById(role.getRoleId());
        if (existById) {
            return roleRepository.save(buildRoleRequest(role, role.getRoleId()));
        }

        return null;
    }

    @Override
    public Boolean removeRoleById(int roleId) {
        boolean isDeleted = false;

        try {

            boolean existsById = roleRepository.existsById(roleId);
            if (existsById) {
                Optional<Role> role = roleRepository.findById(roleId);
                if (role.isPresent()) {
                    role.get().setActivated(false);
                    roleRepository.save(role.get());
                    isDeleted = true;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return isDeleted;
    }

    @Override
    public List<Role> getRolesByIsActivated(String activated) {

        if (activated != null) {
            boolean status = activated.equals(CommonConstants.ACTIVATED.name());
            return roleRepository.findByActivated(status);

        } else {
            return (List<Role>) roleRepository.findAll();
        }

    }

}
