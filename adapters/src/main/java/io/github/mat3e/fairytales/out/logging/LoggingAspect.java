package io.github.mat3e.fairytales.out.logging;

import io.github.mat3e.fairytales.app.HouseQueryRepository;
import io.github.mat3e.fairytales.app.HouseReadModel;
import io.github.mat3e.fairytales.app.command.BlowDown;
import io.github.mat3e.fairytales.app.command.BuildHouse;
import io.github.mat3e.fairytales.model.event.WolfResignedFromAttacking;
import io.github.mat3e.fairytales.model.vo.HouseId;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Aspect
@Component
class LoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);
    private final HouseQueryRepository repository;

    LoggingAspect(final HouseQueryRepository repository) {
        this.repository = repository;
        logger.info("Once upon a time, there were three little pigs");
    }

    @Pointcut(value = "execution(* io.github.mat3e.fairytales.app.ThreePigsCommandHandler.handle(..)) && args(command)", argNames = "command")
    static void buildingHouse(BuildHouse command) {
    }

    @Pointcut(value = "execution(* io.github.mat3e.fairytales.app.ThreePigsCommandHandler.handle(..)) && args(command)", argNames = "command")
    static void wolfBlowing(BlowDown command) {
    }

    @Pointcut("execution(* io.github.mat3e.fairytales.app.HouseQueryRepository.findClosestTo(..))")
    static void findingNearestHouse() {
    }

    @Pointcut(value = "@annotation(org.springframework.context.event.EventListener) && args(event)", argNames = "event")
    static void wolfResigning(WolfResignedFromAttacking event) {
    }

    @Before(value = "buildingHouse(buildHouse)", argNames = "buildHouse")
    void logBeforeHouseBuilding(BuildHouse buildHouse) {
        if (logger.isInfoEnabled()) {
            switch (buildHouse.getOwner()) {
                case VERY_LAZY -> logger.info("The first little pig was very lazy");
                case LAZY -> logger.info("The second little pig was a bit more ambitious");
                case NOT_LAZY -> logger.info("The third little pig was ready for hard work");
            }
        }
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent") // command was handled = there is a house
    @AfterReturning(value = "buildingHouse(buildHouse)", returning = "id", argNames = "buildHouse,id")
    void logAfterHouseBuilding(BuildHouse buildHouse, HouseId id) {
        if (logger.isInfoEnabled()) {
            switch (findHouse(id).get().getMaterial()) {
                case STRAW -> logger.info("He didn't want to work at all and he built his house out of straw");
                case WOOD -> logger.info("He built his house with sticks");
                case BRICKS -> logger.info("He chose to build his house from bricks");
            }
        }
    }

    @Before(value = "wolfBlowing(blowDown)", argNames = "blowDown")
    void logInBlow(BlowDown blowDown) {
        findHouse(blowDown.getId()).map(HouseReadModel::getMaterial).ifPresent(houseMaterial -> {
            if (logger.isInfoEnabled()) {
                switch (houseMaterial) {
                    case STRAW -> logger.info("One night the big bad wolf, who dearly loved to eat fat little piggies, came along and saw the first little pig in his house of straw");
                    case WOOD -> logger.info("The wolf followed – excited that he might get to eat two little pigs");
                    case BRICKS -> logger.info("The wolf followed them and once he reached the house he began to shout");
                }
                switch (houseMaterial) {
                    case STRAW -> logger.info("He knocked on the door. 'Little pig, little pig, let me come in! Or I'll huff and I'll puff and I'll blow your house down!' called the wolf. The little pig felt safe in his house so he shouted back, 'Not by the hair on my chinny chin chin!'");
                    case WOOD, BRICKS -> logger.info("'Little pigs, little pigs let me come in! 'Or I'll huff and I'll puff and I'll blow your house down!'. 'Not by the hair on our chinny chin chins!'");
                }
            }
        });

    }

    @Before("findingNearestHouse()")
    void logBeforeEscaping() {
        if (logger.isInfoEnabled()) {
            logger.info("The wolf huffed and he puffed and he blew the house down");
        }
    }

    @AfterReturning(value = "findingNearestHouse()", returning = "nearestHouse")
    void logAfterEscaping(Optional<HouseId> nearestHouse) {
        nearestHouse
                .filter(__ -> logger.isInfoEnabled())
                .flatMap(this::findHouse)
                .map(HouseReadModel::getMaterial)
                .ifPresent(material -> {
                    switch (material) {
                        case WOOD -> logger.info("The little pig escaped and ran to his brother's house of sticks");
                        case BRICKS -> logger.info("The two little pigs raced to their brother's house of bricks");
                    }
                });
    }

    @After(value = "wolfResigning(resignation)", argNames = "resignation")
    void logAfterResigning(WolfResignedFromAttacking resignation) {
        if (logger.isInfoEnabled()) {
            logger.info("The wolf huffed and he puffed and he huffed and he puffed, but he could not blow the house down!");
            logger.info("The three little pigs never had any trouble from him again, they learnt from their mistakes and they all lived happily ever after");
        }
    }

    private Optional<HouseReadModel> findHouse(HouseId id) {
        return repository.findDirect(id);
    }
}
