package com.manacher.hammer.models;


import java.io.Serializable;

public class RTCData implements Serializable {
    public int type;

    public int random;
    public boolean micOpen;
    public boolean spinLock;
    public String message;
    public String T_O_D;

    public int chip;

    public int selectedBottle;

    public static final int TEXT_MESSAGE = 101;
    public static final int SPIN_REQUEST = 102;
    public static final int MIC_STATE_CHANGE = 103;
    public static final int BOTTLE_CHANGE = 104;
    public static final int TOD_ANSWER = 105;
    public static final int SPIN_LOCK = 106;
    public static final int CHIPS_UPDATE= 107;

    public RTCData(int random, int type){
        this.type = type;
        this.random = random;
    }

    public RTCData(String message, int type){
        this.type = type;
        this.message = message;
    }

    public RTCData(){

    }
}
