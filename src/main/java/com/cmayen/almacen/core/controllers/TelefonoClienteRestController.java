package com.cmayen.almacen.core.controllers;

import com.cmayen.almacen.core.models.entity.TelefonoCliente;
import com.cmayen.almacen.core.models.services.ITelefonoClienteService;
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
@Api(tags = "telefonoclientes")
@CrossOrigin(origins = {"*"})
public class TelefonoClienteRestController {

    private final ITelefonoClienteService telefonoClienteService;

    public TelefonoClienteRestController(ITelefonoClienteService telefonoClienteService){
        this.telefonoClienteService = telefonoClienteService;
    }

    @ApiOperation(value = "Listar Telefono Clientes", notes = "Servicio para listar los telefono Clientes")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Lista de telefono Clientes")})
    @GetMapping("/telefonoclientes")
    public List<TelefonoCliente> index() {
        return this.telefonoClienteService.findAll();
    }

    @ApiOperation(value = "Paginar listado de Telefono Clientes", notes = "Servicio para listar los telefono Clientes paginados")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Lista de telefono Clientes paginados")})
    @GetMapping("/telefonoclientes/page/{page}")
    public Page<TelefonoCliente> index(@PathVariable Integer page){
        Pageable pageable = PageRequest.of(page, 5);
        return telefonoClienteService.findAll(pageable);
    }

    @ApiOperation(value = "Buscar telefono Cliente por Id", notes = "Servicio para buscar telefono Cliente por codigo")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Telefono Cliente encontrado"),
            @ApiResponse(code = 404, message = "Telefono Cliente no encontrado")})
    @GetMapping("/telefonoclientes/{id}")
    public ResponseEntity<?> show(@PathVariable Long id){
        Map<String, Object> response = new HashMap<>();
        TelefonoCliente telefonoClienteEncontrada = this.telefonoClienteService.findById(id);
        if (telefonoClienteEncontrada == null){
            response.put("mensaje", "Telefono Cliente no encontrado");
            return  new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        return  new ResponseEntity<TelefonoCliente>(telefonoClienteEncontrada, HttpStatus.OK);
    }

    @ApiOperation(value = "Crear Telefono Cliente", notes = "Servicio para crear un telefono Cliente")
    @ApiResponses(value = {@ApiResponse(code = 201, message = "Telefono Cliente creado"),
            @ApiResponse(code = 400, message = "Existen errores en el ingreso de datos"),
            @ApiResponse(code = 500, message = "Error en el servidor al crear telefono Cliente")})
    @PostMapping("/telefonoclientes")
    public ResponseEntity<?> create (@Valid @RequestBody TelefonoCliente elemento, BindingResult result){
        TelefonoCliente nuevo = null;
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
            nuevo = this.telefonoClienteService.save(elemento);
        }catch (DataAccessException e){
            response.put("mensaje", "Error al realizar el insert en la base de datos");
            response.put("error", e.getMessage().concat(": ".concat(e.getMostSpecificCause().getMessage())));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje","El telefono Cliente ha sido creado con éxito");
        response.put("telefonocliente", nuevo);
        return  new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @PutMapping("/telefonoclientes/{id}")
    public  ResponseEntity<?> update (@Valid @RequestBody TelefonoCliente telefonoCliente, BindingResult result, @PathVariable Long id){

        Map<String, Object> response = new HashMap<>();
        TelefonoCliente update = this.telefonoClienteService.findById(id);
        TelefonoCliente telefonoClienteUpdate = null;

        if (result.hasErrors()){
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(err -> err.getDefaultMessage())
                    .collect(Collectors.toList());
            response.put("errors", errors);
            return  new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        if (update == null){
            response.put("mensaje", "Error: no se puede editar el telefono Cliente ID "
                    + id.toString()
                    + " no existe en la base de datos");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        try {
            update.setNumero(telefonoCliente.getNumero());
            update.setDescripcion(telefonoCliente.getDescripcion());
            update.setCliente(telefonoCliente.getCliente());
            telefonoClienteUpdate = this.telefonoClienteService.save(update);
        }catch (DataAccessException e){
            response.put("mensaje", "Error al actualizar los datos");
            response.put("error", e.getMessage().concat(": ".concat(e.getMostSpecificCause().getMessage())));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje","El telefono Cliente ha sido actualizado correctamente!!!");
        response.put("telefonocliente", telefonoClienteUpdate);
        return  new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("telefonoclientes/{id}")
    public ResponseEntity<?> delete (@PathVariable Long id){
        Map<String, Object> response = new HashMap<>();
        try{
            this.telefonoClienteService.delete(id);
        }catch (DataAccessException e){
            response.put("mensaje", "Error al eliminar el telefono Cliente en la base datos");
            response.put("error", e.getMessage().concat(": ".concat(e.getMostSpecificCause().getMessage())));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje","El telefono Cliente ha sido eliminado correctamente!!!");
        return  new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }
}