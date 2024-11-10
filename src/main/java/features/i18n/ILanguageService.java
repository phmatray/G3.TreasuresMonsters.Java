package features.i18n;

public interface ILanguageService {
    String getString(LanguageKey key);
    void setLanguage(String languageCode);
}
