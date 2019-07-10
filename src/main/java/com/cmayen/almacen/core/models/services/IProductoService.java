package com.cmayen.almacen.core.models.services;

import com.cmayen.almacen.core.models.entity.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IProductoService {
    public List<Producto> findAll();
    public Page<Producto> findAll(Pageable pageable);
    public Producto save(Producto producto);
    public Producto findById(Long id);
    public void delete (Producto producto);
    public void delete (Long id);
}
