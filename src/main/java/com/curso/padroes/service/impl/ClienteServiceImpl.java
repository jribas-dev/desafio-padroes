package com.curso.padroes.service.impl;

import java.util.Optional;

import com.curso.padroes.exception.ClienteNotFound;
import com.curso.padroes.model.Cliente;
import com.curso.padroes.model.ClienteRepository;
import com.curso.padroes.model.Endereco;
import com.curso.padroes.model.EnderecoRepository;
import com.curso.padroes.service.ClienteService;
import com.curso.padroes.service.ViaCepService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementação da <b>Strategy</b> {@link ClienteService}, a qual pode ser
 * injetada pelo Spring (via {@link Autowired}). Com isso, como essa classe é um
 * {@link Service}, ela será tratada como um <b>Singleton</b>.
 * 
 * @author jribas-dev
 */
@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {

	// Singleton: Injetar os componentes do Spring com @Autowired.
	private final ClienteRepository clienteRepository;
	private final EnderecoRepository enderecoRepository;
	private final ViaCepService viaCepService;
	
	// Strategy: Implementar os métodos definidos na 'interface'.
	// Facade: Abstrair integrações com subsistemas, provendo uma 'interface' simples.

	@Override
	public Iterable<Cliente> buscarTodos() {
		// Buscar todos os Clientes.
		return clienteRepository.findAll();
	}

	@Override
	public Cliente buscarPorId(Long id) {
		// Buscar Cliente por ID.
		Optional<Cliente> cliente = clienteRepository.findById(id);
		return cliente.orElseThrow(() -> new ClienteNotFound(id));
	}

	@Override
	public void inserir(Cliente cliente) {
		salvarClienteComCep(cliente);
	}

	@Override
	public void atualizar(Long id, Cliente cliente) {
		// Buscar Cliente por ID, caso exista:
		Optional<Cliente> clienteBd = clienteRepository.findById(id);
		if (clienteBd.isPresent()) {
			if (cliente.getId() == null) { cliente.setId(clienteBd.get().getId()); }
			if (cliente.getNome() == null) { cliente.setNome(clienteBd.get().getNome()); }
			if (cliente.getEndereco() == null) {cliente.setEndereco(clienteBd.get().getEndereco()); }
			if (cliente.getEndereco().getCep() == null) { cliente.getEndereco().setCep(clienteBd.get().getEndereco().getCep()); }
			salvarClienteComCep(cliente);
		}
		else {
			throw new ClienteNotFound(id);
		}
	}

	@Override
	public void deletar(Long id) {
		// Deletar Cliente por ID.
		clienteRepository.deleteById(id);
	}

	private void salvarClienteComCep(Cliente cliente) {
		// Verificar se o Endereco do Cliente já existe (pelo CEP).
		String cep = cliente.getEndereco().getCep();
		Endereco endereco = enderecoRepository.findById(cep).orElseGet(() -> {
			// Caso não exista, integrar com o ViaCEP e persistir o retorno.
			Endereco novoEndereco = viaCepService.consultarCep(cep);
			enderecoRepository.save(novoEndereco);
			return novoEndereco;
		});
		cliente.setEndereco(endereco);
		// Inserir Cliente, vinculando o Endereco (novo ou existente).
		clienteRepository.save(cliente);
	}

}
