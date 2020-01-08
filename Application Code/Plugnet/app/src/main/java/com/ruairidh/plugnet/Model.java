package com.ruairidh.plugnet;

public class Model {
    String Name, ID;
    int Mode, Plug_Status, Override;
    Float Temperature, Threshhold;

    public Model(){

    }

    public Model(String name, String ID, int mode, int plug_Status, int override, Float temperature, Float threshhold) {
        Name = name;
        this.ID = ID;
        Mode = mode;
        Plug_Status = plug_Status;
        Override = override;
        Temperature = temperature;
        Threshhold = threshhold;
    }

    public int getOverride() {
        return Override;
    }

    public void setOverride(int override) {
        Override = override;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getMode() {
        return Mode;
    }

    public void setMode(int mode) {
        Mode = mode;
    }

    public int getPlug_Status() {
        return Plug_Status;
    }

    public void setPlug_Status(int plug_Status) {
        Plug_Status = plug_Status;
    }

    public Float getTemperature() {
        return Temperature;
    }

    public void setTemperature(Float temperature) {
        Temperature = temperature;
    }

    public Float getThreshhold() {
        return Threshhold;
    }

    public void setThreshhold(Float threshhold) {
        Threshhold = threshhold;
    }
}
