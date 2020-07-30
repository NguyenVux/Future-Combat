package com.codingdummy.futurecombat.util;


public class MathHelperCodingDummy {
    public static final float PI = 3.14159f;
    public static float toRad(float deg)
    {
        return deg*PI/180.0f;
    }
    public static float toDeg(float Rad)
    {
        return Rad*180.0f/PI;
    }
}
