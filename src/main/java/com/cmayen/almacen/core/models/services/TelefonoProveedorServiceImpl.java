package com.cmayen.almacen.core.models.services;

import com.cmayen.almacen.core.models.dao.ITelefonoProveedorDao;
import com.cmayen.almacen.core.models.entity.TelefonoProveedor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TelefonoProveedorServiceImpl implements ITelefonoProveedorService{

    private final ITelefonoProveedorDao telefonoProveedorDao;

    public TelefonoProveedorServiceImpl(ITelefonoProveedorDao telefonoProveedorDao) {
        this.telefonoProveedorDao = telefonoProveedorDao;
    }

    @Override
    public List<TelefonoProveedor> findAll() {
        return this.telefonoProveedorDao.findAll();
    }

    @Override
    public Page<TelefonoProveedor> findAll(Pageable pageable) {
        return this.telefonoProveedorDao.findAll(pageable);
    }

    @Override
    public TelefonoProveedor save(TelefonoProveedor telefonoProveedor) {
        return this.telefonoProveedorDao.save(telefonoProveedor);
    }

    @Override
    public TelefonoProveedor findById(Long id) {
        return this.telefonoProveedorDao.findById(id).orElse(null);
    }

    @Override
    public void delete(TelefonoProveedor telefonoProveedor) {
        this.telefonoProveedorDao.delete(telefonoProveedor);
    }

    @Override
    public void delete(Long id) {
        this.telefonoProveedorDao.deleteById(id);
    }
}
