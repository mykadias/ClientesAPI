package br.com.clienteapi.interfaces;

import org.springframework.data.repository.CrudRepository;
import br.com.clienteapi.entities.Cliente;

public interface IClienteRepository extends CrudRepository<Cliente, Integer> {

}
