import exception.PagamentoPendenteException;
import model.Paciente;
import service.*;

import java.util.Scanner;

import static util.ValidaCPF.formatarCPF;

public class Main {
    public static void main(String[] args) {
        // Instancia os serviços já existentes
        PacienteService pacienteService = new PacienteService();
        MedicoService medicoService = new MedicoService();
        ExameService exameService = new ExameService();
        MedicamentoService medicamentoService = new MedicamentoService();
        ConsultaService consultaService = new ConsultaService(pacienteService, medicoService,
                exameService, medicamentoService);
        PagamentoService pagamentoService = new PagamentoService();

        // Scanner principal
        Scanner scanner = new Scanner(System.in);
        int opcao = 0;

        do {
            System.out.println("==========================================");
            System.out.println(" Sistema de Gerenciamento de Clínica Médica");
            System.out.println("==========================================");
            System.out.println("1. Gerenciar Pacientes");
            System.out.println("2. Gerenciar Médicos");
            System.out.println("3. Agendamento de Consultas");
            System.out.println("4. Prescrição de Exames e Medicamentos");
            System.out.println("5. Gestão de Pagamentos");
            System.out.println("6. Sair");
            System.out.print("Selecione uma opção: ");

            try {
                opcao = Integer.parseInt(scanner.nextLine());
                switch (opcao) {
                    case 1:
                        gerenciarPacientes(pacienteService);
                        break;
                    case 2:
                        gerenciarMedicos(medicoService);
                        break;
                    case 3:
                        gerenciarConsultas(consultaService);
                        break;
                    case 4:
                        gerenciarExamesEMedicamentos(exameService, medicamentoService);
                        break;
                    case 5:
                        gerenciarPagamentos(pagamentoService, pacienteService);
                        break;
                    case 6:
                        System.out.println("\nEncerrando o sistema. Até logo!");
                        break;
                    default:
                        System.out.println("\nOpção inválida! Tente novamente.");
                }
            } catch (NumberFormatException e) {
                System.out.println("\nValor inválido! Por favor, insira um número.");
            }
        } while (opcao != 6);

        scanner.close();
    }

    // Submenu para gerenciamento de Pacientes (igual ao seu exemplo)
    private static void gerenciarPacientes(PacienteService pacienteService) {
        Scanner scanner = new Scanner(System.in);
        int opcaoPaciente = 0;

        do {
            System.out.println("\n=== Gerenciar Pacientes ===");
            System.out.println("1. Cadastrar Paciente");
            System.out.println("2. Atualizar Paciente");
            System.out.println("3. Listar Pacientes");
            System.out.println("4. Remover Paciente");
            System.out.println("5. Voltar ao Menu Principal");
            System.out.print("Selecione uma opção: ");

            try {
                opcaoPaciente = Integer.parseInt(scanner.nextLine());
                switch (opcaoPaciente) {
                    case 1:
                        System.out.println("\n[Cadastrar Paciente].");
                        pacienteService.cadastrarPaciente();
                        break;
                    case 2:
                        System.out.println("\n[Atualizar Paciente].");
                        System.out.print("Digite o CPF do paciente que deseja atualizar o cadastro: ");
                        String cpf = scanner.nextLine();
                        var paciente = pacienteService.buscarPorCpf(formatarCPF(cpf));
                        pacienteService.atualizarCadastroPaciente(paciente);
                        System.out.println("Paciente atualizado com sucesso!");
                        break;
                    case 3:
                        System.out.println("\n[Listar Pacientes].");
                        pacienteService.listarPacientes();
                        break;
                    case 4:
                        System.out.println("\n[Remover Paciente].");
                        System.out.print("Digite o CPF do paciente que deseja remover: ");
                        String cpfPaciente = scanner.nextLine();
                        var pacienteR = pacienteService.buscarPorCpf(formatarCPF(cpfPaciente));
                        pacienteService.removerPaciente(pacienteR);
                        System.out.println("Paciente removido com sucesso!");
                        break;
                    case 5:
                        System.out.println("\nRetornando ao Menu Principal.");
                        break;
                    default:
                        System.out.println("\nOpção inválida! Tente novamente.");
                }
            } catch (NumberFormatException e) {
                System.out.println("\nValor inválido! Por favor, insira um número.");
            }
        } while (opcaoPaciente != 5);
    }

