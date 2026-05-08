package co.com.bar.bar_app.infrastructure.input.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CheckController {
    @GetMapping("check")
    public String checkApi() {
        return "Server is running";
    }
}
