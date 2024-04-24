package controleur;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.awt.image.RenderedImage;


public class ImageUtil {

    public static String getRelativePath(String absolutePath) {
        String projectPath = System.getProperty("user.dir");
        return absolutePath.replace(projectPath, "").replace(File.separator, "/");
    }
    public static List<String> convertImagesToPaths(List<Image> images, int propertyId) {
        List<String> paths = new ArrayList<>();
        for (int i = 0; i < images.size(); i++) {
            String directoryPath = "image"; // Chemin du répertoire d'images
            String imageName = "property_" + propertyId + "image" + i + ".png"; // Nom de l'image avec l'ID de la propriété
            String imagePath = directoryPath + "/" + imageName; // Chemin complet de l'image
            File outputfile = new File(imagePath);
            try {
                ImageIO.write((RenderedImage) images.get(i), "png", outputfile);
                paths.add(imagePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return paths;
    }

}