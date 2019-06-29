package com.cmayen.almacen.core.models.services;

import com.cmayen.almacen.core.models.dao.IUsuarioDao;
import com.cmayen.almacen.core.models.entity.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioServiceImpl implements IUsuarioService, UserDetailsService {
    private final IUsuarioDao usuarioDao;
    private Logger logger = LoggerFactory.getLogger(UsuarioServiceImpl.class);

    public UsuarioServiceImpl(IUsuarioDao usuarioDao){
        this.usuarioDao = usuarioDao;
    }

    @Override
    public List<Usuario> findAll() {
        return this.usuarioDao.findAll();
    }

    @Override
    public Page<Usuario> findAll(Pageable pageable) {
        return this.usuarioDao.findAll(pageable);
    }

    @Override
    public Usuario findById(Long id) {
        return this.usuarioDao.findById(id).orElse(null);
    }

    @Override
    public Usuario save(Usuario usuario) {
        return this.usuarioDao.save(usuario);
    }

    @Override
    public void delete(Usuario usuario) {
        this.usuarioDao.delete(usuario);
    }

    @Override
    public Usuario findByUsername(String username) {
        return usuarioDao.findByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = this.usuarioDao.findByUsername(username);
        if (usuario == null){
            logger.error("Error en el login: no existe el usuario: " + username + " en el sistema");
            throw new UsernameNotFoundException("Error en el login: no existe el usuario: " + username + " en el sistema");
        }

        List<GrantedAuthority> authorities = usuario.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getNombre()))
                .collect(Collectors.toList());

        return new User(usuario.getUsername(), usuario.getPassword(), usuario.isEnabled(),
                true, true, true, authorities);
    }
}
