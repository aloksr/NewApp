package com.app.truxapp.leasedriver.utility;

import com.app.truxapp.leasedriver.Model.LanguageModel;

import java.util.Arrays;
import java.util.List;


public class LanguageList {
    public static LanguageList get() {
        return new LanguageList();
    }
    private LanguageList() {
    }
    public List<LanguageModel> getForecasts() {
        return Arrays.asList(
                new LanguageModel("हिन्दी", "Hindi"),
                new LanguageModel("मराठी", "Marathi"),
                new LanguageModel("English", "" ),
                new LanguageModel("தமிழ்","Tamil"),
                new LanguageModel("తెలుగు", "Telugu"),
                new LanguageModel("عربى", "Arabic"));

    }
}
