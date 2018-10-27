import org.apache.commons.io.FileUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

/**
 * Contains static methods used within the framework.
 *
 * @author mkotsollaris
 * @since 1.0
 */
final class Utilities
{
    private Utilities()
    {
        throw new AssertionError();
    }

    private static BufferedImage byteArrayToImage(byte[] bytes)
    {
        BufferedImage bufferedImage = null;
        try
        {
            InputStream inputStream = new ByteArrayInputStream(bytes);
            bufferedImage = ImageIO.read(inputStream);
        }
        catch(IOException ex)
        {
            System.out.println(ex.getMessage());
        }
        return bufferedImage;
    }

    /**
     * Converts the Image Block Structure (32x32 tiles for each folder) to the
     * accepted structure. After the tiles are moved to the parent directory;
     * the subdirectories get removed.
     *
     * TODO Test
     *
     * @param dataSetPath the location of the data set
     */
    static void convertImageBlockToBasic(String dataSetPath) throws IOException
    {
        String[] directories = FileUtilities.getDirectoriesNames(dataSetPath);
        for(String directory : directories)
        {
            String levelDirectory = dataSetPath + File.separator + directory;
            System.out.println(" Directory Level:  " + levelDirectory);
            String[]
                    subDirectories =
                    FileUtilities.getDirectoriesNames(dataSetPath +
                                                              File.separator +
                                                              directory);
            for(String subDirectory : subDirectories)
            {
                String subDirectoryPath = levelDirectory +
                        File.separator +
                        subDirectory;
                File[] files = FileUtilities.getFiles(levelDirectory +
                                                              File.separator +
                                                              subDirectory);
                System.out.println("Length of moving files: " +
                                           files.length +
                                           "src/test");
                for(File file : files)
                {
                    Files.move(Paths.get(file.getAbsolutePath()),
                               Paths.get(levelDirectory +
                                                 File.separator +
                                                 file.getName()));
                }
                FileUtilities.deleteDirectory(subDirectoryPath);
            }
        }
    }

