package util;

import org.opencv.core.Mat;

import java.util.List;

public class SavedCoordinates {
    private final List<Mat> firstCoordinateImages;
    private final List<Mat> secondCoordinateImages;
    private final List<Mat> thirdCoordinateImages;


    public SavedCoordinates(List<Mat> firstCoordinateImages, List<Mat> secondCoordinateImages, List<Mat> thirdCoordinateImages) {
        this.firstCoordinateImages = firstCoordinateImages;
        this.secondCoordinateImages = secondCoordinateImages;
        this.thirdCoordinateImages = thirdCoordinateImages;
    }

    public List<Mat> getFirstCoordinateImages() {
        return firstCoordinateImages;
    }

    public List<Mat> getSecondCoordinateImages() {
        return secondCoordinateImages;
    }

    public List<Mat> getThirdCoordinateImages() {
        return thirdCoordinateImages;
    }

    public void clear() {
        if (firstCoordinateImages != null) {
            firstCoordinateImages.clear();
        }
        if (secondCoordinateImages != null) {
            secondCoordinateImages.clear();
        }
        if (thirdCoordinateImages != null) {
            thirdCoordinateImages.clear();
        }
    }
}
