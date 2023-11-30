package me.oneqxz.keygeneration;

public interface StepInfo {

    int getMouseX();
    int getMouseY();


    default String toXYString()
    {
        return getMouseX() + String.valueOf(getMouseY()) + (getMouseY() + getMouseY());
    }
}
