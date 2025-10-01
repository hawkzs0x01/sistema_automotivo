package com.fecaf.sistema_automotivo.config;

import com.fecaf.sistema_automotivo.model.Usuario;
import com.fecaf.sistema_automotivo.repository.UsuarioRepository;
import com.fecaf.sistema_automotivo.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Override
    public void run(String... args) throws Exception {
        criarUsuarioSeNaoExistir("admin", "1234", "ROLE_ADMIN", "99999-9999");
        criarUsuarioSeNaoExistir("user", "1234", "ROLE_USER", "88888-8888");
    }

    private void criarUsuarioSeNaoExistir(String username, String password, String role, String telefone) {
        if (usuarioRepository.findByUsername(username).isEmpty()) {
            Usuario novoUsuario = new Usuario();
            novoUsuario.setUsername(username);
            novoUsuario.setPassword(password);
            novoUsuario.setRole(role);
            novoUsuario.setTelefone(telefone);

            usuarioService.criarUsuario(novoUsuario);
            System.out.println("Usuário de teste criado: " + username);
        } else {
            System.out.println("Usuário de teste '" + username + "' já existe.");
        }
    }
}