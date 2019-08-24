package com.cmayen.almacen.core.controllers;

import com.cmayen.almacen.core.models.entity.EmailCliente;
import com.cmayen.almacen.core.models.services.IEmailClienteService;
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
@Api(tags = "emailclientes")
@CrossOrigin(origins = {"*"})
public class EmailClienteRestController {

    private final IEmailClienteService emailClienteService;

    public EmailClienteRestController(IEmailClienteService emailClienteService){
        this.emailClienteService = emailClienteService;
    }

    @ApiOperation(value = "Listar Email Clientes", notes = "Servicio para listar los email Clientes")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Lista de email Clientes")})
    @GetMapping("/emailclientes")
    public List<EmailCliente> index() {
        return this.emailClienteService.findAll();
    }

    @ApiOperation(value = "Paginar listado de Email Clientes", notes = "Servicio para listar los email Clientes paginados")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Lista de email Clientes paginados")})
    @GetMapping("/emailclientes/page/{page}")
    public Page<EmailCliente> index(@PathVariable Integer page){
        Pageable pageable = PageRequest.of(page, 5);
        return emailClienteService.findAll(pageable);
    }

    @ApiOperation(value = "Buscar email Cliente por Id", notes = "Servicio para buscar email Cliente por codigo")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Email Cliente encontrado"),
            @ApiResponse(code = 404, message = "Email Cliente no encontrado")})
    @GetMapping("/emailclientes/{id}")
    public ResponseEntity<?> show(@PathVariable Long id){
        Map<String, Object> response = new HashMap<>();
        EmailCliente emailClienteEncontrada = this.emailClienteService.findById(id);
        if (emailClienteEncontrada == null){
            response.put("mensaje", "Email Cliente no encontrado");
            return  new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        return  new ResponseEntity<EmailCliente>(emailClienteEncontrada, HttpStatus.OK);
    }

    @ApiOperation(value = "Crear Email Cliente", notes = "Servicio para crear un email Cliente")
    @ApiResponses(value = {@ApiResponse(code = 201, message = "Email Cliente creado"),
            @ApiResponse(code = 400, message = "Existen errores en el ingreso de datos"),
            @ApiResponse(code = 500, message = "Error en el servidor al crear email Cliente")})
    @PostMapping("/emailclientes")
    public ResponseEntity<?> create (@Valid @RequestBody EmailCliente elemento, BindingResult result){
        EmailCliente nuevo = null;
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
            nuevo = this.emailClienteService.save(elemento);
        }catch (DataAccessException e){
            response.put("mensaje", "Error al realizar el insert en la base de datos");
            response.put("error", e.getMessage().concat(": ".concat(e.getMostSpecificCause().getMessage())));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje","El email Cliente ha sido creado con éxito");
        response.put("emailcliente", nuevo);
        return  new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @PutMapping("/emailclientes/{id}")
    public  ResponseEntity<?> update (@Valid @RequestBody EmailCliente emailCliente, BindingResult result, @PathVariable Long id){

        Map<String, Object> response = new HashMap<>();
        EmailCliente update = this.emailClienteService.findById(id);
        EmailCliente emailClienteUpdate = null;

        if (result.hasErrors()){
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(err -> err.getDefaultMessage())
                    .collect(Collectors.toList());
            response.put("errors", errors);
            return  new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        if (update == null){
            response.put("mensaje", "Error: no se puede editar el email Cliente ID "
                    + id.toString()
                    + " no existe en la base de datos");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        try {
            update.setEmail(emailCliente.getEmail());
            update.setCliente(emailCliente.getCliente());
            emailClienteUpdate = this.emailClienteService.save(update);
        }catch (DataAccessException e){
            response.put("mensaje", "Error al actualizar los datos");
            response.put("error", e.getMessage().concat(": ".concat(e.getMostSpecificCause().getMessage())));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje","El email Cliente ha sido actualizado correctamente!!!");
        response.put("emailcliente", emailClienteUpdate);
        return  new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("emailclientes/{id}")
    public ResponseEntity<?> delete (@PathVariable Long id){
        Map<String, Object> response = new HashMap<>();
        try{
            this.emailClienteService.delete(id);
        }catch (DataAccessException e){
            response.put("mensaje", "Error al eliminar el email Cliente en la base datos");
            response.put("error", e.getMessage().concat(": ".concat(e.getMostSpecificCause().getMessage())));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje","El email Cliente ha sido eliminado correctamente!!!");
        return  new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }
}
