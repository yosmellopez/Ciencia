package ult.nodo.ciencia.seguridad.jwt;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ult.nodo.ciencia.clases.Usuario;
import ult.nodo.ciencia.repositorio.UsuarioRepositorio;
import ult.nodo.ciencia.seguridad.jwt.token.RawAccessJwtToken;

@Component
@SuppressWarnings("unchecked")
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final JwtSettings jwtSettings;

    @Autowired
    private UsuarioRepositorio usuarioRepository;

    @Autowired
    public JwtAuthenticationProvider(JwtSettings jwtSettings) {
        this.jwtSettings = jwtSettings;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        RawAccessJwtToken rawAccessToken = (RawAccessJwtToken) authentication.getCredentials();
        Jws<Claims> jwsClaims = rawAccessToken.parseClaims(jwtSettings.getTokenSigningKey());
        String subject = jwsClaims.getBody().getSubject();
        Usuario user = usuarioRepository.findByUsuario(subject).get();
        JwtAuthenticationToken jwtAuthenticationToken = new JwtAuthenticationToken(user, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(jwtAuthenticationToken);
        return jwtAuthenticationToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (JwtAuthenticationToken.class.isAssignableFrom(authentication));
    }
}