package service;

import model.Consulta;
import model.Pagamento;

import java.util.Scanner;

import static util.ValidaCPF.formatarCPF;

public class PagamentoService {

    public void realizarPagamento(PacienteService pacienteService) {
        Scanner scanner = new Scanner(System.in);

        try {
            pacienteService.listarPacientes();
            System.out.print("Digite o CPF do paciente que deseja pagar: ");
            String cpf = scanner.nextLine();

            var paciente = pacienteService.buscarPorCpf(formatarCPF(cpf));

            if (paciente == null) {
                System.out.println("Paciente não encontrado.");
                return;
            }

            if (paciente.getHistoricoMedico().isEmpty()) {
                System.out.println("O paciente não tem consultas pendentes para pagamento.");
                return;
            }

            // Calcula o total pendente (considerando apenas consultas ainda não pagas)
            double totalPendente = paciente.getHistoricoMedico().stream()
                    .filter(consulta -> paciente.getPagamentos().stream()
                            .noneMatch(p -> p.getValor() == consulta.getValor() && p.isPago()))
                    .mapToDouble(Consulta::getValor)
                    .sum();

            if (totalPendente == 0) {
                System.out.println("Todas as consultas já foram pagas.");
                return;
            }

            System.out.println("Total pendente: R$" + totalPendente);

            double valorPago = 0;
            boolean entradaValida = false;

            while (!entradaValida) {
                System.out.print("Digite o valor total para pagamento (R$" + totalPendente + "): ");
                try {
                    valorPago = Double.parseDouble(scanner.nextLine());

                    if (valorPago <= 0) {
                        System.out.println("Erro! O valor deve ser maior que zero.");
                    } else if (valorPago != totalPendente) {
                        System.out.println("Erro! Você deve pagar o valor exato de R$" + totalPendente);
                    } else {
                        entradaValida = true;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Erro! Digite um valor numérico válido.");
                }
            }

            // Se chegou até aqui, o pagamento foi validado
            for (Consulta consulta : paciente.getHistoricoMedico()) {
                boolean consultaJaPaga = paciente.getPagamentos().stream()
                        .anyMatch(p -> p.getValor() == consulta.getValor() && p.isPago());

                if (!consultaJaPaga) {
                    Pagamento pagamento = new Pagamento();
                    pagamento.setValor(consulta.getValor());
                    pagamento.setPago(true);
                    paciente.getPagamentos().add(pagamento);
                }
            }

            System.out.println("Pagamento de R$" + totalPendente + " realizado com sucesso!");
        } finally {
            scanner.close();
        }
    }
}
