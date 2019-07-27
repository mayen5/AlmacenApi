package com.cmayen.almacen.core.controllers;

import com.cmayen.almacen.core.models.entity.Inventario;
import com.cmayen.almacen.core.models.services.IInventarioService;
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
@Api(tags = "inventarios")
public class InventarioRestController {

    private final IInventarioService inventarioService;

    public InventarioRestController(IInventarioService inventarioService){
        this.inventarioService = inventarioService;
    }

    @ApiOperation(value = "Listar Inventarios", notes = "Servicio para listar el inventarios")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Lista de inventarios")})
    @GetMapping("/inventarios")
    public List<Inventario> index() {
        return this.inventarioService.findAll();
    }

    @ApiOperation(value = "Paginar listado de Inventarios", notes = "Servicio para listar el inventarios paginadas")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Lista de inventarios paginadas")})
    @GetMapping("/inventarios/page/{page}")
    public Page<Inventario> index(@PathVariable Integer page){
        Pageable pageable = PageRequest.of(page, 5);
        return inventarioService.findAll(pageable);
    }

    @ApiOperation(value = "Buscar inventario por Id", notes = "Servicio para buscar inventario por codigo")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Inventario encontrado"),
            @ApiResponse(code = 404, message = "Inventario no encontrado")})
    @GetMapping("/inventarios/{id}")
    public ResponseEntity<?> show(@PathVariable Long id){
        Map<String, Object> response = new HashMap<>();
        Inventario inventarioEncontrada = this.inventarioService.findById(id);
        if (inventarioEncontrada == null){
            response.put("mensaje", "Inventario no encontrado");
            return  new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        return  new ResponseEntity<Inventario>(inventarioEncontrada, HttpStatus.OK);
    }

    @ApiOperation(value = "Crear Inventario", notes = "Servicio para crear una inventario")
    @ApiResponses(value = {@ApiResponse(code = 201, message = "Inventario creada"),
            @ApiResponse(code = 400, message = "Existen errores en el ingreso de datos"),
            @ApiResponse(code = 500, message = "Error en el servidor al crear inventario")})
    @PostMapping("/inventarios")
    public ResponseEntity<?> create (@Valid @RequestBody Inventario elemento, BindingResult result){
        Inventario nuevo = null;
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
            nuevo = this.inventarioService.save(elemento);
        }catch (DataAccessException e){
            response.put("mensaje", "Error al realizar el insert en la base de datos");
            response.put("error", e.getMessage().concat(": ".concat(e.getMostSpecificCause().getMessage())));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje","La inventario ha sido creada con éxito");
        response.put("inventario", nuevo);
        return  new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @PutMapping("/inventarios/{id}")
    public  ResponseEntity<?> update (@Valid @RequestBody Inventario inventario, BindingResult result, @PathVariable Long id){

        Map<String, Object> response = new HashMap<>();
        Inventario update = this.inventarioService.findById(id);
        Inventario inventarioUpdate = null;

        if (result.hasErrors()){
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(err -> err.getDefaultMessage())
                    .collect(Collectors.toList());
            response.put("errors", errors);
            return  new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        if (update == null){
            response.put("mensaje", "Error: no se puede editar el inventario ID "
                    + id.toString()
                    + " no existe en la base de datos");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        try {
            update.setProducto(inventario.getProducto());
            update.setFecha(inventario.getFecha());
            update.setTipoRegistro(inventario.getTipoRegistro());
            update.setPrecio(inventario.getPrecio());
            update.setEntrada(inventario.getEntrada());
            update.setSalida(inventario.getSalida());
            inventarioUpdate = this.inventarioService.save(update);
        }catch (DataAccessException e){
            response.put("mensaje", "Error al actualizar los datos");
            response.put("error", e.getMessage().concat(": ".concat(e.getMostSpecificCause().getMessage())));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje","La inventario ha sido actualizado correctamente!!!");
        response.put("inventario", inventarioUpdate);
        return  new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("inventarios/{id}")
    public ResponseEntity<?> delete (@PathVariable Long id){
        Map<String, Object> response = new HashMap<>();
        try{
            this.inventarioService.delete(id);
        }catch (DataAccessException e){
            response.put("mensaje", "Error al eliminar el inventario en la base datos");
            response.put("error", e.getMessage().concat(": ".concat(e.getMostSpecificCause().getMessage())));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje","El inventario ha sido eliminado correctamente!!!");
        return  new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }
}
