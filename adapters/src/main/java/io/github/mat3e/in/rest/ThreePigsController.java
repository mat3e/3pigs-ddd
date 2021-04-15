package io.github.mat3e.in.rest;

import io.github.mat3e.app.HouseQueryRepository;
import io.github.mat3e.app.HouseReadModel;
import io.github.mat3e.app.ThreePigsCommandHandler;
import io.github.mat3e.app.command.BlowDown;
import io.github.mat3e.app.command.BuildHouse;
import io.github.mat3e.model.vo.HouseId;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("/houses")
@ConditionalOnProperty(value = "spring.main.web-application-type", havingValue = "servlet")
class ThreePigsController {
    private final HouseQueryRepository repository;
    private final ThreePigsCommandHandler handler;

    ThreePigsController(final HouseQueryRepository repository, final ThreePigsCommandHandler handler) {
        this.repository = repository;
        this.handler = handler;
    }

    @PostMapping
    ResponseEntity<Void> createNewHouse(@RequestBody BuildHouse buildCommand) {
        int id = handler.handle(buildCommand).value();
        return ResponseEntity.created(
                linkTo(ThreePigsController.class)
                        .slash(id)
                        .toUri()
        ).build();
    }

    @GetMapping("/{value}")
    ResponseEntity<EntityModel<HouseReadModel>> readHouse(@PathVariable("value") HouseId id) {
        return ResponseEntity.of(
                repository.findDirect(id)
                        .map(EntityModel::of)
        );
    }

    @DeleteMapping("/{value}")
    ResponseEntity<Void> blowHouseDown(@PathVariable("value") HouseId id) {
        handler.handle(new BlowDown(id));
        return ResponseEntity.noContent().build();
    }
}
