import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Represents the Image Block structure.
 *
 * @author mkotsollaris
 * @since 1.0
 */
class ImageBlock
{
    /**
     * Generates the Image Block structure.
     *
     * @param tileDataPath          the dataset in the 'Simple' structure's file
     *                              path
     * @param imageBlockDataSetPath the Image Block's file path
     */
    static void generateImageBlock(String tileDataPath,
                                   String imageBlockDataSetPath)
            throws IOException
    {
        String[]
                directoriesNames =
                FileUtilities.getDirectoriesNames(tileDataPath);
        for(String directoryName : directoriesNames)
        {
            System.out.println(directoryName);
            int level = Integer.parseInt(directoryName);
            String
                    imageBlockLevelPath =
                    imageBlockDataSetPath + File.separator + level;
            createDirectories(imageBlockLevelPath, level);
            moveTiles(tileDataPath, imageBlockDataSetPath, level);
        }
    }

    private static void moveTiles(String tileDataPath,
                                  String imageBlockDataSetPath, int level)
            throws IOException
    {
        String tileDataSetLevelPath = tileDataPath + File.separator + level;
        int columnNumber = Tile.computeColumnTotalNumber(level);
        for(int column = 0; column < columnNumber; column++)
        {
            for(int row = 0; row < columnNumber; row++)
            {
                String fileName = tileDataSetLevelPath + File.separator +
                        Tile.computeName(level, column, row);
                Tile tile = Tile.getInstance(fileName);
                classify(tile, imageBlockDataSetPath);
            }
        }
    }

    /**
     * Creates the Directories for the particular level.
     *
     * @param imageBlockLevelPath the imageBlock path for the particular level.
     */
    private static void createDirectories(String imageBlockLevelPath, int level)
    {
        FileUtilities.createDir(imageBlockLevelPath);
        createSubDirectories(imageBlockLevelPath, level);
    }

    /**
     * Classifies the tile to the particular sub directory.
     */
    private static void classify(Tile tile, String imageBlockDataSetPath)
            throws IOException
    {
        File filePath = new File(tile.getFilePath());
        String newFileName = getNewFileName(tile, imageBlockDataSetPath);
        File newTile = new File(newFileName);
        FileUtils.copyFile(filePath, newTile);

    }

    /**
     * Constructs thew new file name based on the ImageBlock structure logic.
     *
     * @param tile the tile image
     */
    private static String getNewFileName(Tile tile,
                                         String imageBlockDataSetPath)
    {
        String newFileName = imageBlockDataSetPath + File.separator +
                tile.getLevel() +
                File.separator;
        if(tile.getLevel() <= 4)
        {
            newFileName += tile.computeFileName();
        }
        else
        {
            int column = tile.getColumn() / 32;
            int row = tile.getRow() / 32;
            row *= 32;
            column *= 32;
            newFileName += tile.getLevel() + "_" +
                    column + "_" + row + File.separator +
                    tile.computeFileName();
        }
        return newFileName;
    }

    /**
     * Creates the sub directories of the Image Block.
     *
     * @param imageBlockLevelPath the image block dataset's path
     * @param level               the level of the tiles
     */
    private static void createSubDirectories(String imageBlockLevelPath,
                                             int level)
    {
        long expectedTileNumber = (int) Math.pow(4, level);
        if(expectedTileNumber <= 1024) return;
        long subDirNumber = expectedTileNumber / 1024;
        int maxCol = (int) Math.pow(2, level);
        System.out.println("subDirs: " + subDirNumber);
        for(int columnCounter = 0; columnCounter < maxCol; columnCounter += 32)
        {
            for(int rowCounter = 0; rowCounter < maxCol; rowCounter += 32)
            {
                String
                        directoryPath =
                        imageBlockLevelPath + File.separator + level + "_" +
                                columnCounter + "_" + rowCounter;
                FileUtilities.createDir(directoryPath);
            }
        }
    }

    public static Tile getTile(String imageBlockLevelPath, int level,
                               int column, int row) throws IOException
    {
        String tilePath = tilePath(imageBlockLevelPath, level, column, row);
        return Tile.getInstance(tilePath);
    }

    /**
     * fixme duplicated code; see getNewFileName
     */
    private static String tilePath(String imageBlockLevelPath, int level,
                                   int row, int column)
    {
        if(level <= 4)
        {
            return imageBlockLevelPath + File.separator + level +
                    File.separator + Tile.computeName(level, column, row);
        }
        else
        {
            int dirColumn = column / 32;
            int dirRow = row / 32;
            dirRow *= 32;
            dirColumn *= 32;
            return imageBlockLevelPath + File.separator + level +
                    File.separator + level + "_" +
                    dirColumn + "_" + dirRow + File.separator +
                    Tile.computeName(level, column, row);
        }
    }
}