package com.cmayen.almacen.core.models.services;

import com.cmayen.almacen.core.models.entity.Compra;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICompraService {
    public List<Compra> findAll();
    public Page<Compra> findAll(Pageable pageable);
    public Compra save(Compra compra);
    public Compra findById(Long id);
    public void delete (Compra compra);
    public void delete (Long id);
}
