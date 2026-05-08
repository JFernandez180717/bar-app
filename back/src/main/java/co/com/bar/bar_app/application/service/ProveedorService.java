package co.com.bar.bar_app.application.service;

import co.com.bar.bar_app.application.dto.ActualizarProveedorDto;
import co.com.bar.bar_app.application.dto.ProveedorApplicationDto;
import co.com.bar.bar_app.application.dto.ProveedorCreadoDto;
import co.com.bar.bar_app.application.mapper.ProveedorApplicationMapper;
import co.com.bar.bar_app.application.ports.in.ProveedorInPort;
import co.com.bar.bar_app.application.ports.out.ProveedorOutPort;
import co.com.bar.bar_app.domain.model.Proveedor;
import co.com.bar.bar_app.infrastructure.input.rest.dto.CrearProveedorRequestDto;
import co.com.bar.bar_app.infrastructure.output.persistence.repository.TipoIdentificacionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProveedorService implements ProveedorInPort {
    private final ProveedorOutPort proveedorOutPort;
    private final ProveedorApplicationMapper proveedorApplicationMapper;
    private final TipoIdentificacionRepository tipoIdentificacionRepository;

    public ProveedorService(ProveedorOutPort proveedorOutPort,
                            ProveedorApplicationMapper proveedorApplicationMapper,
                            TipoIdentificacionRepository tipoIdentificacionRepository) {
        this.proveedorOutPort = proveedorOutPort;
        this.proveedorApplicationMapper = proveedorApplicationMapper;
        this.tipoIdentificacionRepository = tipoIdentificacionRepository;
    }

    @Override
    public ProveedorCreadoDto create(CrearProveedorRequestDto requestDto, String username) {
        tipoIdentificacionRepository.findById(requestDto.tipoIdentificacion())
                .orElseThrow(() -> new IllegalArgumentException("Tipo de identificacion no encontrado: " + requestDto.tipoIdentificacion()));

        Proveedor proveedor = Proveedor.builder()
                .identificacion(requestDto.identificacion())
                .tipoIdentificacion(requestDto.tipoIdentificacion())
                .nombre(requestDto.nombre())
                .direccion(requestDto.direccion())
                .telefono(requestDto.telefono())
                .estado(true)
                .build();

        Proveedor guardado = this.proveedorOutPort.create(proveedor, username);
        return proveedorApplicationMapper.domainToProveedorCreadoDto(guardado);
    }

    @Override
    public List<ProveedorApplicationDto> findAll() {
        return this.proveedorApplicationMapper.domainListToProveedorApplicationDtoList(
                this.proveedorOutPort.findAll());
    }

    @Override
    public ProveedorApplicationDto update(String identificacion, ActualizarProveedorDto dto, String username) {
        tipoIdentificacionRepository.findById(dto.tipoIdentificacion())
                .orElseThrow(() -> new IllegalArgumentException("Tipo de identificacion no encontrado: " + dto.tipoIdentificacion()));

        Proveedor proveedor = Proveedor.builder()
                .identificacion(identificacion)
                .tipoIdentificacion(dto.tipoIdentificacion())
                .nombre(dto.nombre())
                .direccion(dto.direccion())
                .telefono(dto.telefono())
                .estado(dto.estado() != null ? dto.estado() : true)
                .build();

        Proveedor actualizado = this.proveedorOutPort.update(proveedor, username);
        return proveedorApplicationMapper.domainToProveedorApplicationDto(actualizado);
    }
}
