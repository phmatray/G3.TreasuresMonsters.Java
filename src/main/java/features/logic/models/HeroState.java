package features.logic.models;

import models.MovementConstraint;

public record HeroState(
        int x,
        int y,
        int health,
        int score,
        MovementConstraint moveConstraint) {
}
