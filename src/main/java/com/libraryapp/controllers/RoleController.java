package com.libraryapp.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.libraryapp.dtos.requests.RoleRequest;
import com.libraryapp.entities.Role;
import com.libraryapp.services.RoleService;

@RestController
@RequestMapping("/api/v1/roles")
public class RoleController {

    RoleService roleService;

    @Autowired
    RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("/roleName/{roleName}")
    ResponseEntity<List<Role>> getRolesName(@PathVariable String roleName) {

        List<Role> roles = roleService.getRolesName(roleName);
        if (roles.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(roles);
    }

    @PostMapping("")
    ResponseEntity<Role> registerRole(@RequestBody RoleRequest role) {

        Role newRole = roleService.registerRole(role);
        if (newRole != null) {
            return new ResponseEntity<>(newRole, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.FOUND);

    }

    @PutMapping("")
    ResponseEntity<Role> editRole(@RequestBody RoleRequest role) {

        Role newRole = roleService.editRole(role);
        if (newRole == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(newRole);
    }

    @DeleteMapping("/ids/{idRole}")
    ResponseEntity<Boolean> removeRoleById(int idRole) {

        boolean removed = roleService.removeRoleById(idRole);
        if (!removed) {
            return ResponseEntity.notFound().build();

        }
        return ResponseEntity.noContent().build();
    }

}
