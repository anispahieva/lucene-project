import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class LuceneTester {
String dataDir;
Indexing indexer;
public static void main(String[] args) {
LuceneTester tester;
try {
tester = new LuceneTester();
tester.createIndex();
} catch (IOException e) {
e.printStackTrace();
}
}
private void createIndex() throws IOException{
	Scanner sc = new Scanner(System.in);
	String indexDir = "../lucene/indexDirectory";
	dataDir = sc.nextLine();
	Path indexPath = Paths.get(indexDir); 
Indexing indexer = new Indexing(indexPath);
int numIndexed;
long startTime = System.currentTimeMillis();
numIndexed = indexer.createIndex(dataDir, new TextFileFilter());
long endTime = System.currentTimeMillis();
indexer.close();
System.out.println(numIndexed+" File(s) indexed, time taken: "
+(endTime-startTime)+" ms");
}
}