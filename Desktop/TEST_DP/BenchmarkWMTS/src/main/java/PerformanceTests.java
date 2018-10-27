import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * Created by Menelaos Kotsollaris on 2016-12-22.
 *
 * Class Explanation: Includes the performance tests.
 *
 * PseudoCode for the retrieval algorithm:
 * <pre>
 *  Loop: 10 times //10 different parts of the world
 * Randomly Select 1000 tiles //threshold that can be changed to 2000, 3000
 * etc.
 * Measure Time: LevelFilesSet //previously named CustomFile
 * Measure Time: ImageBlock //William's design
 * Measure Time: SimpleFormat
 * End Loop
 * Print Average Time for all
 * </pre>
 */
public class PerformanceTests
{
    private final static String
            tileDataSetPath =
            MyFileNames.TileDatasetPath.getFileName();
    private final static String
            lookupFilePath =
            MyFileNames.LookupFilePath.getFileName();
    private final static String
            tileDataFilePath =
            MyFileNames.TileDataFilePath.getFileName();
    private final static String
            imageBlockFilePath =
            MyFileNames.ImageBlockPath.getFileName();
    private final static FileNames
            fileNames =
            new FileNames.Builder(tileDataSetPath, lookupFilePath,
                                  tileDataFilePath).build();

    static void compareRetrieval(int level) throws IOException
    {
        float levelFileSum = 0, simpleFormatSum = 0, imageBlockSum = 0;
        float levelFileMin = Float.MAX_VALUE, simpleFormatMin = Float.MAX_VALUE,
                imageBlockMin = Float.MAX_VALUE;
        int levelFileThreshold = -1, simpleFormatThreshold = -1,
                imageBlockThreshold = -1;
        int iterationTimes = 10;
        //threshold is the number of the requesting tiles
        for(int threshold = 1; threshold <= iterationTimes; threshold++)
        {
            int iterationsTimes = 1;
            float[] levelFilesSetTimer = new float[iterationsTimes];
            float[] simpleFormatTimer = new float[iterationsTimes];
            float[] imageBlockTimer = new float[iterationsTimes];
            for(int j = 0; j < iterationsTimes; j++)
            {
                int[] columnNumbers = generateRandomNumbers(threshold, level);
                int[] rowNumbers = generateRandomNumbers(threshold, level);

                simpleFormatTimer[j] =
                        measureSimpleFormatRetrieval(level, rowNumbers,
                                                     columnNumbers);
                levelFilesSetTimer[j] =
                        measureLevelFilesSetRetrieval(level, rowNumbers,
                                                      columnNumbers);
                imageBlockTimer[j] =
                        measureImageBlockRetrieval(level, rowNumbers,
                                                   columnNumbers);
                System.out
                        .printf("Threshold %d levelFilesSetTimer: %f, imageBlockTimer: %f, simpleFormatTimer: %f\n",
                                threshold, levelFilesSetTimer[j],
                                imageBlockTimer[j], simpleFormatTimer[j]);
                simpleFormatSum += simpleFormatTimer[j];
                levelFileSum += levelFilesSetTimer[j];
                imageBlockSum += imageBlockTimer[j];
                if(isMin((simpleFormatTimer[j] / threshold), simpleFormatMin))
                {
                    simpleFormatMin = simpleFormatTimer[j];
                    simpleFormatThreshold = threshold;
                }
                if(isMin((levelFilesSetTimer[j] / threshold), levelFileMin))
                {
                    levelFileMin = levelFilesSetTimer[j];
                    levelFileThreshold = threshold;
                }
                if(isMin((imageBlockTimer[j] / threshold), imageBlockMin))
                {
                    imageBlockMin = imageBlockTimer[j];
                    imageBlockThreshold = threshold;
                }
            }
        }
        float levelFileAvg = levelFileSum / iterationTimes;
        float simpleFormatAvg = simpleFormatSum / iterationTimes;
        float imgBlockAvg = imageBlockSum / iterationTimes;
        System.out.println(
                "\nLevel: " + level + "\nAverage for levelFilesSetTimer: " +
                        levelFileAvg +
                        ", imageBlockTimer: " +
                        imgBlockAvg +
                        ", simpleFormatAvg: " +
                        simpleFormatAvg + "");
        System.out.println("Min for levelFileThreshold: " +
                                   levelFileThreshold +
                                   ", imageBlockThreshold: " +
                                   imageBlockThreshold +
                                   ", simpleFormatThreshold: " +
                                   simpleFormatThreshold + "\n");
    }

