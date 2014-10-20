package org.paltest.pal.model;

public enum Status {
    Passed(1),
    Failed(2),
    Ignored(0);

    private int viewPriority;

    Status(int viewPriority) {
        this.viewPriority = viewPriority;
    }

    public int getViewPriority() {
        return viewPriority;
    }
}
