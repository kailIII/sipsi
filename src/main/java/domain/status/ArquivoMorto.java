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
@DiscriminatorValue("ArquivoMorto")
@EntityDescriptor(hidden = true)
public class ArquivoMorto extends Status {

    public ArquivoMorto() {
        this.setId(5L);
        this.setDescription("Arquivo Morto");
    }

    public ArquivoMorto(Paciente paciente) {
        this();
        this.paciente = paciente;
    }

    @Override
    public void ok() throws StatusException {
        Status aguarandoValidacao = new AguardandoAtendimento(paciente);
        paciente.setStatus(aguarandoValidacao);
    }

    @Override
    public void rejeitar() throws StatusException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void reverter(String remark) throws StatusException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
