package br.ufu.facom.cursomc;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import br.ufu.facom.cursomc.domain.Categoria;
import br.ufu.facom.cursomc.domain.Cidade;
import br.ufu.facom.cursomc.domain.Cliente;
import br.ufu.facom.cursomc.domain.Endereco;
import br.ufu.facom.cursomc.domain.Estado;
import br.ufu.facom.cursomc.domain.Produto;
import br.ufu.facom.cursomc.domain.enums.TipoCliente;
import br.ufu.facom.cursomc.repositories.CategoriaRepository;
import br.ufu.facom.cursomc.repositories.CidadeRepository;
import br.ufu.facom.cursomc.repositories.ClienteRepository;
import br.ufu.facom.cursomc.repositories.EnderecoRepository;
import br.ufu.facom.cursomc.repositories.EstadoRepository;
import br.ufu.facom.cursomc.repositories.ProdutoRepository;

@SpringBootApplication
public class CursomcApplication implements CommandLineRunner{
	// Para alterar porta, vai no resources e coloca: server.port=${port:8081}, 8081 eh a porta nova!
	
	@Autowired
	private CategoriaRepository categoriaRepository; // Repositorio eh quem salva dados
	
	@Autowired
	private ProdutoRepository produtoRepository; 
	
	@Autowired
	private EstadoRepository estadoRepository;
	
	@Autowired
	private CidadeRepository cidadeRepository;
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	public static void main(String[] args) {
		SpringApplication.run(CursomcApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// Array as List = cria Lista automatica e posso colocar quantos elementos eu quiser dentro
		
		// Instancia das categorias
		Categoria cat1 = new Categoria(null,"Informatica");
		Categoria cat2 = new Categoria(null,"Escritorio");
		
		// Instacia dos produtos
		Produto p1 = new Produto(null,"Computador",2000.00);
		Produto p2 = new Produto(null,"Impressora",800.00);
		Produto p3 = new Produto(null,"Mouse",80.00);
		
		// Adicionando os produtos em cada categoria
		cat1.getProdutos().addAll(Arrays.asList(p1,p2,p3));
		cat2.getProdutos().addAll(Arrays.asList(p2));
		
		// Salvando no repositorio -> BD
		categoriaRepository.saveAll(Arrays.asList(cat1,cat2));
		produtoRepository.saveAll(Arrays.asList(p1,p2,p3));
		
		// Instancia dos estados
		Estado est1 = new Estado(null,"Minas Gerais");
		Estado est2 = new Estado(null,"Sao Paulo");
		
		// Instancia das cidades
		Cidade c1 = new Cidade(null,"Uberlandia", est1);
		Cidade c2 = new Cidade(null,"Sao Paulo",est2);
		Cidade c3 = new Cidade(null,"Campinas",est2);
		
		// Adicionando cidades nos estados
		est1.getCidades().addAll(Arrays.asList(c1));
		est2.getCidades().addAll(Arrays.asList(c2,c3));
		
		// Adicionando as categorias no produto
		p1.getCategorias().addAll(Arrays.asList(cat1));
		p2.getCategorias().addAll(Arrays.asList(cat1,cat2));
		p3.getCategorias().addAll(Arrays.asList(cat1));
		
		// Salvando no repositorio -> BD
		estadoRepository.saveAll(Arrays.asList(est1,est2));
		cidadeRepository.saveAll(Arrays.asList(c1,c2,c3));
		
		// Instancia dos clientes
		Cliente cli1 = new Cliente(null,"Maria Silva","maria@gmail.com","36378912377", TipoCliente.PESSOAFISICA);
			cli1.getTelefones().addAll(Arrays.asList("27363323","93838393")); // Adiciona os telefones
			
		// Instancia dos enderecos
		Endereco e1 = new Endereco(null, "Rua Flores", "300", "Apto 203", "Jardim", "38220834", cli1, c1);
		Endereco e2 = new Endereco(null, "Avenida Matos", "105", "Sala 800", "Centro", "38777012", cli1, c2);
		
		// Adicionando enderecos ao cliente
		cli1.getEnderecos().addAll(Arrays.asList(e1,e2));
		
		// Salvando no repositorio -> BD
		clienteRepository.saveAll(Arrays.asList(cli1));
		enderecoRepository.saveAll(Arrays.asList(e1,e2));
		
	}
	
	
}
