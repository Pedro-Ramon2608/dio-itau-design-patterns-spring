package com.dio.dio_itau_design_patterns_spring.service;

import com.dio.dio_itau_design_patterns_spring.model.Client;

/**
 * Interface que define o padrão <b>Strategy</b> no dominio de client. Com isso, se necessário, é posssível
 * ter múltiplas implementações dessa mesma interface;
 *
 * @author Pedro-Ramon2608
 */
public interface ClientService {

    void save(Client client);

    void update(Long id, Client client);

    void delete(Long id);

    Iterable<Client> findAll();

    Client findById(Long id);

}
