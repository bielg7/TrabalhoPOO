package service;

import model.Medicamento;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MedicamentoService {

    private final List<Medicamento> medicamentos = new ArrayList<>();

    /**
     * Cadastra um novo medicamento, validando os campos obrigatórios.
     */
    public void cadastrarMedicamento() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Cadastro de Medicamento ===");

        // Validação do Nome
        String nome;
        while (true) {
            System.out.print("Digite o nome do medicamento: ");
            nome = scanner.nextLine().trim();
            if (nome.isEmpty()) {
                System.out.println("Nome inválido! Informe um nome.");
            } else {
                break;
            }
        }

        // Validação da Dosagem
        String dosagem;
        while (true) {
            System.out.print("Digite a dosagem (ex: 500mg): ");
            dosagem = scanner.nextLine().trim();
            if (dosagem.isEmpty()) {
                System.out.println("Dosagem inválida! Informe a dosagem.");
            } else {
                break;
            }
        }

        // Validação da Posologia
        String posologia;
        while (true) {
            System.out.print("Digite a posologia (ex: 1 comprimido a cada 8 horas): ");
            posologia = scanner.nextLine().trim();
            if (posologia.isEmpty()) {
                System.out.println("Posologia inválida! Informe as instruções de uso.");
            } else {
                break;
            }
        }

        // Validação do Preço
        double preco;
        while (true) {
            System.out.print("Digite o preço do medicamento: ");
            String precoInput = scanner.nextLine();
            try {
                preco = Double.parseDouble(precoInput);
                if (preco < 0) {
                    System.out.println("Preço inválido! O valor não pode ser negativo.");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido! Informe um número.");
            }
        }

        Medicamento medicamento = new Medicamento(nome, dosagem, posologia, preco);
        medicamentos.add(medicamento);
        System.out.println("Medicamento cadastrado com sucesso!");
    }

    /**
     * Lista todos os medicamentos cadastrados.
     */
    public void listarMedicamentos() {
        if (medicamentos.isEmpty()) {
            System.out.println("Nenhum medicamento cadastrado.");
        } else {
            System.out.println("=== Lista de Medicamentos ===");
            for (int i = 0; i < medicamentos.size(); i++) {
                Medicamento med = medicamentos.get(i);
                System.out.println("Índice: " + i +
                        " | Nome: " + med.getNome() +
                        " | Dosagem: " + med.getDosagem() +
                        " | Posologia: " + med.getPosologia() +
                        " | Preço: " + med.getPreco());
            }
        }
    }

    /**
     * Atualiza os dados de um medicamento cadastrado.
     * Caso o campo seja deixado em branco, o valor atual é mantido.
     */
    public void atualizarMedicamento() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Atualização de Medicamento ===");

        // Listar medicamentos para seleção
        listarMedicamentos();
        System.out.print("Informe o índice do medicamento que deseja atualizar: ");
        int indice = -1;
        try {
            indice = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Índice inválido!");
            return;
        }
        if (indice < 0 || indice >= medicamentos.size()) {
            System.out.println("Índice não encontrado!");
            return;
        }
        Medicamento med = medicamentos.get(indice);

        // Atualizar Nome
        System.out.print("Nome (atual: " + med.getNome() + "): ");
        String novoNome = scanner.nextLine().trim();
        if (!novoNome.isEmpty()) {
            med.setNome(novoNome);
        }

        // Atualizar Dosagem
        System.out.print("Dosagem (atual: " + med.getDosagem() + "): ");
        String novaDosagem = scanner.nextLine().trim();
        if (!novaDosagem.isEmpty()) {
            med.setDosagem(novaDosagem);
        }

        // Atualizar Posologia
        System.out.print("Posologia (atual: " + med.getPosologia() + "): ");
        String novaPosologia = scanner.nextLine().trim();
        if (!novaPosologia.isEmpty()) {
            med.setPosologia(novaPosologia);
        }

        // Atualizar Preço
        System.out.print("Preço (atual: " + med.getPreco() + "): ");
        String novoPreco = scanner.nextLine().trim();
        if (!novoPreco.isEmpty()) {
            double precoValor;
            while (true) {
                try {
                    precoValor = Double.parseDouble(novoPreco);
                    if (precoValor < 0) {
                        System.out.print("Preço não pode ser negativo. Informe um valor válido: ");
                        novoPreco = scanner.nextLine().trim();
                        continue;
                    }
                    med.setPreco(precoValor);
                    break;
                } catch (NumberFormatException e) {
                    System.out.print("Valor inválido! Informe um número: ");
                    novoPreco = scanner.nextLine().trim();
                }
            }
        }

        System.out.println("Medicamento atualizado com sucesso!");
    }

    /**
     * Remove um medicamento da lista com base no índice informado.
     */
    public String removerMedicamento() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Remoção de Medicamento ===");

        listarMedicamentos();
        System.out.print("Informe o índice do medicamento que deseja remover: ");
        int indice = -1;
        try {
            indice = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return "Índice inválido!";
        }
        if (indice < 0 || indice >= medicamentos.size()) {
            return "Índice não encontrado!";
        }
        medicamentos.remove(indice);
        return "Medicamento removido com sucesso!";
    }

    public Medicamento buscarMedicamento(String nome) {
        for (Medicamento med : medicamentos) {
            if (med.getNome().equals(nome)) {
                return med;
            }
        }
        return null;
    }
}
