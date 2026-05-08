package co.com.bar.bar_app.infrastructure.input.rest;

import org.springframework.web.bind.annotation.*;
import co.com.bar.bar_app.application.dto.ActualizarUsuarioDto;
import co.com.bar.bar_app.application.dto.CrearUsuarioResponseDto;
import co.com.bar.bar_app.application.dto.UsuarioApplicationDto;
import co.com.bar.bar_app.application.ports.in.UserInPort;
import co.com.bar.bar_app.domain.model.Usuario;
import co.com.bar.bar_app.infrastructure.input.rest.dto.ActualizarUsuarioRequestDto;
import co.com.bar.bar_app.infrastructure.input.rest.dto.CrearUsuarioRequestDto;
import co.com.bar.bar_app.infrastructure.mapper.UsuarioMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    private final UserInPort userInPort;
    private final UsuarioMapper usuarioMapper;

    public UsuarioController(UserInPort userInPort, UsuarioMapper usuarioMapper) {
        this.userInPort = userInPort;
        this.usuarioMapper = usuarioMapper;
    }

    @GetMapping()
    public ResponseEntity<List<UsuarioApplicationDto>> findAll() {
        return ResponseEntity.ok(this.userInPort.findAll());
    }

    @GetMapping("/{username}")
    public ResponseEntity<UsuarioApplicationDto> findByUsername(@AuthenticationPrincipal User user, @PathVariable String username) throws Exception {
        return ResponseEntity.ok(this.userInPort.findByUsername(username, user.getUsername(), user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList()));
    }

    @PostMapping()
    public ResponseEntity<CrearUsuarioResponseDto> create(@AuthenticationPrincipal User user, @RequestBody CrearUsuarioRequestDto requestDto) throws Exception {
        Usuario usuario = usuarioMapper.requestDtoToDomain(requestDto);
        return ResponseEntity.ok(this.userInPort.create(usuario, user.getUsername()));
    }

    @PatchMapping("/estado/{username}")
    public ResponseEntity<Void> changeStatus(@AuthenticationPrincipal User user, @PathVariable String username) throws Exception {
        this.userInPort.changeStatus(username, user.getUsername());
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{username}")
    public ResponseEntity<Void> update(@AuthenticationPrincipal User user, @PathVariable String username, @RequestBody ActualizarUsuarioRequestDto requestDto) throws Exception {
        ActualizarUsuarioDto applicationDto = usuarioMapper.requestDtoToApplicationDto(requestDto);
        this.userInPort.update(username, applicationDto, user.getUsername());
        return ResponseEntity.ok().build();
    }
}
