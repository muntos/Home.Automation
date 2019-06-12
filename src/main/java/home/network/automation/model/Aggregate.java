package home.network.automation.model;

import java.util.Optional;

public enum Aggregate {
    MIN,
    MAX;

    public static Optional<Aggregate> of(String value){
        try {
            Optional<Aggregate> aggregate = Optional.of(Aggregate.valueOf(value.toUpperCase()));
            return aggregate;
        }
        catch (IllegalArgumentException ex){
            return Optional.empty();
        }
    }
}
