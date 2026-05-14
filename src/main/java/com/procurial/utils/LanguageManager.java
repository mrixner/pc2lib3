package com.procurial.utils;

import edu.csus.ecs.pc2.core.InternalController;
import edu.csus.ecs.pc2.core.model.InternalContest;
import edu.csus.ecs.pc2.core.model.Language;
import edu.csus.ecs.pc2.core.model.LanguageAutoFill;

public class LanguageManager {
    InternalContest contest;
    InternalController controller;

    public LanguageManager(InternalContest contest, InternalController controller) {
        this.contest = contest;
        this.controller = controller;
    }

    /* Default languages for pc2 9.10.0:
        - Default
        - Java
        - GNU C++ (Unix / Windows)
        - GNU C (Unix / Windows)
        - Mono C#
        - Microsoft C#
        - Perl
        - PHP
        - Python
        - Python 3
        - Ruby
        - Microsoft C++
        - Kylix Delphi
        - Kylix C++
        - Free Pascal
        - Kotlin (Windows)
        - Kotlin (Linux/MacOS)
    */

    public void loadDefaultLanguages(){
        if ((this.contest.getLanguages()).length == 0) {
            for (String name : new String[]{"Java", "GNU C++ (Unix / Windows)", "GNU C (Unix / Windows)", "Python 3"}) {
                Language language = createLanguage(name);
                if (language != null)
                    this.controller.addNewLanguage(language);
                System.out.println("quickLoad: add " + language);
            }
        }
    }

    private Language createLanguage(String languageName) {
        byte b;
        int i;
        String[] arrayOfString;
        System.out.println("================");
        for (i = (arrayOfString = LanguageAutoFill.getLanguageList()).length, b = 0; b < i; ) {
            String langName = arrayOfString[b];
            if (langName.equals(languageName)) {
                String[] values = LanguageAutoFill.getAutoFillValues(langName);
                Language language = new Language(langName);
                fillLanguage(language, values, langName);
                return language;
            }
            b++;
        }
        return null;
    }


    private void fillLanguage(Language language, String[] values, String fullLanguageName) {
        language.setCompileCommandLine(values[1]);
        language.setExecutableIdentifierMask(values[2]);
        language.setProgramExecuteCommandLine(values[3]);
        boolean isScript = LanguageAutoFill.isInterpretedLanguage(fullLanguageName);
        language.setInterpreted(isScript);
        language.setID(values[6]);
    }
}
