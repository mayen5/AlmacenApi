package com.cmayen.almacen.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class AlmacenApplication implements CommandLineRunner {

    /*Implementar para el controlador de usuario para que encripte el password*/
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(AlmacenApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println(passwordEncoder.encode("123"));
    }
}
