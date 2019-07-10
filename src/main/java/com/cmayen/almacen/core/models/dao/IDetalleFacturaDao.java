package com.cmayen.almacen.core.models.dao;

import com.cmayen.almacen.core.models.entity.DetalleFactura;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IDetalleFacturaDao extends JpaRepository<DetalleFactura, Long> {
}
