import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Contains helpful static functions regarding file operations (I/O).
 *
 * @author mkotsollaris
 * @since 1.0
 */
final class FileUtilities
{
    /**
     * Returns the bytes of the file.
     *
     * @param path the path where the file is located
     */
    static byte[] readFromFile(String path) throws IOException
    {
        return Files.readAllBytes(Paths.get(path));
    }

    /**
     * Writes data to the given file.
     *
     * Based on: https://examples.javacodegeeks.com/core-java/io/randomaccessfile/java-randomaccessfile-example/
     *
     * @param filePath : the path of the file
     * @param data     : the byte array that will be written to the file
     * @param position : the position of the cursor inside the file that will
     *                 write the data
     */
    static void writeToFile(String filePath, byte[] data, long position)
            throws IOException
    {
        RandomAccessFile file = new RandomAccessFile(filePath, "rw");
        file.seek(position);
        file.write(data);
        file.close();
    }

    /**
     * Writes a long to a file.
     */
    static void writeLongToFile(String filePath, long data, long position)
            throws IOException
    {
        RandomAccessFile file = new RandomAccessFile(filePath, "rw");
        file.seek(position);
        file.writeLong(data);
        file.close();
    }

    /**
     * Writes an integer to the file.
     */
    static void writeIntToFile(String filePath, int data, long position)
            throws IOException
    {
        RandomAccessFile file = new RandomAccessFile(filePath, "rw");
        file.seek(position);
        file.writeInt(data);
        file.close();
    }

    /**
     * Reads from the given file.
     *
     * Based on: https://examples.javacodegeeks.com/core-java/io/randomaccessfile/java-randomaccessfile-example/
     *
     * @param filePath : the path of the file
     * @param position : the position of the cursor inside the file that will
     *                 read the data
     * @param size     : the size of the bytes to be read
     *
     * @return : the wanted bytes of the files
     */
    static byte[] readFromFile(String filePath, long position, int size)
            throws IOException
    {
        RandomAccessFile file = new RandomAccessFile(filePath, "r");
        file.seek(position);
        byte[] bytes = new byte[size];
        file.read(bytes);
        file.close();
        return bytes;
    }

    /**
     * Reads an integer from a file.
     */
    static int readIntFromFile(String filePath, long position)
            throws IOException
    {
        RandomAccessFile file = new RandomAccessFile(filePath, "r");
        file.seek(position);
        int number = file.readInt();
        file.close();
        return number;
    }

    /**
     * Reads a long from a file
     */
    static long readLongFromFile(String filePath, long position)
            throws IOException
    {
        RandomAccessFile file = new RandomAccessFile(filePath, "r");
        file.seek(position);
        long longNumber = file.readLong();
        file.close();
        return longNumber;
    }

    /**
     * Returns the length of the file measured in bytes
     */
    static long getFileLength(String fileName) throws IOException
    {
        RandomAccessFile file = new RandomAccessFile(fileName, "r");
        long fileLength = file.length();
        file.close();
        return fileLength;
    }

    /**
     * Creates a file.
     */
    static void createFile(String pathFileName) throws IOException
    {
        new File(pathFileName).createNewFile();
    }

    /**
     * Creates an empty random access file with a specific size.
     */
    static void createFile(String filename, long fileLength) throws IOException
    {
        RandomAccessFile f = new RandomAccessFile(filename, "rw");
        f.setLength(fileLength);
        f.close();
    }

    /**
     * Checks if file is empty
     *
     * TODO test
     */
    static boolean isEmpty(String filename) throws IOException
    {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        return (br.readLine() == null);
    }

    /**
     * Deletes a file.
     */
    static boolean deleteFile(String filename) throws FileNotFoundException
    {
        return new File(filename).delete();
    }

    /**
     * Checks whether a file exists or not.
     */
    static boolean exists(String filePath)
    {
        File f = new File(filePath);
        return (f.exists() && !f.isDirectory());
    }

    /**
     * Returns all the Files of the given directory.
     *
     * @throws OutOfMemoryError When the directory contains large number of
     *                          files.
     */
    static File[] getFiles(String directoryPath)
    {
        return new File(directoryPath).listFiles();
    }

    /**
     * Returns the size of the given directory.
     */
    static long getDirectorySize(String directoryPath)
    {
        return FileUtils.sizeOfDirectory(new File(directoryPath));
    }

    /**
     * Returns directories names
     *
     * @param directoryPath the folder path that will be examined.
     */
    static String[] getDirectoriesNames(String directoryPath)
    {
        File file = new File(directoryPath);
        return file
                .list((current, name) -> new File(current, name).isDirectory());
    }

    static void deleteDirectory(String directoryPath) throws IOException
    {
        FileUtils.deleteDirectory(new File(directoryPath));
    }

    /**
     * Writes in a new line a String to a the file.
     */
    static void writeStringToFile(PrintWriter writer, String value)
            throws FileNotFoundException, UnsupportedEncodingException
    {
        writer.println(value);
    }

    /**
     * @param directoryPath the new directory path
     **/
    static boolean createDir(String directoryPath)
    {
        return new File(directoryPath).mkdirs();
    }
}