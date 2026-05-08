package co.com.bar.bar_app.application.ports.in;

import co.com.bar.bar_app.application.dto.CrearProductoDto;
import co.com.bar.bar_app.application.dto.ProductoApplicationDto;
import co.com.bar.bar_app.application.dto.ProductoCreadoDto;
import co.com.bar.bar_app.application.dto.UpdateProductoDto;
import co.com.bar.bar_app.domain.model.PageResponse;
import co.com.bar.bar_app.domain.model.PaginationRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface ProductoInPort {
    ProductoCreadoDto create(CrearProductoDto crearProductoDto, MultipartFile archivoImagen, String username) throws IOException;
    PageResponse<ProductoApplicationDto> findAll(PaginationRequest pagination);
    void update(UUID id, UpdateProductoDto dto, String username);
    ProductoApplicationDto findById(UUID id);
    void changeStatus(UUID id, String username);
    void updateImage(MultipartFile imageFile, UUID id, String username) throws IOException;
    List<ProductoApplicationDto> findDestacadosActivos();
}
