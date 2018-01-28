package net.cheltsov.shtoss.resource;

import net.cheltsov.shtoss.validator.Validator;

import java.util.Locale;
import java.util.ResourceBundle;

public enum BundleManager {
    MESSAGE_EN(ResourceBundle.getBundle("properties/message", new Locale("en", "en"))),
    MESSAGE_RU(ResourceBundle.getBundle("properties/message", new Locale("ru", "ru"))),
    PATH_JSP(ResourceBundle.getBundle("properties/jsp")),
    DATABASE(ResourceBundle.getBundle("properties/connection"));

    private ResourceBundle bundle;
    BundleManager(ResourceBundle bundle) {
        this.bundle = bundle;
    }

    public String getString(String parameter) {
        return bundle.getString(parameter);
    }

    public static BundleManager getMessageBundleManager(String language) {
        language = language.trim().toUpperCase();
        return Validator.validateLanguage(language)? BundleManager.valueOf("MESSAGE_" + language): null;
    }

    public ResourceBundle getBundle() {
        return bundle;
    }
}
