package com.app.truxapp.leasedriver.Model;

import java.io.Serializable;
public class AllowedFeatureModel implements Serializable {
    private String featureCode;
    private String feature;
    private boolean isEnabled;
    public String getFeatureCode() {
        return featureCode;
    }

    public void setFeatureCode(String featureCode) {
        this.featureCode = featureCode;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }
}
