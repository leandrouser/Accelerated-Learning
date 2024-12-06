package Model;

public class Tarefa {
    private int id;
    private String nome;
    private boolean concluido;

    public Tarefa(int id, String nome, boolean concluido){
        this.id = id;
        this.nome = nome;
        this.concluido = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public boolean isConcluido() {
        return concluido;
    }

    public void setConcluido(boolean concluido) {
        this.concluido = concluido;
    }

    @Override
    public String toString(){
        return String.format("ID: %d | Descrição: %s | Concluída: %s", id, nome, concluido ? "Sim" : "Não");
    }
}
