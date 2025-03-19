
import java.io.*;
import java.util.*;

class Pessoa{
    private String nome;
    private int idade;

    public Pessoa(String nome, int idade){
        this.nome = nome;
        this.idade = idade;
    }
    
    public void apresentar(){
        System.out.println("Nome: " + nome + "Idade: " + idade);
    }
    
    public String getNome(){
        return nome;
    }
}

class Funcionario extends Pessoa{
    
}
