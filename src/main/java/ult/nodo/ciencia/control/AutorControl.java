package ult.nodo.ciencia.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ult.nodo.ciencia.clases.Autor;
import ult.nodo.ciencia.excepcion.AutorException;
import ult.nodo.ciencia.excepcion.GeneralException;
import ult.nodo.ciencia.repositorio.AutorRepositorio;

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
public class AutorControl {

    @Autowired
    private AutorRepositorio autorRepository;

    @Autowired
    private MessageSource messageSource;

    @GetMapping(value = "/autor")
    public ResponseEntity<AppResponse<Autor>> listarAutors(Pageable pageable) {
        Page<Autor> autors = autorRepository.findAll(pageable);
        return ResponseEntity.ok(success(autors.getContent()).total(autors.getTotalElements()).build());
    }

    @GetMapping(value = "/autor/all")
    public ResponseEntity<AppResponse<Autor>> listarAllAutors() {
        List<Autor> autors = autorRepository.findAll();
        return ResponseEntity.ok(success(autors).total(autors.size()).build());
    }

    @PostMapping(value = "/autor")
    public ResponseEntity<AppResponse<Autor>> insertarAutor(@Valid @RequestBody Autor autor) {
        autorRepository.saveAndFlush(autor);
        return ResponseEntity.ok(success(autor).build());
    }

    @PutMapping(value = "/autor/{idAutor}")
    public ResponseEntity<AppResponse<Autor>> actualizarAutor(@PathVariable("idAutor") Optional<Autor> optional, @RequestBody Autor autor) {
        Autor autorBd = optional.orElseThrow(() -> new EntityNotFoundException("autor_not_found"));
        autorBd.clonar(autor);
        autorRepository.saveAndFlush(autorBd);
        return ResponseEntity.ok(success(autorBd).build());
    }

    @DeleteMapping(value = "/autor/{idAutor}")
    public ResponseEntity<AppResponse> eliminarAutor(@PathVariable("idAutor") Optional<Autor> optional, Locale locale) {
        Autor autor = optional.orElseThrow(() -> new EntityNotFoundException("autor_not_found"));
        autorRepository.delete(autor);
        return ResponseEntity.ok(success(messageSource.getMessage("delete_autor", null, locale)).total(autorRepository.count()).build());
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
        GeneralException exception = new AutorException(e.getCause());
        return ResponseEntity.ok(failure(exception.tratarExcepcion()).build());
    }
}
