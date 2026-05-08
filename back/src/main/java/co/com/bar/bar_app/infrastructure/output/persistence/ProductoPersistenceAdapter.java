package co.com.bar.bar_app.infrastructure.output.persistence;

import co.com.bar.bar_app.application.ports.out.ProductoOutPort;
import co.com.bar.bar_app.domain.model.PageResponse;
import co.com.bar.bar_app.domain.model.PaginationRequest;
import co.com.bar.bar_app.domain.model.Producto;
import co.com.bar.bar_app.infrastructure.mapper.ProductoMapper;
import co.com.bar.bar_app.infrastructure.output.persistence.entity.CategoriaEntity;
import co.com.bar.bar_app.infrastructure.output.persistence.entity.MarcaEntity;
import co.com.bar.bar_app.infrastructure.output.persistence.entity.ProductoEntity;
import co.com.bar.bar_app.infrastructure.output.persistence.repository.CategoriaRepository;
import co.com.bar.bar_app.infrastructure.output.persistence.repository.MarcaRepository;
import co.com.bar.bar_app.infrastructure.output.persistence.repository.ProductoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Component
public class ProductoPersistenceAdapter implements ProductoOutPort {
    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final MarcaRepository marcaRepository;
    private final ProductoMapper productoMapper;

    public ProductoPersistenceAdapter(ProductoRepository productoRepository, CategoriaRepository categoriaRepository, MarcaRepository marcaRepository, ProductoMapper productoMapper) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
        this.marcaRepository = marcaRepository;
        this.productoMapper = productoMapper;
    }

    @Override
    public Producto create(Producto producto, String username) {
        CategoriaEntity categoriaEntity = this.categoriaRepository.findById(producto.getCategoria().getId()).orElse(null);
        MarcaEntity marcaEntity = this.marcaRepository.findById(producto.getMarca().getId()).orElse(null);
        ProductoEntity productoEntity = new ProductoEntity();
        productoEntity.setId(producto.getId());
        productoEntity.setNombre(producto.getNombre());
        productoEntity.setDescripcion(producto.getDescripcion());
        productoEntity.setStock(producto.getStock());
        productoEntity.setStockMinimo(producto.getStockMinimo());
        productoEntity.setPrecio(producto.getPrecio());
        productoEntity.setImagen(producto.getImagen());
        productoEntity.setDestacado(producto.isDestacado());
        productoEntity.setEstado(true);
        productoEntity.setCategoria(categoriaEntity);
        productoEntity.setMarca(marcaEntity);
        productoEntity.setFechaCreacion(LocalDateTime.now());
        productoEntity.setUsuarioCrea(username);
        return productoMapper.entityToDomain(this.productoRepository.save(productoEntity));
    }

    @Override
    public PageResponse<Producto> findAll(PaginationRequest pagination) {
        Sort sort = Sort.unsorted();

        if (pagination.sortBy() != null && !pagination.sortBy().isBlank()) {
            sort = Sort.by(
                    Sort.Direction.fromString(pagination.direction()),
                    pagination.sortBy()
            );
        }
        PageRequest pageRequest = PageRequest.of(
                pagination.page(),
                pagination.size(),
                sort
        );

        Page<ProductoEntity> page = productoRepository.findAll(pageRequest);

        return new PageResponse<>(
                page.getContent().stream().map(productoMapper::entityToDomain).toList(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );

        //return this.productoRepository.findAll().stream().map(productoMapper::entityToDomain).toList();
    }

    @Override
    public void update(UUID id, Producto producto, String username) {
        ProductoEntity productoEntity = this.productoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));;
        CategoriaEntity categoriaEntity = this.categoriaRepository.findById(producto.getCategoria().getId()).orElseThrow(() -> new IllegalArgumentException("Categoria no encontrada"));;
        MarcaEntity marcaEntity = this.marcaRepository.findById(producto.getMarca().getId()).orElseThrow(() -> new IllegalArgumentException("Marca no encontrada"));
        productoEntity.setNombre(producto.getNombre());
        productoEntity.setDescripcion(producto.getDescripcion());
        productoEntity.setStock(producto.getStock());
        productoEntity.setStockMinimo(producto.getStockMinimo());
        productoEntity.setPrecio(producto.getPrecio());
        productoEntity.setCategoria(categoriaEntity);
        productoEntity.setDestacado(producto.isDestacado());
        productoEntity.setMarca(marcaEntity);
        productoEntity.setEstado(producto.isActivo());
        productoEntity.setFechaActualizacion(LocalDateTime.now());
        productoEntity.setUsuarioModifica(username);
        this.productoRepository.save(productoEntity);
    }

    @Override
    public Producto findById(UUID id) {
        return this.productoRepository.findById(id).stream().map(productoMapper::entityToDomain).findFirst().orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
    }

    @Override
    public void changeStatus(UUID id, String username) {
        ProductoEntity entity = this.productoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
        entity.setEstado(!entity.isEstado());
        entity.setFechaActualizacion(LocalDateTime.now());
        entity.setUsuarioModifica(username);
        this.productoRepository.save(entity);
    }

    @Override
    public void updateImage(UUID id, String image, String username) {
        ProductoEntity entity = this.productoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
        entity.setImagen(image);
        entity.setFechaActualizacion(LocalDateTime.now());
        entity.setUsuarioModifica(username);
        this.productoRepository.save(entity);
    }

    @Override
    public String getProductImage(UUID id) {
        ProductoEntity entity = this.productoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
        return entity.getImagen();
    }

    @Override
    public List<Producto> findDestacadosActivos() {
        return this.productoRepository.findByDestacadoTrueAndEstadoTrue().stream()
                .map(productoMapper::entityToDomain)
                .toList();
    }
}
