package client;

import net.sf.jsqlparser.parser.CCJSqlParser;
import net.sf.jsqlparser.statement.Statement;
import util.Catalog;
import util.TreeBuilder;

import java.io.FileReader;
import java.io.PrintStream;

public class StaticRDBMS {
	public static void main(String[] args) {
        Catalog.getCatalog();
        PrintStream printStream = System.out;

        try {
            CCJSqlParser parser = new CCJSqlParser(new FileReader(Catalog.qryPath));
            Statement statement;
            while ((statement = parser.Statement()) != null) {
                TreeBuilder tb = new TreeBuilder(statement);
                tb.root.dump(printStream);
            }
        } catch (Exception e) {
            System.err.println("Exception occurred during parsing");
            e.printStackTrace();
        }
	}
}
