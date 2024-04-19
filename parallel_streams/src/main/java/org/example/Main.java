package org.example;

import org.apache.commons.lang3.tuple.Pair;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    public static int transformPixel(int RGB)
    {
        Color color = new Color(RGB);
        int red = color.getRed();
        int blue = color.getBlue();
        int green = color.getGreen();
        Color outColor = new Color(red, blue, green);
        int outRgb = outColor.getRGB();
        return outRgb;
    }

    public static void non_parallel(List<Path>files)
    {
        files.stream().map(path -> {
            try {
                String name = String.valueOf(path.getFileName());
                BufferedImage image = ImageIO.read(path.toFile());
                Pair<String, BufferedImage> pair = Pair.of(name, image);
                return pair;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).map(oldPair -> {
            BufferedImage oldOne = oldPair.getRight();
            BufferedImage newOne = new BufferedImage(oldOne.getWidth(), oldOne.getHeight(), oldOne.getType());
            for(int i=0;i<newOne.getWidth();i++){
                for(int j=0;j< newOne.getHeight();j++){
                    int rgb = oldOne.getRGB(i, j);

                    newOne.setRGB(i, j, transformPixel(rgb));
                }
            }
            Pair newPair = Pair.of(oldPair.getLeft(), newOne);
            return newPair;
        }).forEach(pair -> {
            File outputFile = new File("./src/main/java/result/non_parallel/"+pair.getLeft());
            try {
                ImageIO.write((BufferedImage) pair.getRight(), "jpg", outputFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

    }

    public static void parallel(List<Path>files, int thread_number) throws ExecutionException, InterruptedException {
        ForkJoinPool customThreadPool = new ForkJoinPool(thread_number);
        customThreadPool.submit( () ->
            files.parallelStream().map(path -> {
                try {
                    String name = String.valueOf(path.getFileName());
                    BufferedImage image = ImageIO.read(path.toFile());
                    Pair<String, BufferedImage> pair = Pair.of(name, image);
                    return pair;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }).map(oldPair -> {
                BufferedImage oldOne = oldPair.getRight();
                BufferedImage newOne = new BufferedImage(oldOne.getWidth(), oldOne.getHeight(), oldOne.getType());
                for(int i=0;i<newOne.getWidth();i++){
                    for(int j=0;j< newOne.getHeight();j++){
                        int rgb = oldOne.getRGB(i, j);

                        newOne.setRGB(i, j, transformPixel(rgb));
                    }
                }
                Pair newPair = Pair.of(oldPair.getLeft(), newOne);
                return newPair;
            }).forEach(pair -> {
                File outputFile = new File("./src/main/java/result/parallel/"+pair.getLeft());
                try {
                    ImageIO.write((BufferedImage) pair.getRight(), "jpg", outputFile);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            })
        ).get();
    }


    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        int thread_number = Integer.parseInt(args[0]);
        List<Path> files;
        Path source = Path.of("./src/main/java/img/");
        Stream<Path> stream = Files.list(source);
        files = stream.collect(Collectors.toList());
        System.out.println("File paths that will be processed:");
        System.out.println(files);
        System.out.println();
        System.out.println("Processing files on a single thread...");
        long time = System.currentTimeMillis();
        non_parallel(files);
        System.out.println("Processing finished after "+(System.currentTimeMillis()-time)+" milliseconds!");
        System.out.println();
        System.out.println("Processing files on "+thread_number+" threads...");
        time = System.currentTimeMillis();
        parallel(files,thread_number);
        System.out.println("Processing finished after "+(System.currentTimeMillis()-time)+" milliseconds!");
    }
}
