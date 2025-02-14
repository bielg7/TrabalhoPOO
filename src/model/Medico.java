package model;

import intefaces.IMedico;
import intefaces.IPaciente;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Medico extends Paciente implements IMedico, IPaciente {

    private String crm;
    private String especialidade;
    private final List<Consulta> historicoMedico;

    public Medico(String nome, String cpf, LocalDate dataNascimento, String crm,
                  String especialidade, List<Consulta> historicoMedico) {
        super(nome, cpf, dataNascimento, new ArrayList<>(), new ArrayList<>());
        this.crm = crm;
        this.especialidade = especialidade;
        this.historicoMedico = new ArrayList<>(historicoMedico);
    }

    @Override
    public String getCrm() {
        return crm;
    }

    @Override
    public void setCrm(String crm) {
        this.crm = crm;
    }

    @Override
    public String getEspecialidade() {
        return especialidade;
    }

    @Override
    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    @Override
    public void addConsulta(Consulta consulta) {
        this.historicoMedico.add(consulta);
    }
}
