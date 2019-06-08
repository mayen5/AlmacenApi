package com.cmayen.almacen.core.controllers;

import com.cmayen.almacen.core.models.entity.Categoria;
import com.cmayen.almacen.core.models.services.ICategoriaService;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @GetMapping("/categorias/{id}")
    public ResponseEntity<?> show(@PathVariable Long id){
        Map<String, Object> response = new HashMap<>();
        Categoria categoriaEncontrada = this.categoriaService.findById(id);
        if (categoriaEncontrada == null){
            response.put("mensaje", "Categoria no encontrada");
            return  new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        return  new ResponseEntity<Categoria>(categoriaEncontrada, HttpStatus.OK);
    }


    @PostMapping("/categorias")
    public ResponseEntity<?> create (@Valid @RequestBody Categoria elemento, BindingResult result){
        Categoria nuevo = null;
        Map<String, Object> response = new HashMap<>();
        if (result.hasErrors()){
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(err -> err.getDefaultMessage())
                    .collect(Collectors.toList());
            response.put("errors", errors);
            return  new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            nuevo = this.categoriaService.save(elemento);
        }catch (DataAccessException e){
            response.put("mensaje", "Error al realizar el insert en la base de datos");
            response.put("error", e.getMessage().concat(": ".concat(e.getMostSpecificCause().getMessage())));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje","La categoria ha sido creada con éxito");
        response.put("categoria", nuevo);
        return  new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }
    
}
