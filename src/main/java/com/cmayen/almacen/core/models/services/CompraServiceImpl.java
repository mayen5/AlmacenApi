package com.cmayen.almacen.core.models.services;

import com.cmayen.almacen.core.models.dao.ICompraDao;
import com.cmayen.almacen.core.models.entity.Compra;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompraServiceImpl implements ICompraService{

    private final ICompraDao compraDao;

    public CompraServiceImpl(ICompraDao compraDao) {
        this.compraDao = compraDao;
    }

    @Override
    public List<Compra> findAll() {
        return this.compraDao.findAll();
    }

    @Override
    public Page<Compra> findAll(Pageable pageable) {
        return this.compraDao.findAll(pageable);
    }

    @Override
    public Compra save(Compra compra) {
        return this.compraDao.save(compra);
    }

    @Override
    public Compra findById(Long id) {
        return this.compraDao.findById(id).orElse(null);
    }

    @Override
    public void delete(Compra compra) {
        this.compraDao.delete(compra);
    }

    @Override
    public void delete(Long id) {
        this.compraDao.deleteById(id);
    }
}
