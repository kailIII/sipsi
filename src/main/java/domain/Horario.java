///*/*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package domain;
//
//import entities.annotations.EntityDescriptor;
//import entities.annotations.PropertyDescriptor;
//import java.io.Serializable;
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.Id;
//import javax.persistence.ManyToOne;
//
///**
// *
// * @author Geovanny
// */
//
//@Entity
//@EntityDescriptor(hidden = true)
//public class Horario implements Serializable{
//    
//    @Id
//    @GeneratedValue()
//    private long id;
//    
//    private boolean segunda;
//    
//    private boolean terça;
//    
//    private boolean quarta;
//    
//    private boolean quinta;
//    
//    private boolean sexta;
//    
//    private boolean sabado;
//          
//    private boolean aCombinar;
//    
//    @Column(length = 5)
//    @PropertyDescriptor(mask = "99:99", displayWidth = 5)
//    private String horarioDe;
//    
//    @Column(length = 5)
//    @PropertyDescriptor(mask = "99:99", displayWidth = 5)
//    private String a;
//    
//    @ManyToOne
//    private Paciente paciente;
//
//    public String getA() {
//        return a;
//    }
//
//    public void setA(String a) {
//        this.a = a;
//    }
//
//    public String getHorarioDe() {
//        return horarioDe;
//    }
//
//    public void setHorarioDe(String horarioDe) {
//        this.horarioDe = horarioDe;
//    }
//
//    public long getId() {
//        return id;
//    }
//
//    public void setId(long id) {
//        this.id = id;
//    }
//
//    public boolean isQuarta() {
//        return quarta;
//    }
//
//    public void setQuarta(boolean quarta) {
//        this.quarta = quarta;
//    }
//
//    public boolean isQuinta() {
//        return quinta;
//    }
//
//    public void setQuinta(boolean quinta) {
//        this.quinta = quinta;
//    }
//
//    public boolean isSabado() {
//        return sabado;
//    }
//
//    public void setSabado(boolean sabado) {
//        this.sabado = sabado;
//    }
//
//    public boolean isSegunda() {
//        return segunda;
//    }
//
//    public void setSegunda(boolean segunda) {
//        this.segunda = segunda;
//    }
//
//    public boolean isSexta() {
//        return sexta;
//    }
//
//    public void setSexta(boolean sexta) {
//        this.sexta = sexta;
//    }
//
//    public boolean isTerça() {
//        return terça;
//    }
//
//    public void setTerça(boolean terça) {
//        this.terça = terça;
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//        if (obj == null) {
//            return false;
//        }
//        if (getClass() != obj.getClass()) {
//            return false;
//        }
//        final Horario other = (Horario) obj;
//        if (this.id != other.id) {
//            return false;
//        }
//        if (this.segunda != other.segunda) {
//            return false;
//        }
//        if (this.terça != other.terça) {
//            return false;
//        }
//        if (this.quarta != other.quarta) {
//            return false;
//        }
//        if (this.quinta != other.quinta) {
//            return false;
//        }
//        if (this.sexta != other.sexta) {
//            return false;
//        }
//        if (this.sabado != other.sabado) {
//            return false;
//        }
//        if ((this.horarioDe == null) ? (other.horarioDe != null) : !this.horarioDe.equals(other.horarioDe)) {
//            return false;
//        }
//        if ((this.a == null) ? (other.a != null) : !this.a.equals(other.a)) {
//            return false;
//        }
//        return true;
//    }
//
//    @Override
//    public int hashCode() {
//        int hash = 3;
//        hash = 89 * hash + (int) (this.id ^ (this.id >>> 32));
//        hash = 89 * hash + (this.segunda ? 1 : 0);
//        hash = 89 * hash + (this.terça ? 1 : 0);
//        hash = 89 * hash + (this.quarta ? 1 : 0);
//        hash = 89 * hash + (this.quinta ? 1 : 0);
//        hash = 89 * hash + (this.sexta ? 1 : 0);
//        hash = 89 * hash + (this.sabado ? 1 : 0);
//        hash = 89 * hash + (this.horarioDe != null ? this.horarioDe.hashCode() : 0);
//        hash = 89 * hash + (this.a != null ? this.a.hashCode() : 0);
//        return hash;
//    }
//
//    @Override
//    public String toString() {
//        return "Horario{" + "id=" + id + ", segunda=" + segunda + ", ter\u00e7a=" + terça + ", quarta=" + quarta + ", quinta=" + quinta + ", sexta=" + sexta + ", sabado=" + sabado + ", horarioDe=" + horarioDe + ", a=" + a + '}';
//    }
//
//    public boolean isaCombinar() {
//        return aCombinar;
//    }
//
//    public void setaCombinar(boolean aCombinar) {
//        this.aCombinar = aCombinar;
//    }
//
//    public Paciente getPaciente() {
//        return paciente;
//    }
//
//    public void setPaciente(Paciente paciente) {
//        this.paciente = paciente;
//    }
//    
//    public void removerHorario(){
//        paciente.getHorario().remove(this);
//       
//    }
//    
//    
//}
