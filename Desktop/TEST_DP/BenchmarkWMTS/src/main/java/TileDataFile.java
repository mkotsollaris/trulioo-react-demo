import java.io.IOException;

/**
 * Class Explanation: Represents a tileDataFile where the tiles' bytes are being
 * saved.
 *
 * @author mkotsollaris
 * @since 1.0
 */
final class TileDataFile
{
    /** the level of the tiledata file */
    private int level;
    /** the filename */
    private final String filePath;

    // Suppresses default constructor, ensuring non-instantiability.
    private TileDataFile()
    {
        throw new AssertionError();
    }

    /**
     * Creates an empty Tile Data File or Retrieves an existing one. The new
     * object is created only if the filename does not exist.
     *
     * @param builder the builder object
     */
    private TileDataFile(Builder builder) throws IOException
    {
        level = builder.level;
        filePath = builder.filePath;
        //delete any previous existing file with the same name
        if(!FileUtilities.exists(filePath))
        {
            FileUtilities.deleteFile(filePath);
            FileUtilities.createFile(filePath);
        }
    }

    /**
     * Returns the {@link Tile} from a given position within the file.
     *
     * @param tileDataPos the {@link TileDataFile} position
     * @param tileSize    the {@link Tile} size
     */
    byte[] getTile(long tileDataPos, int tileSize) throws IOException
    {
        return FileUtilities.readFromFile(filePath, tileDataPos, tileSize);
    }

    /**
     * Provides the Builder pattern for the object initialization.
     *
     * @author mkotsollaris
     * @since 1.0
     */
    static class Builder
    {
        /** the level of the tiledata file */
        private final int level;
        /** the filename */
        private final String filePath;

        Builder(String filePath, int level)
        {
            this.filePath = filePath;
            this.level = level;
        }

        /**
         * Initializes the object.
         */
        TileDataFile build() throws IOException
        {
            return new TileDataFile(this);
        }
    }

    /**
     * Returns the filepath
     */
    String getFilePath()
    {
        return filePath;
    }

    /**
     * Writes the data of the tile.
     *
     * @param tileData the data of the tile
     * @param position the position that the data will be written
     */
    void write(byte[] tileData, long position) throws IOException
    {
        FileUtilities.writeToFile(filePath, tileData, position);
    }

    @Override public String toString()
    {
        return "LookupFile for the level: " + level + " with the filepath:" +
                filePath;
    }
}