package io.github.mat3e.fairytales.pigs3.in.rest;

import io.github.mat3e.fairytales.pigs3.app.HouseReadModel;
import io.github.mat3e.fairytales.pigs3.app.command.BuildHouse;
import io.github.mat3e.fairytales.pigs3.model.vo.HouseId;
import org.jetbrains.annotations.NotNull;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.mediatype.Affordances;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.http.HttpMethod;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

class HouseProcessor implements RepresentationModelProcessor<EntityModel<HouseReadModel>> {
    @NotNull
    @Override
    public EntityModel<HouseReadModel> process(@NotNull final EntityModel<HouseReadModel> model) {
        return model.add(
                Affordances.of(
                        linkTo(methodOn(ThreePigsController.class).readHouse(HouseId.of(model.getContent().getId()))).withSelfRel())
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
