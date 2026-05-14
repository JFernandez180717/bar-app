package co.com.bar.bar_app.infrastructure.input.rest;

import co.com.bar.bar_app.application.dto.CrearIngresoMercanciaDto;
import co.com.bar.bar_app.application.dto.IngresoMercanciaApplicationDto;
import co.com.bar.bar_app.application.dto.IngresoMercanciaCreadoDto;
import co.com.bar.bar_app.application.dto.IngresoMercanciaDetalleDto;
import co.com.bar.bar_app.application.mapper.IngresoMercanciaApplicationMapper;
import co.com.bar.bar_app.application.ports.in.IngresoMercanciaInPort;
import co.com.bar.bar_app.infrastructure.input.rest.dto.CrearIngresoMercanciaRequestDto;
import co.com.bar.bar_app.infrastructure.input.rest.dto.IngresoMercanciaDetalleRequestDto;
import co.com.bar.bar_app.infrastructure.input.rest.dto.IngresoMercanciaDetalleResponseDto;
import co.com.bar.bar_app.infrastructure.input.rest.dto.IngresoMercanciaResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/ingresos")
public class IngresoMercanciaController {

    private final IngresoMercanciaInPort ingresoInPort;
    private final IngresoMercanciaApplicationMapper mapper;

    public IngresoMercanciaController(IngresoMercanciaInPort ingresoInPort,
                                       IngresoMercanciaApplicationMapper mapper) {
        this.ingresoInPort = ingresoInPort;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<IngresoMercanciaResponseDto> create(
            Authentication authentication,
            @RequestBody CrearIngresoMercanciaRequestDto requestDto) {

        // Verificar que el usuario tiene rol válido (super, admin, user)
        String username = authentication.getName();
        String rol = getRolFromAuthentication(authentication);

        // Mapear request a DTO de aplicación
        List<IngresoMercanciaDetalleDto> detalles = requestDto.detalles().stream()
                .map(this::toDetalleDto)
                .toList();

        CrearIngresoMercanciaDto dto = new CrearIngresoMercanciaDto(
                requestDto.fecha(),
                requestDto.usuarioRecibe(),
                requestDto.idProveedor(),
                detalles
        );

        IngresoMercanciaCreadoDto creado = ingresoInPort.create(dto, username);

        return ResponseEntity.ok(toResponseDto(creado));
    }

    @GetMapping
    public ResponseEntity<List<IngresoMercanciaResponseDto>> findAll(
            @RequestParam(required = false) LocalDate fechaInicio,
            @RequestParam(required = false) LocalDate fechaFin,
            @RequestParam(required = false) String idProveedor) {

        List<IngresoMercanciaApplicationDto> ingresos = ingresoInPort.findAll(fechaInicio, fechaFin, idProveedor);
        List<IngresoMercanciaResponseDto> response = ingresos.stream()
                .map(this::toResponseDtoFromApplication)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<IngresoMercanciaResponseDto> findById(@PathVariable UUID id) {
        IngresoMercanciaApplicationDto ingreso = ingresoInPort.findById(id);
        return ResponseEntity.ok(toResponseDtoFromApplication(ingreso));
    }

    @PutMapping("/{id}/anular")
    public ResponseEntity<IngresoMercanciaResponseDto> anular(
            Authentication authentication,
            @PathVariable UUID id) {

        String username = authentication.getName();
        String rol = getRolFromAuthentication(authentication);

        IngresoMercanciaApplicationDto actualizado = ingresoInPort.anular(id, username, rol);
        return ResponseEntity.ok(toResponseDtoFromApplication(actualizado));
    }

    private IngresoMercanciaDetalleDto toDetalleDto(IngresoMercanciaDetalleRequestDto dto) {
        return new IngresoMercanciaDetalleDto(
                dto.idProducto(),
                dto.cantidad(),
                dto.precio()
        );
    }

    private IngresoMercanciaResponseDto toResponseDto(IngresoMercanciaCreadoDto dto) {
        return new IngresoMercanciaResponseDto(
                dto.id(),
                dto.fecha(),
                dto.usuarioRecibe(),
                dto.idProveedor() != null ? dto.idProveedor() : "Sin proveedor",
                dto.estado(),
                List.of()
        );
    }

    private IngresoMercanciaResponseDto toResponseDtoFromApplication(IngresoMercanciaApplicationDto dto) {
        List<IngresoMercanciaDetalleResponseDto> detalles = dto.detalles().stream()
                .map(d -> new IngresoMercanciaDetalleResponseDto(
                        d.idProducto(),
                        d.nombreProducto(),
                        d.cantidad(),
                        d.precio()
                ))
                .toList();

        return new IngresoMercanciaResponseDto(
                dto.id(),
                dto.fecha(),
                dto.usuarioRecibe(),
                dto.nombreProveedor(),
                dto.estado(),
                detalles
        );
    }

    private String getRolFromAuthentication(Authentication authentication) {
        // Extraer el rol del usuario autenticado
        return authentication.getAuthorities().stream()
                .map(auth -> auth.getAuthority())
                .filter(a -> a.startsWith("ROLE_"))
                .map(a -> a.replace("ROLE_", ""))
                .findFirst()
                .orElse("user");
    }
}