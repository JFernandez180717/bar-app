package co.com.bar.bar_app.application.mapper;

import co.com.bar.bar_app.application.dto.CrearUsuarioResponseDto;
import co.com.bar.bar_app.application.dto.UsuarioApplicationDto;
import co.com.bar.bar_app.domain.model.Rol;
import co.com.bar.bar_app.domain.model.Usuario;
import org.mapstruct.Mapper;

import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring")
public interface UsuarioApplicationMapper {

    CrearUsuarioResponseDto toDto(Usuario usuario);

    UsuarioApplicationDto domainToUsuarioApplicationDto(Usuario usuario);

    default String rolDomainToString(Rol rol) {
        return rol != null ? rol.getRol() : null;
    }

    default Rol stringToRol(String rol) {
        if (rol == null) return null;
        return new Rol(rol, null, true);
    }

    default List<String> rolDomainListToStringList(List<Rol> roles) {
        if (roles.isEmpty()) return Collections.emptyList();
        return roles.stream().map(Rol::getRol).toList();
    }
}
