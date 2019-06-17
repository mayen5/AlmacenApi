package com.cmayen.almacen.core.models.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Entity
@Table(name = "producto")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Producto implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo_producto")
    private Long codigoProducto;

    @NotEmpty(message = "Debe definir una descripcion")
    @Column(name = "descripcion")
    private  String descripcion;

    @NotEmpty(message = "Debe definir una precio unitario")
    @Column(name = "precio_unitario")
    private  Double precioUnitario;

    @NotEmpty(message = "Debe definir un precio por docena")
    @Column(name = "precio_por_docena")
    private  Double precioPorDocena;

    @NotEmpty(message = "Debe definir un precio por mayor")
    @Column(name = "precio_por_mayor")
    private  Double precioPorMayor;

    @NotEmpty(message = "Debe definir la existencia")
    @Column(name = "existencia")
    private  Long existencia;

    @Column(name = "imagen")
    private  String imagen;


    //@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_categoria")
    private  Categoria categoria;

    //@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_empaque")
    private  TipoEmpaque tipoEmpaque;

}
