package com.cmayen.almacen.core.models.services;

import com.cmayen.almacen.core.models.dao.ITipoEmpaqueDao;
import com.cmayen.almacen.core.models.entity.TipoEmpaque;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TipoEmpaqueImpl implements  ITipoEmpaqueService{

    private final ITipoEmpaqueDao tipoEmpaqueDao;

    public TipoEmpaqueImpl(ITipoEmpaqueDao tipoEmpaqueDao){
        this.tipoEmpaqueDao = tipoEmpaqueDao;
    }

    @Override
    public List<TipoEmpaque> findAll() {
        return this.tipoEmpaqueDao.findAll();
    }

    @Override
    public Page<TipoEmpaque> findAll(Pageable pageable) {
        return this.tipoEmpaqueDao.findAll(pageable);
    }

    @Override
    public TipoEmpaque save(TipoEmpaque tipoEmpaque) {
        return this.tipoEmpaqueDao.save(tipoEmpaque);
    }

    @Override
    public TipoEmpaque findById(Long id) {
        return this.tipoEmpaqueDao.findById(id).orElse(null );
    }

    @Override
    public void delete(TipoEmpaque tipoEmpaque) {
        this.tipoEmpaqueDao.delete(tipoEmpaque);
    }
}
