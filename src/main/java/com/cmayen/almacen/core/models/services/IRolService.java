package com.cmayen.almacen.core.models.services;

import com.cmayen.almacen.core.models.entity.Rol;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IRolService {
    public List<Rol> findAll();
    public Page<Rol> findAll(Pageable pageable);
    public Rol save(Rol rol);
    public Rol findById(Long id);
    public void delete (Rol rol);
    public void delete (Long id);
}
