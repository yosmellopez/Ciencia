package ult.nodo.ciencia.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ult.nodo.ciencia.clases.Articulo;
import ult.nodo.ciencia.clases.Grupo;
import ult.nodo.ciencia.excepcion.ArticuloException;
import ult.nodo.ciencia.excepcion.GeneralException;
import ult.nodo.ciencia.repositorio.ArticuloRepositorio;
import ult.nodo.ciencia.repositorio.GrupoRepositorio;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.http.ResponseEntity.ok;
import static ult.nodo.ciencia.control.AppResponse.failure;
import static ult.nodo.ciencia.control.AppResponse.success;

@RestController
@RequestMapping(value = "/api")
public class ArticuloControl {

    private final ArticuloRepositorio articuloRepository;

    private final GrupoRepositorio grupoRepositorio;

    @Autowired
    public ArticuloControl(ArticuloRepositorio articuloRepository, GrupoRepositorio grupoRepositorio) {
        this.articuloRepository = articuloRepository;
        this.grupoRepositorio = grupoRepositorio;
    }

    @GetMapping(value = "/articulo")
    public ResponseEntity<AppResponse<Articulo>> listarArticulos(Pageable pageable) {
        Page<Articulo> articulos = articuloRepository.findAll(pageable);
        return ok(success(articulos.getContent()).total(articulos.getTotalElements()).build());
    }

    @PostMapping(value = "/articulo")
    public ResponseEntity<AppResponse<Articulo>> insertarArticulo(@Valid @RequestBody Articulo articulo) {
        articuloRepository.saveAndFlush(articulo);
        return ok(success(articulo).build());
    }

    @PutMapping(value = "/articulo/{idArticulo}")
    public ResponseEntity<AppResponse<Articulo>> actualizarArticulo(@PathVariable("idArticulo") Optional<Articulo> optional, @RequestBody Articulo articulo) {
        Articulo articuloBd = optional.orElseThrow(() -> new EntityNotFoundException("articulo_not_found"));
        articuloBd.clonar(articulo);
        articuloRepository.saveAndFlush(articuloBd);
        return ok(success(articuloBd).build());
    }

    @DeleteMapping(value = "/articulo/{idArticulo}")
    public ResponseEntity<AppResponse> eliminarArticulo(@PathVariable("idArticulo") Optional<Articulo> optional, Locale locale) {
        Articulo articulo = optional.orElseThrow(() -> new EntityNotFoundException("articulo_not_found"));
        articuloRepository.delete(articulo);
        return ok(success("Articulo eliminado correctamente.").total(articuloRepository.count()).build());
    }

    @GetMapping(value = "/grupo")
    public ResponseEntity<AppResponse<Articulo>> listarGrupos() {
        List<Grupo> articulos = grupoRepositorio.findAll();
        return ok(success(articulos).build());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<AppResponse> tratarExcepciones(EntityNotFoundException e, Locale locale) {
        return ok(failure(e.getMessage()).build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<AppResponse> tratarValidacion(MethodArgumentNotValidException ex, Locale locale) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        String mensaje = fieldErrors.parallelStream().map(error -> error.getDefaultMessage()).collect(Collectors.joining(", "));
        return ok(failure(mensaje).build());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<AppResponse> tratarValidacion(ConstraintViolationException ex, Locale locale) {
        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
        String mensaje = violations.parallelStream().map(error -> error.getMessage()).collect(Collectors.joining(", "));
        return ok(failure(mensaje).build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<AppResponse> tratarExcepcion(Exception e, Locale locale) {
        GeneralException exception = new ArticuloException(e.getCause());
        return ok(failure(exception.tratarExcepcion()).build());
    }
}
