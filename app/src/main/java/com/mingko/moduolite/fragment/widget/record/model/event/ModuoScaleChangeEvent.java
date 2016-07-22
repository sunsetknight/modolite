package com.mingko.moduolite.fragment.widget.record.model.event;

/**
 * 魔哆大小变化event
 * Created by ssthouse on 2016/1/24.
 */
public class ModuoScaleChangeEvent {

    private boolean isToBig;

    public ModuoScaleChangeEvent(boolean isToBig) {
        this.isToBig = isToBig;
    }

    public boolean isToBig() {
        return isToBig;
    }

    public void setIsToBig(boolean isToBig) {
        this.isToBig = isToBig;
    }
}
