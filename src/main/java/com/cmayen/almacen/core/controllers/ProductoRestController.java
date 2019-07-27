package com.cmayen.almacen.core.controllers;

import com.cmayen.almacen.core.models.entity.Producto;
import com.cmayen.almacen.core.models.services.IProductoService;
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
@Api(tags = "productos")
public class ProductoRestController {

    private final IProductoService productoService;

    public ProductoRestController(IProductoService productoService){
        this.productoService = productoService;
    }

    @ApiOperation(value = "Listar Productos", notes = "Servicio para listar los productos")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Lista de productos")})
    @GetMapping("/productos")
    public List<Producto> index() {
        return this.productoService.findAll();
    }

    @ApiOperation(value = "Paginar listado de Productos", notes = "Servicio para listar los productos paginados")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Lista de productos paginados")})
    @GetMapping("/productos/page/{page}")
    public Page<Producto> index(@PathVariable Integer page){
        Pageable pageable = PageRequest.of(page, 5);
        return productoService.findAll(pageable);
    }

    @ApiOperation(value = "Buscar producto por Id", notes = "Servicio para buscar producto por codigo")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Producto encontrado"),
            @ApiResponse(code = 404, message = "Producto no encontrado")})
    @GetMapping("/productos/{id}")
    public ResponseEntity<?> show(@PathVariable Long id){
        Map<String, Object> response = new HashMap<>();
        Producto productoEncontrada = this.productoService.findById(id);
        if (productoEncontrada == null){
            response.put("mensaje", "Producto no encontrado");
            return  new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        return  new ResponseEntity<Producto>(productoEncontrada, HttpStatus.OK);
    }

    @ApiOperation(value = "Crear Producto", notes = "Servicio para crear una producto")
    @ApiResponses(value = {@ApiResponse(code = 201, message = "Producto creada"),
            @ApiResponse(code = 400, message = "Existen errores en el ingreso de datos"),
            @ApiResponse(code = 500, message = "Error en el servidor al crear producto")})
    @PostMapping("/productos")
    public ResponseEntity<?> create (@Valid @RequestBody Producto elemento, BindingResult result){
        Producto nuevo = null;
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
            nuevo = this.productoService.save(elemento);
        }catch (DataAccessException e){
            response.put("mensaje", "Error al realizar el insert en la base de datos");
            response.put("error", e.getMessage().concat(": ".concat(e.getMostSpecificCause().getMessage())));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje","La producto ha sido creada con éxito");
        response.put("producto", nuevo);
        return  new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @PutMapping("/productos/{id}")
    public  ResponseEntity<?> update (@Valid @RequestBody Producto producto, BindingResult result, @PathVariable Long id){

        Map<String, Object> response = new HashMap<>();
        Producto update = this.productoService.findById(id);
        Producto productoUpdate = null;

        if (result.hasErrors()){
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(err -> err.getDefaultMessage())
                    .collect(Collectors.toList());
            response.put("errors", errors);
            return  new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        if (update == null){
            response.put("mensaje", "Error: no se puede editar el producto ID "
                    + id.toString()
                    + " no existe en la base de datos");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        try {
            update.setCategoria(producto.getCategoria());
            update.setTipoEmpaque(producto.getTipoEmpaque());
            update.setDescripcion(producto.getDescripcion());
            update.setPrecioUnitario(producto.getPrecioUnitario());
            update.setPrecioPorDocena(producto.getPrecioPorDocena());
            update.setPrecioPorMayor(producto.getPrecioPorMayor());
            update.setExistencia(producto.getExistencia());
            update.setImagen(producto.getImagen());
            productoUpdate = this.productoService.save(update);
        }catch (DataAccessException e){
            response.put("mensaje", "Error al actualizar los datos");
            response.put("error", e.getMessage().concat(": ".concat(e.getMostSpecificCause().getMessage())));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje","La producto ha sido actualizado correctamente!!!");
        response.put("producto", productoUpdate);
        return  new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("productos/{id}")
    public ResponseEntity<?> delete (@PathVariable Long id){
        Map<String, Object> response = new HashMap<>();
        try{
            this.productoService.delete(id);
        }catch (DataAccessException e){
            response.put("mensaje", "Error al eliminar el producto en la base datos");
            response.put("error", e.getMessage().concat(": ".concat(e.getMostSpecificCause().getMessage())));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje","El producto ha sido eliminado correctamente!!!");
        return  new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }
}