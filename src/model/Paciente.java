package model;

import intefaces.IPaciente;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Paciente implements IPaciente {

    protected String nome;
    protected String cpf;
    protected LocalDate dataNascimento;
    protected List<Consulta> historicoMedico;
    protected List<Pagamento> pagamentos;

    public Paciente(String nome, String cpf, LocalDate dataNascimento, List<Consulta> historicoMedico,
                    List<Pagamento> pagamentos) {
        this.nome = nome;
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
        this.historicoMedico = new ArrayList<>(historicoMedico);
        this.pagamentos = new ArrayList<>(pagamentos);
    }

    public Paciente() {}

    @Override
    public void addConsulta(Consulta consulta) {
        this.historicoMedico.add(consulta);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public List<Consulta> getHistoricoMedico() {
        return historicoMedico;
    }

    public void setHistoricoMedico(List<Consulta> historicoMedico) {
        this.historicoMedico = historicoMedico;
    }

    public List<Pagamento> getPagamentos() {
        return pagamentos;
    }

    public void setPagamentos(List<Pagamento> pagamentos) {
        this.pagamentos = pagamentos;
    }
}
