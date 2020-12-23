import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import java.io.*;
import java.nio.file.Path;


public class Indexing {
public Indexing() {
		
	}

	private IndexWriter writer;
	public Indexing(Path indexDirectoryPath) throws IOException{
	//this directory will contain the indexes
	Directory indexDirectory = FSDirectory.open(indexDirectoryPath);
	//create the indexer
	StandardAnalyzer analyzer = new StandardAnalyzer();
	IndexWriterConfig config = new IndexWriterConfig(analyzer);
	writer = new IndexWriter(indexDirectory, config);
	}
	public void close() throws CorruptIndexException, IOException{
	writer.close();
	
	}
	
	private Document getDocument(File file) throws IOException {
		Document document = new Document();
		TextField contentField = new TextField(LuceneConstants.CONTENTS,
				new FileReader(file));
				//index file name
				StringField fileNameField = new StringField(LuceneConstants.FILE_NAME,
				file.getName(), Field.Store.YES);
				//index file path
				StringField filePathField = new StringField(LuceneConstants.FILE_PATH,
				file.getCanonicalPath(),
				Field.Store.YES);
				document.add(contentField);
				document.add(fileNameField);
				document.add(filePathField);
				return document;
	}
	
	public void listFilesForFolder(File[] files) throws IOException {
	    for (File file : files) {
	        if (file.isDirectory()) {
	            listFilesForFolder(file.listFiles());
	        } else {
	        	if(!file.isHidden() 
	        			&& file.exists() 
	        			&& file.canRead() 
	        			&& (file.getName().toLowerCase().endsWith(".txt") || file.getName().toLowerCase().endsWith(".html")) 
	        			) {
	        		indexFile(file);
	        	}
	        }
	    }
		
	}
	
	public int createIndex(String dataDir, FileFilter filter)
			throws IOException{
			//get all files in the data directory
			File[] files = new File(dataDir).listFiles();
			listFilesForFolder(files);
			
			return writer.numDocs();
			}

	private void indexFile(File file) throws IOException{
		System.out.println("Indexing "+file.getCanonicalPath());
		Document document = getDocument(file);
		writer.addDocument(document);
		}
}

