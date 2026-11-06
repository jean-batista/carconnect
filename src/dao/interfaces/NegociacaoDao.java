package dao.interfaces;

import java.util.List;
import java.util.UUID;

import entidades.Negociacao;

public interface NegociacaoDao {

    // Efetua uma Negociacao
    void cadastrar(Negociacao negociacao);

    // Busca uma Negociacao pelo id
    Negociacao buscarNegociacaoPorId(UUID id);

    // Buscar todas as negociacoes
    List<Negociacao> buscarNegociacoes();

    // Buscar todas as negociacoes por Cliente
    List<Negociacao> buscarNegociacoesPorCliente(UUID clienteId);
    
    // Buscar todas as negociacoes por Vendedor
    List<Negociacao> buscarNegociacoesPorVendedor(UUID vendedorId);

}
