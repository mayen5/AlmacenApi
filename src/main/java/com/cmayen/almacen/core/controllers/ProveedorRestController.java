package com.cmayen.almacen.core.controllers;

import com.cmayen.almacen.core.models.entity.Proveedor;
import com.cmayen.almacen.core.models.services.IProveedorService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ProveedorRestController {
    private final IProveedorService proveedorService;

    public ProveedorRestController(IProveedorService proveedorService) {
        this.proveedorService = proveedorService;
    }

    @GetMapping("/proveedores")
    public List<Proveedor> index(){
        return this.proveedorService.findAll();
    }
}

