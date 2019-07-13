package com.cmayen.almacen.core.controllers;

import com.cmayen.almacen.core.models.entity.EmailProveedor;
import com.cmayen.almacen.core.models.services.IEmailProveedorService;
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
@Api(tags = "emailproveedores")
public class EmailProveedorRestController {

    private final IEmailProveedorService emailProveedorService;

    public EmailProveedorRestController(IEmailProveedorService emailProveedorService){
        this.emailProveedorService = emailProveedorService;
    }

    @ApiOperation(value = "Listar Email Proveedores", notes = "Servicio para listar los email Proveedores")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Lista de email Proveedores")})
    @GetMapping("/emailproveedors")
    public List<EmailProveedor> index() {
        return this.emailProveedorService.findAll();
    }

    @ApiOperation(value = "Paginar listado de Email Proveedores", notes = "Servicio para listar los email Proveedores paginados")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Lista de email Proveedores paginados")})
    @GetMapping("/emailproveedores/page/{page}")
    public Page<EmailProveedor> index(@PathVariable Integer page){
        Pageable pageable = PageRequest.of(page, 5);
        return emailProveedorService.findAll(pageable);
    }

    @ApiOperation(value = "Buscar email Proveedor por Id", notes = "Servicio para buscar email Proveedor por codigo")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Email Proveedor encontrado"),
            @ApiResponse(code = 404, message = "Email Proveedor no encontrado")})
    @GetMapping("/emailproveedores/{id}")
    public ResponseEntity<?> show(@PathVariable Long id){
        Map<String, Object> response = new HashMap<>();
        EmailProveedor emailProveedorEncontrada = this.emailProveedorService.findById(id);
        if (emailProveedorEncontrada == null){
            response.put("mensaje", "Email Proveedor no encontrado");
            return  new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        return  new ResponseEntity<EmailProveedor>(emailProveedorEncontrada, HttpStatus.OK);
    }

    @ApiOperation(value = "Crear Email Proveedore", notes = "Servicio para crear un email Proveedore")
    @ApiResponses(value = {@ApiResponse(code = 201, message = "Email Proveedor creado"),
            @ApiResponse(code = 400, message = "Existen errores en el ingreso de datos"),
            @ApiResponse(code = 500, message = "Error en el servidor al crear email Proveedore")})
    @PostMapping("/emailproveedores")
    public ResponseEntity<?> create (@Valid @RequestBody EmailProveedor elemento, BindingResult result){
        EmailProveedor nuevo = null;
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
            nuevo = this.emailProveedorService.save(elemento);
        }catch (DataAccessException e){
            response.put("mensaje", "Error al realizar el insert en la base de datos");
            response.put("error", e.getMessage().concat(": ".concat(e.getMostSpecificCause().getMessage())));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje","El email Proveedor ha sido creado con éxito");
        response.put("emailproveedor", nuevo);
        return  new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @PutMapping("/emailproveedores/{id}")
    public  ResponseEntity<?> update (@Valid @RequestBody EmailProveedor emailProveedor, BindingResult result, @PathVariable Long id){

        Map<String, Object> response = new HashMap<>();
        EmailProveedor update = this.emailProveedorService.findById(id);
        EmailProveedor emailProveedorUpdate = null;

        if (result.hasErrors()){
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(err -> err.getDefaultMessage())
                    .collect(Collectors.toList());
            response.put("errors", errors);
            return  new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        if (update == null){
            response.put("mensaje", "Error: no se puede editar el email Proveedor ID "
                    + id.toString()
                    + " no existe en la base de datos");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        try {
            update.setEmail(emailProveedor.getEmail());
            update.setProveedor(emailProveedor.getProveedor());
            emailProveedorUpdate = this.emailProveedorService.save(emailProveedor);
        }catch (DataAccessException e){
            response.put("mensaje", "Error al actualizar los datos");
            response.put("error", e.getMessage().concat(": ".concat(e.getMostSpecificCause().getMessage())));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje","El email Proveedor ha sido actualizado correctamente!!!");
        response.put("emailproveedor", emailProveedorUpdate);
        return  new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("emailproveedores/{id}")
    public ResponseEntity<?> delete (@PathVariable Long id){
        Map<String, Object> response = new HashMap<>();
        try{
            this.emailProveedorService.delete(id);
        }catch (DataAccessException e){
            response.put("mensaje", "Error al eliminar el email Proveedor en la base datos");
            response.put("error", e.getMessage().concat(": ".concat(e.getMostSpecificCause().getMessage())));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje","El email Proveedor ha sido eliminado correctamente!!!");
        return  new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }
}