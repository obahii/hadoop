import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class CopyLocalToHDFS {
    public static void main(String[] args) {
        // Ensure that the user provides both the local and HDFS file paths as arguments
        if (args.length != 2) {
            System.err.println("Usage: CopyLocalToHDFS <local file path> <hdfs file path>");
            System.exit(-1);
        }

        // Get the local file path and HDFS file path from the command-line arguments
        String localFilePath = args[0];
        String hdfsFilePath = args[1];

        try {
            // Create a configuration object
            Configuration conf = new Configuration();
            // conf.set("fs.defaultFS", "hdfs://localhost:9000"); // Change this to your HDFS URI

            // Get the HDFS file system
            FileSystem fs = FileSystem.get(conf);

            // Local file input stream
            InputStream localFileInputStream = new FileInputStream(localFilePath);

            // HDFS file output stream (create the file if it doesn't exist)
            Path hdfsPath = new Path(hdfsFilePath);
            if (fs.exists(hdfsPath)) {
                System.err.println("HDFS file already exists: " + hdfsFilePath);
                System.exit(-1);
            }

            // Open an output stream to the HDFS file
            try (org.apache.hadoop.fs.FSDataOutputStream hdfsOutputStream = fs.create(hdfsPath)) {
                // Copy content from the local file to HDFS
                IOUtils.copyBytes(localFileInputStream, hdfsOutputStream, 4096, true);
                System.out.println("File copied successfully to HDFS: " + hdfsFilePath);
            } catch (IOException e) {
                System.err.println("Error while writing to HDFS: " + e.getMessage());
                System.exit(-1);
            }
        } catch (IOException e) {
            System.err.println("Error while accessing the file system: " + e.getMessage());
            System.exit(-1);
        }
    }
}
