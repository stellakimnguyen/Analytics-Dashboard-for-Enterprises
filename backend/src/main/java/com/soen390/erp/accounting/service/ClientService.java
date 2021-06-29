package com.soen390.erp.accounting.service;

import com.soen390.erp.accounting.exceptions.ClientNotFoundException;
import com.soen390.erp.accounting.model.Client;
import com.soen390.erp.accounting.repository.ClientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }


    public Client findClientById(int id) throws ClientNotFoundException{

        if ( !clientRepository.existsById(id) )
            throw new ClientNotFoundException(id);

        return clientRepository.findById(id);
    }


    public List<Client> findAllClients() {
        return clientRepository.findAll();
    }


    public Client saveClient(Client client) {
        return clientRepository.save(client);
    }


    public boolean deleteClientById(int id) {

        if ( !clientRepository.existsById(id) )
            return false;

        clientRepository.deleteById(id);

        return true;
    }
}
