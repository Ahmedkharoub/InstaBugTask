package com.example.instabugtask;

import android.widget.ImageButton;

public class headerKey {
    private String key;
    private String value;
    private ImageButton removeRow;

    public ImageButton getRemoveRow() {
        return removeRow;
    }

    public void setRemoveRow(ImageButton removeRow) {
        this.removeRow = removeRow;
    }

    public headerKey(ImageButton removeRow) {
        this.removeRow = removeRow;
    }

    public headerKey(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
