package ult.nodo.ciencia.seguridad;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ult.nodo.ciencia.clases.Usuario;
import ult.nodo.ciencia.repositorio.UsuarioRepositorio;

import java.util.Optional;

@Service("userDetailsService")
public class EntityUserDetailsService implements UserDetailsService {

    private final Logger log = LoggerFactory.getLogger(EntityUserDetailsService.class);

    @Autowired
    private UsuarioRepositorio usuarioRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String login) {
        Optional<Usuario> userFromDatabase = usuarioRepository.findByUsuario(login.toLowerCase());
        return userFromDatabase.orElseThrow(() -> new UsernameNotFoundException("Nombre usuario o contraseña incorrectos"));
    }
}
