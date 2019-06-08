package com.cmayen.almacen.core.models.dao;

import com.cmayen.almacen.core.models.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IClienteDao extends JpaRepository<Cliente, String> {
}
