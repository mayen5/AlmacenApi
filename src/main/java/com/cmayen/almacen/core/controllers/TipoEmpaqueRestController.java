package com.cmayen.almacen.core.controllers;

import com.cmayen.almacen.core.models.entity.TipoEmpaque;
import com.cmayen.almacen.core.models.services.ITipoEmpaqueService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class TipoEmpaqueRestController {

    private final ITipoEmpaqueService tipoEmpaqueService;

    public TipoEmpaqueRestController(ITipoEmpaqueService tipoEmpaqueService) {
        this.tipoEmpaqueService = tipoEmpaqueService;
    }

    @GetMapping("/tiposempaque")
    public List<TipoEmpaque> index(){
        return this.tipoEmpaqueService.findAll();
    }
}
