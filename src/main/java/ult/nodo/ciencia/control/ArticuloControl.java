package ult.nodo.ciencia.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ult.nodo.ciencia.clases.Articulo;
import ult.nodo.ciencia.excepcion.ArticuloException;
import ult.nodo.ciencia.excepcion.GeneralException;
import ult.nodo.ciencia.repositorio.ArticuloRepositorio;

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
public class ArticuloControl {

    @Autowired
    private ArticuloRepositorio articuloRepository;

    @Autowired
    private MessageSource messageSource;

    @GetMapping(value = "/articulo")
    public ResponseEntity<AppResponse<Articulo>> listarArticulos(Pageable pageable) {
        Page<Articulo> articulos = articuloRepository.findAll(pageable);
        return ResponseEntity.ok(success(articulos.getContent()).total(articulos.getTotalElements()).build());
    }

    @GetMapping(value = "/articulo/all")
    public ResponseEntity<AppResponse<Articulo>> listarAllArticulos() {
        List<Articulo> articulos = articuloRepository.findAll();
        return ResponseEntity.ok(success(articulos).total(articulos.size()).build());
    }

    @PostMapping(value = "/articulo")
    public ResponseEntity<AppResponse<Articulo>> insertarArticulo(@Valid @RequestBody Articulo articulo) {
        articuloRepository.saveAndFlush(articulo);
        return ResponseEntity.ok(success(articulo).build());
    }

    @PutMapping(value = "/articulo/{idArticulo}")
    public ResponseEntity<AppResponse<Articulo>> actualizarArticulo(@PathVariable("idArticulo") Optional<Articulo> optional, @RequestBody Articulo articulo) {
        Articulo articuloBd = optional.orElseThrow(() -> new EntityNotFoundException("articulo_not_found"));
        articuloBd.clonar(articulo);
        articuloRepository.saveAndFlush(articuloBd);
        return ResponseEntity.ok(success(articuloBd).build());
    }

    @DeleteMapping(value = "/articulo/{idArticulo}")
    public ResponseEntity<AppResponse> eliminarArticulo(@PathVariable("idArticulo") Optional<Articulo> optional, Locale locale) {
        Articulo articulo = optional.orElseThrow(() -> new EntityNotFoundException("articulo_not_found"));
        articuloRepository.delete(articulo);
        return ResponseEntity.ok(success(messageSource.getMessage("delete_articulo", null, locale)).total(articuloRepository.count()).build());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<AppResponse> tratarExcepciones(EntityNotFoundException e, Locale locale) {
        return ResponseEntity.ok(failure(messageSource.getMessage(e.getMessage(), null, locale)).build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<AppResponse> tratarValidacion(MethodArgumentNotValidException ex, Locale locale) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        String mensaje = fieldErrors.parallelStream().map(error -> messageSource.getMessage(error.getDefaultMessage(), null, locale)).collect(Collectors.joining(", "));
        return ResponseEntity.ok(failure(mensaje).build());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<AppResponse> tratarValidacion(ConstraintViolationException ex, Locale locale) {
        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
        String mensaje = violations.parallelStream().map(error -> messageSource.getMessage(error.getMessage(), null, locale)).collect(Collectors.joining(", "));
        return ResponseEntity.ok(failure(mensaje).build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<AppResponse> tratarExcepcion(Exception e, Locale locale) {
        GeneralException exception = new ArticuloException(e.getCause());
        return ResponseEntity.ok(failure(exception.tratarExcepcion()).build());
    }
}
