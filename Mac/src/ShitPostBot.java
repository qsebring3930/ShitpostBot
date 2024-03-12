import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class ShitPostBot {

    private static Picture overlay;
    private static double aspectratio;
    private static int[] overcoordslist = new int[3];
    private static int[] coordslist = new int[3];
    private static Random random = new Random();
    private static File filesourcesize;
    private static File filetemplate;
    private static File filesource;
    private static File coordinates;
    private static Picture sourcesize;
    private static Picture template;
    private static Picture source;
    private static int filetemplatenum;
    private static int filesourcenum;
    private static int newsourcesize;
    private static BufferedImage result;
    private static Picture shitpost;
    private static int outputfilenum = 0;

    public static Random getRandom() {
        return random;
    }

    public static void randomShit(){
        filetemplatenum = random.nextInt(65) + 1;
        filesourcenum = random.nextInt(607) + 1;
        filesourcesize = new File("Templates/Template" + filetemplatenum + "/TemplateSourceSize.jpg");
        filetemplate = new File("Templates/Template" + filetemplatenum + "/Template.jpg");
        coordinates = new File("Templates/Template" + filetemplatenum + "/TemplateCoords.txt");
        filesource = new File("Sources/Source" + filesourcenum + ".jpg");
        // https://stackoverflow.com/questions/12889275/java-and-windows-error-illegal-escape-character/12889283
        sourcesize = new Picture(filesourcesize);
        template = new Picture(filetemplate);
        source = new Picture(filesource);
    }

    public static double getAspectratio() {
        double aspectratio;
        if(source.height() > source.width()) {
            aspectratio = ((double) (source.height()) / (source.width()));
        } else {
            aspectratio = ((double) (source.width()) / (source.height()));
        }
        return aspectratio;
    }

    public static BufferedImage layer(Image source, Image template, int sourcex, int sourcey) {
        BufferedImage result = new BufferedImage(template.getWidth(null), template.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics resultgraphics = result.getGraphics();
        resultgraphics.drawImage(template, 0, 0, null);
        resultgraphics.drawImage(source, sourcex, sourcey, null);
        if(coordslist[2] > 0) {
            overlay = new Picture("Overlay.png");
        } else {
            overlay = null;
        }
        if(overlay != null) {
            resultgraphics.drawImage(overlay.getImage(), overcoordslist[0], overcoordslist[1], null);
        }
        return result;
    }

    public static void readfile() throws Exception{
        try (Scanner sc = new Scanner(coordinates)) {
            for (int i = 0; sc.hasNextLine(); i++) {
                coordslist[i] = ((int) Math.round(Double.parseDouble(sc.nextLine())));
                int temp = coordslist[i];
                overcoordslist[i] = temp;
                // https://www.journaldev.com/18392/java-convert-string-to-double
            }
        }
    }

    public static void centerpic() {
        if(sourcesize.width() > sourcesize.height()) {
            int sourcesizelength = sourcesize.width();
            int displacement = (sourcesizelength - newsourcesize) / 2;
            coordslist[0] += displacement;
        } else {
            int sourcesizelength = sourcesize.height();
            int displacement = (sourcesizelength - newsourcesize) / 2;
            coordslist[1] += displacement;
        }
    }

    public static void resizeSource() {
        aspectratio = getAspectratio();
        if(sourcesize.height() > sourcesize.width()) {
            newsourcesize = ((int) Math.round(sourcesize.height() * aspectratio));
            source.resize(sourcesize.width(), sourcesize.height());
        }
        if(sourcesize.height() < sourcesize.width()) {
            newsourcesize = ((int) Math.round(sourcesize.width() * aspectratio));
            source.resize(sourcesize.width(), sourcesize.height());
        }
    }

    public static void makeShitPost() throws IOException {
        result = layer(source.getImage(), template.getImage(), coordslist[0], coordslist[1]);
        shitpost = new Picture(result);
        shitpost.show();
        saveOutput();
    }

    public static void saveOutput() throws IOException {
        RenderedImage temp = (RenderedImage) shitpost.getImage();
        File output = new File("OutputImages" + File.separator + "" + outputfilenum + ".jpg");
        output.createNewFile();
        ImageIO.write(temp, "png", output);
        outputfilenum++;
    }

    public static void main(String[] args) throws Exception{
        System.out.println("Welcome to ShitPostBot! The only place to get absolutely original memes! Any images you like make sure to take a snippit of them or you might never see them again! >:)");
        try (Scanner scanner = new Scanner(System.in)) {
            Boolean running = true;
            while(running) {
                System.out.println("Please press (r) for new image or (q) to quit the program.");
                String reply = scanner.nextLine();
                if (reply.equals("r")) {
                    randomShit();
                    resizeSource();
                    readfile();
                    //centerpic();
                    makeShitPost();
                } else if (reply.equals("q")) {
                    running = false;
                } else {
                    System.out.println("Try Again.");
                }
            }
        }
    }

}