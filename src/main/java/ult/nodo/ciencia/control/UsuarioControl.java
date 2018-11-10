package ult.nodo.ciencia.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ult.nodo.ciencia.clases.Rol;
import ult.nodo.ciencia.clases.Usuario;
import ult.nodo.ciencia.excepcion.UsuarioException;
import ult.nodo.ciencia.excepcion.GeneralException;
import ult.nodo.ciencia.repositorio.RolRepositorio;
import ult.nodo.ciencia.repositorio.UsuarioRepositorio;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static ult.nodo.ciencia.control.AppResponse.failure;
import static ult.nodo.ciencia.control.AppResponse.success;

@RestController
@RequestMapping(value = "/api")
public class UsuarioControl {

    private final UsuarioRepositorio usuarioRepository;

    private final RolRepositorio rolRepositorio;

    @Autowired
    public UsuarioControl(UsuarioRepositorio usuarioRepository, RolRepositorio rolRepositorio) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepositorio = rolRepositorio;
    }

    @GetMapping(value = "/usuario")
    public ResponseEntity<AppResponse<Usuario>> listarUsuarios(Pageable pageable) {
        Page<Usuario> usuarios = usuarioRepository.findAll(pageable);
        return ResponseEntity.ok(success(usuarios.getContent()).total(usuarios.getTotalElements()).build());
    }

    @GetMapping(value = "/usuario/all")
    public ResponseEntity<AppResponse<Usuario>> listarAllUsuarios() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        return ResponseEntity.ok(success(usuarios).total(usuarios.size()).build());
    }

    @PostMapping(value = "/usuario")
    public ResponseEntity<AppResponse<Usuario>> insertarUsuario(@Valid @RequestBody Usuario usuario) {
        usuarioRepository.saveAndFlush(usuario);
        return ResponseEntity.ok(success(usuario).build());
    }

    @PutMapping(value = "/usuario/{idUsuario}")
    public ResponseEntity<AppResponse<Usuario>> actualizarUsuario(@PathVariable("idUsuario") Optional<Usuario> optional, @RequestBody Usuario usuario) {
        Usuario usuarioBd = optional.orElseThrow(() -> new EntityNotFoundException("usuario_not_found"));
        usuarioBd.clonar(usuario);
        usuarioRepository.saveAndFlush(usuarioBd);
        return ResponseEntity.ok(success(usuarioBd).build());
    }

    @DeleteMapping(value = "/usuario/{idUsuario}")
    public ResponseEntity<AppResponse> eliminarUsuario(@PathVariable("idUsuario") Optional<Usuario> optional, Locale locale) {
        Usuario usuario = optional.orElseThrow(() -> new EntityNotFoundException("usuario_not_found"));
        usuarioRepository.delete(usuario);
        return ResponseEntity.ok(success("Usuario eliminado correctamente.").total(usuarioRepository.count()).build());
    }

    @GetMapping(value = "/rol")
    public ResponseEntity<AppResponse<Usuario>> listarRoles() {
        List<Rol> roles = rolRepositorio.findAll();
        return ResponseEntity.ok(success(roles).build());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<AppResponse> tratarExcepciones(EntityNotFoundException e, Locale locale) {
        return ResponseEntity.ok(failure(e.getMessage()).build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<AppResponse> tratarValidacion(MethodArgumentNotValidException ex, Locale locale) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        String mensaje = fieldErrors.parallelStream().map(error -> error.getDefaultMessage()).collect(Collectors.joining(", "));
        return ResponseEntity.ok(failure(mensaje).build());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<AppResponse> tratarValidacion(ConstraintViolationException ex, Locale locale) {
        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
        String mensaje = violations.parallelStream().map(error -> error.getMessage()).collect(Collectors.joining(", "));
        return ResponseEntity.ok(failure(mensaje).build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<AppResponse> tratarExcepcion(Exception e, Locale locale) {
        GeneralException exception = new UsuarioException(e.getCause());
        return ResponseEntity.ok(failure(exception.tratarExcepcion()).build());
    }
}
