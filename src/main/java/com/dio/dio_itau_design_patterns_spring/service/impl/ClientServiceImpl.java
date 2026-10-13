package com.dio.dio_itau_design_patterns_spring.service.impl;

import com.dio.dio_itau_design_patterns_spring.model.AddressRepository;
import com.dio.dio_itau_design_patterns_spring.model.Client;
import com.dio.dio_itau_design_patterns_spring.model.ClientRepository;
import com.dio.dio_itau_design_patterns_spring.service.ClientService;
import com.dio.dio_itau_design_patterns_spring.service.ViaCepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClientServiceImpl implements ClientService {

    // TODO Singleton: Implementar os componentes do Spring com @Autowired
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    AddressRepository addressRepository;
    @Autowired
    ViaCepService viaCepService;

    // TODO Strategy: Implementar os métodos definidos na interface ClientService
    // TODO Facade: Abstrair as integrações com subsistemas, promovendo uma interface simples e limpa.

    @Override
    public void save(Client client) {
        saveClientAndVerifyAddress(client);
    }

    @Override
    public void update(Long id, Client client) {
        // Verifica se o client existe por ID, se não existe, lança uma exceção.
        var clientFound = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client with id " + id + " not found"));

        // Atualiza com base no ID exitente
        client.setId(clientFound.getId());
        saveClientAndVerifyAddress(client);
    }

    @Override
    public void delete(Long id) {
        // Deletar o client por ID.
        clientRepository.deleteById(id);
    }

    @Override
    public Iterable<Client> findAll() {
        // Buscar todos os Clients.
        return clientRepository.findAll();
    }

    @Override
    public Client findById(Long id) {
        // Buscar o client por ID.
        return clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client with id " + id + " not found."));
    }


    private void saveClientAndVerifyAddress(Client client) {
        // Verificar se o Address do client já existe (pelo cep).
        var cep = client.getAddress().getCep();

        var address = addressRepository.findById(cep)
                .orElseGet(() -> {
                    // Caso não exista, integrar com o ViaCEP e persistir o retorno.
                    var newAddress = viaCepService.consultCep(cep);
                    addressRepository.save(newAddress);
                    return newAddress;
                });

        // Inserir o client, vinculando o Address novo ou existente.
        client.setAddress(address);
        clientRepository.save(client);
    }

}
