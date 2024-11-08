import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.FSDataInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class CountLinesInHDFS {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: CountLinesInHDFS <hdfs file path>");
            System.exit(-1);
        }

        String filePath = args[0];

        try {
            Configuration conf = new Configuration();
            // conf.set("fs.defaultFS", "hdfs://localhost:9000"); // Change to your HDFS URI
            
            FileSystem fs = FileSystem.get(conf);
            Path path = new Path(filePath);
            FSDataInputStream inputStream = fs.open(path);
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            
            String line;
            int lineCount = 0;
            while ((line = br.readLine()) != null) {
                lineCount++;
            }
            br.close();
            inputStream.close();
            fs.close();
            System.out.println("Number of lines in the file: " + lineCount);
        } catch (IOException e) {
            System.err.println("Error while counting lines in the file: " + e.getMessage());
        }
    }
}
