package util;

import org.opencv.core.Mat;

/**
 * Интерфейс, содержащий в себе методы по получению информации о модели и ее сечении
 */
public interface Help {
    String getInfo();

    Mat getFirstProjection();

    Mat getSecondProjection();

    Mat getThirdProjection();

}
