package com.dio.dio_itau_design_patterns_spring.controller;

import com.dio.dio_itau_design_patterns_spring.model.Client;
import com.dio.dio_itau_design_patterns_spring.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Esse {@link RestController} representa a Facade, pois abstrai toda a complexidade de integrações (Banco
 * de Dados H2 e API do ViaCEP) em uma interface simples e coesa (API REST).
 *
 * @author Pedro-Ramon2608
 */
@RestController
@RequestMapping("clients")
public class ClientRestController {

    @Autowired
    private ClientService clientService;


    @PostMapping
    public ResponseEntity<Client> save(@RequestBody Client client) {
        clientService.save(client);
        return ResponseEntity.ok(client);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Client> update(@PathVariable Long id, @RequestBody Client client) {
        clientService.update(id, client);
        return ResponseEntity.ok(client);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Client> delete(@PathVariable Long id) {
        clientService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Iterable<Client>> findAll() {
        return ResponseEntity.ok(clientService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Client> findById(@PathVariable Long id) {
        return ResponseEntity.ok(clientService.findById(id));
    }

}
