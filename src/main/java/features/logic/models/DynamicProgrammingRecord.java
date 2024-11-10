package features.logic.models;

public record DynamicProgrammingRecord(
        int totalScore,
        HeroState predecessor,
        String move) {
}
