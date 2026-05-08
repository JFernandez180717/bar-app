package co.com.bar.bar_app.application.ports.in;

import co.com.bar.bar_app.application.dto.MesaApplicationDto;

import java.util.List;

public interface MesaInPort {
    MesaApplicationDto create(String username);
    List<MesaApplicationDto> findAll();
    void changeStatus(int id, String username);
}
