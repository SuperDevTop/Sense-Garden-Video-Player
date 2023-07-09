package com.sensegarden.sensegardenplaydev.models.genericflow;

public class SelectableModel {
    private Object value;
    private boolean selected;

    public SelectableModel(Object value, boolean selected) {
        this.value = value;
        this.selected = selected;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
