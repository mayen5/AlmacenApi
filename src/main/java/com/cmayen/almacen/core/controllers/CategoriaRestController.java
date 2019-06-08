package com.cmayen.almacen.core.controllers;

import com.cmayen.almacen.core.models.entity.Categoria;
import com.cmayen.almacen.core.models.services.ICategoriaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class CategoriaRestController {

    private final ICategoriaService categoriaService;

    public CategoriaRestController(ICategoriaService categoriaService){
        this.categoriaService = categoriaService;
    }

    @GetMapping("/categorias")
    public List<Categoria> index() {
        return this.categoriaService.findAll();
    }

    @GetMapping("/categorias/page/{page}")
    public Page<Categoria> index(@PathVariable Integer page){
        Pageable pageable = PageRequest.of(page, 5);
        return categoriaService.findAll(pageable);
    }
}
