package ch.epfl.cs107.stegano;

import ch.epfl.cs107.Helper;
import ch.epfl.cs107.utils.Bit;
import ch.epfl.cs107.utils.Image;

import static ch.epfl.cs107.utils.Text.*;
import static ch.epfl.cs107.utils.Image.*;
import static ch.epfl.cs107.utils.Bit.*;
import static ch.epfl.cs107.stegano.ImageSteganography.*;
import static ch.epfl.cs107.stegano.TextSteganography.*;
import static ch.epfl.cs107.crypto.Encrypt.*;
import static ch.epfl.cs107.crypto.Decrypt.*;
import static ch.epfl.cs107.Main.*;

/**
 * <b>Task 3.1: </b>Utility class to perform Image Steganography
 *
 * @author Hamza REMMAL (hamza.remmal@epfl.ch)
 * @version 1.0.0
 * @since 1.0.0
 */
public final class ImageSteganography {

    // DO NOT CHANGE THIS, MORE ON THAT ON WEEK 7
    private ImageSteganography(){}

    // ============================================================================================
    // ================================== EMBEDDING METHODS =======================================
    // ============================================================================================

    /**
     * Embed an ARGB image on another ARGB image (the cover)
     * @param cover Cover image
     * @param argbImage Embedded image
     * @param threshold threshold to use for binary conversion
     * @return ARGB image with the image embedded on the cover
     */
    public static int[][] embedARGB(int[][] cover, int[][] argbImage, int threshold){
        assert cover != null && cover[0] != null;
        assert cover.length != 0 && cover[0].length != 0;
        assert argbImage != null && argbImage[0] != null;
        assert argbImage.length != 0 && argbImage[0].length != 0;
        int[][] newARGBimage = new int[argbImage.length][argbImage[0].length];
        newARGBimage = Image.toGray(newARGBimage);
        newARGBimage = embedGray(cover, newARGBimage, threshold);
        return newARGBimage;
    } //transformer image en gray, puis en binary, puis changer le lsb de cover puis return cover

    /**
     * Embed a Gray scaled image on another ARGB image (the cover)
     * @param cover Cover image
     * @param grayImage Embedded image
     * @param threshold threshold to use for binary conversion
     * @return ARGB image with the image embedded on the cover
     */
    public static int[][] embedGray(int[][] cover, int[][] grayImage, int threshold){
        assert cover.length >= grayImage.length && cover[0].length >= grayImage[0].length;
        assert cover.length > 0 && grayImage.length > 0 && grayImage[0].length > 0 && cover[0].length > 0;
        assert cover != null && grayImage != null;
        assert cover[0] != null && grayImage[0] != null;
        int[][] result;
        boolean[][] BW = new boolean[grayImage.length][grayImage[0].length];
        for(int i = 0; i < grayImage.length; i++){
            for(int j = 0; j < grayImage[0].length; j++){
                BW[i][j] = binary(grayImage[i][j], threshold);
            }
        }
        result = embedBW(cover, BW);
        return result;
    }

    /**
     * Embed a binary image on another ARGB image (the cover)
     * @param cover Cover image
     * @param load Embedded image
     * @return ARGB image with the image embedded on the cover
     */
    public static int[][] embedBW(int[][] cover, boolean[][] load){ //elle fonctionne (99% sur)
        assert cover.length >= load.length && cover[0].length >= load[0].length;
        assert load.length != 0 && load[0].length != 0;
        assert load != null && load[0] != null;
        assert cover != null && cover[0] != null;
        assert cover.length != 0 && cover[0].length != 0;
        int[][] newCover = new int[cover.length][cover[0].length];
        for (int i = 0; i < cover.length; i++){
            for (int j = 0; j < cover[i].length; j++){
                newCover[i][j] = cover[i][j];
            }
        }
        for(int k = 0; k < load.length; k++){
            for (int l = 0; l < load[0].length; l++){
                boolean lsb = Bit.getLSB(newCover[k][l]);
                if (lsb != load[k][l]){
                    newCover[k][l] = Bit.embedInLSB(newCover[k][l], load[k][l]);
                }
            }
        }
        return newCover;
    }

    // ============================================================================================
    // =================================== REVEALING METHODS ======================================
    // ============================================================================================

    /**
     * Reveal a binary image from a given image
     * @param image Image to reveal from
     * @return binary representation of the hidden image
     */
    public static boolean[][] revealBW(int[][] image) {
        assert image != null && image[0] != null && image.length != 0 && image[0].length != 0;
        boolean[][] load = new boolean[image.length][image[0].length];
        for (int i = 0; i < image.length; i++) {
            for (int j = 0; j < image[0].length; j++) {
                load[i][j] = Bit.getLSB(image[i][j]);
            }
        }
        return load;

    }

}
