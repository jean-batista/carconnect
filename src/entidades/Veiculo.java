package entidades;

import java.util.UUID;

public class Veiculo {

    // Indetificador do Veiculo
    private UUID id;

    // Marca do Veiculo
    private String marca;

    // Modelo do Veiculo
    private String modelo;

    // Ano do Veiculo
    private Integer ano;

    // Combustivel do Veiculo
    private String combustivel;

    // Condicao do Veiculo
    private String condicao;

    // Preco do Veiculo
    private Double preco;

    // Unidades em estoque
    private Integer quantidadeEstoque;

    // Construtor sem argumentos
    public Veiculo() {
    }

    // Construtor com argumentos
    public Veiculo(UUID id, String marca, String modelo, Integer ano, String combustivel, String condicao, Double preco,
            Integer quantidadeEstoque) {
        this.id = id;
        this.marca = marca;
        this.modelo = modelo;
        this.ano = ano;
        this.combustivel = combustivel;
        this.condicao = condicao;
        this.preco = preco;
        this.quantidadeEstoque = quantidadeEstoque;
    }

    // Getters

    public UUID getId() {
        return id;
    }

    public String getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
    }

    public Integer getAno() {
        return ano;
    }

    public String getCombustivel() {
        return combustivel;
    }

    public String getCondicao() {
        return condicao;
    }

    public Double getPreco() {
        return preco;
    }

    public Integer getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    // Setters

    public void setId(UUID id) {
        this.id = id;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public void setCombustivel(String combustivel) {
        this.combustivel = combustivel;
    }

    public void setCondicao(String condicao) {
        this.condicao = condicao;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public void setQuantidadeEstoque(Integer quantidadeEstoque) {
        this.quantidadeEstoque = quantidadeEstoque;
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
        Veiculo other = (Veiculo) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
    
}
