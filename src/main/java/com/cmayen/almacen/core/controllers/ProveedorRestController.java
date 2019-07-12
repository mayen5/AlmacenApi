package com.cmayen.almacen.core.controllers;

import com.cmayen.almacen.core.models.entity.Proveedor;
import com.cmayen.almacen.core.models.services.IProveedorService;
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
@Api(tags = "proveedores")
public class ProveedorRestController {

    private final IProveedorService proveedorService;

    public ProveedorRestController(IProveedorService proveedorService){
        this.proveedorService = proveedorService;
    }

    @ApiOperation(value = "Listar Proveedores", notes = "Servicio para listar los proveedores")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Lista de proveedores")})
    @GetMapping("/proveedores")
    public List<Proveedor> index() {
        return this.proveedorService.findAll();
    }

    @ApiOperation(value = "Paginar listado de Proveedores", notes = "Servicio para listar los proveedores paginados")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Lista de proveedores paginados")})
    @GetMapping("/proveedores/page/{page}")
    public Page<Proveedor> index(@PathVariable Integer page){
        Pageable pageable = PageRequest.of(page, 5);
        return proveedorService.findAll(pageable);
    }

    @ApiOperation(value = "Buscar proveedor por Id", notes = "Servicio para buscar proveedor por codigo")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Proveedor encontrado"),
            @ApiResponse(code = 404, message = "Proveedor no encontrado")})
    @GetMapping("/proveedores/{id}")
    public ResponseEntity<?> show(@PathVariable Long id){
        Map<String, Object> response = new HashMap<>();
        Proveedor proveedorEncontrado = this.proveedorService.findById(id);
        if (proveedorEncontrado == null){
            response.put("mensaje", "Proveedor no encontrado");
            return  new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        return  new ResponseEntity<Proveedor>(proveedorEncontrado, HttpStatus.OK);
    }

    @ApiOperation(value = "Crear Proveedor", notes = "Servicio para crear un proveedor")
    @ApiResponses(value = {@ApiResponse(code = 201, message = "Proveedor creado"),
            @ApiResponse(code = 400, message = "Existen errores en el ingreso de datos"),
            @ApiResponse(code = 500, message = "Error en el servidor al crear proveedor")})
    @PostMapping("/proveedores")
    public ResponseEntity<?> create (@Valid @RequestBody Proveedor elemento, BindingResult result){
        Proveedor nuevo = null;
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
            nuevo = this.proveedorService.save(elemento);
        }catch (DataAccessException e){
            response.put("mensaje", "Error al realizar el insert en la base de datos");
            response.put("error", e.getMessage().concat(": ".concat(e.getMostSpecificCause().getMessage())));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje","El proveedor ha sido creado con éxito");
        response.put("proveedor", nuevo);
        return  new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @PutMapping("/proveedores/{id}")
    public  ResponseEntity<?> update (@Valid @RequestBody Proveedor proveedor, BindingResult result, @PathVariable Long id){

        Map<String, Object> response = new HashMap<>();
        Proveedor update = this.proveedorService.findById(id);
        Proveedor proveedorUpdate = null;

        if (result.hasErrors()){
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(err -> err.getDefaultMessage())
                    .collect(Collectors.toList());
            response.put("errors", errors);
            return  new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        if (update == null){
            response.put("mensaje", "Error: no se puede editar el proveedor ID "
                    + id.toString()
                    + " no existe en la base de datos");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        try {
            update.setNit(proveedor.getNit());
            update.setRazonSocial(proveedor.getRazonSocial());
            update.setDireccion(proveedor.getDireccion());
            update.setPaginaWeb(proveedor.getPaginaWeb());
            update.setContactoPrincipal(proveedor.getContactoPrincipal());
            proveedorUpdate = this.proveedorService.save(proveedor);
        }catch (DataAccessException e){
            response.put("mensaje", "Error al actualizar los datos");
            response.put("error", e.getMessage().concat(": ".concat(e.getMostSpecificCause().getMessage())));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje","El proveedor ha sido actualizado correctamente!!!");
        response.put("proveedor", proveedorUpdate);
        return  new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("proveedores/{id}")
    public ResponseEntity<?> delete (@PathVariable Long id){
        Map<String, Object> response = new HashMap<>();
        try{
            this.proveedorService.delete(id);
        }catch (DataAccessException e){
            response.put("mensaje", "Error al eliminar el proveedor en la base datos");
            response.put("error", e.getMessage().concat(": ".concat(e.getMostSpecificCause().getMessage())));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje","El proveedor ha sido eliminado correctamente!!!");
        return  new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }
}