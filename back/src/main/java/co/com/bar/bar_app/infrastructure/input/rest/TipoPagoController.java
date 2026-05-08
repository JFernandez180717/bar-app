package co.com.bar.bar_app.infrastructure.input.rest;

import co.com.bar.bar_app.application.dto.TipoPagoApplicationDto;
import co.com.bar.bar_app.application.ports.in.TipoPagoInPort;
import co.com.bar.bar_app.infrastructure.input.rest.dto.CreateTipoPagoRequestDto;
import co.com.bar.bar_app.infrastructure.input.rest.dto.TipoPagoResponseDto;
import co.com.bar.bar_app.infrastructure.input.rest.dto.UpdateTipoPagoRequestDto;
import co.com.bar.bar_app.infrastructure.mapper.TipoPagoMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tipospago")
public class TipoPagoController {
    private final TipoPagoInPort tipoPagoInPort;
    private final TipoPagoMapper tipoPagoMapper;

    public TipoPagoController(TipoPagoInPort tipoPagoInPort, TipoPagoMapper tipoPagoMapper) {
        this.tipoPagoInPort = tipoPagoInPort;
        this.tipoPagoMapper = tipoPagoMapper;
    }

    @PostMapping
    public ResponseEntity<TipoPagoApplicationDto> create(@AuthenticationPrincipal User user, @RequestBody CreateTipoPagoRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.tipoPagoInPort.create(tipoPagoMapper.requestDtoToApplicationDto(requestDto), user.getUsername()));
    }

    @GetMapping
    public ResponseEntity<List<TipoPagoResponseDto>> findAll() {
        return ResponseEntity.ok().body(tipoPagoMapper.applicationDtoListToResponseDtoList(this.tipoPagoInPort.findAll()));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> update(@AuthenticationPrincipal User user, @PathVariable UUID id, @RequestBody UpdateTipoPagoRequestDto requestDto) {
        this.tipoPagoInPort.update(id, requestDto, user.getUsername());
        return ResponseEntity.ok().build();
    }
}
