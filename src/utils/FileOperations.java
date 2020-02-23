package utils;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

public class FileOperations {

    /**
     * Appends a string to a text file.
     *
     * @param fileName text file name.
     * @param string   string to be appended.
     */
    public static void appendToTextFile(String fileName, String string) {
        BufferedWriter w = null;
        try {
            w = new BufferedWriter(new FileWriter(fileName, true));
            w.write(string);

        } catch (Exception e) {
            System.err.println("Error: " + e);
        } finally {
            try {
                if (w != null) {
                    w.close();
                }
            } catch (IOException ignore) {
            }
        }
    }

    public static void saveImage(String fileName, BufferedImage renderedImage) {
        // Find a jpeg writer
        ImageWriter writer = null;
        Iterator<ImageWriter> iter = ImageIO.getImageWritersByFormatName("jpg");
        if (iter.hasNext()) {
            writer = (ImageWriter) iter.next();
        }

        //Defining compression level
        ImageWriteParam iwp = writer.getDefaultWriteParam();
        iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        iwp.setCompressionQuality(1);

        try {
            // Prepare output file
            File outFile = new File(fileName);
            FileImageOutputStream output = new FileImageOutputStream(outFile);

            //write the image
            writer.setOutput(output);
            IIOImage imageIIO = new IIOImage(renderedImage, null, null);
            writer.write(null, imageIIO, iwp);

        } catch (IOException ex) {
        }
    }
}
