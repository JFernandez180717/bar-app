package co.com.bar.bar_app.application.service;

import co.com.bar.bar_app.application.dto.CategoriaApplicationDto;
import co.com.bar.bar_app.application.dto.CategoriaCreadaDto;
import co.com.bar.bar_app.application.mapper.CategoriaApplicationMapper;
import co.com.bar.bar_app.application.ports.in.CategoriaInPort;
import co.com.bar.bar_app.application.ports.out.CategoriaOutPort;
import co.com.bar.bar_app.domain.model.Categoria;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CategoriaService implements CategoriaInPort {
    private final CategoriaOutPort categoriaOutPort;
    private final CategoriaApplicationMapper categoriaApplicationMapper;

    public CategoriaService(CategoriaOutPort categoriaOutPort, CategoriaApplicationMapper categoriaApplicationMapper) {
        this.categoriaOutPort = categoriaOutPort;
        this.categoriaApplicationMapper = categoriaApplicationMapper;
    }

    @Override
    public CategoriaCreadaDto create(String descripcion, String username) {
        Categoria categoria = Categoria.crear(descripcion);
        return categoriaApplicationMapper.domainToDto(this.categoriaOutPort.create(categoria, username));
    }

    @Override
    public List<CategoriaApplicationDto> findAll() {
        return this.categoriaApplicationMapper.domainListToApplicationDtoList(this.categoriaOutPort.findAll());
    }

    @Override
    public void update(UUID id, String descripcion, String username) {
        this.categoriaOutPort.update(id, descripcion, username);
    }

    @Override
    public void changeStatus(UUID id, String username) {
        this.categoriaOutPort.changeStatus(id, username);
    }
}
