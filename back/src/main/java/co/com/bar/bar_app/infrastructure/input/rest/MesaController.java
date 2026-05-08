package co.com.bar.bar_app.infrastructure.input.rest;

import co.com.bar.bar_app.application.dto.MesaApplicationDto;
import co.com.bar.bar_app.application.ports.in.MesaInPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mesas")
public class MesaController {
    private final MesaInPort mesaInPort;

    public MesaController(MesaInPort mesaInPort) {
        this.mesaInPort = mesaInPort;
    }

    @PostMapping
    public ResponseEntity<MesaApplicationDto> create(@AuthenticationPrincipal User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.mesaInPort.create(user.getUsername()));
    }

    @GetMapping
    public ResponseEntity<List<MesaApplicationDto>> findAll() {
        return ResponseEntity.ok(this.mesaInPort.findAll());
    }

    @PatchMapping("/estado/{id}")
    public ResponseEntity<String> changeStatus(@AuthenticationPrincipal User user, @PathVariable int id) {
        this.mesaInPort.changeStatus(id, user.getUsername());
        return ResponseEntity.ok("Proceso terminado correctamente");
    }
}