    // Submenu para gerenciamento de Médicos (igual ao seu exemplo)
    private static void gerenciarMedicos(MedicoService medicoService) {
        Scanner scanner = new Scanner(System.in);
        int opcaoMedico = 0;

        do {
            System.out.println("\n=== Gerenciar Médicos ===");
            System.out.println("1. Cadastrar Médico");
            System.out.println("2. Atualizar Médico");
            System.out.println("3. Listar Médicos");
            System.out.println("4. Remover Médico");
            System.out.println("5. Voltar ao Menu Principal");
            System.out.print("Selecione uma opção: ");

            try {
                opcaoMedico = Integer.parseInt(scanner.nextLine());
                switch (opcaoMedico) {
                    case 1:
                        System.out.println("\n[Cadastrar Médico].");
                        medicoService.cadastrarMedico();
                        break;
                    case 2:
                        System.out.println("\n[Atualizar Médico].");
                        System.out.print("Digite o CRM do médico que deseja atualizar o cadastro: ");
                        String crm = scanner.nextLine();
                        var medico = medicoService.buscaPorCrm(crm);
                        medicoService.atualizarCadastroMedico(medico);
                        System.out.println("Médico atualizado com sucesso!");
                        break;
                    case 3:
                        System.out.println("\n[Listar Médicos].");
                        medicoService.listarMedicos();
                        break;
                    case 4:
                        System.out.println("\n[Remover Médico].");
                        System.out.print("Digite o CPF do médico que deseja remover: ");
                        String cpfMedico = scanner.nextLine();
                        var medicoR = medicoService.buscarPorCpf(cpfMedico);
                        medicoService.removerMedico(medicoR);
                        System.out.println("Médico removido com sucesso!");
                        break;
                    case 5:
                        System.out.println("\nRetornando ao Menu Principal.");
                        break;
                    default:
                        System.out.println("\nOpção inválida! Tente novamente.");
                }
            } catch (NumberFormatException e) {
                System.out.println("\nValor inválido! Por favor, insira um número.");
            }
        } while (opcaoMedico != 5);
    }

    // Adicione esta função ao seu código, dentro da classe Main
    private static void gerenciarConsultas(ConsultaService consultaService) {
        Scanner scanner = new Scanner(System.in);
        int opcaoConsulta = 0;

        do {
            System.out.println("\n=== Agendamento de Consultas ===");
            System.out.println("1. Cadastrar Consulta");
            System.out.println("2. Atualizar Consulta");
            System.out.println("3. Listar Consultas");
            System.out.println("4. Remover Consulta");
            System.out.println("5. Voltar ao Menu Principal");
            System.out.print("Selecione uma opção: ");

            try {
                opcaoConsulta = Integer.parseInt(scanner.nextLine());
                switch (opcaoConsulta) {
                    case 1:
                        System.out.println("\n[Cadastrar Consulta].");
                        consultaService.cadastrarConsulta();
                        break;
                    case 2:
                        System.out.println("\n[Atualizar Consulta].");
                        consultaService.atualizarConsulta();
                        break;
                    case 3:
                        System.out.println("\n[Listar Consultas].");
                        consultaService.listarConsultas();
                        break;
                    case 4:
                        System.out.println("\n[Remover Consulta].");
                        consultaService.removerConsulta();
                        break;
                    case 5:
                        System.out.println("\nRetornando ao Menu Principal.");
                        break;
                    default:
                        System.out.println("\nOpção inválida! Tente novamente.");
                }
            } catch (NumberFormatException e) {
                System.out.println("\nValor inválido! Por favor, insira um número.");
            }
        } while (opcaoConsulta != 5);
    }

