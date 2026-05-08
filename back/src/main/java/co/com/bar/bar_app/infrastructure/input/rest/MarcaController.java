package co.com.bar.bar_app.infrastructure.input.rest;

import co.com.bar.bar_app.application.ports.in.MarcaInPort;
import co.com.bar.bar_app.infrastructure.input.rest.dto.ActualizarMarcaRequestDto;
import co.com.bar.bar_app.infrastructure.input.rest.dto.CrearMarcaRequestDto;
import co.com.bar.bar_app.infrastructure.input.rest.dto.MarcaCreadaResponseDto;
import co.com.bar.bar_app.infrastructure.input.rest.dto.MarcaResponseDto;
import co.com.bar.bar_app.infrastructure.mapper.MarcaMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/marcas")
public class MarcaController {
    private final MarcaInPort marcaInPort;
    private final MarcaMapper marcaMapper;

    public MarcaController(MarcaInPort marcaInPort, MarcaMapper marcaMapper) {
        this.marcaInPort = marcaInPort;
        this.marcaMapper = marcaMapper;
    }

    @PostMapping
    public ResponseEntity<MarcaCreadaResponseDto> create(@AuthenticationPrincipal User user, @RequestBody CrearMarcaRequestDto requestDto) {
        MarcaCreadaResponseDto responseDto = marcaMapper.createdDtoToCreatedResponseDto(this.marcaInPort.create(requestDto.descripcion(), user.getUsername()));
        return ResponseEntity.ok().body(responseDto);
    }

    @GetMapping
    public ResponseEntity<List<MarcaResponseDto>> findAll() {
        return ResponseEntity.ok().body(marcaMapper.applicationDtoListToResponseDtoList(this.marcaInPort.findAll()));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> update(@AuthenticationPrincipal User user, @PathVariable UUID id, @RequestBody ActualizarMarcaRequestDto requestDto) {
        this.marcaInPort.update(id, marcaMapper.requestToActualizarDto(requestDto), user.getUsername());
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/estado/{id}")
    public ResponseEntity<Void> changeStatus(@AuthenticationPrincipal User user, @PathVariable UUID id) {
        this.marcaInPort.changeStatus(id, user.getUsername());
        return ResponseEntity.ok().build();
    }
}
