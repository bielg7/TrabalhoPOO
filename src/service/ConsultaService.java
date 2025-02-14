package service;

import exception.PagamentoPendenteException;
import model.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static util.ValidaCPF.formatarCPF;

public class ConsultaService {

    private PacienteService pacienteService;
    private MedicoService medicoService;
    private ExameService exameService;
    private MedicamentoService medicamentoService;

    private final List<Consulta> consultas = new ArrayList<>();

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    public ConsultaService(PacienteService pacienteService, MedicoService medicoService,
                           ExameService exameService, MedicamentoService medicamentoService) {
        this.pacienteService = pacienteService;
        this.medicoService = medicoService;
        this.exameService = exameService;
        this.medicamentoService = medicamentoService;
    }

    public ConsultaService() {}

    // Método para cadastrar uma nova consulta
    public void cadastrarConsulta() {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("=== Cadastro de Consulta ===");

            // Validação da data da consulta
            LocalDate data = validarDataConsulta(scanner);

            // Validação do horário da consulta
            LocalTime horario = validarHorarioConsulta(scanner);

            // Validação da duração da consulta
            int duracao = validarDuracaoConsulta(scanner);

            // Validação do status da consulta
            String status = validarStatusConsulta(scanner);

            // Criação do paciente
            var paciente = adicionarPaciente(scanner);

            // Criação do médico
            var medico = adicionarMedico(scanner);

            // Verificação de disponibilidade do médico
            if (!isMedicoDisponivel(medico, data, horario, duracao)) {
                System.out.println("Médico não disponível no horário selecionado.");
                return;
            }

            // Verificação de consulta do paciente no mesmo dia
            if (hasPacienteConsultaNoDia(paciente, data)) {
                System.out.println("Paciente já possui uma consulta agendada para este dia.");
                return;
            }

            // Adição de exames
            var examesPrescritos = adicionarExames(scanner);

            // Adição de medicamentos prescritos
            var medicamentosPrescritos = adicionarMedicamentos(scanner);

            // Validação do valor da consulta
            double valor = validarValorConsulta(scanner);

            if (paciente != null && !paciente.getHistoricoMedico().isEmpty()) {
                if (paciente.getPagamentos().isEmpty()) {
                    throw new PagamentoPendenteException("O paciente tem pagamento pendente, " +
                            "faça o pagamento para agendar uma consulta!");
                }
            }

            // Criação da consulta
            Consulta consulta = new Consulta(data, horario, duracao, status, paciente, medico,
                    examesPrescritos, medicamentosPrescritos, valor);

            // Adiciona a consulta ao histórico do paciente e do médico
            assert paciente != null;
            paciente.addConsulta(consulta);
            medico.addConsulta(consulta);

            // Adiciona a consulta à lista de consultas
            consultas.add(consulta);
            paciente.addConsulta(consulta);

            System.out.println("Consulta cadastrada com sucesso!");
        } catch (PagamentoPendenteException e) {
            System.out.println("O paciente tem pagamento pendente, " +
                    "faça o pagamento para agendar uma consulta!");
        }
    }

    // Método para listar todas as consultas
    public void listarConsultas() {
        if (consultas.isEmpty()) {
            System.out.println("Nenhuma consulta cadastrada.");
            return;
        }
        System.out.println("=== Lista de Consultas ===");
        for (int i = 0; i < consultas.size(); i++) {
            Consulta consulta = consultas.get(i);
            System.out.printf("Índice: %d | Data: %s | Horário: %s | Duração: %d minutos |" +
                            " Status: %s | Paciente: %s | Médico: %s | Valor: R$ %.2f%n",
                    i,
                    consulta.getData().format(dateFormatter),
                    consulta.getHorario().format(timeFormatter),
                    consulta.getDuracao(),
                    consulta.getStatus(),
                    consulta.getPaciente().getNome(),
                    consulta.getMedico().getNome(),
                    consulta.getValor());
        }
    }

    // Método para atualizar uma consulta existente
    public void atualizarConsulta() {
        Scanner scanner = new Scanner(System.in);
        listarConsultas();
        System.out.print("Informe o índice da consulta que deseja atualizar: ");
        int indice = scanner.nextInt();
        scanner.nextLine(); // Consumir a quebra de linha

        if (indice < 0 || indice >= consultas.size()) {
            System.out.println("Índice inválido.");
            return;
        }

        Consulta consulta = consultas.get(indice);

        // Atualização da data
        System.out.printf("Nova data (atual: %s): ", consulta.getData().format(dateFormatter));
        String novaDataStr = scanner.nextLine();
        if (!novaDataStr.isEmpty()) {
            consulta.setData(LocalDate.parse(novaDataStr, dateFormatter));
        }

        // Atualização do horário
        System.out.printf("Novo horário (atual: %s): ", consulta.getHorario().format(timeFormatter));
        String novoHorarioStr = scanner.nextLine();
        if (!novoHorarioStr.isEmpty()) {
            consulta.setHorario(LocalTime.parse(novoHorarioStr, timeFormatter));
        }

        // Atualização da duração
        System.out.printf("Nova duração (atual: %d minutos): ", consulta.getDuracao());
        String novaDuracaoStr = scanner.nextLine();
        if (!novaDuracaoStr.isEmpty()) {
            consulta.setDuracao(Integer.parseInt(novaDuracaoStr));
        }

        // Atualização do status
        System.out.printf("Novo status (atual: %s): ", consulta.getStatus());
        String novoStatus = scanner.nextLine();
        if (!novoStatus.isEmpty()) {
            consulta.setStatus(novoStatus);
        }

        // Atualização do valor
        System.out.printf("Novo valor (atual: R$ %.2f): ", consulta.getValor());
        String novoValorStr = scanner.nextLine();
        if (!novoValorStr.isEmpty()) {
            consulta.setValor(Double.parseDouble(novoValorStr));
        }

        System.out.println("Consulta atualizada com sucesso!");
    }

    // Método para remover uma consulta
    public void removerConsulta() {
        Scanner scanner = new Scanner(System.in);
        listarConsultas();
        System.out.print("Informe o índice da consulta que deseja remover: ");
        int indice = scanner.nextInt();
        scanner.nextLine(); // Consumir a quebra de linha

        if (indice < 0 || indice >= consultas.size()) {
            System.out.println("Índice inválido.");
            return;
        }

        Consulta consulta = consultas.remove(indice);
        System.out.printf("Consulta do paciente %s removida com sucesso!%n", consulta.getPaciente().getNome());
    }

    // Métodos auxiliares
    private LocalDate validarDataConsulta(Scanner scanner) {
        while (true) {
            System.out.print("Data da consulta (DD-MM-YYYY): ");
            String dataStr = scanner.nextLine();
            try {
                LocalDate data = LocalDate.parse(dataStr, dateFormatter);
                if (data.isBefore(LocalDate.now())) {
                    System.out.println("A data não pode ser no passado.");
                    continue;
                }
                return data;
            } catch (Exception e) {
                System.out.println("Formato de data inválido. Use DD-MM-YYYY.");
            }
        }
    }

    private LocalTime validarHorarioConsulta(Scanner scanner) {
        while (true) {
            System.out.print("Horário da consulta (HH:mm): ");
            String horarioStr = scanner.nextLine();
            try {
                return LocalTime.parse(horarioStr, timeFormatter);
            } catch (Exception e) {
                System.out.println("Formato de horário inválido. Use HH:mm.");
            }
        }
    }

    private int validarDuracaoConsulta(Scanner scanner) {
        while (true) {
            System.out.print("Duração da consulta (minutos): ");
            String duracaoStr = scanner.nextLine();
            try {
                int duracao = Integer.parseInt(duracaoStr);
                if (duracao <= 0) {
                    System.out.println("A duração deve ser maior que 0.");
                    continue;
                }
                return duracao;
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido. Informe um número.");
            }
        }
    }

    private String validarStatusConsulta(Scanner scanner) {
        while (true) {
            System.out.print("Status da consulta (AGENDADA, CANCELADA, REALIZADA): ");
            String status = scanner.nextLine().toUpperCase();
            if (status.equals("AGENDADA") || status.equals("CANCELADA") || status.equals("REALIZADA")) {
                return status;
            }
            System.out.println("Status inválido. Use AGENDADA, CANCELADA ou REALIZADA.");
        }
    }

    private Paciente adicionarPaciente(Scanner scanner) {
        pacienteService.listarPacientes();
        System.out.println("CPF do paciente que vai consultar: ");
        String cpf = scanner.nextLine();
        var paciente = pacienteService.buscarPorCpf(formatarCPF(cpf));
        if (paciente == null) {
            System.out.println("Cadastre um paciente para cadastrar uma consulta.");
            return null;
        }
        return paciente;
    }

    private Medico adicionarMedico(Scanner scanner) {
        medicoService.listarMedicos();
        System.out.println("CRM do medico que vai realizar a consulta: ");
        String crm = scanner.nextLine();
        return medicoService.buscaPorCrm(crm);
    }

    private List<Exame> adicionarExames(Scanner scanner) {
        List<Exame> exames = new ArrayList<>();
        while (true) {
                exameService.listarExames();
                System.out.println("Adicionar exame (SANGUE, RAIO_X, ULTRASSOM) ou deixe em branco para parar: ");
                System.out.print("Digite o tipo do exame: ");
                String tipo = scanner.nextLine().toUpperCase();
                if (tipo.isEmpty()) {
                    break;
                }
                if (!tipo.equals("SANGUE") && !tipo.equals("RAIO_X") && !tipo.equals("ULTRASSOM")) {
                    System.out.println("Tipo de exame inválido.");
                    continue;
                }
                var exame = exameService.buscarExamePorTipo(tipo);
                exames.add(exame);
        }
        return exames;
    }

    private List<Medicamento> adicionarMedicamentos(Scanner scanner) {
        List<Medicamento> medicamentos = new ArrayList<>();
        while (true) {
                medicamentoService.listarMedicamentos();
                System.out.println("Adicionar medicamento ou deixe em branco para parar: ");
                System.out.print("Digite o nome do medicamento: ");
                String nome = scanner.nextLine();
                var medicamento = medicamentoService.buscarMedicamento(nome);
                if (nome.isEmpty()) {
                    break;
                }
                medicamentos.add(medicamento);
        }
        return medicamentos;
    }

    private double validarValorConsulta(Scanner scanner) {
        while (true) {
            System.out.print("Valor da consulta: ");
            double valor = scanner.nextDouble();
            try {
                if (valor < 0) {
                    System.out.println("O valor não pode ser negativo.");
                    continue;
                }
                return valor;
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido. Informe um número.");
            }
        }
    }

    private boolean isMedicoDisponivel(Medico medico, LocalDate data, LocalTime horario, int duracao) {
        for (Consulta consulta : consultas) {
            if (consulta.getMedico().equals(medico) && consulta.getData().equals(data)) {
                LocalTime inicioConsulta = consulta.getHorario();
                LocalTime fimConsulta = inicioConsulta.plusMinutes(consulta.getDuracao());
                LocalTime fimNovaConsulta = horario.plusMinutes(duracao);

                if (horario.isBefore(fimConsulta) && fimNovaConsulta.isAfter(inicioConsulta)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean hasPacienteConsultaNoDia(Paciente paciente, LocalDate data) {
        for (Consulta consulta : consultas) {
            if (consulta.getPaciente().equals(paciente) && consulta.getData().equals(data)) {
                return true;
            }
        }
        return false;
    }
}