package com.mingko.modolite.model.event;

/**
 * 魔哆大小变化event
 * Created by ssthouse on 2016/1/24.
 */
public class ModoScaleChangeEvent {

    private boolean isToBig;

    public ModoScaleChangeEvent(boolean isToBig) {
        this.isToBig = isToBig;
    }

    public boolean isToBig() {
        return isToBig;
    }

    public void setIsToBig(boolean isToBig) {
        this.isToBig = isToBig;
    }
}
