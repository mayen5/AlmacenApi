package com.cmayen.almacen.core.models.services;

import com.cmayen.almacen.core.models.entity.DetalleCompra;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IDetalleCompraService {

    public List<DetalleCompra> findAll();
    public Page<DetalleCompra> findAll(Pageable pageable);
    public DetalleCompra save(DetalleCompra detalleCompra);
    public DetalleCompra findById(Long id);
    public void delete (DetalleCompra detalleCompra);
    public void delete (Long id);
}
