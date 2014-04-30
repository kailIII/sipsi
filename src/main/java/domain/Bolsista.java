/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import entities.annotations.Editor;
import entities.annotations.EntityDescriptor;
import entities.annotations.PropertyDescriptor;
import entities.annotations.View;
import entities.annotations.Views;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import security.Usuario;

/**
 *
 * @author Egberto
 */

@Entity
@NamedQueries({
@NamedQuery(
name = "Bolsistas",
query = "Select us From security.Usuario us where 'Bolsista' in elements(us.papeis)")
})
@Views({
@View(name = "Manter Bolsista",
        title = "Manter Bolsista",
        filters = "[nome,professorOrientador,matricula,Ctrl.DAO.filter()]",
        members= "[Bolsista[nome:2;professorOrientador:2;matricula:1,usuario:1;telefone:2,email:3]]",
        template = "@CRUD",
        rows = 5,
        roles="Coordenador,Administrador,Secretario")
})

@EntityDescriptor(displayName="Bolsista", template="@FORM_CRUD",roles="Estagiario")
@Table(name="bolsista")
public class Bolsista implements Serializable{
    
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @PropertyDescriptor(hidden=true)
    private int id;
    
    @Column(length=15)
    @PropertyDescriptor(index=2, autoFilter=true, displayName="Matricula", mask="9999999")
    @NotNull(message="Informe a matricula")
    private String matricula;
    
    @Column(length=60)
    @PropertyDescriptor(index=1, autoFilter=true, displayName="Nome")
    @NotNull(message="Informe o nome")
    private String nome;
    
    @Column(length = 15)
    @PropertyDescriptor(index = 3, mask = "(99)9999-9999", displayWidth = 15)
    private String telefone;
    
    @PropertyDescriptor(index = 4, displayWidth = 50)
    private String email;

    @Column(length=60)
    @PropertyDescriptor(index=5, displayName="Professor-Orientador")
    @NotNull(message="Informe o nome do Professor orientador")
    private String professorOrientador;
    
    @ManyToOne()
    @NotNull
    @Editor(namedQuery="Bolsistas")
    private Usuario usuario;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getProfessorOrientador() {
        return professorOrientador;
    }

    public void setProfessorOrientador(String professorOrientador) {
        this.professorOrientador = professorOrientador;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
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
    
    
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Bolsista other = (Bolsista) obj;
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
        return "Bolsista{" + "id=" + id + ", matricula=" + matricula + ", nome=" + nome + '}';
    }    
}
