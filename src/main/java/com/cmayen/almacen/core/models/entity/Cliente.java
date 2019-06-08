package com.cmayen.almacen.core.models.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Entity
@Table(name = "cliente")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cliente implements Serializable {
    @Id
    @Column(name = "nit")
    private String nit;

    @NotEmpty(message = "Debe ingresar un DPI")
    @Column(name = "dpi")
    private  String dpi;

    @NotEmpty(message = "Debe ingresar un nombre")
    @Column(name = "nombre")
    private  String nombre;

    @NotEmpty(message = "Debe ingresar una direccion")
    @Column(name = "direccion")
    private  String direccion;
}
