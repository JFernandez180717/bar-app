package co.com.bar.bar_app.infrastructure.input.rest;

import org.springframework.web.bind.annotation.*;
import co.com.bar.bar_app.application.dto.CategoriaApplicationDto;
import co.com.bar.bar_app.application.dto.CategoriaCreadaDto;
import co.com.bar.bar_app.application.ports.in.CategoriaInPort;
import co.com.bar.bar_app.infrastructure.input.rest.dto.ActualizarCategoriaRequestDto;
import co.com.bar.bar_app.infrastructure.input.rest.dto.CrearCategoriaRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {
    private final CategoriaInPort categoriaInPort;

    public CategoriaController(CategoriaInPort categoriaInPort) {
        this.categoriaInPort = categoriaInPort;
    }

    @PostMapping
    public ResponseEntity<CategoriaCreadaDto> create(@AuthenticationPrincipal User user, @RequestBody CrearCategoriaRequestDto crearCategoriaRequestDto) {
        return ResponseEntity.ok(this.categoriaInPort.create(crearCategoriaRequestDto.descripcion(), user.getUsername()));
    }

    @GetMapping
    public ResponseEntity<List<CategoriaApplicationDto>> findAll() {
        return ResponseEntity.ok(this.categoriaInPort.findAll());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> update(@AuthenticationPrincipal User user, @PathVariable UUID id, @RequestBody ActualizarCategoriaRequestDto requestDto) {
        this.categoriaInPort.update(id, requestDto.descripcion(), user.getUsername());
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/estado/{id}")
    public ResponseEntity<Void> changeStatus(@AuthenticationPrincipal User user, @PathVariable UUID id) {
        this.categoriaInPort.changeStatus(id, user.getUsername());
        return ResponseEntity.ok().build();
    }
}
