package co.com.bar.bar_app.application.ports.out;

import co.com.bar.bar_app.domain.model.Mesa;

import java.util.List;

public interface MesaOutPort {
    Mesa create(String username);
    List<Mesa> findAll();
    void changeStatus(int id, String username);
}
