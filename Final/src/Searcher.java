import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Path;
import java.text.ParseException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory; 

public class Searcher 
{ 
	IndexSearcher indexSearcher; 
	IndexReader indexReader;
	QueryParser queryParser; 
	Query query; 
	
	public Searcher(Path indexDirectoryPath) throws IOException
	{ 
		//IndexReader indexReader;
		//File yourFile = indexDirectory;
		//Path yourPath = yourFile.toPath();
		//FSDirectory directory = FSDirectory.open(yourPath);
		Directory indexDirectory = FSDirectory.open(indexDirectoryPath);
	    indexReader = DirectoryReader.open(indexDirectory);
		 // StandardAnalyzer analyzer = new StandardAnalyzer();
		//DirectoryReader indexreader = DirectoryReader.open(directory);
      	//IndexReader indexDirectory = FSDirectory.open(indexDirectoryPath);
		indexSearcher = new IndexSearcher(indexReader);
		queryParser = new QueryParser(LuceneConstants.CONTENTS, 
				new StandardAnalyzer());
	} 
	public TopDocs search(String searchQuery) throws IOException, ParseException
	{ 

			try {
				query = queryParser.parse(searchQuery);
			} catch (Exception e) {
				// TODO: handle exception
			}
			System.out.println("Query : "+query);
		return indexSearcher.search(query, LuceneConstants.MAX_SEARCH); 
	} 
	public Document getDocument(ScoreDoc scoreDoc) throws CorruptIndexException, IOException
	{ 
		return indexSearcher.doc(scoreDoc.doc); 
	} 
	public void close() throws IOException
	{ 
      indexReader.close(); 
	} 
}