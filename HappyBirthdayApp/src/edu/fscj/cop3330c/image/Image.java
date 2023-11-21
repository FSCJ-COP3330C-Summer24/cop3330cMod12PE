// Image.java
// D. Singletary
// 10/9/23
// Class representing an image

package edu.fscj.cop3330c.image;

public class Image {
    private String fileName;
    private ImageType type;

    public Image(String fileName, ImageType type) {
        this.fileName = fileName;
        this.type = type;
    }

    @Override
    public String toString() {
        return this.fileName + "(" + this.type + ")";
    }
}
