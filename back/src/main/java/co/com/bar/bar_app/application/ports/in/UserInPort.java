package co.com.bar.bar_app.application.ports.in;

import co.com.bar.bar_app.application.dto.ActualizarUsuarioDto;
import co.com.bar.bar_app.application.dto.CrearUsuarioResponseDto;
import co.com.bar.bar_app.application.dto.UsuarioApplicationDto;
import co.com.bar.bar_app.domain.model.Usuario;
import co.com.bar.bar_app.infrastructure.output.persistence.entity.UsuarioRolEntity;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

public interface UserInPort {
    String[] getAuthorities(String role);
    List<GrantedAuthority> grantedAuthorities(List<UsuarioRolEntity> roles);
    CrearUsuarioResponseDto create(Usuario usuario, String username) throws Exception;
    List<UsuarioApplicationDto> findAll();
    UsuarioApplicationDto findByUsername(String username, String usuarioLogueado, List<String> authorities) throws Exception;
    void changeStatus(String username, String usuarioLogueado) throws Exception;
    void update(String username, ActualizarUsuarioDto actualizarUsuarioDto, String usuarioLogueado) throws Exception;
}
