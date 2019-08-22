package br.com.smartfull.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class ApiRestController {

    private static final String template = "Olá, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/api")
    public Raiz raiz(@RequestParam(value="nome", defaultValue="Mundo") String name) {
        return new Raiz(counter.incrementAndGet(),
                String.format(template, name));
    }

}
