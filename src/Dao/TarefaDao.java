package Dao;

import Model.Tarefa;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TarefaDao {
    private final List<Tarefa> tarefas = new ArrayList<>();
    private int idCaunter = 1;

    public Tarefa create(String nome){
        Tarefa tarefa = new Tarefa(idCaunter++, nome, false);
        tarefas.add(tarefa);
        return tarefa;
    }
    public List<Tarefa> findAll(){
        return new ArrayList<>(tarefas);
    }
    public Optional<Tarefa> findById(int id){
        return tarefas.stream().filter(tarefa -> tarefa.getId() == id).findFirst();
    }
    public boolean atualizar(int id, String nome, boolean concluido){
        Optional<Tarefa> tarefaOpt = findById(id);
        if (tarefaOpt.isPresent()){
            Tarefa tarefa = tarefaOpt.get();
            tarefa.setNome(nome);
            tarefa.setConcluido(concluido);
            return true;
        }
        return false;
    }
    public boolean delete(int id){
        return tarefas.removeIf(tarefa -> tarefa.getId() == id);
    }
}
