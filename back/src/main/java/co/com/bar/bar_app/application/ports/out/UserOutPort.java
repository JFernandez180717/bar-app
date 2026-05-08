package co.com.bar.bar_app.application.ports.out;

import co.com.bar.bar_app.application.dto.ActualizarUsuarioDto;
import co.com.bar.bar_app.application.dto.UsuarioApplicationDto;
import co.com.bar.bar_app.domain.model.Usuario;
import co.com.bar.bar_app.infrastructure.output.persistence.entity.UsuarioRolEntity;
import org.apache.coyote.BadRequestException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface UserOutPort {
    UserDetails loadUserByUsername(String username);
    String[] getAuthorities(String role);
    List<GrantedAuthority> grantedAuthorities(List<UsuarioRolEntity> roles);
    void create(Usuario usuario, String username)  throws BadRequestException;
    List<Usuario> findAll();
    UsuarioApplicationDto findByUsername(String username) throws Exception;
    void changeStatus(String username, String usuarioLogueado) throws Exception;
    void update(String username, ActualizarUsuarioDto actualizarUsuarioDto, String usuarioLogueado) throws Exception;
}
