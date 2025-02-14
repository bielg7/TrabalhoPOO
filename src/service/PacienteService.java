package service;

import model.Paciente;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static util.ValidaCPF.formatarCPF;

public class PacienteService {

    private final List<Paciente> pacientes = new ArrayList<>();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    /**
     * Cadastra um novo paciente, validando:
     * - Nome não pode ser vazio;
     * - CPF deve ter 11 dígitos, ser numérico, formatado e não pode estar duplicado;
     * - Data de nascimento deve estar no formato correto e não pode ser uma data futura.
     */
    public void cadastrarPaciente() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Bem vindo ao sistema de cadastro do paciente:");

        // Validação do nome
        String nome;
        while (true) {
            System.out.print("Digite o nome do Paciente: ");
            nome = scanner.nextLine();
            if (nome == null || nome.trim().isEmpty()) {
                System.out.println("Nome inválido! Por favor, informe um nome.");
            } else {
                break;
            }
        }

        // Validação do CPF
        String cpf;
        while (true) {
            System.out.print("Digite o CPF (apenas números): ");
            cpf = scanner.nextLine();
            if (!cpf.matches("\\d{11}")) {
                System.out.println("CPF inválido! Digite exatamente 11 números.");
                continue;
            }
            cpf = formatarCPF(cpf); // formata o CPF conforme padrão (ex: 000.000.000-00)
            if (buscarPorCpf(cpf) != null) {
                System.out.println("Já existe um paciente cadastrado com este CPF. Informe outro.");
                continue;
            }
            // Aqui você pode incluir uma validação mais completa (algoritmo de verificação do CPF)
            break;
        }

        // Validação da Data de Nascimento
        LocalDate dataNascimento;
        while (true) {
            System.out.print("Digite a Data de Nascimento (DD-MM-YYYY): ");
            String dataInput = scanner.nextLine();
            try {
                dataNascimento = LocalDate.parse(dataInput, formatter);
                if (dataNascimento.isAfter(LocalDate.now())) {
                    System.out.println("Data de nascimento não pode ser no futuro.");
                    continue;
                }
                break;
            } catch (Exception e) {
                System.out.println("Formato inválido! Use DD-MM-YYYY.");
            }
        }

        Paciente paciente = new Paciente(nome, cpf, dataNascimento, new ArrayList<>(), new ArrayList<>());
        pacientes.add(paciente);
        System.out.println("Paciente cadastrado com sucesso!");
    }

    /**
     * Busca um paciente pelo CPF (deve estar formatado da mesma forma que no cadastro).
     */
    public Paciente buscarPorCpf(String cpf) {
        for (Paciente p : pacientes) {
            if (p.getCpf().equals(cpf)) {
                return p;
            }
        }
        return null;
    }

    /**
     * Lista todos os pacientes cadastrados.
     */
    public void listarPacientes() {
        if (pacientes.isEmpty()) {
            System.out.println("Nenhum paciente cadastrado.");
        } else {
            System.out.println("Lista de pacientes:");
            for (Paciente p : pacientes) {
                System.out.println("Nome: " + p.getNome() +
                        " - CPF: " + p.getCpf() +
                        " - Data de Nascimento: " + p.getDataNascimento().format(formatter));
            }
        }
    }

    /**
     * Atualiza os dados do paciente, realizando as mesmas validações do cadastro.
     * Se o usuário deixar o campo em branco, o valor atual é mantido.
     */
    public void atualizarCadastroPaciente(Paciente paciente) {
        if (paciente == null) {
            System.out.println("Paciente não encontrado.");
            return;
        }
        Scanner scanner = new Scanner(System.in);

        System.out.println("Atualizando cadastro do paciente:");

        // Atualiza o nome, se informado
        System.out.print("Nome (atual: " + paciente.getNome() + "): ");
        String novoNome = scanner.nextLine();
        if (!novoNome.trim().isEmpty()) {
            paciente.setNome(novoNome);
        }

        // Atualiza o CPF, se informado (com validação e verificação de duplicidade)
        System.out.print("CPF (apenas números) (atual: " + paciente.getCpf() + "): ");
        String novoCpf = scanner.nextLine();
        if (!novoCpf.trim().isEmpty()) {
            while (true) {
                if (!novoCpf.matches("\\d{11}")) {
                    System.out.print("CPF inválido! Digite exatamente 11 números: ");
                    novoCpf = scanner.nextLine();
                    continue;
                }
                novoCpf = formatarCPF(novoCpf);
                // Se o novo CPF for diferente do atual, verifica duplicidade
                if (!novoCpf.equals(paciente.getCpf()) && buscarPorCpf(novoCpf) != null) {
                    System.out.print("Já existe um paciente com este CPF. Informe outro: ");
                    novoCpf = scanner.nextLine();
                    continue;
                }
                break;
            }
            paciente.setCpf(novoCpf);
        }

        // Atualiza a data de nascimento, se informado (com validação do formato e se não é data futura)
        System.out.print("Data de Nascimento (DD-MM-YYYY) (atual: " +
                paciente.getDataNascimento().format(formatter) + "): ");
        String novaData = scanner.nextLine();
        if (!novaData.trim().isEmpty()) {
            LocalDate novaDataNascimento;
            while (true) {
                try {
                    novaDataNascimento = LocalDate.parse(novaData, formatter);
                    if (novaDataNascimento.isAfter(LocalDate.now())) {
                        System.out.print("Data de nascimento não pode ser no futuro. Digite uma data válida: ");
                        novaData = scanner.nextLine();
                        continue;
                    }
                    break;
                } catch (Exception e) {
                    System.out.print("Formato inválido! Use DD-MM-YYYY: ");
                    novaData = scanner.nextLine();
                }
            }
            paciente.setDataNascimento(novaDataNascimento);
        }

        System.out.println("Cadastro atualizado com sucesso!");
    }

    /**
     * Remove o paciente da lista, se existir.
     */
    public void removerPaciente(Paciente paciente) {
        if (paciente != null) {
            pacientes.remove(paciente);
        }
    }
}
