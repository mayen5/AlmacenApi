package com.cmayen.almacen.core.models.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "inventario")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Inventario implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo_inventario")
    private Long codigoInventario;

    @NotEmpty(message = "Debe definir un codigo de producto")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_producto")
    private  Producto producto;

    @NotEmpty(message = "Debe definir una fecha")
    @Column(name = "fecha")
    private Date fecha;

    @NotEmpty(message = "Debe definir un tipo de registro")
    @Column(name = "tipo_registro")
    private  String tipoRegistro;

    @NotEmpty(message = "Debe definir un precio")
    @Column(name = "precio")
    private  Double precio;

    @NotEmpty(message = "Debe definir una cantidad de entrada")
    @Column(name = "entrada")
    private  Long entrada;

    @NotEmpty(message = "Debe definir una cantidad de salida")
    @Column(name = "salida")
    private  Long salida;
}
