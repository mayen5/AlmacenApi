package com.cmayen.almacen.core.models.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Entity
@Table(name = "detalle_factura")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetalleFactura implements Serializable {

    @Id
    @Column(name = "codigo_detalle")
    private Long codigoDetalle;

    @NotEmpty(message = "Debe ingresar un numero de factura")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "numero_factura")
    private  Factura factura;

    @NotEmpty(message = "Debe ingresar un codigo de producto")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_producto")
    private  Producto producto;

    @NotEmpty(message = "Debe definir una cantidad del producto")
    @Column(name = "cantidad")
    private  Long cantidad;

    @NotEmpty(message = "Debe definir un precio del producto")
    @Column(name = "precio")
    private  Double precio;

    @NotEmpty(message = "Debe definir un descuento")
    @Column(name = "descuento")
    private  Double descuento;
}
