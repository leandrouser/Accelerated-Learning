package Main;

import java.util.Scanner;

import Model.Tarefa;
import Servico.TarefaServico;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TarefaServico tarefaServico = new TarefaServico();
        boolean running = true;

        while (running) {
            System.out.println("\nGerenciador de Tarefas");
            System.out.println("1. Adicionar tarefa");
            System.out.println("2. Listar tarefas");
            System.out.println("3. Atualizar tarefa");
            System.out.println("4. Deletar tarefa");
            System.out.println("5. Sair");
            System.out.print("Escolha uma opção: ");

            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1 -> {
                    System.out.println("Descrição de tarefa: ");
                    String nome = scanner.nextLine();
                    Tarefa tarefa = tarefaServico.addTarefa(nome);
                    System.out.println("Tarefa adicionada: " + tarefa);
                }
                case 2 -> {
                    System.out.println("\nLista de Tarefas:");
                    for (Tarefa tarefa : tarefaServico.listTarefa()) {
                        System.out.println(tarefa);
                    }
                }
                case 3 -> {
                    System.out.print("ID da tarefa para atualizar: ");
                    int id = scanner.nextInt();
                    scanner.nextLine();  

                    Tarefa tarefaExistente = tarefaServico.buscarTarefaPorId(id);
                    if (tarefaExistente == null) {
                        System.out.println("Tarefa não cadastrada, tente novamente.");
                        break; 
                    }

                    System.out.print("Nova descrição: ");
                    String nome = scanner.nextLine();

                    System.out.print("Está concluída? ");
                    System.out.println("1 - Sim, 2 - Não");
                    int concluidoInput = scanner.nextInt();

                    boolean concluido = (concluidoInput == 1); // 1 -> true, 2 -> false

                    boolean atualizado = tarefaServico.atualizarTarefa(id, nome, concluido);

                    if (atualizado) {
                        System.out.println("Tarefa atualizada com sucesso!");
                    } else {
                        System.out.println("Erro ao atualizar a tarefa.");
                    }
                }

                case 4 -> {
                        System.out.print("ID da tarefa para deletar: ");
                        int id = scanner.nextInt();
                        if (tarefaServico.deleteTarefa(id)) {
                            System.out.println("Tarefa deletada com sucesso!");
                        } else {
                            System.out.println("Tarefa não encontrada.");
                        }
                    }
                case 5 -> {
                    running = false;
                    System.out.println("Encerrando o programa...");
                }
                default -> System.out.println("Opção inválida.");
            }

        }
        scanner.close();
    }
}
