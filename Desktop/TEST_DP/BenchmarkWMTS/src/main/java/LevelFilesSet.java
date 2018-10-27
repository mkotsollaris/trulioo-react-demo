import java.io.IOException;

/**
 * A set of LevelFiles.
 *
 * @author mkotsollaris
 * @since 1.0
 */
final public class LevelFilesSet
{
    /** The levelFiles per each Level */
    private final LevelFiles[] levelFiles;

    /**
     * Private constructor.
     */
    private LevelFilesSet(Builder builder)
    {
        levelFiles = builder.levelFiles;
    }

    private LevelFiles getLevelFile(int level)
    {
        return levelFiles[level];
    }

    // Suppresses default constructor, ensuring non-instantiability.
    private LevelFilesSet()
    {
        throw new AssertionError();
    }

    /**
     * Returns a {@link Tile} object.
     *
     * @param level  the level.
     * @param column the column of the tile.
     * @param row    the row of the tile.
     */
    public Tile getTile(int level, int column, int row) throws IOException
    {
        return levelFiles[level].getTile(column, row);
    }

    /**
     * Provides the Builder pattern for the object initialization.
     */
    public static class Builder
    {
        /** The levelFiles per each Level */
        private LevelFiles[] levelFiles;

        /**
         * Implements the Builder Pattern for the object initialization.
         *
         * @param fileNames the {@link FileNames}.
         */
        public Builder(FileNames fileNames) throws IOException
        {
            levelFiles = LevelFiles.getInstance(fileNames);
        }

        /**
         * Initializes the object.
         */
        public LevelFilesSet build()
        {
            return new LevelFilesSet(this);
        }
    }
}