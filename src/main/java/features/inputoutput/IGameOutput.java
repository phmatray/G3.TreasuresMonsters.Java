package features.inputoutput;

import features.i18n.LanguageKey;
import models.State;

public interface IGameOutput {
    void setState(State state);
    void displayScreen(boolean clearContextMessages);
    void displayMessage(LanguageKey key, Object... args);
    void addStatusMessage(LanguageKey key, Object... args);
    void addContextMessage(LanguageKey key, Object... args);
}
