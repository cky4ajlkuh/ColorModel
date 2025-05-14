package model;

import org.opencv.core.Size;
import util.Help;
import javafx.scene.image.Image;

public abstract class Model implements Help {

    public static Size size;
    public static final int BORDER_0 = 0;

    protected static final double MAX_VAL = 255;
    protected static final double HALF_FI = 180;
    protected static final double HALF_VAL = 127;
    protected static final long HALF_HALF_FI = 90;

    protected static final int BORDER_255 = 255;
    protected static final int BORDER_360 = 360;
    protected static final int BORDER_100 = 100;
    protected static final int BORDER_128 = -128;
    protected static final int BORDER_127 = 127;

    private final Image model;

    private double firstCoordinate;
    private double secondCoordinate;
    private double thirdCoordinate;

    private final int borderMax1;
    private final int borderMax2;
    private final int borderMax3;
    private int borderMin1 = 0;
    private int borderMin2 = 0;
    private int borderMin3 = 0;

    public Model(Image model, int borderMax1, int borderMax2, int borderMax3) {
        this.model = model;
        this.borderMax1 = borderMax1;
        this.borderMax2 = borderMax2;
        this.borderMax3 = borderMax3;
    }

    public Model(Image model,
                 int borderMax1, int borderMax2, int borderMax3,
                 int borderMin1, int borderMin2, int borderMin3) {
        this.model = model;
        this.borderMax1 = borderMax1;
        this.borderMax2 = borderMax2;
        this.borderMax3 = borderMax3;
        this.borderMin1 = borderMin1;
        this.borderMin2 = borderMin2;
        this.borderMin3 = borderMin3;
    }


    public Image getModel() {
        return model;
    }

    public abstract String getFirstCoordinateName();

    public abstract String getSecondCoordinateName();

    public abstract String getThirdCoordinateName();

    public double getFirstCoordinate() {
        return firstCoordinate;
    }

    public double getSecondCoordinate() {
        return secondCoordinate;
    }

    public double getThirdCoordinate() {
        return thirdCoordinate;
    }

    public void setFirstCoordinate(double firstCoordinate) {
        this.firstCoordinate = firstCoordinate;
    }

    public void setSecondCoordinate(double secondCoordinate) {
        this.secondCoordinate = secondCoordinate;
    }

    public void setThirdCoordinate(double thirdCoordinate) {
        this.thirdCoordinate = thirdCoordinate;
    }

    public int getBorderMax1() {
        return borderMax1;
    }

    public int getBorderMax2() {
        return borderMax2;
    }

    public int getBorderMax3() {
        return borderMax3;
    }

    public int getBorderMin1() {
        return borderMin1;
    }

    public int getBorderMin2() {
        return borderMin2;
    }

    public int getBorderMin3() {
        return borderMin3;
    }

    @Override
    public String toString() {
        return "Coordinates: \n first: " + getFirstCoordinate() + "\n second: " + getSecondCoordinate() + "\n third: " + getThirdCoordinate();
    }

}