/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import entities.annotations.EntityDescriptor;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

/**
 *
 * @author maria
 */
@Entity
@EntityDescriptor(hidden = true)
public abstract class Status implements Serializable {

    @Id
    private Long id;
    @Column(length = 40, nullable = false)
    private String description;
    @Transient
    protected Paciente paciente;

    public abstract void ok() throws StatusException;

    public abstract void rejeitar() throws StatusException;

    public abstract void reverter(String remark) throws StatusException;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Status other = (Status) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        if ((this.description == null) ? (other.description != null) : !this.description.equals(other.description)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 97 * hash + (this.description != null ? this.description.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return this.id == null ? "<Novo>" : description;
    }
}
