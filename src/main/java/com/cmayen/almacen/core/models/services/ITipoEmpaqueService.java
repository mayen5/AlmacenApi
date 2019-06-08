package com.cmayen.almacen.core.models.services;

import com.cmayen.almacen.core.models.entity.TipoEmpaque;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ITipoEmpaqueService {

    public List<TipoEmpaque> findAll();
    public Page<TipoEmpaque> findAll(Pageable pageable);
    public TipoEmpaque save(TipoEmpaque tipoEmpaque);
    public TipoEmpaque findById(Long id);
    public void delete (TipoEmpaque tipoEmpaque);
}
