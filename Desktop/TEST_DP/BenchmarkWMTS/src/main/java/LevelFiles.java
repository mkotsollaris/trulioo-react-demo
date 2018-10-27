import java.io.File;
import java.io.IOException;

/**
 * Contains the Lookup file and the TileData file and provides the necessary
 * methods to store and retrieve the tiles into the files.
 *
 * @author mkotsollaris
 * @since 1.0
 */
final class LevelFiles
{
    /** the number of the bytes that we use for the position pointer */
    final static int positionAllocationBytes = 8;
    /** the number of the bytes that we use for the size pointer */
    final static int sizeAllocationBytes = 4;
    /** the Lookup File */
    private final LookupFile lookupFile;
    /** the TileData File */
    private final TileDataFile tileDataFile;
    /** the level of the according tile dataset */
    private final int level;
    /** the path of the tile dataset in the particular level */
    private final String tileDataSetLevelPath;

    /**
     * Provides the Builder pattern for the object initialization.
     */
    public static class Builder
    {
        /** the level of the according tile dataset */
        private final int level;
        /** the file names information */
        private final FileNames fileNames;

        /**
         * Implements the Builder Pattern for the object initialization.
         *
         * @param fileNames the {@link FileNames}.
         * @param level     the {@link Tile} level.
         */
        Builder(FileNames fileNames, int level)
        {
            this.level = level;
            this.fileNames = fileNames;
        }

        /**
         * Initializes the object.
         */
        LevelFiles build() throws IOException
        {
            return new LevelFiles(this);
        }
    }

    //re-insures non instantiability
    LevelFiles()
    {
        throw new AssertionError();
    }

    /**
     * Private constructor.
     */
    private LevelFiles(Builder builder) throws IOException
    {
        boolean generate = false;
        level = builder.level;
        /* the file names information */
        FileNames fileNames = builder.fileNames;
        String lookupFileName = fileNames.getLookupFilePath() + level;
        String tileDataFileName = fileNames.getTileDataFilePath() + level;
        if(!(FileUtilities.exists(lookupFileName) &&
                FileUtilities.exists(tileDataFileName))) generate = true;
        tileDataSetLevelPath = builder.fileNames.getTileDataSetPath() +
                File.separator +
                level;
        lookupFile = new LookupFile.Builder(lookupFileName, level).build();
        tileDataFile =
                new TileDataFile.Builder(tileDataFileName, level).build();
        if(generate)
        {
            System.out.println(
                    "Generating Content for the level: " + level + " ...");
            generateContent();
        }
/*        else System.out.println(
                "LevelFiles already exists for the level: " + level + ".");*/
    }

    /**
     * Generates the LevelFiles (LookupFile and TileDataFile).
     */
    private void generateContent() throws IOException
    {
        int columnNumber = Tile.computeColumnTotalNumber(level);
        long expectedTileNumber = Tile.computeExpectedTileNumber(level);
        long tileCounter = 0;
        for(int column = 0; column < columnNumber; column++)
        {
            for(int row = 0; row < columnNumber; row++)
            {
                String fileName = tileDataSetLevelPath + File.separator +
                        Tile.computeName(level, column, row);
                Tile tile = Tile.getInstance(fileName);
                if(tile.isValid())
                {
                    write(tile);
                }
                tileCounter++;
                Utilities.showStatus(tileCounter, expectedTileNumber, 50000);
            }
        }
    }


    /**
     * Updates the tileData and LookUpFile based on the given tile.
     *
     * As explained in page 135. Note that there will be 2^zoomLevel rows and
     * columns. The tile generation algorithm follows the WGS84 standards.
     *
     * File format: First byte: 32 bit (8 byte) for the first number that shows
     * which byte to read from the File Dataset. Second byte: 16 bit (4 byte)
     * for the second number that shows what's the size of the particular tile
     * image.
     *
     * @param tile: the wanted tile
     */
    private void write(Tile tile) throws IOException
    {
        long
                lookupFilePosition =
                lookupFile.getFilePosition(tile.getColumn(), tile.getRow());
        long
                tileDataFileLength =
                FileUtilities.getFileLength(tileDataFile.getFilePath());
        lookupFile.writeLong(tileDataFileLength, lookupFilePosition);
        lookupFile.writeInt(
                tile.getData().length,
                lookupFilePosition + positionAllocationBytes);
        tileDataFile.write(tile.getData(), tileDataFileLength);
    }

    /**
     * Returns the tile by parsing both lookup and tile data files.
     *
     * @param column the column of the {@link Tile}
     * @param row    the row of the {@link Tile}
     *
     * @return : The corresponding tile object
     */
    public Tile getTile(int column, int row) throws IOException
    {
        long lookupFilePosition = lookupFile.getFilePosition(column, row);
        long tileDataPosition = lookupFile.readLong(lookupFilePosition);
        int
                tileSize =
                lookupFile
                        .readInt(lookupFilePosition + positionAllocationBytes);
        byte[] tileData = tileDataFile.getTile(tileDataPosition, tileSize);
        return new Tile.Builder(tileData, level, column, row).build();
    }

    /**
     * Returns the expected position of the tile based on the following
     * equation:
     *
     * position = column * (2^level+row)*(12), where 12 the total number of the
     * allocation byte per record.
     *
     * @param level  the level of the {@link Tile}
     * @param column the column of the {@link Tile}
     * @param row    the row of the {@link Tile}
     */
    long getPosition(int level, int column, int row)
    {
        return ((column * (int) (Math.pow(2, level))) + row) *
                (positionAllocationBytes + sizeAllocationBytes);
    }

    /**
     * Returns the expected file length depending on the given level. The
     * returning number is given by the following equation: wanted_number =
     * (2^level)*2^level*12
     *
     * @param level the level of the {@link Tile}
     *
     * @return the expected file length
     */
    static long getExpectedFileLength(int level)
    {
        return (int) Math.pow(2, level) *
                (int) Math.pow(2, level) *
                12;
    }

    /**
     * Generates the Level Files for a particular level.
     *
     * @param level the level of the LevelFiles that will be generated
     */
    static LevelFiles getInstance(FileNames fileNames, int level)
            throws IOException
    {
        return new LevelFiles.Builder(fileNames, level).build();
    }

    /**
     * Generates the level files for all the possible levels.
     *
     * @param fileNames the {@link FileNames}.
     */
    static LevelFiles[] getInstance(FileNames fileNames) throws IOException
    {
        int
                totalTileDataSetDirectoryNumber =
                FileUtilities.getDirectoriesNames(
                        fileNames.getTileDataSetPath()).length;
        LevelFiles[]
                levelFiles =
                new LevelFiles[totalTileDataSetDirectoryNumber + 1];
        for(int i = 0; i < totalTileDataSetDirectoryNumber; i++)
        {
            levelFiles[i] = new LevelFiles.Builder(fileNames, i).build();
        }
        return levelFiles;
    }


    @Override public String toString()
    {
        return "LeveFiles for the level " + level;
    }
}