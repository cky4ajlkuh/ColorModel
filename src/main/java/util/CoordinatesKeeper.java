package util;

public class CoordinatesKeeper {
    private SavedCoordinates RGB;
    private SavedCoordinates CMY;
    private SavedCoordinates HLS;
    private SavedCoordinates HSV;
    private SavedCoordinates LAB;
    private SavedCoordinates XYZ;
    private SavedCoordinates YUV;

    public SavedCoordinates getRGB() {
        return RGB;
    }

    public void setRGB(SavedCoordinates RGB) {
        this.RGB = RGB;
        System.out.println("Model RGB is loaded!");
    }

    public SavedCoordinates getCMY() {
        return CMY;
    }

    public void setCMY(SavedCoordinates CMY) {
        this.CMY = CMY;
        System.out.println("Model CMY is loaded!");
    }

    public SavedCoordinates getHLS() {
        return HLS;
    }

    public void setHLS(SavedCoordinates HLS) {
        this.HLS = HLS;
        System.out.println("Model HLS is loaded!");
    }

    public SavedCoordinates getHSV() {
        return HSV;
    }

    public void setHSV(SavedCoordinates HSV) {
        this.HSV = HSV;
        System.out.println("Model HSV is loaded!");
    }

    public SavedCoordinates getLAB() {
        return LAB;
    }

    public void setLAB(SavedCoordinates LAB) {
        this.LAB = LAB;
        System.out.println("Model LAB is loaded!");
    }

    public SavedCoordinates getXYZ() {
        return XYZ;
    }

    public void setXYZ(SavedCoordinates XYZ) {
        this.XYZ = XYZ;
        System.out.println("Model XYZ is loaded!");
    }

    public SavedCoordinates getYUV() {
        return YUV;
    }

    public void setYUV(SavedCoordinates YUV) {
        this.YUV = YUV;
        System.out.println("Model YUV is loaded!");
    }

    public void resetAllModels() {
        if (getRGB() != null) {
            RGB.clear();
            setRGB(null);
        }
        if (getCMY() != null) {
            CMY.clear();
            setCMY(null);
        }
        if (getHLS() != null) {
            HLS.clear();
            setHLS(null);
        }
        if (getHSV() != null) {
            HSV.clear();
            setHSV(null);
        }
        if (getLAB() != null) {
            LAB.clear();
            setLAB(null);
        }
        if (getXYZ() != null) {
            XYZ.clear();
            setXYZ(null);
        }
        if (getYUV() != null) {
            YUV.clear();
            setYUV(null);
        }
    }
}
