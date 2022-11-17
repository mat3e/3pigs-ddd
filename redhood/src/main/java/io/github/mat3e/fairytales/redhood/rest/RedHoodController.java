package io.github.mat3e.fairytales.redhood.rest;

import io.github.mat3e.fairytales.redhood.RedHoodCommandHandler;
import io.github.mat3e.fairytales.redhood.query.RedHoodQuery;
import io.github.mat3e.fairytales.redhood.query.Wolf;
import jakarta.validation.Valid;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;

import static io.github.mat3e.fairytales.redhood.Person.HUNTSMAN;

@Validated
@RestController
@RequestMapping("/wolfs")
@ConditionalOnProperty(value = "spring.main.web-application-type", havingValue = "servlet")
class RedHoodController {
    private final RedHoodCommandHandler commandHandler;
    private final RedHoodQuery query;

    RedHoodController(RedHoodCommandHandler commandHandler, RedHoodQuery query) {
        this.commandHandler = commandHandler;
        this.query = query;
    }

    @PostMapping
    ResponseEntity<Void> createNewWolf() {
        var id = 123;
        return ResponseEntity.created(URI.create("/" + id)).build();
    }

    @GetMapping("/{wolfId}")
    ResponseEntity<Wolf> readWolf(@PathVariable int wolfId) {
        return ResponseEntity.of(Optional.empty());
    }

    @PutMapping("/{wolfId}/meetings/recent")
    ResponseEntity<Void> updateWolfMeeting(@PathVariable int wolfId, @Valid @RequestBody Meeting meeting) {
        // todo: what return type and what to return?
            return ResponseEntity.noContent().build();
    }

    // for convenience
    @DeleteMapping("/{wolfId}")
    ResponseEntity<Void> deleteWolf(@PathVariable int wolfId) {
        return updateWolfMeeting(wolfId, Meeting.with(HUNTSMAN));
    }
}
