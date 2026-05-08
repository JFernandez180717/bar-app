package co.com.bar.bar_app.infrastructure.mapper;

import co.com.bar.bar_app.application.dto.ActualizarUsuarioDto;
import co.com.bar.bar_app.application.dto.UsuarioApplicationDto;
import co.com.bar.bar_app.domain.model.Rol;
import co.com.bar.bar_app.domain.model.Usuario;
import co.com.bar.bar_app.infrastructure.input.rest.dto.ActualizarUsuarioRequestDto;
import co.com.bar.bar_app.infrastructure.input.rest.dto.CrearUsuarioRequestDto;
import co.com.bar.bar_app.infrastructure.output.persistence.entity.RolEntity;
import co.com.bar.bar_app.infrastructure.output.persistence.entity.TipoIdentificacionEntity;
import co.com.bar.bar_app.infrastructure.output.persistence.entity.UsuarioEntity;
import co.com.bar.bar_app.infrastructure.output.persistence.entity.UsuarioRolEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {
    UsuarioMapper INSTANCE = Mappers.getMapper(UsuarioMapper.class);

    @BeanMapping(builder = @Builder(buildMethod = "build"))
    Usuario requestDtoToDomain(CrearUsuarioRequestDto requestDto);

    // Le dices a MapStruct cómo convertir TipoIdentificacionEntity -> String
    default String tipoIdentificacionToString(TipoIdentificacionEntity tipo) {
        return tipo != null ? tipo.getTipo() : null;
    }

    // Y también para el sentido inverso (dominio -> entidad)
    default TipoIdentificacionEntity stringToTipoIdentificacion(String tipo) {
        if (tipo == null) return null;
        TipoIdentificacionEntity entity = new TipoIdentificacionEntity();
        entity.setTipo(tipo);
        return entity;
    }

    default String rolToString(RolEntity rol) {
        return rol != null ? rol.getRol() : null;
    }

    default Rol stringToRol(String rol) {
        if (rol == null) return null;
        return new Rol(rol, null, true);
    }

    default List<String> rolEntitiesToStringList(List<UsuarioRolEntity> roles) {
        if (roles.isEmpty()) return Collections.emptyList();
        return roles.stream().map(rol -> rol.getId().getRol()).toList();
    }

    default Rol usuarioRolEntityToRolDomain(UsuarioRolEntity rol) {
        if (rol == null) return null;
        return new Rol(rol.getId().getRol(), rol.getId().getRol(), true);
    }

    UsuarioEntity domainToEntity(Usuario usuario);

    Usuario entityToDomain(UsuarioEntity entity);

    @Mapping(source = "estado", target = "activo")
    UsuarioApplicationDto entityToDto(UsuarioEntity entity);

    ActualizarUsuarioDto requestDtoToApplicationDto(ActualizarUsuarioRequestDto applicationDto);
}
