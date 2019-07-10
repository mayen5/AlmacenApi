package com.cmayen.almacen.core.models.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "cliente")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetalleCompra implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle")
    private Long idDetalle;


    @NotEmpty(message = "Debe definir un id de compra")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_compra")
    private  Compra compra;

    @NotEmpty(message = "Debe definir un codigo de producto")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_producto")
    private  Producto producto;

    @NotEmpty(message = "Debe definir una cantidad de compra")
    @Column(name = "cantidad")
    private Long cantidad;

    @NotEmpty(message = "Debe definir un precio de compra")
    @Column(name = "precio")
    private Double precio;
}
