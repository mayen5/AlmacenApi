package com.cmayen.almacen.core.models.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "compra")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Compra implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_compra")
    private Long idCompra;

    @NotEmpty(message = "Debe definir un numero de documento")
    @Column(name = "numero_documento")
    private  Long numeroDocumento;

    @NotEmpty(message = "Debe definir un codigo de proveedor")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_proveedor")
    private  Proveedor proveedor;

    @NotEmpty(message = "Debe definir una fecha de compra")
    @Column(name = "fecha_compra")
    private Date fecha;

    @Column(name = "total")
    private Double total;

    @JsonIgnore
    @OneToMany(mappedBy = "compra", fetch = FetchType.LAZY)
    private List<DetalleCompra> detalleCompras;
}
