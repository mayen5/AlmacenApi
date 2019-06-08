package com.cmayen.almacen.core.models.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Entity
@Table(name = "tipo_empaque")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TipoEmpaque implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo_empaque")
    private Long codigoEmpaque;

    @NotEmpty(message = "Debe definir una descripcion")
    @Column(name = "descripcion")
    private  String descripcion;
}
