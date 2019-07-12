package com.cmayen.almacen.core.models.services;

import com.cmayen.almacen.core.models.dao.IInventarioDao;
import com.cmayen.almacen.core.models.entity.Inventario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventarioServiceImpl implements IInventarioService{

    private final IInventarioDao inventarioDao;

    public InventarioServiceImpl(IInventarioDao inventarioDao) {
        this.inventarioDao = inventarioDao;
    }

    @Override
    public List<Inventario> findAll() {
        return this.inventarioDao.findAll();
    }

    @Override
    public Page<Inventario> findAll(Pageable pageable) {
        return this.inventarioDao.findAll(pageable);
    }

    @Override
    public Inventario save(Inventario inventario) {
        return this.inventarioDao.save(inventario);
    }

    @Override
    public Inventario findById(Long id) {
        return this.inventarioDao.findById(id).orElse(null);
    }

    @Override
    public void delete(Inventario inventario) {
        this.inventarioDao.delete(inventario);
    }

    @Override
    public void delete(Long id) {
        this.inventarioDao.deleteById(id);
    }
}
