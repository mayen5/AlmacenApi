package com.cmayen.almacen.core.models.dao;

import com.cmayen.almacen.core.models.entity.TelefonoProveedor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ITelefonoProveedorDao extends JpaRepository<TelefonoProveedor, Long> {
}
