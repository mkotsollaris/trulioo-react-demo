import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Menelaos Kotsollaris on 2017-05-04.
 *
 * Class Explanation: TODO
 */
@WebServlet public class BenchmarkServlet extends HttpServlet
{
    private LevelFilesSet levelFilesSet;

    public void init()
    {
        /*String tileDataSetPath = MyFileNames.TileDatasetPath.getFileName();
        String lookupFilePath = MyFileNames.LookupFilePath.getFileName();
        String tileDataFilePath = MyFileNames.TileDataFilePath.getFileName();
        String
                imageBlockFilePath =
                MyFileNames.ImageBlockPath
                        .getFileName();
        FileNames
                fileNames = new FileNames.Builder(tileDataSetPath, lookupFilePath,
                                      tileDataFilePath)
                        .build();
        try
        {
            levelFilesSet = new LevelFilesSet.Builder(fileNames).build();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }*/

    }

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException
    {

    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        // Set response content type
        response.setContentType("text/html");

        // Actual logic goes here.
        PrintWriter out = response.getWriter();
        out.println("<h1>" + "Hey! :)" + "</h1>");
    }

    public void destroy()
    {
        // Finalization code...
    }

    /**
 * Checks if the file exists and if it does then it prints it to the user's
 * browser, otherwise in throws an error.
 */
    public void outputTileToBrowser(Tile[] tiles, HttpServletResponse response)
            throws Exception
    {
        response.setContentType("text/html");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        for(Tile tile : tiles)
        {
            outputStream.write(tile.getData());
        }
        byte c[] = outputStream.toByteArray();
        response.getOutputStream().write(c, 0, c.length);
    }
}
