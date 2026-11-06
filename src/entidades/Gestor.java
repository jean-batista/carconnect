package entidades;

import java.util.List;
import java.util.UUID;

public class Gestor extends Pessoa {
    
    // Veiculos gerenciados pelo Gestor
    private List<Veiculo> veiculos;

    // Construtor sem argumentos
    public Gestor() {
    }

    // Construtor com argumentos
    public Gestor(UUID id, String nome, String sobrenome, String email, String telefone, String cpf, String senha,
            List<Veiculo> veiculos) {
        super(id, nome, sobrenome, email, telefone, cpf, senha);
        this.veiculos = veiculos;
    }

    // Getters

    public List<Veiculo> getVeiculos() {
        return veiculos;
    }

    // Setters

    public void setVeiculos(List<Veiculo> veiculos) {
        this.veiculos = veiculos;
    }

    // Hashcode e equals

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((veiculos == null) ? 0 : veiculos.hashCode());
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
        Gestor other = (Gestor) obj;
        if (veiculos == null) {
            if (other.veiculos != null)
                return false;
        } else if (!veiculos.equals(other.veiculos))
            return false;
        return true;
    }

    

}
