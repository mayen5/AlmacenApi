package com.cmayen.almacen.core.models.services;

import com.cmayen.almacen.core.models.dao.IProveedorDao;
import com.cmayen.almacen.core.models.entity.Proveedor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProveedorServiceImpl implements  IProveedorService{

    private final IProveedorDao proveedorDao;

    public ProveedorServiceImpl(IProveedorDao proveedorDao) {
        this.proveedorDao = proveedorDao;
    }

    @Override
    public List<Proveedor> findAll() {
        return this.proveedorDao.findAll();
    }

    @Override
    public Page<Proveedor> findAll(Pageable pageable) {
        return this.proveedorDao.findAll(pageable);
    }

    @Override
    public Proveedor save(Proveedor proveedor) {
        return this.proveedorDao.save(proveedor);
    }

    @Override
    public Proveedor findById(Long id) {
        return this.proveedorDao.findById(id).orElse(null);
    }

    @Override
    public void delete(Proveedor proveedor) {
        this.proveedorDao.delete(proveedor);
    }

    @Override
    public void delete(Long id) {
        this.proveedorDao.deleteById(id);
    }
}
