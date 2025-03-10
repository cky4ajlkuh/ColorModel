package util;

import model.Model;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * Класс помощник, содержащий в себе различные вспомогательные методы
 */
public class Helper {

    private static final String PATH = System.getenv("TEMP") + "\\ColorModel\\" + (int) Model.size.height + "x"
            + (int) Model.size.width + "\\";

    /**
     * Метод, принимающий на вход цветовую модель и возвращающий список изображений всех сечений по 1 координате
     *
     * @param model Цветовая модель
     * @return список изображений всех сечений по 1 координате
     */
    public static List<Mat> getFirstCoordinateImages(Model model) {
        List<Mat> firstCoordinateImages = new ArrayList<>();
        for (int i = 0; i <= model.getBorder1(); i++) {
            model.setFirstCoordinate(i);
            firstCoordinateImages.add(model.getFirstProjection());
        }
        return firstCoordinateImages;
    }

    /**
     * Метод, принимающий на вход цветовую модель и возвращающий список изображений всех сечений по 2 координате
     *
     * @param model Цветовая модель
     * @return список изображений всех сечений по 2 координате
     */
    private static List<Mat> getSecondCoordinateImages(Model model) {
        List<Mat> secondCoordinateImages = new ArrayList<>();
        for (int i = 0; i <= model.getBorder2(); i++) {
            model.setSecondCoordinate(i);
            secondCoordinateImages.add(model.getSecondProjection());
        }
        return secondCoordinateImages;
    }

    /**
     * Метод, принимающий на вход цветовую модель и возвращающий список изображений всех сечений по 3 координате
     *
     * @param model Цветовая модель
     * @return список изображений всех сечений по 3 координате
     */
    private static List<Mat> getThirdCoordinateImages(Model model) {
        List<Mat> thirdCoordinateImages = new ArrayList<>();
        for (int i = 0; i <= model.getBorder3(); i++) {
            model.setThirdCoordinate(i);
            thirdCoordinateImages.add(model.getThirdProjection());
        }
        return thirdCoordinateImages;
    }

    /**
     * Метод, принимающий на вход цветовую модель и сохраняющий все изображения сечений на компьютере пользователя
     *
     * @param model цветовая модель
     */
    public static void serializer(Model model) {
        if (model == null) {
            return;
        }
        String serializationPath = PATH + model.getClass().getSimpleName() + "\\";
        if (checkDirectoryForWrite(serializationPath + model.getFirstCoordinateName())) {
            List<Mat> images = getFirstCoordinateImages(model);
            saveData(images, serializationPath + model.getFirstCoordinateName());
            System.out.println("Сохранены изображения по координате " + model.getFirstCoordinateName() +
                    " цветовой модели " + model.getClass().getSimpleName().replace("Model", ""));
        }
        if (checkDirectoryForWrite(serializationPath + model.getSecondCoordinateName())) {
            List<Mat> images = getSecondCoordinateImages(model);
            saveData(images, serializationPath + model.getSecondCoordinateName());
            System.out.println("Сохранены изображения по координате " + model.getSecondCoordinateName() +
                    " цветовой модели " + model.getClass().getSimpleName().replace("Model", ""));
        }
        if (checkDirectoryForWrite(serializationPath + model.getThirdCoordinateName())) {
            List<Mat> images = getThirdCoordinateImages(model);
            saveData(images, serializationPath + model.getThirdCoordinateName());
            System.out.println("Сохранены изображения по координате " + model.getThirdCoordinateName() +
                    " цветовой модели " + model.getClass().getSimpleName().replace("Model", ""));
        }
    }

    /**
     * Метод, осуществляющий сохранение изображений на компьютере пользователя
     *
     * @param images сохраненные сечения модели
     * @param path   путь к директории
     */
    private static void saveData(List<Mat> images, String path) {
        if (images == null || path == null) {
            return;
        }
        try {
            Files.createDirectories(Path.of(path));
            for (int i = 0; i < images.size(); i++) {
                String name;
                if (i < 10) {
                    name = "00" + i;
                } else if (i < 100) {
                    name = "0" + i;
                } else {
                    name = String.valueOf(i);
                }
                Imgcodecs.imwrite(path + "\\" + name + ".jpg", images.get(i));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Метод, осуществляющий загрузку в приложение сохраненных изображений модели
     *
     * @param model Цветовая модель
     * @return все сохраненные значения сечений цветовой модели
     */
    public static SavedCoordinates deserializer(Model model) {
        if (model == null) {
            return new SavedCoordinates(null, null, null);
        }
        serializer(model);
        String deserializationPath = PATH + model.getClass().getSimpleName() + "\\";
        return new SavedCoordinates(readData(deserializationPath + model.getFirstCoordinateName()),
                readData(deserializationPath + model.getSecondCoordinateName()),
                readData(deserializationPath + model.getThirdCoordinateName()));
    }

    /**
     * Метод, осуществляющий непосредственно считывание данных из директории
     *
     * @param path путь к директории
     * @return список изображений
     */
    private static List<Mat> readData(String path) {
        if (checkDirectoryForRead(path)) {
            File directory = new File(path);
            File[] files = directory.listFiles();
            ArrayList<Mat> mats = new ArrayList<>();
            if (files != null) {
                for (File file : files) {
                    if (file.getName().endsWith(".jpg") || file.getName().endsWith(".png")) {
                        Mat mat = Imgcodecs.imread(file.getAbsolutePath());
                        mats.add(mat);
                    }
                }
            }
            return mats;
        }
        return null;
    }

    /**
     * Метод, проверяющий отсутствие директории. А при ее наличие количество элементов в ней
     *
     * @param path путь к директории
     * @return если директория отсутствует или она пуста, тогда true, иначе false
     */
    private static boolean checkDirectoryForWrite(String path) {
        File directory = new File(path);
        return !directory.exists();
    }

    /**
     * Метод, проверяющий наличие директории и наличие в ней файлов
     *
     * @param path путь к директории
     * @return если директория существует и в ней есть файлы, тогда true, иначе false
     */
    private static boolean checkDirectoryForRead(String path) {
        File directory = new File(path);
        return directory.exists();
    }

}
