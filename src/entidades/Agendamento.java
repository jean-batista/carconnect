package entidades;

import java.time.LocalDateTime;
import java.util.UUID;

public class Agendamento {
    
    // Identificador do Agendamento
    private UUID id;

    // Data em que o Agendamento foi feito 
    private LocalDateTime agendadoEm;

    // Data do agendamento
    private LocalDateTime agendadoPara;

    // Cliente que solicitou o Agendamento
    private Cliente cliente;

    // Vendedor responsavel pelo Agendamento
    private Vendedor vendedor;
    
    // Cosntrutor sem argumentos
    public Agendamento() {
    }

    // Construtor com argumentos
    public Agendamento(UUID id, LocalDateTime agendadoEm, LocalDateTime agendadoPara, Cliente cliente,
            Vendedor vendedor) {
        this.id = id;
        this.agendadoEm = agendadoEm;
        this.agendadoPara = agendadoPara;
        this.cliente = cliente;
        this.vendedor = vendedor;
    }

    // Getters

    public UUID getId() {
        return id;
    }

    public LocalDateTime getAgendadoEm() {
        return agendadoEm;
    }

    public LocalDateTime getAgendadoPara() {
        return agendadoPara;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Vendedor getVendedor() {
        return vendedor;
    }

    // Setters

    public void setId(UUID id) {
        this.id = id;
    }

    public void setAgendadoEm(LocalDateTime agendadoEm) {
        this.agendadoEm = agendadoEm;
    }

    public void setAgendadoPara(LocalDateTime agendadoPara) {
        this.agendadoPara = agendadoPara;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public void setVendedor(Vendedor vendedor) {
        this.vendedor = vendedor;
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
        Agendamento other = (Agendamento) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
