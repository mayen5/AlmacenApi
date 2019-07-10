package com.cmayen.almacen.core.models.dao;

import com.cmayen.almacen.core.models.entity.TelefonoCliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ITelefonoClienteDao extends JpaRepository<TelefonoCliente, Long> {
}
