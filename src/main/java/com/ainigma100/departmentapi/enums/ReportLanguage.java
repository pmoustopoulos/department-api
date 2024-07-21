package com.ainigma100.departmentapi.enums;

import lombok.Getter;
import java.util.Locale;

@Getter
public enum ReportLanguage {
    EN("en", Locale.ENGLISH), // English
    ES("es", new Locale("es")), // Spanish
    DE("de", Locale.GERMAN), // German
    EL("el", new Locale("el")); // Greek

    private final String code;
    private final Locale locale;

    ReportLanguage(String code, Locale locale) {
        this.code = code;
        this.locale = locale;
    }
}
