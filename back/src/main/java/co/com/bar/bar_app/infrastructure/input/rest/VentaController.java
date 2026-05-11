package co.com.bar.bar_app.infrastructure.input.rest;

import co.com.bar.bar_app.application.dto.VentaApplicationDto;
import co.com.bar.bar_app.application.dto.VentaCreadaDto;
import co.com.bar.bar_app.application.ports.in.VentaInPort;
import co.com.bar.bar_app.domain.model.PageResponse;
import co.com.bar.bar_app.domain.model.PaginationRequest;
import co.com.bar.bar_app.infrastructure.input.rest.dto.CrearVentaRequestDto;
import co.com.bar.bar_app.infrastructure.mapper.VentaMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ventas")
public class VentaController {
    private final VentaInPort ventaInPort;
    private final VentaMapper ventaMapper;

    public VentaController(VentaInPort ventaInPort, VentaMapper ventaMapper) {
        this.ventaInPort = ventaInPort;
        this.ventaMapper = ventaMapper;
    }

    @PostMapping
    public ResponseEntity<VentaCreadaDto> crearVenta(
            @AuthenticationPrincipal User user,
            @RequestBody CrearVentaRequestDto requestDto) {
        return ResponseEntity.ok(
                this.ventaInPort.crearVenta(
                        this.ventaMapper.requestToApplicationDto(requestDto),
                        user.getUsername()
                )
        );
    }

    @GetMapping
    public ResponseEntity<PageResponse<VentaApplicationDto>> findAll(Pageable pageable) {
        String sortBy = "codigo";
        String direction = "DESC";
        if (pageable.getSort().isSorted()) {
            Sort.Order order = pageable.getSort().iterator().next();
            sortBy = order.getProperty();
            direction = order.getDirection().name();
        }
        PaginationRequest request = new PaginationRequest(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                sortBy,
                direction
        );
        return ResponseEntity.ok(this.ventaInPort.findAll(request));
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<VentaApplicationDto> findById(@PathVariable int codigo) {
        return ResponseEntity.ok(this.ventaInPort.findById(codigo));
    }
}