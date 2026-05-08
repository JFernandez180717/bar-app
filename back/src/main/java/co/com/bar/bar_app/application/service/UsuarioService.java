package co.com.bar.bar_app.application.service;

import co.com.bar.bar_app.application.dto.ActualizarUsuarioDto;
import co.com.bar.bar_app.application.dto.CrearUsuarioResponseDto;
import co.com.bar.bar_app.application.dto.UsuarioApplicationDto;
import co.com.bar.bar_app.application.mapper.UsuarioApplicationMapper;
import co.com.bar.bar_app.application.ports.in.UserInPort;
import co.com.bar.bar_app.application.ports.out.UserOutPort;
import co.com.bar.bar_app.domain.model.Usuario;
import co.com.bar.bar_app.infrastructure.output.persistence.entity.UsuarioRolEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService implements UserDetailsService, UserInPort {

    private final UserOutPort userOutPort;
    private final UsuarioApplicationMapper usuarioApplicationMapper;

    public UsuarioService(UserOutPort userOutPort, UsuarioApplicationMapper usuarioApplicationMapper) {
        this.userOutPort = userOutPort;
        this.usuarioApplicationMapper = usuarioApplicationMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userOutPort.loadUserByUsername(username);
    }

    @Override
    public String[] getAuthorities(String rol) {
        return this.userOutPort.getAuthorities(rol);
    }

    @Override
    public List<GrantedAuthority> grantedAuthorities(List<UsuarioRolEntity>roles) {
        return this.userOutPort.grantedAuthorities(roles);
    }

    @Override
    public CrearUsuarioResponseDto create(Usuario usuario, String emailUsuario) throws Exception {
        this.userOutPort.create(usuario, emailUsuario);
        return usuarioApplicationMapper.toDto(usuario);
    }

    @Override
    public List<UsuarioApplicationDto> findAll() {
        return this.userOutPort.findAll().stream().map(usuarioApplicationMapper::domainToUsuarioApplicationDto).toList();
    }

    @Override
    public UsuarioApplicationDto findByUsername(String username, String usuarioLogueado, List<String> authorities) throws Exception {
        if (!authorities.contains("ROLE_ADMIN") && !authorities.contains("ROLE_SUPER") && !usuarioLogueado.equals(username)) {
            throw new AccessDeniedException("El usuario no tiene permisos para la acción");
        }
        return this.userOutPort.findByUsername(username);
    }

    @Override
    public void changeStatus(String username, String usuarioLogueado) throws Exception {
        this.userOutPort.changeStatus(username, usuarioLogueado);
    }

    @Override
    public void update(String username, ActualizarUsuarioDto actualizarUsuarioDto, String usuarioLogueado) throws Exception {
        this.userOutPort.update(username, actualizarUsuarioDto, usuarioLogueado);
    }
}
