package com.cmayen.almacen.core.controllers;

import com.cmayen.almacen.core.models.entity.TelefonoProveedor;
import com.cmayen.almacen.core.models.services.ITelefonoProveedorService;
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
@Api(tags = "telefonoproveedores")
public class TelefonoProveedorRestController {

    private final ITelefonoProveedorService telefonoProveedorService;

    public TelefonoProveedorRestController(ITelefonoProveedorService telefonoProveedorService){
        this.telefonoProveedorService = telefonoProveedorService;
    }

    @ApiOperation(value = "Listar Telefono Proveedores", notes = "Servicio para listar los telefono Proveedores")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Lista de telefono Proveedores")})
    @GetMapping("/telefonoproveedors")
    public List<TelefonoProveedor> index() {
        return this.telefonoProveedorService.findAll();
    }

    @ApiOperation(value = "Paginar listado de Telefono Proveedores", notes = "Servicio para listar los telefono Proveedores paginados")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Lista de telefono Proveedores paginados")})
    @GetMapping("/telefonoproveedores/page/{page}")
    public Page<TelefonoProveedor> index(@PathVariable Integer page){
        Pageable pageable = PageRequest.of(page, 5);
        return telefonoProveedorService.findAll(pageable);
    }

    @ApiOperation(value = "Buscar telefono Proveedor por Id", notes = "Servicio para buscar telefono Proveedor por codigo")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Telefono Proveedor encontrado"),
            @ApiResponse(code = 404, message = "Telefono Proveedor no encontrado")})
    @GetMapping("/telefonoproveedores/{id}")
    public ResponseEntity<?> show(@PathVariable Long id){
        Map<String, Object> response = new HashMap<>();
        TelefonoProveedor telefonoProveedorEncontrada = this.telefonoProveedorService.findById(id);
        if (telefonoProveedorEncontrada == null){
            response.put("mensaje", "Telefono Proveedor no encontrado");
            return  new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        return  new ResponseEntity<TelefonoProveedor>(telefonoProveedorEncontrada, HttpStatus.OK);
    }

    @ApiOperation(value = "Crear Telefono Proveedore", notes = "Servicio para crear un telefono Proveedore")
    @ApiResponses(value = {@ApiResponse(code = 201, message = "Telefono Proveedor creado"),
            @ApiResponse(code = 400, message = "Existen errores en el ingreso de datos"),
            @ApiResponse(code = 500, message = "Error en el servidor al crear telefono Proveedore")})
    @PostMapping("/telefonoproveedores")
    public ResponseEntity<?> create (@Valid @RequestBody TelefonoProveedor elemento, BindingResult result){
        TelefonoProveedor nuevo = null;
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
            nuevo = this.telefonoProveedorService.save(elemento);
        }catch (DataAccessException e){
            response.put("mensaje", "Error al realizar el insert en la base de datos");
            response.put("error", e.getMessage().concat(": ".concat(e.getMostSpecificCause().getMessage())));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje","El telefono Proveedor ha sido creado con éxito");
        response.put("telefonoproveedor", nuevo);
        return  new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @PutMapping("/telefonoproveedores/{id}")
    public  ResponseEntity<?> update (@Valid @RequestBody TelefonoProveedor telefonoProveedor, BindingResult result, @PathVariable Long id){

        Map<String, Object> response = new HashMap<>();
        TelefonoProveedor update = this.telefonoProveedorService.findById(id);
        TelefonoProveedor telefonoProveedorUpdate = null;

        if (result.hasErrors()){
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(err -> err.getDefaultMessage())
                    .collect(Collectors.toList());
            response.put("errors", errors);
            return  new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        if (update == null){
            response.put("mensaje", "Error: no se puede editar el telefono Proveedor ID "
                    + id.toString()
                    + " no existe en la base de datos");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        try {
            update.setNumero(telefonoProveedor.getNumero());
            update.setDescripcion(telefonoProveedor.getDescripcion());
            update.setProveedor(telefonoProveedor.getProveedor());
            telefonoProveedorUpdate = this.telefonoProveedorService.save(update);
        }catch (DataAccessException e){
            response.put("mensaje", "Error al actualizar los datos");
            response.put("error", e.getMessage().concat(": ".concat(e.getMostSpecificCause().getMessage())));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje","El telefono Proveedor ha sido actualizado correctamente!!!");
        response.put("telefonoproveedor", telefonoProveedorUpdate);
        return  new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("telefonoproveedores/{id}")
    public ResponseEntity<?> delete (@PathVariable Long id){
        Map<String, Object> response = new HashMap<>();
        try{
            this.telefonoProveedorService.delete(id);
        }catch (DataAccessException e){
            response.put("mensaje", "Error al eliminar el telefono Proveedor en la base datos");
            response.put("error", e.getMessage().concat(": ".concat(e.getMostSpecificCause().getMessage())));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje","El telefono Proveedor ha sido eliminado correctamente!!!");
        return  new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }
}