package model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class Consulta {

    private LocalDate data;
    private LocalTime horario;
    private int duracao;
    private String status;
    private Paciente paciente;
    private Medico medico;
    private List<Exame> examesPrescritos;
    private List<Medicamento> medicamentosPrescritos;
    private double valor;

    public Consulta(LocalDate data, LocalTime horario, int duracao, String status, Paciente paciente,
                    Medico medico, List<Exame> examesPrescritos, List<Medicamento> medicamentosPrescritos, double valor) {
        this.data = data;
        this.horario = horario;
        this.duracao = duracao;
        this.status = status;
        this.paciente = paciente;
        this.medico = medico;
        this.examesPrescritos = examesPrescritos;
        this.medicamentosPrescritos = medicamentosPrescritos;
        this.valor = valor;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public LocalTime getHorario() {
        return horario;
    }

    public void setHorario(LocalTime horario) {
        this.horario = horario;
    }

    public int getDuracao() {
        return duracao;
    }

    public void setDuracao(int duracao) {
        this.duracao = duracao;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public List<Exame> getExamesPrescritos() {
        return examesPrescritos;
    }

    public void setExamesPrescritos(List<Exame> examesPrescritos) {
        this.examesPrescritos = examesPrescritos;
    }

    public List<Medicamento> getMedicamentosPrescritos() {
        return medicamentosPrescritos;
    }

    public void setMedicamentosPrescritos(List<Medicamento> medicamentosPrescritos) {
        this.medicamentosPrescritos = medicamentosPrescritos;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }
}
