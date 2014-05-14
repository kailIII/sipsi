package security;

import entities.Context;
import entities.CurrentUser;
import entities.Repository;
import entities.annotations.*;
import entities.dao.DAOConstraintException;
import entities.dao.DAOException;
import entities.dao.DAOValidationException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@NamedQueries({
    @NamedQuery(name = "autenticacao",
            query = ""
            + "Select distinct u"
            + "  From Usuario u"
            + "  left join fetch u.papeis"
            + " Where u.usuario = :usuario "
            + "   and u.senha = :senha "),
    
    @NamedQuery(name = "UsuarioCorrente",
            query = ""
            + "Select u"
            + "  From Usuario u"
            + " Where u = :usuario"),
    
    @NamedQuery(name = "Administradores",
            query = ""
            + "Select u"
            + "  From Usuario u"
            + " Where 'Administrador' in elements(u.papeis)"
            + " Order By u.nome ")})
@Views({
    /**
     * View de Login
     */
    @View(name = "Login",
            title = "Login",
            members = "[#usuario;#senha;login()]",
            namedQuery = "Select new security.Usuario()"),
    /**
     * View de Logout
     */
    @View(name = "Logout",
            title = "Logout",
            members = "[*nome;logout()]",
            namedQuery = "UsuarioCorrente",
            params = {
        @Param(name = "usuario", value = "#{context.currentUser()}")},
            roles = "Administrador,Supervisor,Estagiario,Secretario,Coordenador,Bolsista,Super"),
    /**
     * Cadastro de Usuarios
     */
    @View(name = "CadastroDeUsuarios",
            title = "Cadastro de Usuários",
            members = "Dados do Usuário[nome:2;usuario:2;senha,alterarSenha()],papeis",
            template = "@CRUD+@PAGER",
            roles = "Super,Administrador,Coordenador")
})

@EntityDescriptor(displayName = "Usuário",pluralDisplayName = "Usuários",roles = "Super,Administrador")
public class Usuario implements Serializable, CurrentUser {

    public enum Papel {

        Administrador, Coordenador, Supervisor, Estagiario, Secretario, Bolsista
    }

    public enum Status {

        Ativo("aprovada"),
        Inativo("inativa"),
        Bloqueado("bloqueada");

        Status(String icone) {
//            InputStream inp = Thread.currentThread().
//                    getContextClassLoader().
//                    getResourceAsStream("resources/" + icone + ".png");
//            try {
//                this.icone = FileHelper.getBytes(inp);
//            } catch (IOException ex) {
//                Logger.getLogger(Usuario.class.getName()).log(Level.SEVERE, null, ex);
//            }
        }
        private byte[] icone;

        public byte[] getIcone() {
            return icone;
        }

        public void setIcone(byte[] icone) {
            this.icone = icone;
        }
    }
    
    @NotEmpty(message = "É necessário informar o nome completo!")
    @Column(length = 100)
    @PropertyDescriptor(displayName = "Nome", displayWidth = 100)
    private String nome;
    
    @Id
    @Column(length = 32)
    @PropertyDescriptor(displayName = "Usuário", displayWidth = 32)
    @NotEmpty(message = "É necessário informar o usuário!")
    private String usuario;
    
    @Column(length = 32)
    @Type(type = "entities.dao.hibernate.PasswordType")
    @PropertyDescriptor(secret = true, displayWidth = 32)
    @NotEmpty(message = "É necessário informar a senha!")
    private String senha;
    
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @Cascade(value = {CascadeType.ALL})
    @Editor(inputComponentName = "org.richfaces.component.html.HtmlPickList",
            outputComponentName = "org.richfaces.component.html.HtmlPickList")
    @PropertyDescriptor(shortDescription = "Papel do usuário, ou melhor, a sua função. Essa informação é importante, pois o acesso as telas do sistema dependende de função do usuário.")
    private List<Papel> papeis = new ArrayList<Papel>();
    
    @NotNull(message = "É preciso informar o status.")
    @Enumerated(EnumType.STRING)
    private Status status = Status.Ativo;

    //<editor-fold defaultstate="collapsed" desc="gets e sets">
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Papel> getPapeis() {
        return papeis;
    }

    public void setPapeis(List<Papel> papeis) {
        this.papeis = papeis;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    //</editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Implentação de CurrentUser">
    @Override
    public String userName() {
        return getNome();
    }

    @Override
    public boolean hasRoles(String... roles) {
        for (String role : roles) {
            for (Papel papel : getPapeis()) {
                if (papel.name().equals(role)) {
                    return true;
                }
            }
        }
        return false;
    }// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Autorização & Autenticação">
    @ActionDescriptor(hidden = true, value = "Entrar", displayName = "Entrar")
    public String login() throws DAOException, DAOValidationException, DAOConstraintException {
        if (usuario.equals("Super") && senha.equals("Super")) {
            if (Repository.queryCount("Administradores") > 0) {
                throw new SecurityException("O sistema já possui administrador");
            }

            CurrentUser currentUser = new Usuario() {
                @Override
                public String userName() {
                    return "Super";
                }

                @Override
                public boolean hasRoles(String... roles) {
                    for (String role : roles) {
                        if (role.equals("Super")) {
                            return true;
                        }
                    }
                    return false;
                }
            };
            Context.setCurrentUser(currentUser);
            return "redirect:main.jsf?view=security.Usuario@CadastroDeUsuarios";
        } else {
            Usuario login = null;
            List<Usuario> usuarios = Repository.query("autenticacao", usuario, senha);
            if (usuarios.size() == 1) {
                login = usuarios.get(0);
                if (!Status.Ativo.equals(login.getStatus())) {
                    throw new SecurityException("Usuário não está ativo!");
                }
                Context.setCurrentUser(login);
            }
        }
        return "redirect:main";
    }

    @ActionDescriptor(hidden = true)
    public static Usuario getCurrentUser() {
        return (Usuario) Context.getCurrentUser();
    }

    @ActionDescriptor(value = "Sair", methodRendered = "#{not empty context.currentUser()}")
    static public String logout() {
        Context.clear();
        return "redirect:main.jsf?view=security.Usuario@Login";
    }

    @ActionDescriptor(hidden = false, value = "Alterar Senha")
    public String alterarSenha(@ParameterDescriptor(displayName = "Nova Senha", secret = true, required = true) String novaSenha,
            @ParameterDescriptor(displayName = "Confirme Senha", secret = true, required = true) String confirmaSenha) throws
            DAOValidationException, DAOConstraintException, DAOException {
        if (novaSenha.equals(confirmaSenha)) {
            this.setSenha(novaSenha);
            Context.setCurrentUser(this);
            Repository.save(this);
            return "Senha alterada com sucesso!";
        } else {
            throw new SecurityException("Nova senha diferente da sua confirmação!");
        }
    }    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Equals, Hashcode e toString()">
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Usuario other = (Usuario) obj;
        if ((this.nome == null) ? (other.nome != null) : !this.nome.equals(other.nome)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (this.nome != null ? this.nome.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return usuario;
    }//</editor-fold>
}
