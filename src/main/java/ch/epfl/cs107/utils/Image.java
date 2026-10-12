package ch.epfl.cs107.utils;

import ch.epfl.cs107.Helper;

import static ch.epfl.cs107.utils.Text.*;
import static ch.epfl.cs107.utils.Image.*;
import static ch.epfl.cs107.utils.Bit.*;
import static ch.epfl.cs107.stegano.ImageSteganography.*;
import static ch.epfl.cs107.stegano.TextSteganography.*;
import static ch.epfl.cs107.crypto.Encrypt.*;
import static ch.epfl.cs107.crypto.Decrypt.*;
import static ch.epfl.cs107.Main.*;

/**
 * <b>Task 1.3: </b>Utility class to manipulate ARGB images
 *
 * @author Hamza REMMAL (hamza.remmal@epfl.ch)
 * @version 1.0.0
 * @since 1.0.0
 */
public final class   Image {

    // DO NOT CHANGE THIS, MORE ON THAT ON WEEK 7
    private Image(){}

    // ============================================================================================
    // ==================================== PIXEL MANIPULATION ====================================
    // ============================================================================================

    /**
     * Build a given pixel value from its respective components
     *
     * @param alpha alpha component of the pixel
     * @param red red component of the pixel
     * @param green green component of the pixel
     * @param blue blue component of the pixel
     * @return packed value of the pixel
     */
    public static int argb(byte alpha, byte red, byte green, byte blue){ //ici on reconstruit un pixe
        //avec les valeurs des 4 bytes en mode non signé
        int value = (alpha & 0xFF) << 24;
        value |= (red & 0xFF) << 16;
        value |= (green & 0xFF) << 8;
        value |= blue & 0xFF ;
        return value;
    }

    /**
     * Extract the alpha component of a given pixel
     *
     * @param pixel packed value of the pixel
     * @return the alpha component of the pixel
     */
    public static byte alpha(int pixel){
        return (byte)((pixel >> 24)& 0xFF); //on a un int "value" qui vaut 4bytes, ici on veut
        //celui tout à gauche donc je décale tout de 24 pour qu'il soit tout à droite, puis je
        //transtype en byte pour garder que le byte tout à droite et je le return
        //et en gros pour les 3 prochaines méthodes c'est la même chose
    }

    /**
     * Extract the red component of a given pixel
     *
     * @param pixel packed value of the pixel
     * @return the red component of the pixel
     */
    public static byte red(int pixel){
        return (byte)((pixel >> 16)& 0xFF);
    }
    /**
     * Extract the green component of a given pixel
     *
     * @param pixel packed value of the pixel
     * @return the green component of the pixel
     */
    public static byte green(int pixel){
        return (byte)((pixel >> 8)& 0xFF);
    }

    /**
     * Extract the blue component of a given pixel
     *
     * @param pixel packed value of the pixel
     * @return the blue component of the pixel
     */
    public static byte blue(int pixel){
        return (byte) (pixel & 0xFF);
    }

    /**
     * Compute the gray scale of the given pixel
     *
     * @param pixel packed value of the pixel
     * @return gray scaling of the given pixel
     */
    public static int gray(int pixel){ //ça fait une moyenne du RGB pour dire à quel point c'est gris
        int red = pixel >>> 16 & 0xFF;
        int green = pixel >>> 8 & 0xFF;
        int blue = pixel & 0xFF;
        return (red + green + blue)/3;
    }

    /**
     * Compute the binary representation of a given pixel.
     *
     * @param gray gray scale value of the given pixel
     * @param threshold when to consider a pixel white
     * @return binary representation of a pixel
     */
    public static boolean binary(int gray, int threshold){
        assert threshold > 0 && threshold < 255;
        if (gray < threshold){
            return false;
        }
        else{
            return true;
        }
    }

    // ============================================================================================
    // =================================== IMAGE MANIPULATION =====================================
    // ============================================================================================

    /**
     * Build the gray scale version of an ARGB image
     *
     * @param image image in ARGB format
     * @return the gray scale version of the image
     */
    public static int[][] toGray(int[][] image){
        assert (image.length !=0) : "l'image est trop petite";
        assert (image[0].length !=0) : "l'image est trop petite";

        int [][] toGray = new int[image.length][image[0].length];

        for (int i =0; i < image.length; i++){
            for (int j =0; j < image[i].length; j++){
                 toGray[j][i]= gray(image[j][i]);
            }
        }
        return  toGray;



        //return Helper.fail("NOT IMPLEMENTED");
    }

    /**
     * Build the binary representation of an image from the gray scale version
     *
     * @param image Image in gray scale representation
     * @param threshold Threshold to consider
     * @return binary representation of the image
     */
    public static boolean[][] toBinary(int[][] image, int threshold){
        assert (image.length !=0) : "l'image est trop petite";
        assert (image[0].length !=0) : "l'image est trop petite";

        boolean [][] toBinary = new boolean[image.length][image[0].length];

        for (int i =0; i < image.length; i++){
            for (int j =0; j < image[i].length; j++){
                toBinary[j][i]= binary(image[j][i], threshold);
            }
        }
        return  toBinary;


        //return Helper.fail("NOT IMPLEMENTED");
    }

    /**
     * Build an ARGB image from the gray-scaled image
     * @implNote The result of this method will a gray image, not the original image
     * @param image grayscale image representation
     * @return <b>gray ARGB</b> representation
     */
    public static int[][] fromGray(int[][] image){
        assert image != null : "l'image est nulle";

        int [][] fromGray = new int[image.length][image[0].length];

        for (int i =0; i < image.length; i++){
            for (int j =0; j < image[i].length; j++){
                fromGray [i][j]= argb ((byte)0xFF,(byte)image[i][j],(byte)image[i][j],(byte)image[i][j]);
            }
        }
        return fromGray;

        //return Helper.fail("NOT IMPLEMENTED");
    }

    /**
     * Build an ARGB image from the binary image
     * @implNote The result of this method will a black and white image, not the original image
     * @param image binary image representation
     * @return <b>black and white ARGB</b> representation
     */
    public static int[][] fromBinary(boolean[][] image){
        assert image != null : "l'image est nulle";

        int [][] fromBinary = new int[image.length][image[0].length];

        for (int i =0; i < image.length; i++){
            for (int j =0; j < image[i].length; j++){
                if (image[i][j] == true){
                    fromBinary [i][j] = argb ((byte)0xFF,(byte)255,(byte)255,(byte)255 );
                }else {
                    fromBinary [i][j] = argb ((byte)0xFF,(byte)0,(byte)0,(byte)0 );
                }
            }
        }
        return fromBinary;
        //return Helper.fail("NOT IMPLEMENTED");
    }

}
