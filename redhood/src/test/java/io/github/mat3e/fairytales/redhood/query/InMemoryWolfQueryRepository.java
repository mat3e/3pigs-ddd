package io.github.mat3e.fairytales.redhood.query;

import io.github.mat3e.fairytales.redhood.InMemoryWolfRepository;
import io.github.mat3e.fairytales.redhood.Person;

import java.util.List;
import java.util.Optional;

public class InMemoryWolfQueryRepository implements WolfQueryRepository {
    private final InMemoryWolfRepository domainRepo;

    public InMemoryWolfQueryRepository(InMemoryWolfRepository domainRepo) {
        this.domainRepo = domainRepo;
    }

    @Override
    public Optional<Wolf> findById(int wolfId) {
        return domainRepo.findEatenPeople(wolfId).map(WolfImpl::new);
    }

    private record WolfImpl(List<Person> getEatenPeople) implements Wolf {
        private WolfImpl {
            getEatenPeople = List.copyOf(getEatenPeople);
        }
    }
}