    static void printTile(Tile tile)
    {
        BufferedImage img = byteArrayToImage(tile.getData());
        JFrame frame = new JFrame();
        frame.getContentPane().setLayout(new FlowLayout());
        frame.getContentPane().add(new JLabel(new ImageIcon(img)));
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Measures the Generation time
     */
    static void measureGenerationTime(FileNames fileNames) throws IOException
    {
        System.out.println("Measuring LevelFilesSet Generation time: ");
        for(int i = 13; i <= 13; i++)
        {
            long time1 = System.nanoTime();
            LevelFiles levelFiles = LevelFiles.getInstance(fileNames, i);
            long
                    result =
                    TimeUnit.MILLISECONDS.convert(System.nanoTime() - time1,
                                                  TimeUnit.NANOSECONDS);
            float
                    averageGenerationTime =
                    (float) result / FileUtilities.getFiles(
                            fileNames.getTileDataSetPath() + File.separator +
                                    i).length;
            System.out.println("MilliSeconds passed for  " + i +
                                       " creation: " +
                                       result);
            System.out.println("Average MilliSeconds passed for  " + i +
                                       " creation: " +
                                       averageGenerationTime);
        }
    }

    /**
     * Adds any missing tiles.
     *
     * @param tileDataSetPath the tileDataSetPath file path
     */
    static void fulfilTileDataset(String tileDataSetPath) throws IOException
    {
        for(int level = 1; level < 9; level++)
        {
            String directoryPath = tileDataSetPath + File.separator + level +
                    File.separator;
            System.out.println(directoryPath);
            int counter = 0;
            long expectedTileNumber = Tile.computeExpectedTileNumber(level);
            int columnTotalNumber = (int) Math.pow(2, level); // same as row
            for(int column = 0; column < columnTotalNumber; column++)
            {
                for(int row = 0; row < columnTotalNumber; row++)
                {
                    String
                            tilePath =
                            directoryPath + level + "_" + column + "_" +
                                    row + ".jpg";
                    counter++;
                    if(!FileUtilities.exists(tilePath))
                    {
                        Utilities.addTile(tilePath);
                    }
                    showStatus(counter, expectedTileNumber, 50000);
                }
            }
        }
    }

    /**
     * Prints the writting status each time a specific number of tiles
     * (threshold) is written.
     *
     * @param counter            the counter of the tiles written.
     * @param expectedTileNumber the expected number of the tiles.
     * @param threshold          The number of the status update.
     */
    static boolean showStatus(long counter, long expectedTileNumber,
                              int threshold)
    {
        if(counter == 0 || counter < threshold || (counter % threshold != 0))
            return false;
        System.out.println(counter + ", " + expectedTileNumber + " = " +
                                   (counter / expectedTileNumber));
        float progress = (float) counter / expectedTileNumber;
        System.out.printf("Written Tiles: %d, Progress: %f %%\n", counter,
                          progress);
        return true;
    }

    private static final File
            standardFile =
            new File(MyFileNames.standardTilePath.getFileName());

    /**
     * @param tilePath the tile's new path (including name)
     */
    private static void addTile(String tilePath) throws IOException
    {
        File newTile = new File(tilePath);
        FileUtils.copyFile(standardFile, newTile);
    }

    /**
     * @return the tile in the middle of the list.
     * @deprecated
     */
    private static Tile getMidElement(TreeSet<Tile> tileTreeSet)
    {
        if(tileTreeSet.size() < 2) return tileTreeSet.first();
        int wantedMidIndex = tileTreeSet.size() / 2;
        Iterator<Tile> iterator = tileTreeSet.iterator();
        int counter = 1;
        Tile current = tileTreeSet.first();
        while(iterator.hasNext())
        {
            if(counter == wantedMidIndex)
            {
                return current;
            }
            current = iterator.next();
            counter++;
        }
        throw new IllegalArgumentException(
                "Could not retrieve the mid element of the list.");
    }

    /**
     * Measures and outputs the LevelFilesSet's Get time.
     */
    public static void measureLevelFilesSetGetTime(FileNames fileNames)
            throws IOException
    {
        System.out.println("Measuring LevelFilesSet GET time. ");
        for(int level = 0; level <= 13; level++)
        {
            LevelFiles levelFiles = LevelFiles.getInstance(fileNames, level);
            long time1 = System.nanoTime();
            int rowNum = (int) Math.pow(2, level);
            for(int i = 0; i < rowNum; i++)
            {
                for(int j = 0; j < rowNum; j++)
                {
                    Tile tile = levelFiles.getTile(i, j);
                }
            }
            long
                    result =
                    TimeUnit.MILLISECONDS.convert(System.nanoTime() - time1,
                                                  TimeUnit.NANOSECONDS);
            float
                    averageGenerationTime =
                    (float) result / FileUtilities
                            .getFiles(fileNames.getTileDataSetPath() +
                                              File.separator +
                                              level).length;
            System.out.println("MilliSeconds passed for  " + level +
                                       " : " +
                                       result);
            System.out.println("Average MilliSeconds passed for  " + level +
                                       " : " +
                                       averageGenerationTime);
        }
    }

    public static void measureFileOpenTime(FileNames fileNames)
            throws IOException
    {
        LevelFilesSet
                levelFilesSet =
                new LevelFilesSet.Builder(fileNames).build();
        for(int level = 0; level <= 13; level++)
        {
            long time1 = System.nanoTime();
            levelFilesSet.getTile(0, 0, 0);
            long time2 = System.nanoTime();
            long
                    result =
                    TimeUnit.NANOSECONDS
                            .convert(time2 - time1, TimeUnit.NANOSECONDS);
            System.out.println("Time for level: " + level + " = " + result);
        }

    }

    public static void measureFileSystemGetTime(FileNames fileNames)
            throws IOException
    {
        System.out.println("Measuring File System GET time. ");
        for(int level = 0; level <= 13; level++)
        {
            long time1 = System.nanoTime();
            int rowNum = (int) Math.pow(2, level);
            for(int i = 0; i < rowNum; i++)
            {
                for(int j = 0; j < rowNum; j++)
                {
                    String filePath = fileNames.getTileName(level, i, j);
                    //System.out.println(filePath);
                    Tile tile = Tile.getInstance(filePath);
                }
            }
            long
                    result =
                    TimeUnit.MILLISECONDS.convert(System.nanoTime() - time1,
                                                  TimeUnit.NANOSECONDS);
            float
                    averageGenerationTime =
                    (float) result / FileUtilities
                            .getFiles(fileNames.getTileDataSetPath() +
                                              File.separator +
                                              level).length;
            System.out.println("MilliSeconds passed for  " + level +
                                       " creation: " +
                                       result);
            System.out.println("Average MilliSeconds passed for  " + level +
                                       " creation: " +
                                       averageGenerationTime);
        }
    }

    public static void measureImageBlockGetTime(String imageBlockPath)
            throws IOException
    {
        System.out.println("Measuring ImageBlock GET time.");
        for(int level = 0; level <= 9; level++)
        {
            ImageBlock.getTile(imageBlockPath, level, 0, 0);//dummy load!
            long time1 = System.nanoTime();
            int rowNum = (int) Math.pow(2, level);
            for(int column = 0; column < rowNum; column++)
            {
                for(int row = 0; row < rowNum; row++)
                {
                    Tile
                            tile =
                            ImageBlock.getTile(imageBlockPath, level, column,
                                               row);
                }
            }
            long
                    result =
                    TimeUnit.MILLISECONDS.convert(System.nanoTime() - time1,
                                                  TimeUnit.NANOSECONDS);
            float
                    averageGenerationTime =
                    (float) result / FileUtilities.getFiles(imageBlockPath +
                                                                    File.separator +
                                                                    level).length;
            System.out.println("MilliSeconds passed for  " + level +
                                       " GET: " +
                                       result);
            System.out.println("Average MilliSeconds passed for  " + level +
                                       " creation: " +
                                       averageGenerationTime);
        }
    }
}