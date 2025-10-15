package com.felipeemanuel.mineiracaodados.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.felipeemanuel.mineiracaodados.controller.dto.TurmaDto;
import com.felipeemanuel.mineiracaodados.model.TurmaEntity;
import com.felipeemanuel.mineiracaodados.repository.TurmaRepository;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/turma")
public class TurmaController {

    @Autowired
    private TurmaRepository turmaRepository;
    
    @PostMapping
    public ResponseEntity<TurmaDto> salvarTurma(@RequestBody TurmaDto turma) {
        TurmaEntity entity = toEntity(turma);
        TurmaEntity saved = turmaRepository.save(entity);

        TurmaDto dto = toDto(saved);
        return ResponseEntity.created(URI.create("/turma/" + saved.getId())).body(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TurmaDto> buscarTurmaPorId(@PathVariable Integer id) {
        Optional<TurmaEntity> opt = turmaRepository.findById(id);
        return opt.map(e -> ResponseEntity.ok(toDto(e)))
                  .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<TurmaDto>> listarTodasTurmas() {
        Iterable<TurmaEntity> all = turmaRepository.findAll();
        List<TurmaDto> dtos = new ArrayList<>();
        for (TurmaEntity e : all) {
            dtos.add(toDto(e));
        }
        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TurmaDto> atualizarTurma(@PathVariable Integer id, @RequestBody TurmaDto turma) {
        Optional<TurmaEntity> opt = turmaRepository.findById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        TurmaEntity entity = opt.get();
        entity.setCurso(turma.getCurso());
        entity.setNome(turma.getNome());

        TurmaEntity updated = turmaRepository.save(entity);
        return ResponseEntity.ok(toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarTurma(@PathVariable Integer id) {
        if (!turmaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        turmaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private TurmaEntity toEntity(TurmaDto dto) {
        TurmaEntity e = new TurmaEntity();
        if (dto.getId() != null) {
            e.setId(dto.getId()); // TurmaEntity usa int; Integer será auto-unboxed
        }
        e.setCurso(dto.getCurso());
        e.setNome(dto.getNome());
        return e;
    }

    private TurmaDto toDto(TurmaEntity e) {
        TurmaDto dto = new TurmaDto();
        dto.setId(e.getId());
        dto.setCurso(e.getCurso());
        dto.setNome(e.getNome());
        return dto;
    }
}
