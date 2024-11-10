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
            case Level -> "--- Level {0} ---";
            case ScoreToBeat -> "Score to beat: {0}";
            case MovePrompt -> "Move (" + Constants.MOVE_LEFT + "/" + Constants.MOVE_DOWN + "/" + Constants.MOVE_RIGHT + "), H for hint, Q to quit: ";
            case InvalidInput -> "Invalid input.";
            case CannotMoveUp -> "You cannot move up.";
            case CannotMoveLeft -> "You cannot change direction to the left.";
            case CannotMoveRight -> "You cannot change direction to the right.";
            case CannotMoveThere -> "You cannot move there.";
            case MonsterEncounter -> "You encountered a monster! You lose {0} health points.";
            case TreasureFound -> "You found a treasure! You gain {0} points.";
            case NoHintAvailable -> "No hints available.";
            case HintUsed -> "You used a hint.";
            case PerfectPath -> "Perfect path: {0}";
            case LevelCompleted -> "Level completed!";
            case YourScore -> "Your score: {0}";
            case BeatScore -> "You beat the score! You gain a hint for the next levels.";
            case DidNotBeatScore -> "You did not beat the score.";
            case GameOver -> "You are dead. Game over!";
            case ThanksForPlaying -> "Thank you for playing!";
            case HeroStatus -> "Health: {0} / 100 | Score: {1} | Hints: {2}";
            case LevelEnd -> "Press '" + Constants.MOVE_DOWN + "' to move to the next level.";
            case HeroIsDead -> "The hero is already dead.";
            case NoValidPath -> "There is no valid path.";
            default -> key.toString(); // Return key name if not found
        };
    }

    private static String getFrenchString(LanguageKey key) {
        return switch (key) {
            case Level -> "--- Niveau {0} ---";
            case ScoreToBeat -> "Score à battre : {0}";
            case MovePrompt -> "Déplacez-vous (" + Constants.MOVE_LEFT + "/" + Constants.MOVE_DOWN + "/" + Constants.MOVE_RIGHT + "), H pour indice, Q pour quitter : ";
            case InvalidInput -> "Entrée invalide.";
            case CannotMoveUp -> "Impossible de remonter.";
            case CannotMoveLeft -> "Vous ne pouvez pas changer de direction vers la gauche.";
            case CannotMoveRight -> "Vous ne pouvez pas changer de direction vers la droite.";
            case CannotMoveThere -> "Vous ne pouvez pas vous déplacer là.";
            case MonsterEncounter -> "Vous avez rencontré un monstre ! Vous perdez {0} points de vie.";
            case TreasureFound -> "Vous avez trouvé un trésor ! Vous gagnez {0} points.";
            case NoHintAvailable -> "Aucun indice disponible.";
            case HintUsed -> "Vous avez utilisé un indice.";
            case PerfectPath -> "Chemin parfait : {0}";
            case LevelCompleted -> "Niveau terminé !";
            case YourScore -> "Votre score : {0}";
            case BeatScore -> "Vous avez battu le score ! Vous gagnez un indice pour les niveaux suivants.";
            case DidNotBeatScore -> "Vous n'avez pas battu le score.";
            case GameOver -> "Vous êtes mort. Fin du jeu !";
            case ThanksForPlaying -> "Merci d'avoir joué !";
            case HeroStatus -> "Vie : {0} / 100 | Score : {1} | Indices : {2}";
            case LevelEnd -> "Appuyez sur '" + Constants.MOVE_DOWN + "' pour passer au niveau suivant.";
            case HeroIsDead -> "Le héros est déjà mort.";
            case NoValidPath -> "Il n'y a pas de chemin valide.";
            default -> key.toString(); // Return key name if not found
        };
    }
}
