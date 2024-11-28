package features.i18n;

import models.Constants;

public class LanguageService implements ILanguageService {
    private String languageCode = "en";

    @Override
    public void setLanguage(String languageCode) {
        // Supported languages: "en" and "fr"
        this.languageCode = "fr".equals(languageCode) ? "fr" : "en";
    }

    @Override
    public String getString(LanguageKey key) {
        return switch (languageCode) {
            case "fr" -> getFrenchString(key);
            default -> getEnglishString(key);
        };
    }

    private static String getEnglishString(LanguageKey key) {
        return switch (key) {
            case Level -> "--- Level %d ---";
            case ScoreToBeat -> "Score to beat: %d";
            case MovePrompt -> String.format("Move (%s/%s/%s), H for hint, Q to quit: ",
                    Constants.MOVE_LEFT, Constants.MOVE_DOWN, Constants.MOVE_RIGHT);
            case InvalidInput -> "Invalid input.";
            case CannotMoveUp -> "You cannot move up.";
            case CannotMoveLeft -> "You cannot change direction to the left.";
            case CannotMoveRight -> "You cannot change direction to the right.";
            case CannotMoveThere -> "You cannot move there.";
            case MonsterEncounter -> "You encountered a monster! You lose %d health points.";
            case TreasureFound -> "You found a treasure! You gain %d points.";
            case NoHintAvailable -> "No hints available.";
            case HintUsed -> "You used a hint.";
            case PerfectPath -> "Perfect path: %s";
            case LevelCompleted -> "Level completed!";
            case YourScore -> "Your score: %d";
            case BeatScore -> "You beat the score! You gain a hint for the next levels.";
            case DidNotBeatScore -> "You did not beat the score.";
            case GameOver -> "You are dead. Game over!";
            case ThanksForPlaying -> "Thank you for playing!";
            case HeroStatus -> "Health: %d / 100 | Score: %d | Hints: %d";
            case LevelEnd -> String.format("Press '%s' to move to the next level.", Constants.MOVE_DOWN);
            case HeroIsDead -> "The hero is already dead.";
            case NoValidPath -> "There is no valid path.";
        };
    }

    private static String getFrenchString(LanguageKey key) {
        return switch (key) {
            case Level -> "--- Niveau %d ---";
            case ScoreToBeat -> "Score à battre : %d";
            case MovePrompt -> String.format("Déplacez-vous (%s/%s/%s), H pour indice, Q pour quitter : ",
                    Constants.MOVE_LEFT, Constants.MOVE_DOWN, Constants.MOVE_RIGHT);
            case InvalidInput -> "Entrée invalide.";
            case CannotMoveUp -> "Impossible de remonter.";
            case CannotMoveLeft -> "Vous ne pouvez pas changer de direction vers la gauche.";
            case CannotMoveRight -> "Vous ne pouvez pas changer de direction vers la droite.";
            case CannotMoveThere -> "Vous ne pouvez pas vous déplacer là.";
            case MonsterEncounter -> "Vous avez rencontré un monstre ! Vous perdez %d points de vie.";
            case TreasureFound -> "Vous avez trouvé un trésor ! Vous gagnez %d points.";
            case NoHintAvailable -> "Aucun indice disponible.";
            case HintUsed -> "Vous avez utilisé un indice.";
            case PerfectPath -> "Chemin parfait : %s";
            case LevelCompleted -> "Niveau terminé !";
            case YourScore -> "Votre score : %d";
            case BeatScore -> "Vous avez battu le score ! Vous gagnez un indice pour les niveaux suivants.";
            case DidNotBeatScore -> "Vous n'avez pas battu le score.";
            case GameOver -> "Vous êtes mort. Fin du jeu !";
            case ThanksForPlaying -> "Merci d'avoir joué !";
            case HeroStatus -> "Vie : %d / 100 | Score : %d | Indices : %d";
            case LevelEnd -> String.format("Appuyez sur '%s' pour passer au niveau suivant.", Constants.MOVE_DOWN);
            case HeroIsDead -> "Le héros est déjà mort.";
            case NoValidPath -> "Il n'y a pas de chemin valide.";
        };
    }
}
