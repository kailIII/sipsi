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
import org.hibernate.validator.constraints.NotEmpty;
import security.Usuario;

/**
 *
 * @author roberto
 */

@Entity
@NamedQueries({
    @NamedQuery(
            name = "Secretarios",
            query = "Select us From security.Usuario us where 'Secretario' in elements(us.papeis)")
})

@Views({
    @View(name = "Manter Secretario",
            title = "Manter Secretário",
            filters = "Filtro[nome,siape,Ctrl.DAO.filter()]",
            members = "[Secretário[nome:10;telefone:3,siape:3;email:6,usuario:4]]:10",
            template = "@CRUD",
            roles = "Coordenador,Administrador")
})

@EntityDescriptor(template = "@FORM_CRUD", displayName = "Secretário",
        pluralDisplayName = "Secretários")
public class Secretario implements Serializable {

    public enum Sexo {

        MASCULINO, FEMININO
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @PropertyDescriptor(hidden = true)
    private long id;
    
    @NotEmpty(message = "Informe o Nome")
    @Column(length = 100)
    @PropertyDescriptor(autoFilter = true, displayWidth = 100)
    private String nome;
    
    @PropertyDescriptor(mask = "(99)9999-9999", displayWidth = 15)
    private String telefone;
    
    @PropertyDescriptor(index = 10, displayWidth = 50)
    private String email;
    
    @NotEmpty(message = "Informe SIAPE")
    @Column(length = 7, unique = true)
    @PropertyDescriptor(mask = "9999999", autoFilter = true, displayWidth = 7)
    private String siape;
    
    @ManyToOne()
    @NotNull
    @Editor(namedQuery = "Secretarios")
    private Usuario usuario;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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
    
    
    public String getSiape() {
        return siape;
    }

    public void setSiape(String siape) {
        this.siape = siape;
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
        final Secretario other = (Secretario) obj;
        if (this.id != other.id) {
            return false;
        }
        if ((this.nome == null) ? (other.nome != null) : !this.nome.equals(other.nome)) {
            return false;
        }
        if ((this.siape == null) ? (other.siape != null) : !this.siape.equals(other.siape)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + (int) (this.id ^ (this.id >>> 32));
        hash = 67 * hash + (this.nome != null ? this.nome.hashCode() : 0);
        hash = 67 * hash + (this.siape != null ? this.siape.hashCode() : 0);
        return hash;
    }
}