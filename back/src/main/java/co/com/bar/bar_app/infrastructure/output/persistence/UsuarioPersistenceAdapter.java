package co.com.bar.bar_app.infrastructure.output.persistence;

import co.com.bar.bar_app.application.dto.ActualizarUsuarioDto;
import co.com.bar.bar_app.application.dto.UsuarioApplicationDto;
import co.com.bar.bar_app.application.ports.out.UserOutPort;
import co.com.bar.bar_app.domain.model.Password;
import co.com.bar.bar_app.domain.model.Rol;
import co.com.bar.bar_app.domain.model.Usuario;
import co.com.bar.bar_app.infrastructure.mapper.UsuarioMapper;
import co.com.bar.bar_app.infrastructure.output.persistence.entity.TipoIdentificacionEntity;
import co.com.bar.bar_app.infrastructure.output.persistence.entity.UsuarioEntity;
import co.com.bar.bar_app.infrastructure.output.persistence.entity.UsuarioRolEntity;
import co.com.bar.bar_app.infrastructure.output.persistence.id.UsuarioRolId;
import co.com.bar.bar_app.infrastructure.output.persistence.repository.TipoIdentificacionRepository;
import co.com.bar.bar_app.infrastructure.output.persistence.repository.UsuarioRepository;
import org.apache.coyote.BadRequestException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class UsuarioPersistenceAdapter implements UserOutPort {
    private static final String USER_NOT_FOUND_MESSAGE = "Usuario no encontrado";
    private final UsuarioRepository usuarioRepository;
    private final TipoIdentificacionRepository tipoIdentificacionRepository;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioMapper usuarioMapper;

    public UsuarioPersistenceAdapter(UsuarioRepository usuarioRepository, TipoIdentificacionRepository tipoIdentificacionRepository, PasswordEncoder passwordEncoder, UsuarioMapper usuarioMapper) {
        this.usuarioRepository = usuarioRepository;
        this.tipoIdentificacionRepository = tipoIdentificacionRepository;
        this.passwordEncoder = passwordEncoder;
        this.usuarioMapper = usuarioMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        UsuarioEntity usuarioEntity = this.usuarioRepository.findById(username).orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND_MESSAGE));
        List<UsuarioRolEntity> roles = usuarioEntity.getRoles();

        return new SecurityUser(
                usuarioEntity.getUsername(),
                usuarioEntity.getPass(),
                usuarioEntity.isEstado(),
                usuarioEntity.isEstado(),
                this.grantedAuthorities(roles)
        );
    }

    @Override
    public String[] getAuthorities(String role) {
        return new String[0];
    }

    @Override
    public List<GrantedAuthority> grantedAuthorities(List<UsuarioRolEntity> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>(roles.size());

        for (UsuarioRolEntity role: roles) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getId().getRol()));

            for (String authority: this.getAuthorities(role.getId().getRol())) {
                authorities.add(new SimpleGrantedAuthority(authority));
            }
        }

        return authorities;
    }

    @Override
    public void create(Usuario usuario, String username) throws BadRequestException {
        List<UsuarioRolEntity> roles = new ArrayList<>();
        Password password = Password.fromPlain(usuario.getPass());
        UsuarioEntity entity = new UsuarioEntity();
        entity.setUsername(usuario.getUsername());
        entity.setEmail(usuario.getEmail());
        entity.setPass(passwordEncoder.encode(password.value()));
        entity.setPrimerNombre(usuario.getPrimerNombre());
        entity.setSegundoNombre(usuario.getSegundoNombre());
        entity.setPrimerApellido(usuario.getPrimerApellido());
        entity.setSegundoApellido(usuario.getSegundoApellido());
        TipoIdentificacionEntity tipoIdentificacionEntity = tipoIdentificacionRepository.findById(usuario.getTipoIdentificacion()).orElseThrow(() -> new BadRequestException("Tipo de identificacion no existe"));
        entity.setTipoIdentificacion(tipoIdentificacionEntity);
        entity.setIdentificacion(usuario.getIdentificacion());
        entity.setDireccion(usuario.getDireccion());
        entity.setTelefono(usuario.getTelefono());
        entity.setFechaCreacion(LocalDateTime.now());
        entity.setUsuarioCrea(username);
        entity.setEstado(true);
        for (Rol rol: usuario.getRoles()) {
            UsuarioRolEntity rolEntity = new UsuarioRolEntity();
            UsuarioRolId id  = new UsuarioRolId();
            id.setUsername(usuario.getUsername());
            id.setRol(rol.getRol());
            rolEntity.setId(id);
            rolEntity.setUsuario(entity);
            roles.add(rolEntity);
        }
        entity.setRoles(roles);
        this.usuarioRepository.save(entity);
    }

    @Override
    public List<Usuario> findAll() {
        return this.usuarioRepository.findAll().stream().map(usuarioMapper::entityToDomain).toList();
    }

    @Override
    public UsuarioApplicationDto findByUsername(String username) throws Exception {
        return usuarioMapper.entityToDto(this.usuarioRepository.findById(username).orElse(null));
    }

    @Override
    public void changeStatus(String username, String usuarioLogueado) throws Exception {
        UsuarioEntity usuarioEntity = this.usuarioRepository.findById(username).orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND_MESSAGE));
        usuarioEntity.setEstado(!usuarioEntity.isEstado());
        usuarioEntity.setFechaActualizacion(LocalDateTime.now());
        usuarioEntity.setUsuarioModifica(usuarioLogueado);
        this.usuarioRepository.save(usuarioEntity);
    }

    @Override
    public void update(String username, ActualizarUsuarioDto actualizarUsuarioDto, String usuarioLogueado) throws Exception {
        UsuarioEntity entity = this.usuarioRepository.findById(username).orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND_MESSAGE));
        TipoIdentificacionEntity tipoIdentificacionEntity = tipoIdentificacionRepository.findById(actualizarUsuarioDto.tipoIdentificacion()).orElseThrow(() -> new BadRequestException("Tipo de identificacion no existe"));
        entity.setEmail(actualizarUsuarioDto.email());
        entity.setPrimerNombre(actualizarUsuarioDto.primerNombre());
        entity.setSegundoNombre(actualizarUsuarioDto.segundoNombre());
        entity.setPrimerApellido(actualizarUsuarioDto.primerApellido());
        entity.setSegundoApellido(actualizarUsuarioDto.segundoApellido());
        entity.setDireccion(actualizarUsuarioDto.direccion());
        entity.setTelefono(actualizarUsuarioDto.telefono());
        entity.setTipoIdentificacion(tipoIdentificacionEntity);
        entity.setIdentificacion(actualizarUsuarioDto.identificacion());
        entity.setFechaActualizacion(LocalDateTime.now());
        entity.setUsuarioModifica(usuarioLogueado);
        this.usuarioRepository.save(entity);
    }
}
