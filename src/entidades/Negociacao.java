package entidades;

import java.time.LocalDateTime;
import java.util.UUID;

public class Negociacao {
    
    // Identificador da Negociacao
    private UUID id;

    // Data da Negociacao
    private LocalDateTime data;

    // Cliente da Negociacao
    private Cliente cliente;

    // Vendedor responsavel pela Negociacao
    private Vendedor vendedor;

    // Veiculo da Negociacao
    private Veiculo veiculo;

    // Construtor sem argumentos
    public Negociacao() {
    }

    // Construtor com argumentos
    public Negociacao(UUID id, LocalDateTime data, Cliente cliente, Vendedor vendedor, Veiculo veiculo) {
        this.id = id;
        this.data = data;
        this.cliente = cliente;
        this.vendedor = vendedor;
        this.veiculo = veiculo;
    }

    // Getters

    public UUID getId() {
        return id;
    }

    public LocalDateTime getData() {
        return data;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Vendedor getVendedor() {
        return vendedor;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    // Setters

    public void setId(UUID id) {
        this.id = id;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public void setVendedor(Vendedor vendedor) {
        this.vendedor = vendedor;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }

    // Hashcode e equals

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Negociacao other = (Negociacao) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    

}
