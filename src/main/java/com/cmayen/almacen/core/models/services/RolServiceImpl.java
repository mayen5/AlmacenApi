package com.cmayen.almacen.core.models.services;

import com.cmayen.almacen.core.models.dao.IRolDao;
import com.cmayen.almacen.core.models.entity.Rol;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RolServiceImpl implements IRolService {

    private final IRolDao rolDao;

    public RolServiceImpl(IRolDao rolDao) {
        this.rolDao = rolDao;
    }

    @Override
    public List<Rol> findAll() {
        return this.rolDao.findAll();
    }

    @Override
    public Page<Rol> findAll(Pageable pageable) {
        return this.rolDao.findAll(pageable);
    }

    @Override
    public Rol save(Rol rol) {
        return this.rolDao.save(rol);
    }

    @Override
    public Rol findById(Long id) {
        return this.rolDao.findById(id).orElse(null);
    }

    @Override
    public void delete(Rol rol) {
        this.rolDao.delete(rol);
    }

    @Override
    public void delete(Long id) {
        this.rolDao.deleteById(id);
    }
}
