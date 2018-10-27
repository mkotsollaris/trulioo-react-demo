import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.IOException;
import java.util.Comparator;

/**
 * Represents a Tile Image (file) that holds the name, level, column, row, data,
 * and the file type.
 *
 * The tile name in the format X_Y_Z where X = Level, Y = Column, Z = Row.
 *
 * @author mkotsollaris
 * @since 1.0
 */
public final class Tile
{
    /** The Level of the tile (e.g. 0 for the 0_1_2 tile) */
    private final int level;
    /** The Column of the tile (e.g. 1 for the 0_1_2 tile) */
    private final int column;
    /** The Row of the tile (e.g. 2 for the 0_1_2 tile) */
    private final int row;
    /** The data of the tile (bytes) */
    private final byte[] data;
    /** The File path */
    private final String filePath;

    /** @return the {@link Tile#level} */
    public int getLevel()
    {
        return level;
    }

    /** @return the {@link Tile#column} */
    public int getColumn()
    {
        return column;
    }

    /** @return the {@link Tile#row} */
    public int getRow()
    {
        return row;
    }

    /** @return the {@link Tile#data} */
    public byte[] getData()
    {
        return data;
    }

    /** @return the fileName */
    public String computeFileName()
    {
        return getLevel() + "_" + getColumn() + "_" + getRow() + ".jpg";
    }

    /**
     * Private constructor for internal initialization.
     */
    private Tile(Builder builder)
    {
        data = builder.data;
        level = builder.level;
        column = builder.column;
        row = builder.row;
        filePath = builder.filePath;
    }


    /**
     * Initializes a {@link Tile} object.
     *
     * @param filePath the file path of the particular tile
     *
     * @return the {@link Tile}
     * @throws IllegalArgumentException if the filepath is not valid
     */
    public static Tile getInstance(String filePath)
            throws IllegalStateException, IOException
    {
        try
        {
            String name = computeName(filePath);
            return new Builder(getData(filePath), computeLevel(name),
                               computeColumn(name), computeRow(name))
                    .filePath(filePath).build();
        }
        catch(Exception e)
        {
            //TODO what about returning null?
            throw new IllegalArgumentException(
                    "Not A valid tile path: " + filePath);
        }
    }

    /** @return the {@link Tile#filePath} */
    public String getFilePath()
    {
        return filePath;
    }

    /**
     * Computers the number of the expected tiles per zoom level. Assumes that
     * there are no missing tiles.
     *
     * @return long the number of the expected tiles
     */
    static long computeExpectedTileNumber(int level)
    {
        return (long) Math.pow(4, level);
    }

    /**
     * Provides the Builder pattern for the object initialization.
     *
     * @author mkotsollaris
     * @since 1.0
     */
    static class Builder
    {
        /** The Level of the tile (e.g. 0 for the 0_1_2 tile) */
        private final int level;
        /** The Column of the tile (e.g. 1 for the 0_1_2 tile) */
        private final int column;
        /** The Row of the tile (e.g. 2 for the 0_1_2 tile) */
        private final int row;
        /** The data of the tile (bytes) */
        private final byte[] data;
        /** The File path */
        private String filePath;

        /**
         * Implements the Builder Pattern for the object initialization.
         *
         * @param data   the data to be written in the file
         * @param level  the level of the {@link Tile}
         * @param column the column of the {@link Tile}
         * @param row    the row of the {@link Tile}
         */
        Builder(byte[] data, int level, int column, int row)
        {
            this.data = data;
            this.level = level;
            this.column = column;
            this.row = row;
        }

        /**
         * Implements the Builder Pattern for the object initialization.
         *
         * @param filePath the file path
         */
        Builder filePath(String filePath)
        {
            this.filePath = filePath;
            return this;
        }

        /**
         * Initializes the object.
         */
        Tile build()
        {
            return new Tile(this);
        }

    }

