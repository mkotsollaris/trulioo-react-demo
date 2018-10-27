/**
 * Provides the classes necessary to store and retrieve a tile dataset.
 *
 * See below an usage example:
 *
 * In order to use the library, you will need to have the hosted tiles (.jpg)
 * and instantiate a FileNames object as below:
 *
 * <pre>
 * <code>
 * //Alternate the paths with your own values.
 * //the tile dataset path
 * String tileDataSetPath = "/Users/mkotsollaris/Desktop/tile_dataset_test/tile_dataset";
 * //where the lookupFile is going to be stored
 * String lookupFilePath = "/Users/mkotsollaris/Desktop/tile_dataset_test/LevelFiles/LookupFile";
 * //where the tileDataFile is going to be stored
 * String tileDataFilePath = "/Users/mkotsollaris/Desktop/tile_dataset_test/img_block";
 * </code>
 * </pre>
 *
 * In order for the files to generate (or load) the LevelFiles, run the
 * following command:
 *
 * <pre>
 *     <code>
 *         LevelFilesSet levelFilesSet = new LevelFilesSet.Builder(fileNames).build();
 *     </code>
 * </pre>
 *
 * After the LevelFiles are generated, for a specific tile retrieval use the
 * {@link unb.mkotsollaris.tilemanagement.LevelFilesSet#getTile(int, int, int)}
 * method, which instantiates a {@link unb.mkotsollaris.tilemanagement.Tile}
 * object.
 */
