package io.github.mat3e.in.rest;

import io.github.mat3e.app.command.BuildHouse;
import io.github.mat3e.model.vo.HouseSnapshot;
import org.jetbrains.annotations.NotNull;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.mediatype.Affordances;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.http.HttpMethod;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

class HouseProcessor implements RepresentationModelProcessor<EntityModel<HouseSnapshot>> {
    @NotNull
    @Override
    public EntityModel<HouseSnapshot> process(@NotNull final EntityModel<HouseSnapshot> model) {
        return model.add(
                Affordances.of(
                        linkTo(methodOn(ThreePigsController.class).readHouse(model.getContent().id())).withSelfRel())
                        .afford(HttpMethod.DELETE)
                        .withName("blowHouseDown")
                        .andAfford(HttpMethod.POST)
                        .withInput(BuildHouse.class)
                        .withName("buildOtherHouse")
                        .withTarget(linkTo(methodOn(ThreePigsController.class).createNewHouse(null)).withRel(IanaLinkRelations.COLLECTION))
                        .toLink()
        );
    }
}
