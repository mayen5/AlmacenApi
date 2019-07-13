package com.cmayen.almacen.core.controllers;

import com.cmayen.almacen.core.models.entity.Factura;
import com.cmayen.almacen.core.models.services.IFacturaService;
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
@Api(tags = "facturas")
public class FacturaRestController {

    private final IFacturaService facturaService;

    public FacturaRestController(IFacturaService facturaService){
        this.facturaService = facturaService;
    }

    @ApiOperation(value = "Listar Facturas", notes = "Servicio para listar las facturas")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Lista de facturas")})
    @GetMapping("/facturas")
    public List<Factura> index() {
        return this.facturaService.findAll();
    }

    @ApiOperation(value = "Paginar listado de Facturas", notes = "Servicio para listar las facturas paginadas")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Lista de facturas paginadas")})
    @GetMapping("/facturas/page/{page}")
    public Page<Factura> index(@PathVariable Integer page){
        Pageable pageable = PageRequest.of(page, 5);
        return facturaService.findAll(pageable);
    }

    @ApiOperation(value = "Buscar factura por Id", notes = "Servicio para buscar factura por codigo")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Factura encontrada"),
            @ApiResponse(code = 404, message = "Factura no encontrada")})
    @GetMapping("/facturas/{id}")
    public ResponseEntity<?> show(@PathVariable Long id){
        Map<String, Object> response = new HashMap<>();
        Factura facturaEncontrada = this.facturaService.findById(id);
        if (facturaEncontrada == null){
            response.put("mensaje", "Factura no encontrada");
            return  new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        return  new ResponseEntity<Factura>(facturaEncontrada, HttpStatus.OK);
    }

    @ApiOperation(value = "Crear Factura", notes = "Servicio para crear una factura")
    @ApiResponses(value = {@ApiResponse(code = 201, message = "Factura creada"),
            @ApiResponse(code = 400, message = "Existen errores en el ingreso de datos"),
            @ApiResponse(code = 500, message = "Error en el servidor al crear factura")})
    @PostMapping("/facturas")
    public ResponseEntity<?> create (@Valid @RequestBody Factura elemento, BindingResult result){
        Factura nuevo = null;
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
            nuevo = this.facturaService.save(elemento);
        }catch (DataAccessException e){
            response.put("mensaje", "Error al realizar el insert en la base de datos");
            response.put("error", e.getMessage().concat(": ".concat(e.getMostSpecificCause().getMessage())));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje","La factura ha sido creada con éxito");
        response.put("factura", nuevo);
        return  new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @PutMapping("/facturas/{id}")
    public  ResponseEntity<?> update (@Valid @RequestBody Factura factura, BindingResult result, @PathVariable Long id){

        Map<String, Object> response = new HashMap<>();
        Factura update = this.facturaService.findById(id);
        Factura facturaUpdate = null;

        if (result.hasErrors()){
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(err -> err.getDefaultMessage())
                    .collect(Collectors.toList());
            response.put("errors", errors);
            return  new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        if (update == null){
            response.put("mensaje", "Error: no se puede editar la factura ID "
                    + id.toString()
                    + " no existe en la base de datos");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        try {
            update.setCliente(factura.getCliente());
            update.setFecha(factura.getFecha());
            update.setTotal(factura.getTotal());
            facturaUpdate = this.facturaService.save(factura);
        }catch (DataAccessException e){
            response.put("mensaje", "Error al actualizar los datos");
            response.put("error", e.getMessage().concat(": ".concat(e.getMostSpecificCause().getMessage())));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje","La factura ha sido actualizada correctamente!!!");
        response.put("factura", facturaUpdate);
        return  new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("facturas/{id}")
    public ResponseEntity<?> delete (@PathVariable Long id){
        Map<String, Object> response = new HashMap<>();
        try{
            this.facturaService.delete(id);
        }catch (DataAccessException e){
            response.put("mensaje", "Error al eliminar la factura en la base datos");
            response.put("error", e.getMessage().concat(": ".concat(e.getMostSpecificCause().getMessage())));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje","La factura ha sido eliminada correctamente!!!");
        return  new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }
}
