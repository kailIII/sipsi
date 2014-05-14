/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package domain.status;

import entities.annotations.EntityDescriptor;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import domain.Paciente;
import domain.Status;
import domain.StatusException;

/**
 *
 * @author maria
 */
@Entity
@DiscriminatorValue("Encaminhamento")
@EntityDescriptor(hidden = true)
public class Encaminhamento extends Status {

    public Encaminhamento() {
        this.setId(3L);
        this.setDescription("Encaminhado");
    }

    public Encaminhamento(Paciente paciente) {
        this();
        this.paciente = paciente;

    }

    @Override
    public void ok() throws StatusException {
        Status aguardandoValidacao = new EmAtendimento(paciente);
        paciente.setStatus(aguardandoValidacao);
    }

    @Override
    public void rejeitar() throws StatusException {
        Status aguardandoValidacao = new AguardandoAtendimento(paciente);
        paciente.setStatus(aguardandoValidacao);
    }

    @Override
    public void reverter(String remark) throws StatusException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
