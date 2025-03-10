package model;

import org.opencv.core.Size;
import util.Help;
import javafx.scene.image.Image;

public abstract class Model implements Help {

    protected static final double MAX_VAL = 255;
    protected static final double HALF_FI = 180;
    protected static final double HALF_VAL = 127;
    protected static final long HALF_HALF_FI = 90;
    public static Size size;

    private final Image model;
    private double firstCoordinate;
    private double secondCoordinate;
    private double thirdCoordinate;
    private final int border1;
    private final int border2;
    private final int border3;

    public Model(Image model, int border1, int border2, int border3) {
        this.model = model;
        this.border1 = border1;
        this.border2 = border2;
        this.border3 = border3;
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

    public int getBorder1() {
        return border1;
    }

    public int getBorder2() {
        return border2;
    }

    public int getBorder3() {
        return border3;
    }

    @Override
    public String toString() {
        return "Coordinates: \n first: " + getFirstCoordinate() + "\n second: " + getSecondCoordinate() + "\n third: " + getThirdCoordinate();
    }

}