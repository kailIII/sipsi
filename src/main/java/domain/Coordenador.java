/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import entities.annotations.Editor;
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
 * @author maria
 */
@Entity
@NamedQueries({
@NamedQuery(
    name = "Coordenadores",
    query = "Select us From security.Usuario us where 'Coordenador' in elements(us.papeis)")
})
@Views({
@View(name = "Manter Coordenador",
        title = "Manter Coordenador",
        filters = "[nome,siape,Ctrl.DAO.filter()]",
        members= "[Coordenador[nome:2;siape:1,usuario;telefone:2,email:3]]",
        template = "@CRUD",
        rows = 5,
        roles="Coordenador,Administrador")
})

public class Coordenador implements Serializable {
   
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @PropertyDescriptor(hidden=true)
    private long id; 
    
    @NotEmpty (message= "Informe o Nome do Coordenador")
    @Column(length=35)
    @PropertyDescriptor(index=1,autoFilter=true,displayWidth = 32)
    private String nome;
    
   
    @NotEmpty (message= "Informe o SIAPE do Coordenador")
    @Column (length= 7, unique=true)
    @PropertyDescriptor(index=2,mask="9999999",autoFilter=true,displayWidth = 7)    
    private String siape;
    
    @Column(length = 15)
    @PropertyDescriptor(index = 3, mask = "(99)9999-9999", displayWidth = 15)
    private String telefone;
    
    @PropertyDescriptor(index = 4, displayWidth = 50)
    private String email;
    
    @ManyToOne()
    @NotNull
    @Editor(namedQuery="Coordenadores")
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

    public String getSiape() {
        return siape;
    }

    public void setSiape(String siape) {
        this.siape = siape;
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
        final Coordenador other = (Coordenador) obj;
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
        int hash = 5;
        hash = 73 * hash + (int) (this.id ^ (this.id >>> 32));
        hash = 73 * hash + (this.nome != null ? this.nome.hashCode() : 0);
        hash = 73 * hash + (this.siape != null ? this.siape.hashCode() : 0);
        return hash;
    }
    
}    
