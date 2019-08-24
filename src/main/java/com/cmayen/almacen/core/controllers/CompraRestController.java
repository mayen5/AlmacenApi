package com.cmayen.almacen.core.controllers;

import com.cmayen.almacen.core.models.entity.Compra;
import com.cmayen.almacen.core.models.services.ICompraService;
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
@Api(tags = "compras")
@CrossOrigin(origins = {"*"})
public class CompraRestController {

    private final ICompraService compraService;

    public CompraRestController(ICompraService compraService){
        this.compraService = compraService;
    }

    @ApiOperation(value = "Listar Compras", notes = "Servicio para listar las compras")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Lista de compras")})
    @GetMapping("/compras")
    public List<Compra> index() {
        return this.compraService.findAll();
    }

    @ApiOperation(value = "Paginar listado de Compras", notes = "Servicio para listar las compras paginadas")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Lista de compras paginadas")})
    @GetMapping("/compras/page/{page}")
    public Page<Compra> index(@PathVariable Integer page){
        Pageable pageable = PageRequest.of(page, 5);
        return compraService.findAll(pageable);
    }

    @ApiOperation(value = "Buscar compra por Id", notes = "Servicio para buscar compra por codigo")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Compra encontrada"),
            @ApiResponse(code = 404, message = "Compra no encontrada")})
    @GetMapping("/compras/{id}")
    public ResponseEntity<?> show(@PathVariable Long id){
        Map<String, Object> response = new HashMap<>();
        Compra compraEncontrada = this.compraService.findById(id);
        if (compraEncontrada == null){
            response.put("mensaje", "Compra no encontrada");
            return  new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        return  new ResponseEntity<Compra>(compraEncontrada, HttpStatus.OK);
    }

    @ApiOperation(value = "Crear Compra", notes = "Servicio para crear una compra")
    @ApiResponses(value = {@ApiResponse(code = 201, message = "Compra creada"),
            @ApiResponse(code = 400, message = "Existen errores en el ingreso de datos"),
            @ApiResponse(code = 500, message = "Error en el servidor al crear compra")})
    @PostMapping("/compras")
    public ResponseEntity<?> create (@Valid @RequestBody Compra elemento, BindingResult result){
        Compra nuevo = null;
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
            nuevo = this.compraService.save(elemento);
        }catch (DataAccessException e){
            response.put("mensaje", "Error al realizar el insert en la base de datos");
            response.put("error", e.getMessage().concat(": ".concat(e.getMostSpecificCause().getMessage())));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje","La compra ha sido creada con éxito");
        response.put("compra", nuevo);
        return  new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @PutMapping("/compras/{id}")
    public  ResponseEntity<?> update (@Valid @RequestBody Compra compra, BindingResult result, @PathVariable Long id){

        Map<String, Object> response = new HashMap<>();
        Compra update = this.compraService.findById(id);
        Compra compraUpdate = null;

        if (result.hasErrors()){
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(err -> err.getDefaultMessage())
                    .collect(Collectors.toList());
            response.put("errors", errors);
            return  new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        if (update == null){
            response.put("mensaje", "Error: no se puede editar la compra ID "
                    + id.toString()
                    + " no existe en la base de datos");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        try {
            update.setNumeroDocumento(compra.getNumeroDocumento());
            update.setProveedor(compra.getProveedor());
            update.setFecha(compra.getFecha());
            update.setTotal(compra.getTotal());
            compraUpdate = this.compraService.save(update);
        }catch (DataAccessException e){
            response.put("mensaje", "Error al actualizar los datos");
            response.put("error", e.getMessage().concat(": ".concat(e.getMostSpecificCause().getMessage())));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje","La compra ha sido actualizada correctamente!!!");
        response.put("compra", compraUpdate);
        return  new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("compras/{id}")
    public ResponseEntity<?> delete (@PathVariable Long id){
        Map<String, Object> response = new HashMap<>();
        try{
            this.compraService.delete(id);
        }catch (DataAccessException e){
            response.put("mensaje", "Error al eliminar la compra en la base datos");
            response.put("error", e.getMessage().concat(": ".concat(e.getMostSpecificCause().getMessage())));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje","La compra ha sido eliminada correctamente!!!");
        return  new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }
}
