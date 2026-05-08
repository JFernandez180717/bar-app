package co.com.bar.bar_app.application.service;

import co.com.bar.bar_app.application.dto.MarcaApplicationDto;
import co.com.bar.bar_app.application.dto.MarcaCreadaDto;
import co.com.bar.bar_app.application.dto.ActualizarMarcaDto;
import co.com.bar.bar_app.application.mapper.MarcaApplicationMapper;
import co.com.bar.bar_app.application.ports.in.MarcaInPort;
import co.com.bar.bar_app.application.ports.out.MarcaOutPort;
import co.com.bar.bar_app.domain.model.Marca;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class MarcaService implements MarcaInPort {
    private final MarcaOutPort marcaOutPort;
    private final MarcaApplicationMapper marcaApplicationMapper;

    public MarcaService(MarcaOutPort marcaOutPort, MarcaApplicationMapper marcaApplicationMapper) {
        this.marcaOutPort = marcaOutPort;
        this.marcaApplicationMapper = marcaApplicationMapper;
    }

    @Override
    public MarcaCreadaDto create(String descripcion, String username) {
        Marca marca = Marca.crear(descripcion);
        this.marcaOutPort.create(marca, username);
        return marcaApplicationMapper.domainToMarcaCreadaDto(marca);
    }

    @Override
    public List<MarcaApplicationDto> findAll() {
        return this.marcaApplicationMapper.domainListToMarcaApplicationDtoList(this.marcaOutPort.findAll());
    }

    @Override
    public void update(UUID id, ActualizarMarcaDto dto, String username) {
        if (dto.descripcion() == null || dto.descripcion().isEmpty()) throw new IllegalArgumentException("La descripcion no puede estar vacia.");
        if (dto.descripcion().length() > 100) throw new IllegalArgumentException("La descripcion es demasiado larga.");
        this.marcaOutPort.update(id, dto.descripcion(), dto.estado(), username);
    }

    @Override
    public void changeStatus(UUID id, String username) {
        this.marcaOutPort.changeStatus(id, username);
    }
}
