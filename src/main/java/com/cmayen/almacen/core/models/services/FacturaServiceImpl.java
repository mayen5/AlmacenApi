package com.cmayen.almacen.core.models.services;

import com.cmayen.almacen.core.models.dao.IFacturaDao;
import com.cmayen.almacen.core.models.entity.Factura;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FacturaServiceImpl implements IFacturaService{

    private final IFacturaDao facturaDao;

    public FacturaServiceImpl(IFacturaDao facturaDao) {
        this.facturaDao = facturaDao;
    }

    @Override
    public List<Factura> findAll() {
        return this.facturaDao.findAll();
    }

    @Override
    public Page<Factura> findAll(Pageable pageable) {
        return this.facturaDao.findAll(pageable);
    }

    @Override
    public Factura save(Factura factura) {
        return this.facturaDao.save(factura);
    }

    @Override
    public Factura findById(Long id) {
        return this.facturaDao.findById(id).orElse(null);
    }

    @Override
    public void delete(Factura factura) {
        this.facturaDao.delete(factura);
    }

    @Override
    public void delete(Long id) {
        this.facturaDao.deleteById(id);
    }
}
