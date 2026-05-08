package co.com.bar.bar_app.application.service;

import co.com.bar.bar_app.application.dto.*;
import co.com.bar.bar_app.application.mapper.CategoriaApplicationMapper;
import co.com.bar.bar_app.application.mapper.MarcaApplicationMapper;
import co.com.bar.bar_app.application.mapper.ProductoApplicationMapper;
import co.com.bar.bar_app.application.ports.in.ProductoInPort;
import co.com.bar.bar_app.application.ports.out.CategoriaOutPort;
import co.com.bar.bar_app.application.ports.out.MarcaOutPort;
import co.com.bar.bar_app.application.ports.out.ProductoOutPort;
import co.com.bar.bar_app.domain.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class ProductoService implements ProductoInPort {
    @Value("${app.upload.dir}")
    private String uploadFolder;

    private final ProductoOutPort productoOutPort;
    private final CategoriaOutPort categoriaOutPort;
    private final MarcaOutPort marcaOutPort;
    private final ProductoApplicationMapper productoApplicationMapper;
    private final MarcaApplicationMapper marcaApplicationMapper;
    private final CategoriaApplicationMapper categoriaApplicationMapper;

    public ProductoService(ProductoOutPort productoOutPort, CategoriaOutPort categoriaOutPort, MarcaOutPort marcaOutPort, ProductoApplicationMapper productoApplicationMapper, MarcaApplicationMapper marcaApplicationMapper, CategoriaApplicationMapper categoriaApplicationMapper) {
        this.productoOutPort = productoOutPort;
        this.categoriaOutPort = categoriaOutPort;
        this.marcaOutPort = marcaOutPort;
        this.productoApplicationMapper = productoApplicationMapper;
        this.marcaApplicationMapper = marcaApplicationMapper;
        this.categoriaApplicationMapper = categoriaApplicationMapper;
    }

    @Override
    public ProductoCreadoDto create(CrearProductoDto crearProductoDto, MultipartFile archivoImagen, String username) throws IOException {
        this.validateImage(archivoImagen);

        Path root = Paths.get(uploadFolder);
        if (!Files.exists(root)) {
            Files.createDirectories(root);
        }

        String nombreUnico = UUID.randomUUID() + "_" + archivoImagen.getOriginalFilename();

        Categoria categoria = this.categoriaOutPort.findById(crearProductoDto.idCategoria());
        Marca marca = this.marcaOutPort.findById(crearProductoDto.idMarca());
        Producto producto = Producto.crear(crearProductoDto.nombre(), crearProductoDto.descripcion(), crearProductoDto.stock(), crearProductoDto.stockMinimo(), crearProductoDto.precio(), nombreUnico, categoria, marca, crearProductoDto.destacado());
        this.productoOutPort.create(producto, username);
        this.guardarEnDisco(archivoImagen, nombreUnico, root);
        return productoApplicationMapper.domainToCretedDto(producto);
    }

    @Override
    public PageResponse<ProductoApplicationDto> findAll(PaginationRequest pagination) {
        PageResponse<Producto> productos = this.productoOutPort.findAll(pagination);
        List<ProductoApplicationDto> content = productos.content()
                .stream().map(p -> {
                    CategoriaApplicationDto categoriaDto = this.categoriaApplicationMapper.domainToApplicationDto(p.getCategoria());
                    MarcaApplicationDto marcaDto = this.marcaApplicationMapper.domainToMarcaApplicationDto(p.getMarca());
                    return new ProductoApplicationDto(
                            p.getId(),
                            p.getNombre(),
                            p.getDescripcion(),
                            p.getStock(),
                            p.getStockMinimo(),
                            p.getPrecio(),
                            p.getImagen(),
                            p.isDestacado(),
                            p.isActivo(),
                            categoriaDto,
                            marcaDto
                    );
        }).toList();

        return new PageResponse<>(
                content,
                productos.page(),
                productos.size(),
                productos.totalElements(),
                productos.totalPages()
        );
    }

    @Override
    public void update(UUID id, UpdateProductoDto dto, String username) {
        Categoria categoria = this.categoriaOutPort.findById(dto.idCategoria());
        Marca marca = this.marcaOutPort.findById(dto.idMarca());
        Producto producto = this.productoOutPort.findById(id);
        Producto productoToUpdate = new Producto(
                id,
                dto.nombre(),
                dto.descripcion(),
                dto.stock(),
                dto.stockMinimo(),
                dto.precio(),
                producto.getImagen(),
                categoria,
                marca,
                dto.destacado(),
                dto.estado() != null ? dto.estado() : producto.isActivo()
        );
        this.productoOutPort.update(id, productoToUpdate, username);
    }

    @Override
    public ProductoApplicationDto findById(UUID id) {
        return productoApplicationMapper.domainToApplicationDto(this.productoOutPort.findById(id));
    }

    @Override
    public void changeStatus(UUID id, String username) {
        this.productoOutPort.changeStatus(id, username);
    }

    @Override
    public void updateImage(MultipartFile imageFile, UUID id, String username) throws IOException {
        this.validateImage(imageFile);
        String imageOld = this.productoOutPort.getProductImage(id);
        Path root = Paths.get(uploadFolder);
        if (!Files.exists(root)) {
            Files.createDirectories(root);
        }

        String nombreUnico = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();

        this.productoOutPort.updateImage(id, nombreUnico, username);
        this.guardarEnDisco(imageFile, nombreUnico, root);
        this.deleteFromDisc(imageOld);
    }

    @Override
    public List<ProductoApplicationDto> findDestacadosActivos() {
        return this.productoOutPort.findDestacadosActivos().stream()
                .map(p -> {
                    CategoriaApplicationDto categoriaDto = this.categoriaApplicationMapper.domainToApplicationDto(p.getCategoria());
                    MarcaApplicationDto marcaDto = this.marcaApplicationMapper.domainToMarcaApplicationDto(p.getMarca());
                    return new ProductoApplicationDto(
                            p.getId(),
                            p.getNombre(),
                            p.getDescripcion(),
                            p.getStock(),
                            p.getStockMinimo(),
                            p.getPrecio(),
                            p.getImagen(),
                            p.isDestacado(),
                            p.isActivo(),
                            categoriaDto,
                            marcaDto
                    );
                }).toList();
    }

    public void validateImage(MultipartFile imageFile) {
        List<String> allowedTypes = Arrays.asList("image/jpeg", "image/png", "image/gif");

        if (imageFile == null || imageFile.isEmpty()) {
            throw new IllegalArgumentException("No se puede subir un archivo vacío.");
        }

        String contentType = imageFile.getContentType();
        if (contentType == null || !allowedTypes.contains(contentType)) {
            throw new IllegalArgumentException("Solo se permiten imágenes JPG, PNG o GIF.");
        }

        String nombreOriginal = StringUtils.cleanPath(imageFile.getOriginalFilename());
        if (nombreOriginal.contains("..")) {
            throw new IllegalArgumentException("Nombre de archivo inválido.");
        }
    }

    public void guardarEnDisco(MultipartFile archivoImagen, String nombreUnico, Path root) throws IOException {
        Path destino = root.resolve(nombreUnico);
        Files.copy(archivoImagen.getInputStream(), destino, StandardCopyOption.REPLACE_EXISTING);
    }

    public void deleteFromDisc(String image) throws IOException {
        Path ruta = Paths.get(uploadFolder).resolve(image).normalize();
        Files.deleteIfExists(ruta);
    }
}
