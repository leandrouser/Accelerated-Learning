package Servico;

import Dao.TarefaDao;
import Model.Tarefa;

import java.util.List;
import java.util.Optional;

public class TarefaServico {
    private final TarefaDao tarefaDao = new TarefaDao();

    public Tarefa addTarefa(String nome){
        return tarefaDao.create(nome);
    }
    public List<Tarefa> listTarefa(){
        return tarefaDao.findAll();
    }
    public Optional<Tarefa> getTarefa(int id){
        return tarefaDao.findById(id);
    }
    public boolean atualizarTarefa(int id, String nome, boolean completo){
        return tarefaDao.atualizar(id, nome, completo);
    }
    public Tarefa buscarTarefaPorId(int id) {
        for (Tarefa tarefa : listTarefa()) {
            if (tarefa.getId() == id) {
                return tarefa;  
            }
        }
        return null;
    }
    public boolean deleteTarefa(int id){
        return tarefaDao.delete(id);
    }
}
