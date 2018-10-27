import java.io.IOException;

/**
 * The file containing the pointer to the TileDataFile and the size of the
 * tile.
 *
 * @author mkotsollaris
 */
final class LookupFile
{
    /** the level of the tiledata file */
    private int level;
    /** the filepath of where the LookupFile is being allocated */
    private final String filePath;

    /**
     * Creates an Empty lookup file or retrieves an existing one.
     */
    private LookupFile(Builder builder) throws IOException
    {
        level = builder.level;
        filePath = builder.filePath;
        if(!FileUtilities.exists(filePath))
        {
            FileUtilities.createFile(filePath,
                                     LevelFiles.getExpectedFileLength(level));
        }
    }

    // Suppresses default constructor, ensuring non-instantiability.
    private LookupFile()
    {
        throw new AssertionError();
    }

    /**
     * Reads a long variable.
     *
     * @param position the byte of the file.
     */
    long readLong(long position) throws IOException
    {
        return FileUtilities.readLongFromFile(filePath, position);
    }

    /**
     * Provides the Builder pattern for the object initialization.
     */
    static class Builder
    {
        /** the level of the tile dataset */
        private final int level;
        /** the filepath of where the LookupFile is being allocated */
        private final String filePath;

        /**
         * Implements the Builder Pattern for the object initialization.
         *
         * @param filePath the file path
         * @param level    the level of of the {@link Tile}
         */
        Builder(String filePath, int level)
        {
            this.filePath = filePath;
            this.level = level;
        }

        /**
         * Initializes the object.
         */
        LookupFile build() throws IOException
        {
            return new LookupFile(this);
        }
    }

    /**
     * Reads an Integer from the file.
     *
     * @param position the byte of the file.
     */
    int readInt(long position) throws IOException
    {
        return FileUtilities.readIntFromFile(filePath, position);
    }

    /** gets the file name */
    String getFilePath()
    {
        return filePath;
    }

    /**
     * Calculates the position of the lookup file based on the level, row and
     * column of the tile.
     *
     * @param row:    the row of the wanted tile
     * @param column: the column of the wanted tile
     *
     * @return : the position (in bytes) that the requested tile should be found
     * in the file
     */
    long getFilePosition(int column, int row)
    {
        if(Math.pow(2, level) < column || Math.pow(2, level) < row)
            throw new IllegalArgumentException(
                    "level: " + level + ", column: " + column + ", row: " +
                            row);
        return ((column * (int) (Math.pow(2, level))) + row) *
                (LevelFiles.positionAllocationBytes +
                        LevelFiles.sizeAllocationBytes);
    }

    @Override public String toString()
    {
        return "LookupFile for the level: " + level + " with the filepath:" +
                filePath;
    }

    /**
     * Writes a long variable to the file.
     *
     * @param tileDataFileLength the length of the {@link TileDataFile}
     * @param lookupFilePosition the position (byte) of the {@link LookupFile}
     */
    void writeLong(long tileDataFileLength, long lookupFilePosition)
            throws IOException
    {
        FileUtilities.writeLongToFile(filePath, tileDataFileLength,
                                      lookupFilePosition);
    }

    /**
     * Writes a long variable to the file.
     *
     * @param tileLength the length of the {@link Tile}
     * @param position   the position (byte) of the file
     */
    void writeInt(int tileLength, long position) throws IOException
    {
        FileUtilities.writeIntToFile(filePath, tileLength, position);
    }
}