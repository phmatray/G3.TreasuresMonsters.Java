package features.logic.models;

import models.MovementConstraint;

public record PositionResult(
        int x,
        int y,
        MovementConstraint moveConstraint) {
}
