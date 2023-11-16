package com.softyorch.cursospring.app.util.paginator;

public class PageItem {

    private int num;

    private boolean isCurrent = false;

    public PageItem(int num, boolean isCurrent) {
        this.num = num;
        this.isCurrent = isCurrent;
    }

    public int getNum() {
        return num;
    }

    public boolean isCurrent() {
        return isCurrent;
    }
}
