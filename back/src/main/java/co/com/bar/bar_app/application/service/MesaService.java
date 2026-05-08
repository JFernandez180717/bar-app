package co.com.bar.bar_app.application.service;

import co.com.bar.bar_app.application.dto.MesaApplicationDto;
import co.com.bar.bar_app.application.mapper.MesaApplicationMapper;
import co.com.bar.bar_app.application.ports.in.MesaInPort;
import co.com.bar.bar_app.application.ports.out.MesaOutPort;
import co.com.bar.bar_app.domain.model.Mesa;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MesaService implements MesaInPort {
    private final MesaOutPort mesaOutPort;
    private final MesaApplicationMapper mesaApplicationMapper;

    public MesaService(MesaOutPort mesaOutPort, MesaApplicationMapper mesaApplicationMapper) {
        this.mesaOutPort = mesaOutPort;
        this.mesaApplicationMapper = mesaApplicationMapper;
    }

    @Override
    public MesaApplicationDto create(String username) {
        Mesa newMesa = this.mesaOutPort.create(username);
        return new MesaApplicationDto(newMesa.getId(), newMesa.isActivo());
    }

    @Override
    public List<MesaApplicationDto> findAll() {
        return this.mesaOutPort.findAll().stream().map(mesaApplicationMapper::domainToApplicationDto).toList();
    }

    @Override
    public void changeStatus(int id, String username) {
        this.mesaOutPort.changeStatus(id, username);
    }
}
