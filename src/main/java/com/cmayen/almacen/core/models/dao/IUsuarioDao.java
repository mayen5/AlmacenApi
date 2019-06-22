package com.cmayen.almacen.core.models.dao;

import com.cmayen.almacen.core.models.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IUsuarioDao extends JpaRepository<Usuario, Long> {

    public Usuario findByUsername (String username);

    @Query("from Usuario u where u.username = ?1")
    public Usuario fingByUsername2(String username);

    @Query("from Usuario u where u.username = ?1 and u.enabled = ?2")
    public Usuario fingByUsername3(String username, boolean enabled);
}
