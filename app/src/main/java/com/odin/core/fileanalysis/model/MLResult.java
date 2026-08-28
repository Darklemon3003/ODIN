package com.odin.core.fileanalysis.model;

import java.util.Map;

/**
 * Real machine learning analysis result from actual ML model inference.
 * No simulated results - only actual ML model predictions.
 */
public class MLResult {
    private double maliciousProbability;
    private double benignProbability;
    private String predictedClass;
    private double confidenceScore;
    private Map<String, Double> classProbabilities;
    private String modelVersion;
    private long inferenceTimeMs;

    public MLResult() {}

    // Getters and Setters
    public double getMaliciousProbability() { return maliciousProbability; }
    public void setMaliciousProbability(double maliciousProbability) { this.maliciousProbability = maliciousProbability; }

    public double getBenignProbability() { return benignProbability; }
    public void setBenignProbability(double benignProbability) { this.benignProbability = benignProbability; }

    public String getPredictedClass() { return predictedClass; }
    public void setPredictedClass(String predictedClass) { this.predictedClass = predictedClass; }

    public double getConfidenceScore() { return confidenceScore; }
    public void setConfidenceScore(double confidenceScore) { this.confidenceScore = confidenceScore; }

    public Map<String, Double> getClassProbabilities() { return classProbabilities; }
    public void setClassProbabilities(Map<String, Double> classProbabilities) { this.classProbabilities = classProbabilities; }

    public String getModelVersion() { return modelVersion; }
    public void setModelVersion(String modelVersion) { this.modelVersion = modelVersion; }

    public long getInferenceTimeMs() { return inferenceTimeMs; }
    public void setInferenceTimeMs(long inferenceTimeMs) { this.inferenceTimeMs = inferenceTimeMs; }
}
