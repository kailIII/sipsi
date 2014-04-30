/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import entities.annotations.*;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.persistence.Temporal;

/**
 *
 * @author roberto
 */
@Entity
@Views({
    @View(name = "Historico",
            title = "Histórico",
            filters = "[nomePaciente,nomeEstagiario,Ctrl.DAO.filter()]",
            members = "[Histórico[nomePaciente:1;nomeEstagiario:1;dataTransacao:1;'Observações':observacao]]",
            template = "@PAGER",
            rows = 5,
            roles = "Administrador,Coordenador")
})
@EntityDescriptor(roles = "Coordenador,Administrador")
public class Historico implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @PropertyDescriptor(hidden = true)
    private long id;
    
    /*@OneToOne
     private Paciente paciente;*/
    
    @Column(length = 100)
    @PropertyDescriptor(autoFilter = true)
    private String nomePaciente;
    
    @Column(length = 100)
    @PropertyDescriptor(autoFilter = true)
    private String nomeEstagiario;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    @PropertyDescriptor(index = 2, displayName = "Data de Transação", autoFilter = true)
    private Date dataTransacao;
    
    @Column(length = 200)
    @Editor(inputComponentName = "javax.faces.component.html.HtmlInputTextarea")
    private String observacao;
    
    @Column(length = 100)
    private String mudancaDeStatus;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNomePaciente() {
        return nomePaciente;
    }

    public void setNomePaciente(String nomePaciente) {
        this.nomePaciente = nomePaciente;
    }

    public String getNomeEstagiario() {
        return nomeEstagiario;
    }

    public void setNomeEstagiario(String nomeEstagiario) {
        this.nomeEstagiario = nomeEstagiario;
    }

    public Date getDataTransacao() {
        return dataTransacao;
    }

    public void setDataTransacao(Date dataTransacao) {
        this.dataTransacao = dataTransacao;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getMudancaDeStatus() {
        return mudancaDeStatus;
    }

    public void setMudancaDeStatus(String mudancaDeStatus) {
        this.mudancaDeStatus = mudancaDeStatus;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Historico other = (Historico) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }
}
