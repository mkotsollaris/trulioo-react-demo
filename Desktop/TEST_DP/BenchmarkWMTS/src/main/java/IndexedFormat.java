import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * @deprecated This structure doesn't make any sense >.<.
 *
 * Created by Menelaos Kotsollaris on 2016-12-22.
 *
 * Class Explanation: TODO create interface of other Formats?
 */
public class IndexedFormat
{
    /**
     * Generates the Indexed Format Structure
     */
    public static void generateIndexedFormat(String tileDatasetPath,
                                             String indexedFormatPath)
            throws IOException
    {
        String[]
                directoriesNames =
                FileUtilities.getDirectoriesNames(tileDatasetPath);
        for(int i = 0; i < directoriesNames.length; i++)
        {
            String directoryName = directoriesNames[i];
            int level = Integer.parseInt(directoryName);
            String
                    indexedFormatPathLevel =
                    indexedFormatPath + File.separator + directoryName;
            FileUtilities.createDir(directoryName);
            createDirectories(indexedFormatPathLevel, level);
            moveTiles(tileDatasetPath, indexedFormatPathLevel, level);
        }
    }

    /**
     * Moves the tiles to the new Path.
     */
    public static void moveTiles(String tileDataset,
                                 String indexedFormatPathLevel, int level)
            throws IOException
    {
        int expectedColumn = Tile.computeColumnTotalNumber(level);
        for(int column = 0; column < expectedColumn; column++)
        {
            for(int row = 0; row < expectedColumn; row++)
            {
                String tileName = Tile.computeName(level, column, row);
                String
                        filePath =
                        tileDataset + File.separator + level + File.separator +
                                tileName;
                String
                        newFilePath =
                        indexedFormatPathLevel + File.separator + column +
                                File.separator + row + File.separator +
                                tileName;
                /*System.out.println("filepath: " + filePath);
                System.out.println("newFilePath: " + newFilePath);*/
                File file = new File(filePath);
                File newFile = new File(newFilePath);
                FileUtils.copyFile(file, newFile);
            }
        }
    }

    /**
     * Creates the new column/row directories.
     */
    public static void createDirectories(String indexedFormatPath, int level)
    {
        int expectedColumnNumber = Tile.computeColumnTotalNumber(level);
        for(int column = 0; column < expectedColumnNumber; column++)
        {
            String columnDir = indexedFormatPath + File.separator + column;
            FileUtilities.createDir(columnDir);
            for(int row = 0; row < expectedColumnNumber; row++)
            {
                String rowDir = columnDir + File.separator + row;
                FileUtilities.createDir(rowDir);
            }
        }
    }

}
