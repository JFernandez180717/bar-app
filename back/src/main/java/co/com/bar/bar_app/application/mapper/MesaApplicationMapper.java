package co.com.bar.bar_app.application.mapper;

import co.com.bar.bar_app.application.dto.MesaApplicationDto;
import co.com.bar.bar_app.domain.model.Mesa;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MesaApplicationMapper {
    MesaApplicationDto domainToApplicationDto(Mesa mesa);
}
