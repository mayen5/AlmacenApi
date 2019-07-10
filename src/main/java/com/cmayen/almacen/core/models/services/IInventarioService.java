package com.cmayen.almacen.core.models.services;

import com.cmayen.almacen.core.models.entity.Inventario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IInventarioService {
    public List<Inventario> findAll();
    public Page<Inventario> findAll(Pageable pageable);
    public Inventario save(Inventario inventario);
    public Inventario findById(Long id);
    public void delete (Inventario inventario);
    public void delete (Long id);
}
