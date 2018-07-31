package br.ufu.facom.cursomc.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.ufu.facom.cursomc.domain.ItemPedido;
import br.ufu.facom.cursomc.domain.PagamentoComBoleto;
import br.ufu.facom.cursomc.domain.Pedido;
import br.ufu.facom.cursomc.domain.enums.EstadoPagamento;
import br.ufu.facom.cursomc.repositories.ItemPedidoRepository;
import br.ufu.facom.cursomc.repositories.PagamentoRepository;
import br.ufu.facom.cursomc.repositories.PedidoRepository;
import br.ufu.facom.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class PedidoService {
	
	@Autowired // Isso faz com que a dependencia seja automaticamente instanciada pelo String
	private PedidoRepository repo; 
	
	@Autowired
	private BoletoService boletoService;
	
	@Autowired
	private PagamentoRepository pagamentoRepository;
	
	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private ItemPedidoRepository itemPedidoRepository;
	
	@Autowired
	private ClienteService clienteService;
	
	public Pedido find(Integer id) {
		Optional<Pedido> obj = repo.findById(id);
		// TRATAMENTO DE ERRO PARA CASO NAO EXISTA OBJETO
		return obj.orElseThrow( () -> new ObjectNotFoundException(
				"Objeto nao encontrado! Id: " + id + ", Tipo: " + Pedido.class.getName()));
	}
	
	@Transactional
	public Pedido insert(Pedido obj) {
		obj.setId(null); // Usado para garantir que estou inserindo um Pedido NOVO (com id null)
		obj.setInstante(new Date()); // Isso faz com que o instante seja o instante em que o pedido eh adicionado
		obj.setCliente(clienteService.find(obj.getCliente().getId())); // Comando feito pra tirar o NULL do cliente na impressao
		obj.getPagamento().setEstado(EstadoPagamento.PENDENTE); // Um pedido inserido agora eh PENDENTE
		obj.getPagamento().setPedido(obj); // Faz com que o pagamento conheca quem eh o seu Pedido
		
		if(obj.getPagamento() instanceof PagamentoComBoleto) {
			// Se o pagamento for com boleto, precisa gerar um vencimento
			PagamentoComBoleto pagto = (PagamentoComBoleto) obj.getPagamento();
			boletoService.preencherPagamentoComBoleto(pagto,obj.getInstante());
		}
		
		obj = repo.save(obj); // Salva o pedido
		
		// Salvando Pagamento:
		pagamentoRepository.save(obj.getPagamento());
		
		// Salvando ItemPedido
		for (ItemPedido ip: obj.getItens()){
			ip.setDesconto(0.0); // Desconto sera sempre 0
			ip.setProduto(produtoService.find(ip.getProduto().getId())); // Comando feito pra tirar o NULL do produto na impressao
			ip.setPreco(produtoService.find(ip.getProduto().getId()).getPreco()); // Seta o mesmo preco
			ip.setPedido(obj); // Item pedido conhece seu Pedido
		}
		
		itemPedidoRepository.saveAll(obj.getItens()); 
		
		System.out.println(obj);
		
		return obj;		
	}
}
