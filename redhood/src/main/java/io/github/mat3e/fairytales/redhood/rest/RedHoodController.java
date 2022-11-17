package io.github.mat3e.fairytales.redhood.rest;

import io.github.mat3e.fairytales.redhood.RedHoodService;
import io.github.mat3e.fairytales.redhood.query.RedHoodQuery;
import io.github.mat3e.fairytales.redhood.query.Wolf;
import jakarta.validation.Valid;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static io.github.mat3e.fairytales.redhood.Person.HUNTSMAN;

@Validated
@RestController
@RequestMapping("/wolfs")
@ConditionalOnProperty(value = "spring.main.web-application-type", havingValue = "servlet")
class RedHoodController {
    private final RedHoodService commandHandler;
    private final RedHoodQuery query;

    RedHoodController(RedHoodService commandHandler, RedHoodQuery query) {
        this.commandHandler = commandHandler;
        this.query = query;
    }

    @PostMapping
    ResponseEntity<Void> createNewWolf(@Valid @RequestBody MeetingsOrder meetings) {
        var id = commandHandler.startInteracting(meetings.expectedPeopleToMeet());
        return ResponseEntity.created(URI.create("/" + id)).build();
    }

    @GetMapping("/{wolfId}")
    ResponseEntity<Wolf> readWolf(@PathVariable int wolfId) {
        return ResponseEntity.of(query.findById(wolfId));
    }

    @PutMapping("/{wolfId}/meetings/recent")
    ResponseEntity<Wolf> updateWolfMeeting(@PathVariable int wolfId, @Valid @RequestBody Meeting meeting) {
        commandHandler.meetWolf(meeting.participant(), wolfId);
        return readWolf(wolfId);
    }

    // for convenience
    @DeleteMapping("/{wolfId}")
    ResponseEntity<Void> deleteWolf(@PathVariable int wolfId) {
        updateWolfMeeting(wolfId, Meeting.with(HUNTSMAN));
        return ResponseEntity.noContent().build();
    }
}
