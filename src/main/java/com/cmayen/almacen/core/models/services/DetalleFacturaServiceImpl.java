package com.cmayen.almacen.core.models.services;

import com.cmayen.almacen.core.models.dao.IDetalleFacturaDao;
import com.cmayen.almacen.core.models.entity.DetalleFactura;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DetalleFacturaServiceImpl implements IDetalleFacturaService {

    private final IDetalleFacturaDao detalleFacturaDao;

    public DetalleFacturaServiceImpl(IDetalleFacturaDao detalleFacturaDao) {
        this.detalleFacturaDao = detalleFacturaDao;
    }

    @Override
    public List<DetalleFactura> findAll() {
        return this.detalleFacturaDao.findAll();
    }

    @Override
    public Page<DetalleFactura> findAll(Pageable pageable) {
        return this.detalleFacturaDao.findAll(pageable);
    }

    @Override
    public DetalleFactura save(DetalleFactura detalleFactura) {
        return this.detalleFacturaDao.save(detalleFactura);
    }

    @Override
    public DetalleFactura findById(Long id) {
        return this.detalleFacturaDao.findById(id).orElse(null );
    }

    @Override
    public void delete(DetalleFactura detalleFactura) {
        this.detalleFacturaDao.delete(detalleFactura);
    }

    @Override
    public void delete(Long id) {
        this.detalleFacturaDao.deleteById(id);
    }
}
