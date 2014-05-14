/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import entities.annotations.*;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;
import security.Usuario;

/**
 *
 * @author Egberto
 */
@Entity
@NamedQueries({
    @NamedQuery(
        name = "PapelEstagiario",
        query = "Select us From security.Usuario us where 'Estagiario' in elements(us.papeis)")
})
@Views({
    @View(name = "Manter Estagiario",
            title = "Manter Estagiário",
            filters = "[nome,matricula,Ctrl.DAO.filter()]",
            members = "[Estagiario[nome:2;matricula:1,usuario:1;telefone:2,email:3;semestre:1;supervisor:1]]",
            template = "@CRUD",
            rows = 5,
            roles = "Administrador,Coordenador,Bolsista,Secretario"),
    
        @View(name = "Listar Estagiarios",
            title = "Listar Estagiários",
            filters = "[supervisor,Ctrl.DAO.filter()]",
            members = "[Estagiario[nome:1;semestre:1,matricula:1;supervisor:1,telefone:2,email:3]]",
            rows = 5,
            template = "@PAGER",
            roles = "Administrador,Coordenador,Supervisor")
})
    
    @EntityDescriptor(displayName = "Estagiário",pluralDisplayName = "Manter Estagiários")
    @Table(name="estagiario")
    public class Estagiario implements Serializable {

    public enum Sexo {

        MASCULINO, FEMININO
    }

    public enum Semestre {

        Oitavo, Nono, Décimo
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @PropertyDescriptor(hidden = true)
    private long id;
    
    @PropertyDescriptor(displayWidth = 14)
    @NotNull(message = "Informe o Semestre do Estagiário")
    private Semestre semestre;
    
    @Column(length = 35)
    @NotEmpty(message = "Informe o Nome do Estagiário")
    @PropertyDescriptor(displayWidth = 36)
    private String nome;
    
    @Column(length = 7, unique = true)
    @PropertyDescriptor(mask = "9999999", displayWidth = 8)
    @NotEmpty(message = "Informe a Matrícula do Estagiário")
    @Editor(namedQuery = "PapelEstagiario")
    private String matricula;
    
    @Column(length = 15)
    @PropertyDescriptor(index = 9, mask = "(99)9999-9999", displayWidth = 15)
    private String telefone;
    
    @PropertyDescriptor(index = 10, displayWidth = 50)
    private String email;
    
    @ManyToOne()
    @NotNull
    private Supervisor supervisor;
    
    @ManyToOne()
    @NotNull
    @Editor(namedQuery="PapelEstagiário")
    private Usuario usuario;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Semestre getSemestre() {
        return semestre;
    }

    public void setSemestre(Semestre semestre) {
        this.semestre = semestre;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    
    
    public Supervisor getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(Supervisor supervisor) {
        this.supervisor = supervisor;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Estagiario other = (Estagiario) obj;
        if (this.id != other.id) {
            return false;
        }
        if ((this.matricula == null) ? (other.matricula != null) : !this.matricula.equals(other.matricula)) {
            return false;
        }
        if ((this.nome == null) ? (other.nome != null) : !this.nome.equals(other.nome)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    } 

  
    @Override
    public String toString() {
        return nome;
    }
}
