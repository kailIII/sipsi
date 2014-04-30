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
@DiscriminatorValue("Emtratamento")
@EntityDescriptor(hidden = true)
public class EmAtendimento extends Status {

    public EmAtendimento() {
        this.setId(2L);
        this.setDescription("Em tratamento");
    }

    public EmAtendimento(Paciente paciente) {
        this();
        this.paciente = paciente;
    }

    @Override
    public void ok() throws StatusException {
        Status aguardandoValidacao = new Encaminhamento(paciente);
        paciente.setStatus(aguardandoValidacao);
    }

    @Override
    public void rejeitar() throws StatusException {
        Status aguardandoValidacao = new ArquivoMorto(paciente);
        paciente.setStatus(aguardandoValidacao);
    }

    @Override
    public void reverter(String remark) throws StatusException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
