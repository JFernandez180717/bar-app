package co.com.bar.bar_app.infrastructure.input.rest;

import co.com.bar.bar_app.application.dto.ActualizarProveedorDto;
import co.com.bar.bar_app.application.ports.in.ProveedorInPort;
import co.com.bar.bar_app.infrastructure.input.rest.dto.ActualizarProveedorRequestDto;
import co.com.bar.bar_app.infrastructure.input.rest.dto.CrearProveedorRequestDto;
import co.com.bar.bar_app.infrastructure.input.rest.dto.ProveedorCreadoResponseDto;
import co.com.bar.bar_app.infrastructure.input.rest.dto.ProveedorResponseDto;
import co.com.bar.bar_app.infrastructure.mapper.ProveedorMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/proveedores")
public class ProveedorController {
    private final ProveedorInPort proveedorInPort;
    private final ProveedorMapper proveedorMapper;

    public ProveedorController(ProveedorInPort proveedorInPort, ProveedorMapper proveedorMapper) {
        this.proveedorInPort = proveedorInPort;
        this.proveedorMapper = proveedorMapper;
    }

    @PostMapping
    public ResponseEntity<ProveedorCreadoResponseDto> create(
            @AuthenticationPrincipal User user,
            @RequestBody CrearProveedorRequestDto requestDto) {
        ProveedorCreadoResponseDto responseDto = proveedorMapper.createdDtoToCreatedResponseDto(
                this.proveedorInPort.create(requestDto, user.getUsername()));
        return ResponseEntity.ok().body(responseDto);
    }

    @GetMapping
    public ResponseEntity<List<ProveedorResponseDto>> findAll() {
        return ResponseEntity.ok().body(
                proveedorMapper.applicationDtoListToResponseDtoList(
                        this.proveedorInPort.findAll()));
    }

    @PutMapping("/{identificacion}")
    public ResponseEntity<ProveedorResponseDto> update(
            @AuthenticationPrincipal User user,
            @PathVariable String identificacion,
            @RequestBody ActualizarProveedorRequestDto requestDto) {
        
        ActualizarProveedorDto dto = new ActualizarProveedorDto(
                requestDto.identificacion(),
                requestDto.tipoIdentificacion(),
                requestDto.nombre(),
                requestDto.direccion(),
                requestDto.telefono(),
                requestDto.estado()
        );
        
        return ResponseEntity.ok().body(
                proveedorMapper.applicationDtoToResponseDto(
                        this.proveedorInPort.update(identificacion, dto, user.getUsername())));
    }
}
