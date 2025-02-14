package service;

import model.Exame;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ExameService {

    private final List<Exame> exames = new ArrayList<>();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    /**
     * Cadastra um novo exame, validando os campos obrigatórios.
     */
    public void cadastrarExame() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Cadastro de Exame ===");

        // Validação do Tipo (aceita SANGUE, RAIO_X ou ULTRASSOM)
        String tipo;
        while (true) {
            System.out.print("Digite o tipo do exame (SANGUE, RAIO_X, ULTRASSOM): ");
            tipo = scanner.nextLine().trim().toUpperCase();
            if (!tipo.equals("SANGUE") && !tipo.equals("RAIO_X") && !tipo.equals("ULTRASSOM")) {
                System.out.println("Tipo inválido! Informe SANGUE, RAIO_X ou ULTRASSOM.");
            } else {
                break;
            }
        }

        // Validação da Data de Prescrição
        LocalDate dataPrescricao;
        while (true) {
            System.out.print("Digite a data de prescrição (DD-MM-YYYY): ");
            String dataInput = scanner.nextLine();
            try {
                dataPrescricao = LocalDate.parse(dataInput, formatter);
                if (dataPrescricao.isAfter(LocalDate.now())) {
                    System.out.println("A data de prescrição não pode ser futura.");
                    continue;
                }
                break;
            } catch (Exception e) {
                System.out.println("Formato inválido! Utilize DD-MM-YYYY.");
            }
        }

        // Validação da Data de Realização (não pode ser anterior à prescrição nem futura)
        LocalDate dataRealizacao;
        while (true) {
            System.out.print("Digite a data de realização (DD-MM-YYYY): ");
            String dataInput = scanner.nextLine();
            try {
                dataRealizacao = LocalDate.parse(dataInput, formatter);
                if (dataRealizacao.isBefore(dataPrescricao)) {
                    System.out.println("A data de realização não pode ser anterior à data de prescrição.");
                    continue;
                }
                if (dataRealizacao.isAfter(LocalDate.now())) {
                    System.out.println("A data de realização não pode ser futura.");
                    continue;
                }
                break;
            } catch (Exception e) {
                System.out.println("Formato inválido! Utilize DD-MM-YYYY.");
            }
        }

        // Validação do Resultado (não pode ser vazio)
        String resultado;
        while (true) {
            System.out.print("Digite o resultado do exame: ");
            resultado = scanner.nextLine();
            if (resultado == null || resultado.trim().isEmpty()) {
                System.out.println("Resultado inválido! Informe o resultado do exame.");
            } else {
                break;
            }
        }

        // Validação do Custo (valor numérico e não negativo)
        double custo;
        while (true) {
            System.out.print("Digite o custo do exame: ");
            String custoInput = scanner.nextLine();
            try {
                custo = Double.parseDouble(custoInput);
                if (custo < 0) {
                    System.out.println("Custo inválido! O valor não pode ser negativo.");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido! Informe um número.");
            }
        }

        Exame exame = new Exame(tipo, dataPrescricao, dataRealizacao, resultado, custo);
        exames.add(exame);
        System.out.println("Exame cadastrado com sucesso!");
    }

    /**
     * Lista todos os exames cadastrados.
     */
    public void listarExames() {
        if (exames.isEmpty()) {
            System.out.println("Nenhum exame cadastrado.");
        } else {
            System.out.println("=== Lista de Exames ===");
            for (int i = 0; i < exames.size(); i++) {
                Exame ex = exames.get(i);
                System.out.println("Índice: " + i +
                        " | Tipo: " + ex.getTipo() +
                        " | Data Prescrição: " + ex.getDataPrescricao().format(formatter) +
                        " | Data Realização: " + ex.getDataRealizacao().format(formatter) +
                        " | Resultado: " + ex.getResultado() +
                        " | Custo: " + ex.getCusto());
            }
        }
    }

    /**
     * Atualiza os dados de um exame já cadastrado.
     * O usuário informa o índice e, se o campo ficar em branco, o valor atual é mantido.
     */
    public Exame atualizarExame() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Atualização de Exame ===");

        // Listar exames para seleção
        listarExames();
        System.out.print("Informe o índice do exame que deseja atualizar: ");
        int indice = -1;
        try {
            indice = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Índice inválido!");
            return null;
        }
        if (indice < 0 || indice >= exames.size()) {
            System.out.println("Índice não encontrado!");
            return null;
        }
        Exame exame = exames.get(indice);

        // Atualizar Tipo
        System.out.print("Tipo (atual: " + exame.getTipo() + ") [SANGUE, RAIO_X, ULTRASSOM]: ");
        String novoTipo = scanner.nextLine().trim().toUpperCase();
        if (!novoTipo.isEmpty()) {
            while (!novoTipo.equals("SANGUE") && !novoTipo.equals("RAIO_X") && !novoTipo.equals("ULTRASSOM")) {
                System.out.print("Tipo inválido! Informe SANGUE, RAIO_X ou ULTRASSOM: ");
                novoTipo = scanner.nextLine().trim().toUpperCase();
            }
            exame.setTipo(novoTipo);
        }

        // Atualizar Data de Prescrição
        System.out.print("Data de Prescrição (atual: " + exame.getDataPrescricao().format(formatter) + ") (DD-MM-YYYY): ");
        String novaDataPrescricao = scanner.nextLine();
        if (!novaDataPrescricao.trim().isEmpty()) {
            LocalDate novaData;
            while (true) {
                try {
                    novaData = LocalDate.parse(novaDataPrescricao, formatter);
                    if (novaData.isAfter(LocalDate.now())) {
                        System.out.print("Data de prescrição não pode ser futura. Informe novamente (DD-MM-YYYY): ");
                        novaDataPrescricao = scanner.nextLine();
                        continue;
                    }
                    exame.setDataPrescricao(novaData);
                    break;
                } catch (Exception e) {
                    System.out.print("Formato inválido! Use DD-MM-YYYY: ");
                    novaDataPrescricao = scanner.nextLine();
                }
            }
        }

        // Atualizar Data de Realização
        System.out.print("Data de Realização (atual: " + exame.getDataRealizacao().format(formatter) + ") (DD-MM-YYYY): ");
        String novaDataRealizacao = scanner.nextLine();
        if (!novaDataRealizacao.trim().isEmpty()) {
            LocalDate novaData;
            while (true) {
                try {
                    novaData = LocalDate.parse(novaDataRealizacao, formatter);
                    if (novaData.isBefore(exame.getDataPrescricao())) {
                        System.out.print("Data de realização não pode ser anterior à data de prescrição. Informe novamente (DD-MM-YYYY): ");
                        novaDataRealizacao = scanner.nextLine();
                        continue;
                    }
                    if (novaData.isAfter(LocalDate.now())) {
                        System.out.print("Data de realização não pode ser futura. Informe novamente (DD-MM-YYYY): ");
                        novaDataRealizacao = scanner.nextLine();
                        continue;
                    }
                    exame.setDataRealizacao(novaData);
                    break;
                } catch (Exception e) {
                    System.out.print("Formato inválido! Use DD-MM-YYYY: ");
                    novaDataRealizacao = scanner.nextLine();
                }
            }
        }

        // Atualizar Resultado
        System.out.print("Resultado (atual: " + exame.getResultado() + "): ");
        String novoResultado = scanner.nextLine();
        if (!novoResultado.trim().isEmpty()) {
            exame.setResultado(novoResultado);
        }

        // Atualizar Custo
        System.out.print("Custo (atual: " + exame.getCusto() + "): ");
        String novoCusto = scanner.nextLine();
        if (!novoCusto.trim().isEmpty()) {
            double custoValor;
            while (true) {
                try {
                    custoValor = Double.parseDouble(novoCusto);
                    if (custoValor < 0) {
                        System.out.print("Custo não pode ser negativo. Informe um valor válido: ");
                        novoCusto = scanner.nextLine();
                        continue;
                    }
                    exame.setCusto(custoValor);
                    break;
                } catch (NumberFormatException e) {
                    System.out.print("Valor inválido! Informe um número: ");
                    novoCusto = scanner.nextLine();
                }
            }
        }

        System.out.println("Exame atualizado com sucesso!");
        return exame;
    }

    /**
     * Remove um exame da lista com base no índice informado.
     */
    public String removerExame() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Remoção de Exame ===");

        listarExames();
        System.out.print("Informe o índice do exame que deseja remover: ");
        int indice = -1;
        try {
            indice = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return "Índice inválido!";
        }
        if (indice < 0 || indice >= exames.size()) {
            return "Índice não encontrado!";
        }
        exames.remove(indice);
        return "Exame removido com sucesso!";
    }

    public Exame buscarExamePorTipo (String tipo) {
        for (Exame ex : exames) {
            if (ex.getTipo().equals(tipo)) {
                return ex;
            }
        }
        return null;
    }
}
