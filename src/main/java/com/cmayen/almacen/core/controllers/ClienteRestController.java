package com.cmayen.almacen.core.controllers;

import com.cmayen.almacen.core.models.entity.Cliente;
import com.cmayen.almacen.core.models.services.IClienteService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ClienteRestController {
    private final IClienteService clienteService;

    public ClienteRestController(IClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping("/clientes")
    public List<Cliente> index(){
        return this.clienteService.findAll();
    }
}
