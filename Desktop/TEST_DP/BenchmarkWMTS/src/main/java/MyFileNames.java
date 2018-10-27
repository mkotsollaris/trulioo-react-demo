/**
 * Stores the I/O path names of the API.
 *
 * Note that the Tile dataset should have a structure of ZoomLevel/Tile (e.g.:
 * 0/0_0_0.jpg). If not, then correction must be applied per each case.
 *
 * @author mkotsollaris
 * @since 1.0
 */
enum MyFileNames
{
    TileDatasetPath(
            "/Users/mkotsollaris/Desktop/tile_dataset_test/tile_dataset"),
    ImageBlockPath(
            "/Users/mkotsollaris/Desktop/tile_dataset_test/img_block_test"),
    LookupFilePath("/Users/mkotsollaris/Desktop/LevelFiles/LookupFile"),
    TileDataFilePath("/Users/mkotsollaris/Desktop/LevelFiles/TileDataFile"),
    standardTilePath("/Users/mkotsollaris/Desktop/tile_dataset/0/0_0_0.jpg"),
    Windows("Windows");
    private String fileName;

    MyFileNames(String fileName)
    {
        this.fileName = fileName;
    }

    public String getFileName()
    {
        return fileName;
    }
}