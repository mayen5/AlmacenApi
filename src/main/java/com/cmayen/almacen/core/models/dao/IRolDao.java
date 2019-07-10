package com.cmayen.almacen.core.models.dao;

import com.cmayen.almacen.core.models.entity.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRolDao extends JpaRepository<Rol, Long> {
}
