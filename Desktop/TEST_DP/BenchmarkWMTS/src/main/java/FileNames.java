import java.io.File;

/**
 * Contains all the file paths and the file names.
 *
 * @author mkotsollaris
 * @since 1.0
 */
final public class FileNames
{
    /** The tile dataset full directory path */
    private final String tileDataSetPath;
    /** The LookupFile path */
    private final String lookupFilePath;
    /** The TileData path */
    private final String tileDataFilePath;

    private FileNames()
    {
        throw new AssertionError();
    }

    private FileNames(Builder builder)
    {
        this.lookupFilePath = builder.lookupFilePath;
        this.tileDataFilePath = builder.tileDataFilePath;
        this.tileDataSetPath = builder.tileDataSetPath;
    }

    /**
     * Returns the {@link FileNames#tileDataSetPath}.
     */
    public String getTileDataSetPath()
    {
        return tileDataSetPath;
    }

    /**
     * Returns the {@link FileNames#lookupFilePath}.
     */
    public String getLookupFilePath()
    {
        return lookupFilePath;
    }

    /**
     * Returns the {@link FileNames#tileDataFilePath}.
     */
    public String getTileDataFilePath()
    {
        return tileDataFilePath;
    }

    /**
     * Returns the expected {@link Tile} name.
     *
     * @param level  the level of the {@link Tile}.
     * @param column the column of the {@link Tile}.
     * @param row    the row of the {@link Tile}.
     */
    public String getTileName(int level, int column, int row)
    {
        return getTileDataSetPath() +
                File.separator +
                level +
                File.separator +
                level +
                "_" +
                column +
                "_" +
                row +
                ".jpg";
    }

    /**
     * Provides the Builder pattern for the object initialization.
     */
    public static class Builder
    {
        /** The tile dataset full directory path */
        private final String tileDataSetPath;
        /** The LookupFile path */
        private final String lookupFilePath;
        /** The TileData path */
        private final String tileDataFilePath;

        /**
         * Implements the Builder Pattern for the object initialization.
         *
         * @param tileDataSetPath  the tile dataset's path.
         * @param lookupFilePath   the lookupFile's path.
         * @param tileDataFilePath the tileDataFile path.
         */
        public Builder(String tileDataSetPath, String lookupFilePath,
                       String tileDataFilePath)
        {
            this.tileDataSetPath = tileDataSetPath;
            this.tileDataFilePath = tileDataFilePath;
            this.lookupFilePath = lookupFilePath;
        }

        /**
         * Initializes the object.
         */
        public FileNames build()
        {
            return new FileNames(this);
        }
    }
}