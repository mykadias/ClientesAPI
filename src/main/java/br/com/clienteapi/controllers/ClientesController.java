package br.com.clienteapi.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import br.com.clienteapi.dtos.ClienteGetResponse;
import br.com.clienteapi.dtos.ClientePostRequest;
import br.com.clienteapi.dtos.ClientePutRequest;
import br.com.clienteapi.entities.Cliente;
import br.com.clienteapi.interfaces.IClienteRepository;

@Transactional
@Controller
public class ClientesController {

	@Autowired
	private IClienteRepository clienteRepository;

	@RequestMapping(value = "api/clientes", method = RequestMethod.POST)
	public ResponseEntity<String> post(@RequestBody ClientePostRequest requestPost) {

		try {

			Cliente cliente = new Cliente();

			cliente.setNome(requestPost.getNome());
			cliente.setCpf(requestPost.getCpf());
			cliente.setEmail(requestPost.getEmail());

			clienteRepository.save(cliente);

			return ResponseEntity.status(HttpStatus.CREATED).body("Cliente cadastrado com sucesso!");

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@RequestMapping(value = "api/clientes", method = RequestMethod.PUT)
	public ResponseEntity<String> put(@RequestBody ClientePutRequest requestPut) {

		try {
			Optional<Cliente> consultaPorId = clienteRepository.findById(requestPut.getIdCliente());

			if (consultaPorId.isEmpty()) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body("Cliente nao encontrado, favor verifique o ID informado.");
			}

			Cliente cliente = new Cliente();

			cliente.setIdCliente(requestPut.getIdCliente());
			cliente.setNome(requestPut.getNome());
			cliente.setCpf(requestPut.getCpf());
			cliente.setEmail(requestPut.getEmail());

			clienteRepository.save(cliente);

			return ResponseEntity.status(HttpStatus.CREATED).body("Cliente atualizado com sucesso!");

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@RequestMapping(value = "api/clientes/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<String> delete(@PathVariable("id") Integer id) {

		try {

			Optional<Cliente> consultaPorId = clienteRepository.findById(id);

			if (consultaPorId.isPresent()) {

				Cliente cliente = consultaPorId.get();
				clienteRepository.delete(cliente);

				return ResponseEntity.status(HttpStatus.CREATED).body("Cliente excluido com sucesso");

			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body("Cliente nao foi excluido, favor verificar o ID informado.");
			}

		} catch (Exception e) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Cliente não foi excluido!" + "ERRO:" + e.getMessage());
		}
	}

	@RequestMapping(value = "api/clientes", method = RequestMethod.GET)
	public ResponseEntity<List<ClienteGetResponse>> getAll() {

		try {

			List<Cliente> clientes = (List<Cliente>) clienteRepository.findAll();

			List<ClienteGetResponse> lista = new ArrayList<ClienteGetResponse>();

			for (Cliente cliente : clientes) {
				ClienteGetResponse response = new ClienteGetResponse();
				
				response.setIdCliente(cliente.getIdCliente());
				response.setNome(cliente.getNome());
				response.setCpf(cliente.getCpf());
				response.setEmail(cliente.getEmail());

				lista.add(response);
			}
			return ResponseEntity.status(HttpStatus.OK).body(lista);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);

		}
	}
	
	@RequestMapping(value = "/api/clientes/{id}", method = RequestMethod.GET)
	public ResponseEntity<ClienteGetResponse> getById(@PathVariable("id") Integer id) { 

		try {
			Optional<Cliente> consultaPorId = clienteRepository.findById(id);

			if (consultaPorId.isPresent()) {
		
				Cliente cliente = consultaPorId.get();
				ClienteGetResponse responseGet = new ClienteGetResponse();	
				
				responseGet.setIdCliente(cliente.getIdCliente());
				responseGet.setNome(cliente.getNome());
				responseGet.setCpf(cliente.getCpf());
				responseGet.setEmail(cliente.getEmail());
			
				return ResponseEntity.status(HttpStatus.OK).body(responseGet);
				
			} else {
				return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
			}
			
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}
}
