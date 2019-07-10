package com.cmayen.almacen.core.models.dao;

import com.cmayen.almacen.core.models.entity.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IInventarioDao extends JpaRepository<Inventario, Long> {
}
