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
@Table(name = "factura")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Factura implements Serializable {

    @Id
    @Column(name = "numero_factura")
    private Long numeroFactura;

    @NotEmpty(message = "Debe ingresar un cliente")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nit")
    private  Cliente cliente;

    @NotEmpty(message = "Debe ingresar una fecha")
    @Column(name = "fecha")
    private Date fecha;

    @Column(name = "total")
    private  Double total;

    @JsonIgnore
    @OneToMany(mappedBy = "factura", fetch = FetchType.LAZY)
    private List<DetalleFactura> detalleFacturas;

}
