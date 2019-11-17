package net.dolpen.mcmod.lib.capability;

import com.google.common.collect.Sets;
import net.minecraftforge.event.AttachCapabilitiesEvent;

import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class CapabilityHolder<T> {

    private Set<Rule<T>> rules;

    CapabilityHolder() {
        rules = Sets.newHashSet();
    }

    public void register(
            Predicate<AttachCapabilitiesEvent<T>> filter,
            Consumer<AttachCapabilitiesEvent<T>> consumer
    ) {
        rules.add(new Rule<>(filter, consumer));
    }

    void handle(AttachCapabilitiesEvent<T> event) {
        rules.stream()
                .filter(rule -> rule.filter.test(event))
                .forEach(rule -> rule.consumer.accept(event));
    }

    private static class Rule<T> {

        private final Predicate<AttachCapabilitiesEvent<T>> filter;
        private final Consumer<AttachCapabilitiesEvent<T>> consumer;

        Rule(
                Predicate<AttachCapabilitiesEvent<T>> filter,
                Consumer<AttachCapabilitiesEvent<T>> consumer
        ) {
            this.filter = filter;
            this.consumer = consumer;
        }
    }
}
