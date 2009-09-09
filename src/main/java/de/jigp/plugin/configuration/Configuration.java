package de.jigp.plugin.configuration;

import java.io.Serializable;

public class Configuration implements Serializable {
    private boolean moveCursorToFirstMethod = true;
    private InsertionPolicy insertionPolicy = InsertLastPolicy.getInstance();

    public Configuration() {
    }

    public boolean isMoveCursorToFirstMethod() {
        return moveCursorToFirstMethod;
    }

    public void setMoveCursorToFirstMethod(boolean moveCursorToFirstMethod) {
        this.moveCursorToFirstMethod = moveCursorToFirstMethod;
    }

    public InsertionPolicy getInsertionPolicy() {
        return insertionPolicy;
    }

    public void setInsertionPolicy(InsertionPolicy insertionPolicy) {
        this.insertionPolicy = insertionPolicy;
    }

    @Override
    public String toString() {
        return "Configuration{" +
                "moveCursorToFirstMethod=" + moveCursorToFirstMethod +
                ", insertionPolicy=" + insertionPolicy +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Configuration that = (Configuration) o;

        if (moveCursorToFirstMethod != that.moveCursorToFirstMethod) return false;
        return !(insertionPolicy != null ? !insertionPolicy.equals(that.insertionPolicy) : that.insertionPolicy != null);
    }

    @Override
    public int hashCode() {
        int result = (moveCursorToFirstMethod ? 1 : 0);
        result = 31 * result + (insertionPolicy != null ? insertionPolicy.hashCode() : 0);
        return result;
    }
}