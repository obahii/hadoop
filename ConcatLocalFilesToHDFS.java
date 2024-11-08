import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ConcatLocalFilesToHDFS {
    public static void main(String[] args) {
        // Ensure that the user provides at least two arguments (one for HDFS file path and at least one local file path)
        if (args.length < 2) {
            System.err.println("Usage: ConcatLocalFilesToHDFS <local file1> <local file2> ... <hdfs destination file>");
            System.exit(-1);
        }

        // Get the HDFS destination file path (last argument)
        String hdfsFilePath = args[args.length - 1];

        // Create a configuration object
        Configuration conf = new Configuration();
        // conf.set("fs.defaultFS", "hdfs://localhost:9000"); // Change to your HDFS URI if necessary

        try {
            // Get the HDFS file system
            FileSystem fs = FileSystem.get(conf);

            // Check if the HDFS file already exists
            Path hdfsPath = new Path(hdfsFilePath);
            if (fs.exists(hdfsPath)) {
                System.err.println("HDFS file already exists: " + hdfsFilePath);
                System.exit(-1);
            }

            // Create the HDFS destination file if it doesn't exist
            try (org.apache.hadoop.fs.FSDataOutputStream hdfsOutputStream = fs.create(hdfsPath)) {

                // Iterate over each local file (except the last argument which is the HDFS destination)
                for (int i = 0; i < args.length - 1; i++) {
                    String localFilePath = args[i];
                    System.out.println("Appending local file: " + localFilePath);

                    // Open the local file input stream
                    InputStream localFileInputStream = new FileInputStream(localFilePath);

                    // Copy content from the local file to HDFS file
                    IOUtils.copyBytes(localFileInputStream, hdfsOutputStream, 4096, false);
                    localFileInputStream.close();
                }

                System.out.println("Files successfully concatenated into HDFS file: " + hdfsFilePath);
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
