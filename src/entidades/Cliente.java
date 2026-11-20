package entidades;

import java.util.List;
import java.util.UUID;

public class Cliente extends Pessoa {
    
    // Visitas que o cliente fez
    private List<Agendamento> visitas;

    // Compras que o cliente fez
    private List<Negociacao> compras;

    // Construtor sem argumentos
    public Cliente() {
    }

    // Construtor com argumentos
    public Cliente(UUID id, String nome, String sobrenome, String email, String telefone, String cpf, String senha,
            List<Agendamento> visitas, List<Negociacao> compras) {
        super(id, nome, sobrenome, email, telefone, cpf, senha);
        this.visitas = visitas;
        this.compras = compras;
    }

    // Getters

    public List<Agendamento> getVisitas() {
        return visitas;
    }

    public List<Negociacao> getCompras() {
        return compras;
    }

    // Setters

    public void setVisitas(List<Agendamento> visitas) {
        this.visitas = visitas;
    }

    public void setCompras(List<Negociacao> compras) {
        this.compras = compras;
    }

    // Hashcode e equals

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((visitas == null) ? 0 : visitas.hashCode());
        result = prime * result + ((compras == null) ? 0 : compras.hashCode());
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
        Cliente other = (Cliente) obj;
        if (visitas == null) {
            if (other.visitas != null)
                return false;
        } else if (!visitas.equals(other.visitas))
            return false;
        if (compras == null) {
            if (other.compras != null)
                return false;
        } else if (!compras.equals(other.compras))
            return false;
        return true;
    }

}
