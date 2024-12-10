package features.engine;

import features.i18n.*;
import features.inputoutput.*;
import features.logic.Algorithms;
import models.*;

public class GameEngine {
    private final IGameInput input;
    private final IGameOutput output;
    private State state;

    public GameEngine(IGameInput input, IGameOutput output) {
        this.input = input;
        this.output = output;
    }

    public void startNewGame() {
        initializeState();

        output.displayMessage(LanguageKey.MovePrompt);

        startNewLevel();
    }

    private void initializeState() {
        state = new State(new int[] {0, 0}, Constants.MAX_HEALTH, 0, new int[0][0], new int[0][0], 1, 0);
    }

    private void startNewLevel() {
        state.initializeDungeon();
        output.setState(state);
        output.displayScreen(false);
        playLevel();
    }

    private void playLevel() {
        while (true) {
            if (state.isHeroDead()) {
                handleGameOver();
            }

            try {
                int inputKey = input.readKey();
                System.out.println(inputKey);
                handleInput(inputKey);
            } catch (Exception e) {
                // Ignore exceptions
            }
        }
    }

    private void handleInput(int inputKey) {
        switch (inputKey) {
            case IGameInput.ARROW_UP -> handleMoveUp();
            case IGameInput.ARROW_DOWN -> handleMoveDown();
            case IGameInput.ARROW_LEFT -> handleMoveLeft();
            case IGameInput.ARROW_RIGHT -> handleMoveRight();
            case 'H' -> handleShowHint();
            case 'Q' -> handleQuitGame();
            default -> output.addStatusMessage(LanguageKey.InvalidInput);
        }
    }

    private void handleMoveUp() {
        output.addContextMessage(LanguageKey.CannotMoveUp);
        output.displayScreen(true);
    }

    private void handleMoveDown() {
        CellResolution cellResolution = state.moveDown();
        processCellResolution(cellResolution);
    }

    private void handleMoveLeft() {
        if (state.canMoveLeft()) {
            CellResolution cellResolution = state.moveLeft();
            processCellResolution(cellResolution);
        } else {
            LanguageKey cannotMoveKey = state.getHeroMoveConstraint() == MovementConstraint.Left
                    ? LanguageKey.CannotMoveLeft
                    : LanguageKey.CannotMoveThere;
            output.addContextMessage(cannotMoveKey);
            output.displayScreen(true);
        }
    }

    private void handleMoveRight() {
        if (state.canMoveRight()) {
            CellResolution cellResolution = state.moveRight();
            processCellResolution(cellResolution);
        } else {
            LanguageKey cannotMoveKey = state.getHeroMoveConstraint() == MovementConstraint.Right
                    ? LanguageKey.CannotMoveRight
                    : LanguageKey.CannotMoveThere;
            output.addContextMessage(cannotMoveKey);
            output.displayScreen(true);
        }
    }

    private void processCellResolution(CellResolution cellResolution) {
        output.setState(state);
        switch (cellResolution.getType()) {
            case Empty -> output.displayScreen(false);
            case Monster -> {
                output.addContextMessage(LanguageKey.MonsterEncounter, cellResolution.getValue());
                output.displayScreen(true);
            }
            case Treasure -> {
                output.addContextMessage(LanguageKey.TreasureFound, cellResolution.getValue());
                output.displayScreen(true);
            }
            case EndOfLevel -> {
                endLevel();
                startNewLevel();
            }
        }

        if (state.getHeroY() == state.getDungeonHeight() - 1) {
            output.addContextMessage(LanguageKey.LevelEnd);
            output.displayScreen(true);
        }
    }

    private void handleShowHint() {
        if (state.getNbHint() > 0) {
            state.decreaseHint();
            String path = Algorithms.DP.perfectSolution(state);

            switch (path) {
                case "<DEAD>" -> output.addStatusMessage(LanguageKey.HeroIsDead);
                case "<INVALID>" -> output.addStatusMessage(LanguageKey.NoValidPath);
                default -> {
                    String simplifiedPath = new HeroPath(path).getNormalizedPath();
                    output.addStatusMessage(LanguageKey.PerfectPath, simplifiedPath);
                }
            }
            output.addContextMessage(LanguageKey.HintUsed);
        } else {
            output.addContextMessage(LanguageKey.NoHintAvailable);
        }
        output.displayScreen(true);
    }

    private void handleQuitGame() {
        output.addContextMessage(LanguageKey.ThanksForPlaying);
        output.displayScreen(true);
        System.exit(0);
    }

    private void handleGameOver() {
        output.addContextMessage(LanguageKey.GameOver);
        output.displayScreen(true);

        boolean wantsToRestart = output.askRestartGame();
        if (wantsToRestart) {
            startNewGame();
        } else {
            System.exit(0); // or handle quitting gracefully without immediate exit if desired
        }
    }

    private void endLevel() {
        output.addContextMessage(LanguageKey.LevelCompleted);
        output.addContextMessage(LanguageKey.YourScore, state.getHeroScore());

        if (state.getHeroScore() > state.getDungeonScoreToBeat()) {
            state.addHint();
            output.addContextMessage(LanguageKey.BeatScore);
        } else {
            output.addContextMessage(LanguageKey.DidNotBeatScore);
        }

        output.displayScreen(true);
    }
}
