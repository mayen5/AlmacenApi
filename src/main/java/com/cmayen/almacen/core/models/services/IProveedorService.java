package com.cmayen.almacen.core.models.services;

import com.cmayen.almacen.core.models.entity.Proveedor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IProveedorService {
    public List<Proveedor> findAll();
    public Page<Proveedor> findAll(Pageable pageable);
    public Proveedor save(Proveedor proveedor);
    public Proveedor findById(Long id);
    public void delete (Proveedor proveedor);
    public void delete (Long id);
}
