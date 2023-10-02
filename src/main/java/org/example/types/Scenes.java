package org.example.types;

public enum Scenes {
    MainWorkspace("mainworkspace");

    private final String label;

    private Scenes(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }

}
