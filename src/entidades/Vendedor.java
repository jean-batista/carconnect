package entidades;

import java.util.List;
import java.util.UUID;

public class Vendedor extends Pessoa {
    
    // Vendas realizadas pelo Vendedor
    private List<Negociacao> vendas;

    // Agendamentos do Vendedor
    private List<Agendamento> agendamentos;
    
    // Construtor sem argumentos
    public Vendedor() {
    }

    // Construtor com argumentos
    public Vendedor(UUID id, String nome, String sobrenome, String email, String telefone, String cpf, String senha,
            List<Negociacao> vendas, List<Agendamento> agendamentos) {
        super(id, nome, sobrenome, email, telefone, cpf, senha);
        this.vendas = vendas;
        this.agendamentos = agendamentos;
    }

    // Getters

    public List<Negociacao> getVendas() {
        return vendas;
    }

    public List<Agendamento> getAgendamentos() {
        return agendamentos;
    }

    // Setters

    public void setVendas(List<Negociacao> vendas) {
        this.vendas = vendas;
    }

    public void setAgendamentos(List<Agendamento> agendamentos) {
        this.agendamentos = agendamentos;
    }

    // Hashcode e equals

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((vendas == null) ? 0 : vendas.hashCode());
        result = prime * result + ((agendamentos == null) ? 0 : agendamentos.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        Vendedor other = (Vendedor) obj;
        if (vendas == null) {
            if (other.vendas != null)
                return false;
        } else if (!vendas.equals(other.vendas))
            return false;
        if (agendamentos == null) {
            if (other.agendamentos != null)
                return false;
        } else if (!agendamentos.equals(other.agendamentos))
            return false;
        return true;
    }

}