    /**
     * Returns the name of the file. For instance, given the path:
     * /Users/mkotsollaris/Desktop/tile_dataset/2/2_0_1.jpg will return
     * '2_0_1.jpg'.
     *
     * FIXME won't work for mac, linux etc. (path hardcoded)! Test
     *
     * @param filePath the filePath (e.g.: /Users/mkotsollaris/Desktop/tile_dataset/2/2_0_1.jpg)
     */
    static String computeName(String filePath)
    {
        String[]
                strArray =
                (System.getProperty("os.name")
                        .contains(MyFileNames.Windows.getFileName())) ?
                        filePath.split("\\\\") : filePath.split("/");

        return strArray[strArray.length - 1].split("\\.")[0];
    }

    static String computeName(int level, int column, int row)
    {
        return level + "_" + column + "_" + row + ".jpg";
    }

    /**
     * Returns the byte[] array containing the image information.
     *
     * @param filePath the filePath (e.g.: /Users/mkotsollaris/Desktop/tile_dataset/2/2_0_1.jpg)
     */
    private static byte[] getData(String filePath) throws IOException
    {
        return FileUtilities.readFromFile(filePath);
    }

    /**
     * Returns if the file has the proper format.
     */
    boolean isValid()
    {
        return level != -1;
    }

    /**
     * Returns the level of the string.
     *
     * @param fileName: the tile name in the format X_Y_Z where X = Level, Y =
     *                  Column Z = Row. Example Input: 3_5_1.jpg then the
     *                  function will return 3.
     */
    static int computeLevel(String fileName)
    {
        try
        {
            return Integer.parseInt(fileName.split("_")[0]);
        }
        catch(Exception e)
        {
            return -1;
        }
    }

    /**
     * Returns the column of the string.
     *
     * @param fileName: Example Input: 3_5_1.jpg then the function will return
     *                  5.
     */
    static int computeColumn(String fileName)
    {
        return Integer.parseInt(fileName.split("_")[1]);
    }

    /**
     * Returns the row of the string. For instance if the input is
     * "/Users/mkotsollaris/Desktop/tile_dataset/2/2_0_1.jpg" the the output
     * will be 0.
     *
     * @param fileName: the tile filePath in the format X_Y_Z where X = Level, Y
     *                  = Column Z = Row. Example Input: 3_5_1.jpg then the
     *                  function will return 1.
     */
    static int computeRow(String fileName)
    {
        return Integer.parseInt((fileName.split("_")[2]));
    }

    /**
     * Returns the file type (e.g. if input "/Users/mkotsollaris/Desktop/tile_dataset/2/2_0_1.jpg"
     * then it returns "jpg".
     *
     * @param filePath the filename (e.g. "0_1_2.jpg")
     */
    static String computeFileType(String filePath)
    {
        return filePath.split("\\.")[1];
    }

    @Override public String toString()
    {
        return "Tile level: " +
                level +
                ", column: " +
                column +
                ", row: " +
                row + ", data Length: " + data.length;
    }

    /**
     * Returns the total number of columns for a particular level. The same
     * number stands for the total number of rows as well.
     */
    public static int computeColumnTotalNumber(int level)
    {
        return (int) Math.pow(2, level);
    }

    private static Comparator<Tile> tileLengthComparator = (tile1, tile2) -> {
        if(tile1.getData().length == tile2.getData().length) return 0;
        if(tile1.getData().length > tile2.getData().length) return 1;
        return -1;
    };

    static Comparator<Tile> getTileLengthComparator()
    {
        return tileLengthComparator;
    }

    /**
     * A tile is equal to another tile if they both have the same level, column,
     * row and data (bytes).
     *
     * TODO add tests
     */
    @Override public boolean equals(Object obj)
    {
        if(obj == null) return false;
        if(!(obj instanceof Tile)) return false;
        if(obj == this) return true;

        Tile otherTile = (Tile) obj;
        return new EqualsBuilder().
                append(level, otherTile.level).
                append(column, otherTile.column).
                append(row, otherTile.row).
                append(data, otherTile.data).
                isEquals();
    }

    /**
     * TODO Add Tests
     */
    @Override public int hashCode()
    {
        return new HashCodeBuilder(17, 31). // two randomly chosen prime numbers
                append(level).
                append(column).
                append(row).
                append(row).
                append(data).
                toHashCode();
    }
}