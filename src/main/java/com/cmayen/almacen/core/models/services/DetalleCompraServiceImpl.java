package com.cmayen.almacen.core.models.services;

import com.cmayen.almacen.core.models.dao.IDetalleCompraDao;
import com.cmayen.almacen.core.models.entity.DetalleCompra;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DetalleCompraServiceImpl implements IDetalleCompraService {

    private final IDetalleCompraDao detalleCompraDao;

    public DetalleCompraServiceImpl(IDetalleCompraDao detalleCompraDao) {
        this.detalleCompraDao = detalleCompraDao;
    }

    @Override
    public List<DetalleCompra> findAll() {
        return this.detalleCompraDao.findAll();
    }

    @Override
    public Page<DetalleCompra> findAll(Pageable pageable) {
        return this.detalleCompraDao.findAll(pageable);
    }

    @Override
    public DetalleCompra save(DetalleCompra detalleCompra) {
        return this.detalleCompraDao.save(detalleCompra);
    }

    @Override
    public DetalleCompra findById(Long id) {
        return this.detalleCompraDao.findById(id).orElse(null);
    }

    @Override
    public void delete(DetalleCompra detalleCompra) {
        this.detalleCompraDao.delete(detalleCompra);
    }

    @Override
    public void delete(Long id) {
        this.detalleCompraDao.deleteById(id);
    }
}
