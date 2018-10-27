import java.io.IOException;

/**
 * Test Created by Menelaos Kotsollaris on 2016-10-02.
 *
 * Class Explanation: TODO
 */
class MainClass
{
    public static void main(String[] args) throws IOException
    {
        String tileDataSetPath = MyFileNames.TileDatasetPath.getFileName();
        String lookupFilePath = MyFileNames.LookupFilePath.getFileName();
        String tileDataFilePath = MyFileNames.TileDataFilePath.getFileName();
        String imageBlockFilePath = MyFileNames.ImageBlockPath.getFileName();
        FileNames
                fileNames =
                new FileNames.Builder(tileDataSetPath, lookupFilePath,
                                      tileDataFilePath).build();
        System.out.println("hello world");
        /*LevelFilesSet
                levelFilesSet =
                new LevelFilesSet.Builder(fileNames).build();*/
        //Utilities.fulfilTileDataset(fileNames.getTileDataSetPath());
        /*for(int i=0;i<=10;i++)
        {
            PerformanceTests.compareRetrieval(i);
        }*/
        /*PerformanceTests.compareRetrieval(1);
        PerformanceTests.compareRetrieval(2);
        PerformanceTests.compareRetrieval(3);
        PerformanceTests.compareRetrieval(4);
        PerformanceTests.compareRetrieval(5);
        PerformanceTests.compareRetrieval(6);
        PerformanceTests.compareRetrieval(7);
        PerformanceTests.compareRetrieval(8);
        PerformanceTests.compareRetrieval(9);*/
        //PerformanceTests.compareRetrieval(10);
    }
}