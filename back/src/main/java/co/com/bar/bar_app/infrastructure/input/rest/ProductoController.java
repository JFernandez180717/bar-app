package co.com.bar.bar_app.infrastructure.input.rest;

import co.com.bar.bar_app.domain.model.PageResponse;
import co.com.bar.bar_app.domain.model.PaginationRequest;
import co.com.bar.bar_app.infrastructure.input.rest.dto.UpdateProductoRequestDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import co.com.bar.bar_app.application.dto.ProductoApplicationDto;
import co.com.bar.bar_app.application.dto.ProductoCreadoDto;
import co.com.bar.bar_app.application.ports.in.ProductoInPort;
import co.com.bar.bar_app.infrastructure.input.rest.dto.CrearProductoRequestDto;
import co.com.bar.bar_app.infrastructure.mapper.ProductoMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {
    private final ProductoInPort productoInPort;
    private final ProductoMapper productoMapper;

    public ProductoController(ProductoInPort productoInPort, ProductoMapper productoMapper) {
        this.productoInPort = productoInPort;
        this.productoMapper = productoMapper;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductoCreadoDto> create(@AuthenticationPrincipal User user, @ModelAttribute CrearProductoRequestDto requestDto) throws IOException {
        return ResponseEntity.ok(this.productoInPort.create(productoMapper.requestCreateDtoToApplicationCreateDto(requestDto), requestDto.archivoImagen(), user.getUsername()));
    }

    @GetMapping
    public ResponseEntity<PageResponse<ProductoApplicationDto>> findAll(Pageable pageable) {
        String sortBy = "nombre";
        String direction = "ASC";
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
        return ResponseEntity.ok(this.productoInPort.findAll(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoApplicationDto> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(this.productoInPort.findById(id));
    }

    @GetMapping("/destacados")
    public ResponseEntity<List<ProductoApplicationDto>> findDestacadosActivos() {
        return ResponseEntity.ok(this.productoInPort.findDestacadosActivos());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> update(@AuthenticationPrincipal User user, @PathVariable("id") UUID id, @RequestBody UpdateProductoRequestDto requestDto) {
        this.productoInPort.update(id, productoMapper.requestUpdateDtoToApplicationUpdateDto(requestDto), user.getUsername());
        return ResponseEntity.ok("Producto Actualizado Correctamente");
    }

    @PatchMapping("/estado/{id}")
    public ResponseEntity<String> changeStatus(@AuthenticationPrincipal User user, @PathVariable("id") UUID id) {
        this.productoInPort.changeStatus(id, user.getUsername());
        return ResponseEntity.ok("Estado del Producto Actualizado Correctamente");
    }

    @PatchMapping("/image/{id}")
    public ResponseEntity<String> updateImage(@AuthenticationPrincipal User user, @PathVariable("id") UUID id, @RequestParam("file") MultipartFile file) throws IOException {
        this.productoInPort.updateImage(file, id, user.getUsername());
        return ResponseEntity.ok("Imagen del Producto Actualizada Correctamente");
    }
}
