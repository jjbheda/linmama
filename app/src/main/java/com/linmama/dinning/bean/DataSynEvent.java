package com.linmama.dinning.bean;

/**
 * Created by jiangjingbo on 2017/11/5.
 */

public class DataSynEvent {
    public DataSynEvent(){
    }

    public DataSynEvent (boolean shouldUpdateData){
        this.shouldUpdateData = shouldUpdateData;
    }

    public boolean isShouldUpdateData() {
        return shouldUpdateData;
    }

    public void setShouldUpdateData(boolean shouldUpdateData) {
        this.shouldUpdateData = shouldUpdateData;
    }

    private boolean shouldUpdateData;
    private boolean autoReceiveorderIsSet;

    public boolean isAutoReceiveorderIsSet() {
        return autoReceiveorderIsSet;
    }

    public void setAutoReceiveorderIsSet(boolean autoReceiveorderIsSet) {
        this.autoReceiveorderIsSet = autoReceiveorderIsSet;
    }
}
