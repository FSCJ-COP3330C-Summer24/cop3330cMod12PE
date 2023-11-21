// ImageType.java
// D. Singletary
// 10/9/23
// Enum representing image types

package edu.fscj.cop3330c.image;

public enum ImageType {
    NONE, PNG, JPG;

    public static String getFileExtension(ImageType type) {
        String fileExt = "";
        switch (type) {
            case PNG:
                fileExt = ".png";
                break;
            case JPG:
                fileExt = ".jpg";
                break;
        }
        return fileExt;
    }
}