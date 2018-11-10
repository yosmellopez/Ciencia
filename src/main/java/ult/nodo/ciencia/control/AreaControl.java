package ult.nodo.ciencia.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ult.nodo.ciencia.clases.Area;
import ult.nodo.ciencia.excepcion.AreaException;
import ult.nodo.ciencia.excepcion.GeneralException;
import ult.nodo.ciencia.repositorio.AreaRepositorio;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static ult.nodo.ciencia.control.AppResponse.success;
import static ult.nodo.ciencia.control.AppResponse.failure;

@RestController
@RequestMapping(value = "/api")
public class AreaControl {

    @Autowired
    private AreaRepositorio areaRepository;

    @GetMapping(value = "/area")
    public ResponseEntity<AppResponse<Area>> listarAreas(Pageable pageable) {
        Page<Area> areas = areaRepository.findAll(pageable);
        return ResponseEntity.ok(success(areas.getContent()).total(areas.getTotalElements()).build());
    }

    @GetMapping(value = "/area/all")
    public ResponseEntity<AppResponse<Area>> listarAllAreas() {
        List<Area> areas = areaRepository.findAll();
        return ResponseEntity.ok(success(areas).total(areas.size()).build());
    }

    @PostMapping(value = "/area")
    public ResponseEntity<AppResponse<Area>> insertarArea(@Valid @RequestBody Area area) {
        areaRepository.saveAndFlush(area);
        return ResponseEntity.ok(success(area).build());
    }

    @PutMapping(value = "/area/{idArea}")
    public ResponseEntity<AppResponse<Area>> actualizarArea(@PathVariable("idArea") Optional<Area> optional, @RequestBody Area area) {
        Area areaBd = optional.orElseThrow(() -> new EntityNotFoundException("area_not_found"));
        areaBd.clonar(area);
        areaRepository.saveAndFlush(areaBd);
        return ResponseEntity.ok(success(areaBd).build());
    }

    @DeleteMapping(value = "/area/{idArea}")
    public ResponseEntity<AppResponse> eliminarArea(@PathVariable("idArea") Optional<Area> optional, Locale locale) {
        Area area = optional.orElseThrow(() -> new EntityNotFoundException("area_not_found"));
        areaRepository.delete(area);
        return ResponseEntity.ok(success("Area eliminada correctamente.").total(areaRepository.count()).build());
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
        GeneralException exception = new AreaException(e.getCause());
        return ResponseEntity.ok(failure(exception.tratarExcepcion()).build());
    }
}