    // Submenu para Prescrição de Exames e Medicamentos
    private static void gerenciarExamesEMedicamentos(ExameService exameService, MedicamentoService medicamentoService) {
        Scanner scanner = new Scanner(System.in);
        int opcao = 0;

        do {
            System.out.println("\n=== Prescrição de Exames e Medicamentos ===");
            System.out.println("1. Gerenciar Exames");
            System.out.println("2. Gerenciar Medicamentos");
            System.out.println("3. Voltar ao Menu Principal");
            System.out.print("Selecione uma opção: ");

            try {
                opcao = Integer.parseInt(scanner.nextLine());
                switch (opcao) {
                    case 1:
                        gerenciarExames(exameService);
                        break;
                    case 2:
                        gerenciarMedicamentosSubmenu(medicamentoService);
                        break;
                    case 3:
                        System.out.println("Retornando ao Menu Principal.");
                        break;
                    default:
                        System.out.println("Opção inválida! Tente novamente.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido! Por favor, insira um número.");
            }
        } while (opcao != 3);
    }

    // Submenu para gerenciamento de Exames
    private static void gerenciarExames(ExameService exameService) {
        Scanner scanner = new Scanner(System.in);
        int opcaoExame = 0;

        do {
            System.out.println("\n=== Gerenciar Exames ===");
            System.out.println("1. Cadastrar Exame");
            System.out.println("2. Atualizar Exame");
            System.out.println("3. Listar Exames");
            System.out.println("4. Remover Exame");
            System.out.println("5. Voltar ao Menu de Prescrição");
            System.out.print("Selecione uma opção: ");

            try {
                opcaoExame = Integer.parseInt(scanner.nextLine());
                switch (opcaoExame) {
                    case 1:
                        System.out.println("\n[Cadastrar Exame].");
                        exameService.cadastrarExame();
                        break;
                    case 2:
                        System.out.println("\n[Atualizar Exame].");
                        exameService.atualizarExame();
                        break;
                    case 3:
                        System.out.println("\n[Listar Exames].");
                        exameService.listarExames();
                        break;
                    case 4:
                        System.out.println("\n[Remover Exame].");
                        System.out.println(exameService.removerExame());
                        break;
                    case 5:
                        System.out.println("Retornando ao Menu de Prescrição.");
                        break;
                    default:
                        System.out.println("Opção inválida! Tente novamente.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido! Por favor, insira um número.");
            }
        } while (opcaoExame != 5);
    }

    // Submenu para gerenciamento de Medicamentos
    private static void gerenciarMedicamentosSubmenu(MedicamentoService medicamentoService) {
        Scanner scanner = new Scanner(System.in);
        int opcaoMedicamento = 0;

        do {
            System.out.println("\n=== Gerenciar Medicamentos ===");
            System.out.println("1. Cadastrar Medicamento");
            System.out.println("2. Atualizar Medicamento");
            System.out.println("3. Listar Medicamentos");
            System.out.println("4. Remover Medicamento");
            System.out.println("5. Voltar ao Menu de Prescrição");
            System.out.print("Selecione uma opção: ");

            try {
                opcaoMedicamento = Integer.parseInt(scanner.nextLine());
                switch (opcaoMedicamento) {
                    case 1:
                        System.out.println("\n[Cadastrar Medicamento].");
                        medicamentoService.cadastrarMedicamento();
                        break;
                    case 2:
                        System.out.println("\n[Atualizar Medicamento].");
                        medicamentoService.atualizarMedicamento();
                        break;
                    case 3:
                        System.out.println("\n[Listar Medicamentos].");
                        medicamentoService.listarMedicamentos();
                        break;
                    case 4:
                        System.out.println("\n[Remover Medicamento].");
                        System.out.println(medicamentoService.removerMedicamento());
                        break;
                    case 5:
                        System.out.println("Retornando ao Menu de Prescrição.");
                        break;
                    default:
                        System.out.println("Opção inválida! Tente novamente.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido! Por favor, insira um número.");
            }
        } while (opcaoMedicamento != 5);
    }

    private static void gerenciarPagamentos(PagamentoService pagamentoService, PacienteService pacienteService) {
        Scanner scanner = new Scanner(System.in);
        int opcaoPagamento = 0;

        do {
            System.out.println("\n=== Gerenciar Pagamentos ===");
            System.out.println("1. Realizar Pagamento");
            System.out.println("2. Voltar ao Menu Principal");
            System.out.print("Selecione uma opção: ");

            try {
                opcaoPagamento = Integer.parseInt(scanner.nextLine());
                switch (opcaoPagamento) {
                    case 1:
                        System.out.println("\n[Realizar Pagamento].");
                        pagamentoService.realizarPagamento(pacienteService);
                        break;
                    case 2:
                        System.out.println("\nRetornando ao Menu Principal.");
                        break;
                    default:
                        System.out.println("\nOpção inválida! Tente novamente.");
                }
            } catch (NumberFormatException e) {
                System.out.println("\nValor inválido! Por favor, insira um número válido.");
            }
        } while (opcaoPagamento != 2);
    }
}