/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import entities.annotations.*;
import java.io.Serializable;
import java.util.List;
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
            name = "Supervisores",
            query = "Select us From security. Usuario us where 'Supervisor' in elements(us.papeis)")
})

@Views({
    @View(name = "Listar Estagiarios",
     title = "Listar Estagiários",
     members = "Supervisor[estagiarios]",
     roles="Supervisor",
     namedQuery="Select ot From security.Usuario ot where ot.usuario = nome",
     params = @Param(name = "nome", value = "#{context.currentUser()}")
     ),
    
    @View(name = "Manter Supervisor",
            title = "Manter Supervisor",
            filters = "Filtro[nome,siape,Ctrl.DAO.filter()]",
            members = "[Supervisor[nome:10;telefone:3,email:7; siape:3;usuario:4]]:10",
            template = "@CRUD",
            roles = "Coordenador,Administrador,Secretario,Bolsista")
})

@Table(name = "supervisor")
@EntityDescriptor(displayName = "Supervisor",pluralDisplayName = "Manter Supervisores")
public class Supervisor implements Serializable {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "supervisor")
    @PropertyDescriptor(index = 4, autoFilter = true)
    private List<Estagiario> estagiarios;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @PropertyDescriptor(hidden = true)
    private long id;
    
    @NotEmpty(message = "Informe o Nome")
    @Column(length = 100)
    @PropertyDescriptor(index = 1, autoFilter = true, displayWidth = 100)
    private String nome;
    
    @NotEmpty(message = "Informe SIAPE")
    @Column(length = 7, unique = true)
    @PropertyDescriptor(index = 2, mask = "9999999", autoFilter = true, displayWidth = 7)
    private String siape;
    
    @Column(length = 15)
    @PropertyDescriptor(index = 3, mask = "(99)9999-9999", displayWidth = 15)
    private String telefone;
    
    @PropertyDescriptor(index = 4, displayWidth = 50)
    private String email;
    
    @ManyToOne()
    @NotNull
    @Editor(namedQuery = "Supervisores")
    private Usuario usuario;

    public List<Estagiario> getEstagiarios() {
        return estagiarios;
    }

    public void setEstagiarios(List<Estagiario> estagiarios) {
        this.estagiarios = estagiarios;
    }

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
        final Supervisor other = (Supervisor) obj;
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
        hash = 79 * hash + (int) (this.id ^ (this.id >>> 32));
        hash = 79 * hash + (this.nome != null ? this.nome.hashCode() : 0);
        hash = 79 * hash + (this.siape != null ? this.siape.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return nome;
    }
}
