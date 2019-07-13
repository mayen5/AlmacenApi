package com.cmayen.almacen.core.controllers;

import com.cmayen.almacen.core.models.entity.DetalleFactura;
import com.cmayen.almacen.core.models.services.IDetalleFacturaService;
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
@Api(tags = "detallefacturas")
public class DetalleFacturaRestController {

    private final IDetalleFacturaService detalleFacturaService;

    public DetalleFacturaRestController(IDetalleFacturaService detalleFacturaService){
        this.detalleFacturaService = detalleFacturaService;
    }

    @ApiOperation(value = "Listar Detalle Facturas", notes = "Servicio para listar los detalle Facturas")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Lista de detalle Facturas")})
    @GetMapping("/detallefacturas")
    public List<DetalleFactura> index() {
        return this.detalleFacturaService.findAll();
    }

    @ApiOperation(value = "Paginar listado de Detalle Facturas", notes = "Servicio para listar los detalle Facturas paginadas")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Lista de detalle Facturas paginadas")})
    @GetMapping("/detallefacturas/page/{page}")
    public Page<DetalleFactura> index(@PathVariable Integer page){
        Pageable pageable = PageRequest.of(page, 5);
        return detalleFacturaService.findAll(pageable);
    }

    @ApiOperation(value = "Buscar detalle Factura por Id", notes = "Servicio para buscar detalle Factura por codigo")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Detalle Factura encontrada"),
            @ApiResponse(code = 404, message = "Detalle Factura no encontrada")})
    @GetMapping("/detallefacturas/{id}")
    public ResponseEntity<?> show(@PathVariable Long id){
        Map<String, Object> response = new HashMap<>();
        DetalleFactura detalleFacturaEncontrada = this.detalleFacturaService.findById(id);
        if (detalleFacturaEncontrada == null){
            response.put("mensaje", "Detalle Factura no encontrada");
            return  new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        return  new ResponseEntity<DetalleFactura>(detalleFacturaEncontrada, HttpStatus.OK);
    }

    @ApiOperation(value = "Crear Detalle Factura", notes = "Servicio para crear una detalle Factura")
    @ApiResponses(value = {@ApiResponse(code = 201, message = "Detalle Factura creada"),
            @ApiResponse(code = 400, message = "Existen errores en el ingreso de datos"),
            @ApiResponse(code = 500, message = "Error en el servidor al crear detalle Factura")})
    @PostMapping("/detallefacturas")
    public ResponseEntity<?> create (@Valid @RequestBody DetalleFactura elemento, BindingResult result){
        DetalleFactura nuevo = null;
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
            nuevo = this.detalleFacturaService.save(elemento);
        }catch (DataAccessException e){
            response.put("mensaje", "Error al realizar el insert en la base de datos");
            response.put("error", e.getMessage().concat(": ".concat(e.getMostSpecificCause().getMessage())));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje","El detalle Factura ha sido creada con éxito");
        response.put("detallefactura", nuevo);
        return  new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @PutMapping("/detallefacturas/{id}")
    public  ResponseEntity<?> update (@Valid @RequestBody DetalleFactura detalleFactura, BindingResult result, @PathVariable Long id){

        Map<String, Object> response = new HashMap<>();
        DetalleFactura update = this.detalleFacturaService.findById(id);
        DetalleFactura detalleFacturaUpdate = null;

        if (result.hasErrors()){
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(err -> err.getDefaultMessage())
                    .collect(Collectors.toList());
            response.put("errors", errors);
            return  new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        if (update == null){
            response.put("mensaje", "Error: no se puede editar el detalle Factura ID "
                    + id.toString()
                    + " no existe en la base de datos");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        try {
            update.setFactura(detalleFactura.getFactura());
            update.setProducto(detalleFactura.getProducto());
            update.setCantidad(detalleFactura.getCantidad());
            update.setPrecio(detalleFactura.getPrecio());
            update.setDescuento(detalleFactura.getDescuento());
            detalleFacturaUpdate = this.detalleFacturaService.save(detalleFactura);
        }catch (DataAccessException e){
            response.put("mensaje", "Error al actualizar los datos");
            response.put("error", e.getMessage().concat(": ".concat(e.getMostSpecificCause().getMessage())));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje","El detalle Factura ha sido actualizado correctamente!!!");
        response.put("detallefactura", detalleFacturaUpdate);
        return  new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("detallefacturas/{id}")
    public ResponseEntity<?> delete (@PathVariable Long id){
        Map<String, Object> response = new HashMap<>();
        try{
            this.detalleFacturaService.delete(id);
        }catch (DataAccessException e){
            response.put("mensaje", "Error al eliminar el detalle Factura en la base datos");
            response.put("error", e.getMessage().concat(": ".concat(e.getMostSpecificCause().getMessage())));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje","El detalle Factura ha sido eliminado correctamente!!!");
        return  new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }
}

