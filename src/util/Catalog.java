package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//Catalog keeps track of information such as
//where a file for a given table is located,
//what the schema of different tables is, and so on

//the catalog is a global entity that various components
//of the system may want to access, 
//so it adapts the Singleton Pattern which means at most one instance 
//of this class could exist

public class Catalog {
	private static Catalog catalog;

	public static String inputDir = "/Users/lirongshao/Desktop/Interpreter/staticRDBMS/samples" + File.separator + "input";
	public static String outputDir = "/Users/lirongshao/Desktop/Interpreter/staticRDBMS/samples" + File.separator + "output";
	public static String qryPath;
	public static String dbDir;
	public static String dataDir;
	public static String schemaPath;
	
	public static HashMap<String, List<String>> schemas = new HashMap<>();
	public static HashMap<String, String> aliases = new HashMap<>();
	
	/** 
	 * intentionally make the constructor private, which 
	 * avoids instances being created outside the class
	 */
	private Catalog() {
		resetDirs(inputDir, outputDir);
	}
	
	/**	
	 * makes sure there is only one instance exist
	 * @return the instance of the class
	 */
	public static Catalog getCatalog() {
		if(catalog == null) {
			catalog = new Catalog();
		}
		return catalog;
	}
	
	public static void resetDirs(String _inputDir, String _outputDir) {
		if(_inputDir != null) {
			inputDir = _inputDir + File.separator;
			qryPath = inputDir + "queries.sql";
			dbDir = inputDir + "db" + File.separator;
			dataDir = dbDir + "data" + File.separator;
			schemaPath = dbDir + "schema.txt";
		}
		if(_outputDir != null) {
			outputDir = _outputDir + File.separator;
		}
		
		resetSchemas();
	}
	
	private static void resetSchemas() {
		BufferedReader br;
		schemas.clear();
		
		try {
			br = new BufferedReader(new FileReader(schemaPath));
			String line = null;
			while ((line = br.readLine()) != null) {
				String[] schema = line.split(" ");
				
				String key = schema[0];
				List<String> val = new ArrayList<String>();
				for (int i = 1; i < schema.length; i++) {
					val.add(schema[i]);
				}
				
				schemas.put(key, val);
			}
			
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String tablePath(String tableName) {
		return dataDir + tableName;
	}
	
	public static String origName(String tableName) {
		if(aliases.containsKey(tableName))
			tableName = aliases.get(tableName);
		return tableName;
	}
	
	public static List<String> getSchema(String tableName) {
		tableName = origName(tableName);
		return schemas.get(tableName);
	}
	
	/**
	 * create a table with alias, schema and BufferedReader
	 * @param tableName
	 * @return the created table
	 */
	public static Table getTable(String tableName) {
		BufferedReader br = getTableReader(tableName);
		
		if(br == null) return null;
		return new Table(tableName, getSchema(tableName), br);
	}
	
	public static BufferedReader getTableReader(String tableName) {
		tableName = origName(tableName);
		try {
			return new BufferedReader(new FileReader(tablePath(tableName)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
}
