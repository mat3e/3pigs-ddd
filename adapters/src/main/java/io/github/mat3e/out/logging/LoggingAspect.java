package io.github.mat3e.out.logging;

import io.github.mat3e.app.command.BuildHouse;
import io.github.mat3e.model.House;
import io.github.mat3e.model.HouseRepository;
import io.github.mat3e.model.vo.HouseId;
import io.github.mat3e.model.vo.HouseSnapshot;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Aspect
@Component
class LoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);
    private final HouseRepository repository;

    LoggingAspect(final HouseRepository repository) {
        this.repository = repository;
        logger.info("Once upon a time, there were three little pigs");
    }

    @Pointcut("execution(* io.github.mat3e.app.ThreePigsCommandHandler.handle(..))")
    static void handling() {
    }

    @Pointcut("execution(void io.github.mat3e.model.BigBadWolfService.blowDown(..))")
    static void wolfBlowing() {
    }

    @Pointcut("execution(* io.github.mat3e.app.HouseQueryRepository.findClosestTo(..))")
    static void findingNearestHouse() {
    }

    @Before("handling()")
    void logBeforeHandling(JoinPoint jp) {
        if (logger.isInfoEnabled()) {
            var command = jp.getArgs()[0];
            if (command instanceof BuildHouse buildCommand) {
                switch (buildCommand.getOwner()) {
                    case VERY_LAZY -> logger.info("The first little pig was very lazy");
                    case LAZY -> logger.info("The second little pig was a bit more ambitious");
                    case NOT_LAZY -> logger.info("The third little pig was ready for hard work");
                }
            }
        }
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent") // command was handled = there is a house
    @AfterReturning(value = "handling()", returning = "id")
    void logAfterHandling(JoinPoint jp, HouseId id) {
        if (logger.isInfoEnabled()) {
            var command = jp.getArgs()[0];
            if (command instanceof BuildHouse) {
                switch (findHouse(id).get().material()) {
                    case STRAW -> logger.info("He didn't want to work at all and he built his house out of straw");
                    case WOOD -> logger.info("He built his house with sticks");
                    case BRICKS -> logger.info("He chose to build his house from bricks");
                }
            }
        }
    }

    @Around("wolfBlowing()")
    Object logInBlow(ProceedingJoinPoint jp) {
        var houseMaterial = ((House) jp.getArgs()[0]).getSnapshot().material();
        try {
            if (logger.isInfoEnabled()) {
                switch (houseMaterial) {
                    case STRAW -> logger.info("One night the big bad wolf, who dearly loved to eat fat little piggies, came along and saw the first little pig in his house of straw");
                    case WOOD -> logger.info("The wolf followed – excited that he might get to eat two little pigs");
                    case BRICKS -> logger.info("The wolf followed them and once he reached the house he began to shout");
                }
            }
            var result = jp.proceed();
            if (logger.isInfoEnabled()) {
                switch (houseMaterial) {
                    case STRAW -> logger.info("He knocked on the door. 'Little pig, little pig, let me come in! Or I'll huff and I'll puff and I'll blow your house down!' called the wolf. The little pig felt safe in his house so he shouted back, 'Not by the hair on my chinny chin chin!'");
                    case WOOD, BRICKS -> logger.info("'Little pigs, little pigs let me come in! 'Or I'll huff and I'll puff and I'll blow your house down!'. 'Not by the hair on our chinny chin chins!'");
                }
            }
            return result;
        } catch (Throwable e) {
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            }
            throw new RuntimeException(e);
        } finally {
            if (logger.isInfoEnabled()) {
                switch (houseMaterial) {
                    case STRAW, WOOD -> logger.info("The wolf huffed and he puffed and he blew the house down");
                    case BRICKS -> {
                        logger.info("The wolf huffed and he puffed and he huffed and he puffed but he could not blow the house down!");
                        logger.info("The three little pigs never had any trouble from him again, they learnt from their mistakes and they all lived happily ever after");
                    }
                }
            }
        }
    }

    @AfterReturning(value = "findingNearestHouse()", returning = "nearestHouse")
    void logAfterEscaping(JoinPoint jp, Optional<HouseId> nearestHouse) {
        nearestHouse
                .filter(ignored -> logger.isInfoEnabled())
                .flatMap(this::findHouse)
                .map(HouseSnapshot::material)
                .ifPresent(material -> {
                    switch (material) {
                        case WOOD -> logger.info("The little pig escaped and ran to his brother's house of sticks");
                        case BRICKS -> logger.info("The two little pigs raced to their brother's house of bricks");
                    }
                });
    }

    private Optional<HouseSnapshot> findHouse(HouseId id) {
        return repository.findById(id).map(House::getSnapshot);
    }
}
