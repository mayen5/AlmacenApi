package com.cmayen.almacen.core.controllers;

import com.cmayen.almacen.core.models.entity.DetalleCompra;
import com.cmayen.almacen.core.models.services.IDetalleCompraService;
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
@Api(tags = "detallecompras")
public class DetalleCompraRestController {

    private final IDetalleCompraService detalleCompraService;

    public DetalleCompraRestController(IDetalleCompraService detalleCompraService){
        this.detalleCompraService = detalleCompraService;
    }

    @ApiOperation(value = "Listar Detalle Compras", notes = "Servicio para listar los detalles Compras")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Lista de detalle Compras")})
    @GetMapping("/detalleCompras")
    public List<DetalleCompra> index() {
        return this.detalleCompraService.findAll();
    }

    @ApiOperation(value = "Paginar listado de Detalle Compras", notes = "Servicio para listar los detalles Compras paginadas")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Lista de detalle Compras paginadas")})
    @GetMapping("/detallecompras/page/{page}")
    public Page<DetalleCompra> index(@PathVariable Integer page){
        Pageable pageable = PageRequest.of(page, 5);
        return detalleCompraService.findAll(pageable);
    }

    @ApiOperation(value = "Buscar detalle Compra por Id", notes = "Servicio para buscar detalle Compra por codigo")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Detalle Compra encontrada"),
            @ApiResponse(code = 404, message = "Detalle Compra no encontrada")})
    @GetMapping("/detallecompras/{id}")
    public ResponseEntity<?> show(@PathVariable Long id){
        Map<String, Object> response = new HashMap<>();
        DetalleCompra detalleCompraEncontrada = this.detalleCompraService.findById(id);
        if (detalleCompraEncontrada == null){
            response.put("mensaje", "Detalle Compra no encontrada");
            return  new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        return  new ResponseEntity<DetalleCompra>(detalleCompraEncontrada, HttpStatus.OK);
    }

    @ApiOperation(value = "Crear Detalle Compra", notes = "Servicio para crear un detalle Compra")
    @ApiResponses(value = {@ApiResponse(code = 201, message = "Detalle Compra creada"),
            @ApiResponse(code = 400, message = "Existen errores en el ingreso de datos"),
            @ApiResponse(code = 500, message = "Error en el servidor al crear detalleCompra")})
    @PostMapping("/detallecompras")
    public ResponseEntity<?> create (@Valid @RequestBody DetalleCompra elemento, BindingResult result){
        DetalleCompra nuevo = null;
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
            nuevo = this.detalleCompraService.save(elemento);
        }catch (DataAccessException e){
            response.put("mensaje", "Error al realizar el insert en la base de datos");
            response.put("error", e.getMessage().concat(": ".concat(e.getMostSpecificCause().getMessage())));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje","El detalle Compra ha sido creado con éxito");
        response.put("detalleCompra", nuevo);
        return  new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @PutMapping("/detallecompras/{id}")
    public  ResponseEntity<?> update (@Valid @RequestBody DetalleCompra detalleCompra, BindingResult result, @PathVariable Long id){

        Map<String, Object> response = new HashMap<>();
        DetalleCompra update = this.detalleCompraService.findById(id);
        DetalleCompra detalleCompraUpdate = null;

        if (result.hasErrors()){
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(err -> err.getDefaultMessage())
                    .collect(Collectors.toList());
            response.put("errors", errors);
            return  new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        if (update == null){
            response.put("mensaje", "Error: no se puede editar la detalleCompra ID "
                    + id.toString()
                    + " no existe en la base de datos");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        try {
            update.setCompra(detalleCompra.getCompra());
            update.setProducto(detalleCompra.getProducto());
            update.setCantidad(detalleCompra.getCantidad());
            update.setPrecio(detalleCompra.getPrecio());
            detalleCompraUpdate = this.detalleCompraService.save(detalleCompra);
        }catch (DataAccessException e){
            response.put("mensaje", "Error al actualizar los datos");
            response.put("error", e.getMessage().concat(": ".concat(e.getMostSpecificCause().getMessage())));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje","El detalle Compra ha sido actualizado correctamente!!!");
        response.put("detalleCompra", detalleCompraUpdate);
        return  new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("detallecompras/{id}")
    public ResponseEntity<?> delete (@PathVariable Long id){
        Map<String, Object> response = new HashMap<>();
        try{
            this.detalleCompraService.delete(id);
        }catch (DataAccessException e){
            response.put("mensaje", "Error al eliminar el detalle Compra en la base datos");
            response.put("error", e.getMessage().concat(": ".concat(e.getMostSpecificCause().getMessage())));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje","El detalle Compra ha sido eliminado correctamente!!!");
        return  new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }
}
