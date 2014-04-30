/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import domain.status.*;
import entities.Context;
import entities.Repository;
import entities.annotations.*;
import entities.dao.DAOConstraintException;
import entities.dao.DAOException;
import entities.dao.DAOValidationException;
import entities.descriptor.PropertyType;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;
import security.Usuario;

/*
 * @author maria
 */

/**
 *
 * @author diogenes
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "PacientePorPrioridade",
            query = ""
            + "Select p"
            + "  From Paciente p"
            + " Order By p.prioridade "),
    @NamedQuery(
            name = "Estagiarios",
            query = "Select us From security.Usuario us where 'Estagiario' in elements(us.papeis)")
})
@Views({
    @View(name = "Pacientes",
            title = "Inscrição de Pacientes",
            filters = "nome,'Número de Inscrição':numeroDeInscricao,Ctrl.DAO.filter()",
            members = "[Paciente["
            + "'Número de Inscrição':numeroDeInscricao:1,gerarID():1;"
            + "nome:1,'Nome do Pai':nomePai:1,'Nome da Mãe':nomeMae:1;"
            + "estadoCivil:1,sexo:1;"
            + "'Telefone Residencial':foneResid:1,'Telefone Trabalho':foneTrab:1,'Celular':foneCel:1;"
            + "'Data da Inscrição':dataInscricao,'Data de Nascimento':dataNascimente:1,renda:1;"
            + "rua:1,"
            + "'Número':numero:1,apartamento:1;bairro:1;"
            + "cidade,cep:1;"
            + "profissao:1,'Local de Trabalho':localTrabalho,"
            + "escolaridade:1;"
            + "'Série':serie,turno:1,'Faixa Etária':faixaEtaria:1];"
            + "[[Responsável Pelo Paciente['Nome do Responsável':nomeDoResponsavel;parentesco]],"
            + "[Atendimento Solicitado[acompanhamentoIndividual,orientacaoVocacional;acompanhamentoEmGrupo,psicomotricidade;psicodiagnostico]],"
            + "[Dias Disponíveis[segunda:1,'Terça':terca:1;quarta:1,quinta:1;sexta:1,'Sábado':sabado:1,aCombinar:1]],"
            + "[Horarios Disponíveis['08:00 às 10:00':primeiro;'10:00 às 12:00':segundo;'13:00 às 15:00':terceiro,'15:00 às 17:00':quarto]]];"    
            + "[Observações[observacoes,reponsavelPeloEncaminhamento]]]",
            template = "@CRUD_PAGE",
            rows = 1,
            roles = "Secretario,Bolsista,Coordenador,Administrador"),
    
    @View(name = "DefinirPrioridade",
            title = "Cadastrar Prioridades",
            rows = 10,
            members = "nome,sexo,localTrabalho,faixaEtaria,observacoes,prioridade",
            namedQuery = "PacientePorPrioridade",
            roles = "Supervisor,Coordenador,Administrador",
            template = "@CRUD+@PAGER"),
    
    @View(name = "SelecionadosTriagem",
            title = "Selecionados Triagem",
            members = "estagiario,nome,prioridade,dataInicioAtendimento,status",
            namedQuery = "Select ot From Paciente ot,EmAtendimento st Where ot.status = st Order By ot.prioridade",
            roles = "Coordenador,Administrador",
            rows = 5,
            template = "@PAGER"),
    
    @View(name = "SelecionarTriagem",
            title = "Selecionar Para Triagem",
            filters = "Filtro[nome:1,'Número de Inscrição':numeroDeInscricao:1,Ctrl.DAO.filter()]",
            members = "[Paciente[#estagiario:1,status:1;nome:1,prioridade:1;sexo:1,'Data de Nascimento':dataNascimente:1;Horários Disponíveis[segunda,'Terça':terca,quarta,quinta,sexta,aCombinar;'08:00 às 10:00':primeiro,'10:00 às 12:00':segundo,'13:00 às 15:00':terceiro,'15:00 às 17:00':quarto],'Observações':#obs;[escolherPaciente()]]]",
            namedQuery = "Select ot From Paciente ot,EsperandoAtendimento st Where ot.status = st Order By ot.prioridade",
            template = "@CRUD_PAGE",
            rows = 5,
            roles = "Coordenador,Administrador"),
    
    @View(name = "SelecionarTriagemEstagiario",
            title = "Selecionar Para Triagem",
            filters = "Filtro[nome:1,'Número de Inscrição':numeroDeInscricao:1,Ctrl.DAO.filter()]",
            members = "[Paciente[#estagiario:1,status:1;nome:1,prioridade:1;sexo:1,'Data de Nascimento':dataNascimente:1;Horários Disponíveis[segunda,'Terça':terca,quarta,quinta,sexta,aCombinar;'08:00 às 10:00':primeiro,'10:00 às 12:00':segundo,'13:00 às 15:00':terceiro,'15:00 às 17:00':quarto],'Observações':#obs;[escolherPaciente()]]]",
            namedQuery = "Select ot From Paciente ot,EsperandoAtendimento st Where ot.status = st Order By ot.prioridade",
            params = @Param(name = "outra", value = "#{context.currentUser()}"),
            template = "@PAGER",
            rows = 5,
            roles = "Estagiario"),
    
    @View(name = "status",
            title = "Status do Paciente",
            filters = "[nome:1,'Número de Inscrição':numeroDeInscricao:1,Ctrl.DAO.filter()]",
            members = "[Paciente[nome:1,'Número de Inscrição':numeroDeInscricao:1;prioridade:1,status:1]]",
            roles = "Coordenador,Administrador,Secretario,Bolsista",
            rows = 5,
            template = "@PAGER"),
    
    @View(name = "MeusPacientesCoordenador",
            title = "Pacientes Em Atendimento",
            filters = "Filtro[estagiario:1,nome:1,Ctrl.DAO.filter()]",
            members = "[Meus Pacientes[#estagiario,nome;prioridade,dataInicioAtendimento;status,'Observações':#obs;[encaminhar(),arquivoMorto()]]]",
            namedQuery = "Select ot From Paciente ot,EmAtendimento st Where ot.status = st Order By ot.prioridade",
            roles = "Coordenador,Administrador",
            rows = 5,
            template = "@PAGER"),
    
    @View(name = "MeusPacientesEstagiario",
            title = "Pacientes Em Atendimento",
            filters = "Filtro[nome,'Número de Inscrição':numeroDeInscricao:1,Ctrl.DAO.filter()]",
            members = "[Meus Pacientes[#estagiario:1,nome:1;prioridade:1,dataInicioAtendimento:1;status:1,'Observações':#obs;[encaminhar(),arquivoMorto()]]]",
            namedQuery = "Select ot From Paciente ot,EmAtendimento st Where ot.status = st and ot.estagiario = :outra Order By ot.prioridade",
            params =
            @Param(name = "outra", value = "#{context.currentUser()}"),
            roles = "Estagiario",
            rows = 5,
            template = "@PAGER"),
    
    @View(name = "AprovarEncaminhamento",
            title = "Aprovar Encaminhamentos",
            members = "[estagiario:2;nome,'Data do Pedido':dataInicioAtendimento;prioridade;status;#obs;[ok(),rejeitar()]]",
            namedQuery = "Select ot From Paciente ot,Encaminhamento st Where ot.status = st Order By ot.prioridade",
            template = "@PAGER",
            rows = 5,
            roles = "Estagiario,Coordenador,Administrador"),
    
    @View(name = "ArquivoMorto",
            title = "Arquivo Morto",
            filters = "Filtro[nome,estagiario,Ctrl.DAO.filter()]",
            members = "[Paciente[estagiario;nome,prioridade;'Data':dataInicioAtendimento;status,[retornarAtendimento()]]]",
            namedQuery = "Select ot From Paciente ot,ArquivoMorto st where ot.status = st Order By ot.prioridade",
            template="@PAGER",
            rows = 5,
            roles = "Estagiario,Coordenador,Administrador,Supervisor"),
    
    @View(name = "AguardandoAtendimentoCoordenador",
            title = "Aguardando Atendimento",
            filters = "Filtro[nome:1,'Número de Inscrição':numeroDeInscricao:1,Ctrl.DAO.filter()]",
            members = "[Paciente[#estagiario:2;nome,prioridade;'Data de Nascimento':dataNascimente,sexo;status;Horarios Disponíveis [segunda,'Terça':terca,quarta,quinta,sexta,aCombinar;'08:00 às 10:00':primeiro,'10:00 às 12:00':segundo,'13:00 às 15:00':terceiro,'15:00 às 17:00':quarto],'Observações':#obs;[ok()]]]",
            namedQuery = "Select ot From Paciente ot,AguardandoAtendimento st where ot.status = st Order By ot.prioridade",
            template = "@PAGER",
            rows = 5,
            roles = "Coordenador,Administrador"),
    
    @View(name = "AguardandoAtendimentoEstagiario",
            title = "Aguardando Atendimento",
            filters = "Filtro[nome:1,'Número de Inscrição':numeroDeInscricao:1,Ctrl.DAO.filter()]",
            members = "[Paciente[nome,prioridade;'Data de Nascimento':dataNascimente,sexo;status;Horarios Disponíveis [segunda,'Terça':terca,quarta,quinta,sexta,aCombinar;'08:00 às 10:00':primeiro,'10:00 às 12:00':segundo,'13:00 às 15:00':terceiro,'15:00 às 17:00':quarto],#obs;[Ok()]]]",
            namedQuery = "Select ot From Paciente ot,AguardandoAtendimento st where ot.status = st Order By ot.prioridade",
            params = @Param(name = "outra", value = "#{context.currentUser()}"),
            template = "@PAGER",
            rows = 5,
            roles = "Estagiario")
})

public class Paciente implements Serializable {

    public enum Prioridade{
        Muito_Alta, Alta, Media, Normal, Baixa, Muito_Baixa
    }
    
    public enum Renda {
        De_1_a_3_Salarios, De_3_a_5_Salarios, De_5_a_7_Salarios
    }
    
    public enum Sexo{
        Masculino,Femininino
    }
    
    public enum FaixaEtaria{
        Criança, Adolescente, Jovem, Adulto, Idoso
    }
    
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataInscricao;
    
    @Id
    @GeneratedValue()
    private long id;
    
    @Column(length = 50)
    @NotEmpty(message = "Informe o Nome do Paciente")
    private String nome;
    
    boolean acompanhamentoIndividual;
    boolean acompanhamentoEmGrupo;
    boolean psicodiagnostico;
    boolean orientacaoVocacional;
    boolean psicomotricidade;
    
    private Boolean segunda;
    private Boolean terca;
    private Boolean quarta;
    private Boolean quinta;
    private Boolean sexta;
    private Boolean sabado;
    private Boolean primeiro;
    private Boolean segundo;
    private Boolean terceiro;
    private Boolean quarto;
    private Boolean aCombinar;
    
    @NotEmpty(message = "Informe o Nome do Pai")
    @Column(length = 50)
    private String nomePai;
    
    @NotEmpty(message = "Informe o Nome da Mãe")
    @Column(length = 50)
    private String nomeMae;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataNascimente;
    
    @Column(length = 50)
    private String estadoCivil;
    
    @Column(length = 15)
    @PropertyDescriptor(mask = "(99)9999-9999", displayWidth = 15)
    private String foneResid;
    
    @Column(length = 15)
    @PropertyDescriptor(mask = "(99)9999-9999", displayWidth = 15)
    private String foneTrab;
    
    @Column(length = 15)
    @PropertyDescriptor(mask = "(99)9999-9999", displayWidth = 15)
    private String foneCel;
    
    @Column(length = 9, unique = true)
    @NotEmpty(message = "Informe o Número de Inscrição")
    @PropertyDescriptor(mask = "9999/9999", displayWidth = 9)
    private String numeroDeInscricao;
    
    @NotEmpty(message = "Informe a Rua")
    @Column(length = 50)
    private String rua;
    
    @NotEmpty(message = "Informe o Número da Casa")
    @Column(length = 50)
    private String numero;
    
    @Column(length = 50)
    private String apartamento;
    
    @Column(length = 50)
    private String bairro;
    
    @NotEmpty(message = "Informe a Cidade")
    @Column(length = 50)
    private String cidade;
    
    @Column(length = 50)
    @PropertyDescriptor(mask = "99999-999", displayWidth = 9)
    private String cep;
    
    @NotEmpty(message = "Informe a Profissão")
    @Column(length = 50)
    private String profissao;
    
    @Column(length = 50)
    private String localTrabalho;
    
    @NotEmpty(message = "Informe a Escolaridade")
    @Column(length = 50)
    private String escolaridade;
    
    @Column(length = 50)
    private String serie;
    
    @Column(length = 50)
    private String turno;
    
    private Renda renda;
    
    @PropertyDescriptor(autoSort = true)
    private Prioridade prioridade;
    
    @NotNull(message = "Selecione o Sexo do Paciente")
    private Sexo sexo;
    
    @NotNull(message = "Selecione a faixa etária ao Paciente")
    private FaixaEtaria faixaEtaria;
    
    @Column(length = 50)
    //@NotEmpty(message = "Informe o Nome do Responsável")
    private String nomeDoResponsavel;
    
    @Column(length = 50)
    //@NotEmpty(message = "Informe o Parentesco")
    private String parentesco;
    
    @Editor(inputComponentName = "javax.faces.component.html.HtmlInputTextarea")
    @Column(length = 100)
    private String observacoes;
    
    @Column(length = 50)
    private String reponsavelPeloEncaminhamento;
    
    @Editor(inputComponentName = "javax.faces.component.html.HtmlInputTextarea")
    @Column(length = 200)
    private String obs;
    
    @Column(length = 35)
    private String nomeUltimoEstagiario;
    
    @ManyToOne()
    @PropertyDescriptor(autoFilter = true)
    @Editor(namedQuery = "Estagiarios")
    private Usuario estagiario;
    
    @NotNull
    @ManyToOne(cascade = CascadeType.ALL)
    private Status status = new EsperandoAtendimento(this);
    
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataInicioAtendimento;

    public Date getDataInscricao() {
        return dataInscricao;
    }

    public void setDataInscricao(Date dataInscricao) {
        this.dataInscricao = dataInscricao;
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

    public boolean isAcompanhamentoIndividual() {
        return acompanhamentoIndividual;
    }

    public void setAcompanhamentoIndividual(boolean acompanhamentoIndividual) {
        this.acompanhamentoIndividual = acompanhamentoIndividual;
    }

    public boolean isAcompanhamentoEmGrupo() {
        return acompanhamentoEmGrupo;
    }

    public void setAcompanhamentoEmGrupo(boolean acompanhamentoEmGrupo) {
        this.acompanhamentoEmGrupo = acompanhamentoEmGrupo;
    }

    public boolean isPsicodiagnostico() {
        return psicodiagnostico;
    }

    public void setPsicodiagnostico(boolean psicodiagnostico) {
        this.psicodiagnostico = psicodiagnostico;
    }

    public boolean isOrientacaoVocacional() {
        return orientacaoVocacional;
    }

    public void setOrientacaoVocacional(boolean orientacaoVocacional) {
        this.orientacaoVocacional = orientacaoVocacional;
    }

    public boolean isPsicomotricidade() {
        return psicomotricidade;
    }

    public void setPsicomotricidade(boolean psicomotricidade) {
        this.psicomotricidade = psicomotricidade;
    }

    public Boolean getSegunda() {
        return segunda;
    }

    public void setSegunda(Boolean segunda) {
        this.segunda = segunda;
    }

    public Boolean getTerca() {
        return terca;
    }

    public void setTerca(Boolean terca) {
        this.terca = terca;
    }

    public Boolean getQuarta() {
        return quarta;
    }

    public void setQuarta(Boolean quarta) {
        this.quarta = quarta;
    }

    public Boolean getQuinta() {
        return quinta;
    }

    public void setQuinta(Boolean quinta) {
        this.quinta = quinta;
    }

    public Boolean getSexta() {
        return sexta;
    }

    public void setSexta(Boolean sexta) {
        this.sexta = sexta;
    }

    public Boolean getSabado() {
        return sabado;
    }

    public void setSabado(Boolean sabado) {
        this.sabado = sabado;
    }

    public Boolean getPrimeiro() {
        return primeiro;
    }

    public void setPrimeiro(Boolean primeiro) {
        this.primeiro = primeiro;
    }

    public Boolean getSegundo() {
        return segundo;
    }

    public void setSegundo(Boolean segundo) {
        this.segundo = segundo;
    }

    public Boolean getTerceiro() {
        return terceiro;
    }

    public void setTerceiro(Boolean terceiro) {
        this.terceiro = terceiro;
    }

    public Boolean getQuarto() {
        return quarto;
    }

    public void setQuarto(Boolean quarto) {
        this.quarto = quarto;
    }

    public Boolean getaCombinar() {
        return aCombinar;
    }

    public void setaCombinar(Boolean aCombinar) {
        this.aCombinar = aCombinar;
    }

    public String getNomePai() {
        return nomePai;
    }

    public void setNomePai(String nomePai) {
        this.nomePai = nomePai;
    }

    public String getNomeMae() {
        return nomeMae;
    }

    public void setNomeMae(String nomeMae) {
        this.nomeMae = nomeMae;
    }

    public Date getDataNascimente() {
        return dataNascimente;
    }

    public void setDataNascimente(Date dataNascimente) {
        this.dataNascimente = dataNascimente;
    }

    public String getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(String estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public String getFoneResid() {
        return foneResid;
    }

    public void setFoneResid(String foneResid) {
        this.foneResid = foneResid;
    }

    public String getFoneTrab() {
        return foneTrab;
    }

    public void setFoneTrab(String foneTrab) {
        this.foneTrab = foneTrab;
    }

    public String getFoneCel() {
        return foneCel;
    }

    public void setFoneCel(String foneCel) {
        this.foneCel = foneCel;
    }

    public String getNumeroDeInscricao() {
        return numeroDeInscricao;
    }

    public void setNumeroDeInscricao(String numeroDeInscricao) {
        this.numeroDeInscricao = numeroDeInscricao;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getApartamento() {
        return apartamento;
    }

    public void setApartamento(String apartamento) {
        this.apartamento = apartamento;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getProfissao() {
        return profissao;
    }

    public void setProfissao(String profissao) {
        this.profissao = profissao;
    }

    public String getLocalTrabalho() {
        return localTrabalho;
    }

    public void setLocalTrabalho(String localTrabalho) {
        this.localTrabalho = localTrabalho;
    }

    public String getEscolaridade() {
        return escolaridade;
    }

    public void setEscolaridade(String escolaridade) {
        this.escolaridade = escolaridade;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public Renda getRenda() {
        return renda;
    }

    public void setRenda(Renda renda) {
        this.renda = renda;
    }

    public Prioridade getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(Prioridade prioridade) {
        this.prioridade = prioridade;
    }

    public Sexo getSexo() {
        return sexo;
    }

    public void setSexo(Sexo sexo) {
        this.sexo = sexo;
    }

    public FaixaEtaria getFaixaEtaria() {
        return faixaEtaria;
    }

    public void setFaixaEtaria(FaixaEtaria faixaEtaria) {
        this.faixaEtaria = faixaEtaria;
    }

    public String getNomeDoResponsavel() {
        return nomeDoResponsavel;
    }

    public void setNomeDoResponsavel(String nomeDoResponsavel) {
        this.nomeDoResponsavel = nomeDoResponsavel;
    }

    public String getParentesco() {
        return parentesco;
    }

    public void setParentesco(String parentesco) {
        this.parentesco = parentesco;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public String getReponsavelPeloEncaminhamento() {
        return reponsavelPeloEncaminhamento;
    }

    public void setReponsavelPeloEncaminhamento(String reponsavelPeloEncaminhamento) {
        this.reponsavelPeloEncaminhamento = reponsavelPeloEncaminhamento;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public String getNomeUltimoEstagiario() {
        return nomeUltimoEstagiario;
    }

    public void setNomeUltimoEstagiario(String nomeUltimoEstagiario) {
        this.nomeUltimoEstagiario = nomeUltimoEstagiario;
    }

    public Usuario getEstagiario() {
        return estagiario;
    }

    public void setEstagiario(Usuario estagiario) {
        this.estagiario = estagiario;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Date getDataInicioAtendimento() {
        return dataInicioAtendimento;
    }

    public void setDataInicioAtendimento(Date dataInicioAtendimento) {
        this.dataInicioAtendimento = dataInicioAtendimento;
    }

    

    @Override
    public String toString() {
        return "Paciente{" + "nome=" + nome + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 73 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Paciente other = (Paciente) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    
    
    
    @Transaction
    public String ok() throws StatusException, DAOValidationException, DAOConstraintException, DAOException {
        this.status.setPaciente(this);
        Historico hist = new domain.Historico();
        hist.setNomePaciente(this.nome);
        hist.setObservacao(this.obs);
        hist.setNomeEstagiario(this.nomeUltimoEstagiario);
        Date date = new Date();
        hist.setDataTransacao(date);
        Repository.save(hist);
        this.obs = " ";
        this.nomeUltimoEstagiario = this.estagiario.getNome();
        this.dataInicioAtendimento = date;
        status.ok();
        return "Paciente Escolhido com Sucesso";
    }

    @Transaction
    public String Ok() throws StatusException, DAOValidationException, DAOConstraintException, DAOException {
        this.status.setPaciente(this);
        this.estagiario = (Usuario) Context.getCurrentUser();

        Historico hist = new domain.Historico();
        hist.setNomePaciente(this.nome);
        hist.setObservacao(this.obs);
        hist.setNomeEstagiario(this.nomeUltimoEstagiario);
        Date date = new Date();
        hist.setDataTransacao(date);
        Repository.save(hist);
        this.obs = " ";
        this.nomeUltimoEstagiario = this.estagiario.getNome();
        this.dataInicioAtendimento = date;
        status.ok();
        return "Paciente Escolhido com Sucesso";
    }

    @Transaction
    public String escolherPaciente() throws StatusException, DAOValidationException, DAOConstraintException, DAOException {
        Usuario u = (Usuario) Context.getCurrentUser();
        if (u.getPapeis().get(0) == Usuario.Papel.Estagiario) {
            this.estagiario = u;
        }
        this.status.setPaciente(this);

        Historico hist = new domain.Historico();
        hist.setNomePaciente(this.nome);
        hist.setObservacao(this.obs);
        hist.setNomeEstagiario(this.estagiario.getNome());
        Date date = new Date();
        hist.setDataTransacao(date);
        hist.setMudancaDeStatus("Saiu de Esperando Atendimento para Em Atendimento");
        Repository.save(hist);
        this.obs = " ";
        this.nomeUltimoEstagiario = this.estagiario.getNome();
        this.dataInicioAtendimento = date;              
        status.ok();
        return "Paciente Escolhido com Sucesso";
    }

    @Transaction
    public String rejeitar() throws StatusException, DAOValidationException, DAOConstraintException, DAOException {
        this.status.setPaciente(this);
        Historico hist = new domain.Historico();
        hist.setNomePaciente(this.nome);
        hist.setObservacao(this.obs);
        hist.setNomeEstagiario(this.nomeUltimoEstagiario);
        Date date = new Date();
        hist.setDataTransacao(date);
        Repository.save(hist);
        this.obs = " ";
        status.rejeitar();
        return "Encaminhamento Rejeitado";
    }

    @Transaction
    public void reverter(
            @Editor(propertyType = PropertyType.MEMO)
            @ParameterDescriptor(displayName = "Remark") String remark)
            throws StatusException {
        this.status.setPaciente(this);
        status.reverter(remark);
    }

    /**
     *
     * @return
     * @throws StatusException
     * @throws DAOValidationException
     * @throws DAOConstraintException
     * @throws DAOException
     */
    @Transaction
    public String encaminhar() throws StatusException, DAOValidationException, DAOConstraintException, DAOException {
        this.status.setPaciente(this);
        Historico hist = new domain.Historico();
        hist.setNomePaciente(this.nome);
        hist.setObservacao(this.obs);
        hist.setNomeEstagiario(this.nomeUltimoEstagiario);
        Date date = new Date();
        hist.setDataTransacao(date);
        Repository.save(hist);
        this.obs = " ";
        this.nomeUltimoEstagiario = this.estagiario.getNome();
        this.dataInicioAtendimento = date;
        status.ok();
        return "Paciente Encaminhado com Sucesso";
    }

    /**
     *
     * @return
     * @throws StatusException
     * @throws DAOValidationException
     * @throws DAOConstraintException
     * @throws DAOException
     */
    @Transaction
    public String arquivoMorto() throws StatusException, DAOValidationException, DAOConstraintException, DAOException {
        this.status.setPaciente(this);
        Historico hist = new domain.Historico();
        hist.setNomePaciente(this.nome);
        hist.setObservacao(this.obs);
        hist.setNomeEstagiario(this.nomeUltimoEstagiario);
        Date date = new Date();
        hist.setDataTransacao(date);
        Repository.save(hist);
        this.obs = " ";
        this.dataInicioAtendimento = date;
        status.rejeitar();
        return "Paciente Movido para Arquivo Morto";
    }

    @Transaction
    public String retornarAtendimento() throws StatusException, DAOValidationException, DAOConstraintException, DAOException {
        this.status.setPaciente(this);
        Historico hist = new domain.Historico();
        hist.setNomePaciente(this.nome);
        hist.setObservacao(this.obs);
        hist.setNomeEstagiario(this.nomeUltimoEstagiario);
        Date date = new Date();
        hist.setDataTransacao(date);
        Repository.save(hist);
        this.obs = " ";
        this.nomeUltimoEstagiario = this.estagiario.getNome();
        this.dataInicioAtendimento = date;
        status.ok();

        return "Paciente Movido para Aguardando Atendimento";
    }

    
    
    public void estagiario() {
        this.estagiario = (Usuario) Context.getCurrentUser();

    }
    
    public void gerarID() {
        String zero = "";
        String numero = GravaArquivo.realizaInscricao();

        for (int i = 0; i < 9 - numero.length(); i++) {
            zero += "0";
        }

        this.numeroDeInscricao = zero + numero;
    }
}
