package com.sshpro.album.config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.Selector;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;

@Component
@Endpoint(id = "features1")
public class FeatureEndPoint {
    private final Map<String, Feature> featureMap = new ConcurrentHashMap<>();

    public FeatureEndPoint() {
        featureMap.put("Album", new Feature(true));
        featureMap.put("Authentication", new Feature(false));
        featureMap.put("User", new Feature(false));
        featureMap.put("Photo", new Feature(false));
    }

    @ReadOperation
    public Map<String, Feature> features() {
        return featureMap;
    }

    @ReadOperation
    public Feature feature(@Selector String featureName) {
        return featureMap.get(featureName);
    }

    @Data
    @AllArgsConstructor
    private static class Feature {
        private boolean isEnabled;
    }
}