    private static boolean isMin(float value, float simpleFormatMin)
    {
        return (value < simpleFormatMin);
    }

    private static void printAverageTimer(float[]... floatArray)
    {
        for(float[] floats : floatArray)
        {

        }
    }

    private static long measureImageBlockRetrieval(int level, int[] rowNumbers,
                                                   int[] columnNumbers)
            throws IOException
    {
        long time1 = System.nanoTime();
        for(int columnNumber : columnNumbers)
        {
            for(int j = 0; j < rowNumbers.length; j++)
            {
                String
                        filePath =
                        fileNames.getTileName(level, columnNumber,
                                              columnNumbers[j]);
                Tile
                        tile =
                        ImageBlock.getTile(
                                MyFileNames.ImageBlockPath.getFileName(), level,
                                columnNumber, columnNumbers[j]);
            }
        }
        long time2 = System.nanoTime();
        return TimeUnit.MILLISECONDS
                .convert(time2 - time1, TimeUnit.NANOSECONDS);
    }

    private static long getAverage(long[] timerLevelSimpleFormat)
    {
        long sum = 0;
        for(int i = 0; i < timerLevelSimpleFormat.length; i++)
        {
            sum += timerLevelSimpleFormat[i];
        }
        return sum / timerLevelSimpleFormat.length;
    }

    private static long measureSimpleFormatRetrieval(int level,
                                                     int[] rowNumbers,
                                                     int[] columnNumbers)
            throws IOException
    {
        long time1 = System.nanoTime();
        for(int columnNumber : columnNumbers)
        {
            for(int j = 0; j < rowNumbers.length; j++)
            {
                String
                        filePath =
                        fileNames.getTileName(level, columnNumber,
                                              columnNumbers[j]);
                Tile tile = Tile.getInstance(filePath);
            }
        }
        long time2 = System.nanoTime();
        return TimeUnit.MILLISECONDS
                .convert(time2 - time1, TimeUnit.NANOSECONDS);
    }

    private static long measureLevelFilesSetRetrieval(int level,
                                                      int[] columnNumbers,
                                                      int[] rowNumbers)
            throws IOException
    {
        LevelFilesSet
                levelFilesSet =
                new LevelFilesSet.Builder(fileNames).build();
        long time1 = System.nanoTime();
        for(int i = 0; i < columnNumbers.length; i++)
        {
            for(int j = 0; j < rowNumbers.length; j++)
            {
                Tile
                        tile =
                        levelFilesSet.getTile(level, columnNumbers[i],
                                              rowNumbers[j]);
            }
        }
        long time2 = System.nanoTime();
        return TimeUnit.MILLISECONDS
                .convert(time2 - time1, TimeUnit.NANOSECONDS);
    }

    public static void testTime() throws IOException
    {
        LevelFilesSet
                levelFilesSet =
                new LevelFilesSet.Builder(fileNames).build();
        long time1 = System.nanoTime();
        levelFilesSet.getTile(6, 0, 0);
        long time2 = System.nanoTime();
        System.out.println("LevelFilesSse TIME: " + (time2 - time1));
        String filePath = fileNames.getTileName(6, 1, 8);
        time1 = System.nanoTime();
        Tile tile = Tile.getInstance(filePath);
        time2 = System.nanoTime();
        System.out.println("SimpleFormat TIME: " + (time2 - time1));
    }

    private static int[] generateRandomNumbers(int threshold, int level)
    {
        int max = Tile.computeColumnTotalNumber(level) - 1;
        int[] randomArray = new int[threshold];
        for(int i = 0; i < threshold; i++)
        {
            randomArray[i] = getRandomNumber(0, max);
        }
        return randomArray;
    }

    private enum TileMember
    {
        COLUMN,
        ROW
    }

    private static int getRandomNumber(int min, int max)
    {
        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
}
