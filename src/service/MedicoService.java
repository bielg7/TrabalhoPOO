package service;

import model.Medico;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static util.ValidaCPF.formatarCPF;

public class MedicoService {

    private final List<Medico> medicos = new ArrayList<>();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    /**
     * Realiza o cadastro de um novo médico, aplicando as seguintes validações:
     * - ID: deve ser um número inteiro positivo.
     * - Nome: não pode ser vazio.
     * - CPF: deve conter exatamente 11 dígitos numéricos, ser formatado e não pode já estar cadastrado.
     * - Data de Nascimento: deve ser uma data válida no formato DD-MM-YYYY e não pode ser futura.
     * - CRM: não pode ser vazio e não pode estar duplicado.
     * - Especialidade: não pode ser vazia.
     */
    public void cadastrarMedico() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Bem vindo ao sistema de cadastro do médico:");

        // Validação do Nome
        String nome;
        while (true) {
            System.out.print("Digite o nome do Medico: ");
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
            // Formata o CPF (exemplo: 000.000.000-00)
            cpf = formatarCPF(cpf);
            if (buscarPorCpf(cpf) != null) {
                System.out.println("Já existe um médico cadastrado com este CPF. Informe outro.");
                continue;
            }
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

        // Validação do CRM
        String crm;
        while (true) {
            System.out.print("Digite o CRM: ");
            crm = scanner.nextLine();
            if (crm == null || crm.trim().isEmpty()) {
                System.out.println("CRM inválido! Informe o CRM.");
            } else if (buscaPorCrm(crm) != null) {
                System.out.println("Já existe um médico cadastrado com este CRM. Informe outro.");
            } else {
                break;
            }
        }

        // Validação da Especialidade
        String especialidade;
        while (true) {
            System.out.print("Digite a Especialidade: ");
            especialidade = scanner.nextLine();
            if (especialidade == null || especialidade.trim().isEmpty()) {
                System.out.println("Especialidade inválida! Informe a especialidade.");
            } else {
                break;
            }
        }

        Medico medico = new Medico(nome, cpf, dataNascimento, crm, especialidade, new ArrayList<>());
        medicos.add(medico);
        System.out.println("Médico cadastrado com sucesso!");
    }

    /**
     * Busca um médico pelo CRM.
     */
    public Medico buscaPorCrm(String crm) {
        for (Medico m : medicos) {
            if (m.getCrm().equals(crm)) {
                return m;
            }
        }
        return null;
    }

    /**
     * Busca um médico pelo CPF (deve estar formatado da mesma forma que no cadastro).
     */
    public Medico buscarPorCpf(String cpf) {
        for (Medico m : medicos) {
            if (m.getCpf().equals(cpf)) {
                return m;
            }
        }
        return null;
    }

    /**
     * Retorna a lista de médicos cadastrados.
     */
    public void listarMedicos() {
        if (medicos.isEmpty()) {
            System.out.println("Nenhum médico cadastrado.");
        } else {
            System.out.println("Lista de médicos:");
            for (Medico m : medicos) {
                System.out.println("Nome: " + m.getNome() +
                        " - CPF: " + m.getCpf() +
                        " - Data de Nascimento: " + m.getDataNascimento().format(formatter) +
                        " - CRM: " + m.getCrm() +
                        " - Especialidade: " + m.getEspecialidade());
            }
        }
    }

    /**
     * Atualiza o cadastro do médico, permitindo a alteração dos dados.
     * Caso o usuário deixe o campo em branco, o valor atual é mantido.
     */
    public void atualizarCadastroMedico(Medico medico) {
        if (medico == null) {
            System.out.println("Médico não encontrado.");
            return;
        }
        Scanner scanner = new Scanner(System.in);
        System.out.println("Atualizando cadastro do médico:");

        // Atualiza o Nome
        System.out.print("Nome (atual: " + medico.getNome() + "): ");
        String novoNome = scanner.nextLine();
        if (!novoNome.trim().isEmpty()) {
            medico.setNome(novoNome);
        }

        // Atualiza o CPF com validação
        System.out.print("CPF (apenas números) (atual: " + medico.getCpf() + "): ");
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
                if (!novoCpf.equals(medico.getCpf()) && buscarPorCpf(novoCpf) != null) {
                    System.out.print("Já existe um médico com este CPF. Informe outro: ");
                    novoCpf = scanner.nextLine();
                    continue;
                }
                break;
            }
            medico.setCpf(novoCpf);
        }

        // Atualiza a Data de Nascimento com validação
        System.out.print("Data de Nascimento (DD-MM-YYYY) (atual: " +
                medico.getDataNascimento().format(formatter) + "): ");
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
            medico.setDataNascimento(novaDataNascimento);
        }

        // Atualiza o CRM com validação
        System.out.print("CRM (atual: " + medico.getCrm() + "): ");
        String novoCrm = scanner.nextLine();
        if (!novoCrm.trim().isEmpty()) {
            while (true) {
                if (novoCrm.trim().isEmpty()) {
                    System.out.print("CRM inválido! Informe um CRM: ");
                    novoCrm = scanner.nextLine();
                    continue;
                }
                // Se o CRM for alterado, verifica duplicidade
                if (!novoCrm.equals(medico.getCrm()) && buscaPorCrm(novoCrm) != null) {
                    System.out.print("Já existe um médico cadastrado com este CRM. Informe outro: ");
                    novoCrm = scanner.nextLine();
                    continue;
                }
                break;
            }
            medico.setCrm(novoCrm);
        }

        // Atualiza a Especialidade
        System.out.print("Especialidade (atual: " + medico.getEspecialidade() + "): ");
        String novaEspecialidade = scanner.nextLine();
        if (!novaEspecialidade.trim().isEmpty()) {
            medico.setEspecialidade(novaEspecialidade);
        }

        System.out.println("Cadastro do médico atualizado com sucesso!");
    }

    /**
     * Remove o médico da lista, se encontrado.
     */
    public void removerMedico(Medico medico) {
        if (medico != null) {
            medicos.remove(medico);
        }
    }
}
