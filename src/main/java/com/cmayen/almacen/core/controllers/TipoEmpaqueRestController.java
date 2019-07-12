package com.cmayen.almacen.core.controllers;


import com.cmayen.almacen.core.models.entity.TipoEmpaque;
import com.cmayen.almacen.core.models.services.ITipoEmpaqueService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
@Api(tags = "tiposempaque")
public class TipoEmpaqueRestController {

    private final ITipoEmpaqueService tipoEmpaqueService;

    public TipoEmpaqueRestController(ITipoEmpaqueService tipoEmpaqueService){
        this.tipoEmpaqueService = tipoEmpaqueService;
    }

    @ApiOperation(value = "Listar Tipos Empaques", notes = "Servicio para listar los tipos de empaques")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Lista de tipos de empaques")})
    @GetMapping("tiposempaque")
    public List<TipoEmpaque> index() {
        return this.tipoEmpaqueService.findAll();
    }

    @ApiOperation(value = "Paginar listado de Tipos de Empaques", notes = "Servicio para listar los tipos de Empaques paginados")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Lista de tipos de Empaques paginados")})
    @GetMapping("tiposempaque/page/{page}")
    public Page<TipoEmpaque> index(@PathVariable Integer page){
        Pageable pageable = PageRequest.of(page, 5);
        return tipoEmpaqueService.findAll(pageable);
    }

    @ApiOperation(value = "Buscar tipos de Empaque por Id", notes = "Servicio para buscar tipos de Empaque por codigo")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Tipo de Empaque encontrado"),
            @ApiResponse(code = 404, message = "Tipo de Empaque no encontrado")})
    @GetMapping("tiposempaque/{id}")
    public ResponseEntity<?> show(@PathVariable Long id){
        Map<String, Object> response = new HashMap<>();
        TipoEmpaque tipoEmpaqueEncontrada = this.tipoEmpaqueService.findById(id);
        if (tipoEmpaqueEncontrada == null){
            response.put("mensaje", "Tipo de Empaque no encontrado");
            return  new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        return  new ResponseEntity<TipoEmpaque>(tipoEmpaqueEncontrada, HttpStatus.OK);
    }

    @ApiOperation(value = "Crear Tipo de Empaque", notes = "Servicio para crear un tipo de Empaque")
    @ApiResponses(value = {@ApiResponse(code = 201, message = "Tipo de Empaque creado"),
            @ApiResponse(code = 400, message = "Existen errores en el ingreso de datos"),
            @ApiResponse(code = 500, message = "Error en el servidor al crear tipoEmpaque")})
    @PostMapping("tiposempaque")
    public ResponseEntity<?> create (@Valid @RequestBody TipoEmpaque elemento, BindingResult result){
        TipoEmpaque nuevo = null;
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
            nuevo = this.tipoEmpaqueService.save(elemento);
        }catch (DataAccessException e){
            response.put("mensaje", "Error al realizar el insert en la base de datos");
            response.put("error", e.getMessage().concat(": ".concat(e.getMostSpecificCause().getMessage())));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje","La tipoEmpaque ha sido creada con éxito");
        response.put("tipoEmpaque", nuevo);
        return  new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @PutMapping("tiposempaque/{id}")
    public  ResponseEntity<?> update (@Valid @RequestBody TipoEmpaque tipoEmpaque, BindingResult result, @PathVariable Long id){

        Map<String, Object> response = new HashMap<>();
        TipoEmpaque update = this.tipoEmpaqueService.findById(id);
        TipoEmpaque tipoEmpaqueUpdate = null;

        if (result.hasErrors()){
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(err -> err.getDefaultMessage())
                    .collect(Collectors.toList());
            response.put("errors", errors);
            return  new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        if (update == null){
            response.put("mensaje", "Error: no se puede editar el tipo de Empaque ID "
                    + id.toString()
                    + " no existe en la base de datos");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        try {
            update.setDescripcion(tipoEmpaque.getDescripcion());
            tipoEmpaqueUpdate = this.tipoEmpaqueService.save(tipoEmpaque);
        }catch (DataAccessException e){
            response.put("mensaje", "Error al actualizar los datos");
            response.put("error", e.getMessage().concat(": ".concat(e.getMostSpecificCause().getMessage())));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje","El tipo de Empaque ha sido actualizado correctamente!!!");
        response.put("tipoEmpaque", tipoEmpaqueUpdate);
        return  new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("tipoEmpaques/{id}")
    public ResponseEntity<?> delete (@PathVariable Long id){
        Map<String, Object> response = new HashMap<>();
        try{
            this.tipoEmpaqueService.delete(id);
        }catch (DataAccessException e){
            response.put("mensaje", "Error al eliminar el tipo de Empaque en la base datos");
            response.put("error", e.getMessage().concat(": ".concat(e.getMostSpecificCause().getMessage())));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje","El tipo de Empaque ha sido eliminado correctamente!!!");
        return  new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }
}