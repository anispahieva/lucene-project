import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.text.SimpleDateFormat;
import org.apache.lucene.document.Document; 
import org.apache.lucene.queryParser.ParseException; 
import org.apache.lucene.search.ScoreDoc; 
import org.apache.lucene.search.TopDocs;
import org.jsoup.Jsoup;

public class LuceneTester 
{ 
	String indexDir = "index"; 
 	static String dataDir;
	Indexer indexer; 
	Searcher searcher;
	public static void main(String[] args) 
	{ 
		dataDir = "E:\\Info Retr\\Project\\Data"; 
		LuceneTester tester; 
		try 
		{ 
			tester = new LuceneTester(); 
			tester.createIndex(); 
			System.out.println("Give a query --> ");
			Scanner sc = new Scanner(System.in);
			String q = sc.next();
			tester.search(q); 
			sc.close();
		} 
		catch (IOException e) 
		{ 
			e.printStackTrace(); 
		} 
		catch (ParseException e) 
		{ 
			e.printStackTrace(); 
		}
	} 
	private void createIndex() throws IOException
	{ 
		indexer = new Indexer(indexDir); 
		int numIndexed; 
		long startTime = System.currentTimeMillis(); 
		numIndexed = indexer.createIndex(dataDir, new TextFileFilter()); 
		long endTime = System.currentTimeMillis(); 
		indexer.close(); 
		//System.out.println(numIndexed+" File indexed, time taken: " +(endTime-startTime)+" ms"); 
	} 
	private void search(String searchQuery) throws IOException, ParseException
	{ 
		searcher = new Searcher(indexDir); 
		long startTime = System.currentTimeMillis(); 
		TopDocs hits = searcher.search(searchQuery); 
		long endTime = System.currentTimeMillis(); 
		System.out.println(hits.totalHits + " documents found. Time :" + (endTime - startTime)); 
		List<DocFile> documents = new ArrayList<DocFile>();
		int i=1;
		for(ScoreDoc scoreDoc : hits.scoreDocs) 
		{ 
			Document doc = searcher.getDocument(scoreDoc); 
			String file = doc.get(LuceneConstants.FILE_PATH);
			DocFile document = new DocFile();
			document.setPath(file);
			document.setRank(i);
			document.setScore(scoreDoc.score);
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			document.setModification_time(sdf.format((new File(file)).lastModified()));
			documents.add(document);
			i++;
		} 
		// displaying files informations
		for(DocFile df:documents) {
			System.out.println("Path : "+df.getPath());
			System.out.println("Score : "+df.getScore());
			System.out.println("Rank : "+df.getRank());
			System.out.println("Last Modified : "+df.getModification_time());
			String content = readFile(df.getPath(), StandardCharsets.US_ASCII);
			if(df.getPath().endsWith(".html")) {
				org.jsoup.nodes.Document docc = Jsoup.parse(content);
		        String title = docc.title();
				System.out.println("Html Title : "+title);
			}
			System.out.println("----------------------------------------");
		}
		searcher.close(); 
	} 
	
	static String readFile(String path, Charset encoding)
			  throws IOException
			{
			  byte[] encoded = Files.readAllBytes(Paths.get(path));
			  return new String(encoded, encoding);
			}
}