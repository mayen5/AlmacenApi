package com.cmayen.almacen.core.models.services;

import com.cmayen.almacen.core.models.entity.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IUsuarioService {
    public List<Usuario> findAll();
    public Page<Usuario> findAll(Pageable pageable);
    public Usuario findById(Long id);
    public Usuario save(Usuario usuario);
    public  void delete(Usuario usuario);
    public Usuario findByUsername(String username);
}
