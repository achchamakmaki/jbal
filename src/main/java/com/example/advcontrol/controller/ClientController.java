package com.example.advcontrol.controller;

import com.example.advcontrol.entity.Client;
import com.example.advcontrol.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientRepository clientRepository;

    @GetMapping
    public List<Client> getAllClients() {
        return clientRepository.findByActiveTrue();
    }

    @PostMapping
    public Client createClient(@RequestBody Client client) {
        client.setActive(true);
        return clientRepository.save(client);
    }

    @PostMapping("/seed")
    public List<Client> seedClients() {
        if (clientRepository.count() > 0) {
            return clientRepository.findAll();
        }

        clientRepository.save(create("Atlas Construction", "001525252000047", "RC12345", "IF78945", "CNSS001", "contact@atlas.ma", "0600000000", "Casablanca", "Zone industrielle Casablanca"));
        clientRepository.save(create("Nour Bâtiment", "001999888000033", "RC55555", "IF22222", "CNSS002", "contact@nour.ma", "0611111111", "Rabat", "Rabat Centre"));
        clientRepository.save(create("Sahara Travaux", "001777666000011", "RC77777", "IF33333", "CNSS003", "contact@sahara.ma", "0622222222", "Marrakech", "Marrakech Route Safi"));

        return clientRepository.findAll();
    }

    private Client create(String raisonSociale, String ice, String rc, String ifNumber,
                          String cnss, String email, String phone, String city, String address) {
        Client client = new Client();
        client.setRaisonSociale(raisonSociale);
        client.setIce(ice);
        client.setRc(rc);
        client.setIfNumber(ifNumber);
        client.setCnss(cnss);
        client.setEmail(email);
        client.setPhone(phone);
        client.setCity(city);
        client.setAddress(address);
        client.setActive(true);
        return client;
    }

    @GetMapping("/{id}")
    public Client getClient(@PathVariable Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client introuvable"));
    }

    @PutMapping("/{id}")
    public Client updateClient(@PathVariable Long id, @RequestBody Client updatedClient) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client introuvable"));

        client.setRaisonSociale(updatedClient.getRaisonSociale());
        client.setIce(updatedClient.getIce());
        client.setRc(updatedClient.getRc());
        client.setIfNumber(updatedClient.getIfNumber());
        client.setCnss(updatedClient.getCnss());
        client.setEmail(updatedClient.getEmail());
        client.setPhone(updatedClient.getPhone());
        client.setCity(updatedClient.getCity());
        client.setAddress(updatedClient.getAddress());

        return clientRepository.save(client);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client introuvable"));

        client.setActive(false);
        clientRepository.save(client);

        return ResponseEntity.noContent().build();
    }
}
