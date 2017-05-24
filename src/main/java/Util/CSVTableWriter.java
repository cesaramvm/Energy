package Util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author César Valdés
 */
public class CSVTableWriter {

    private final PrintWriter pw;
    private final int tableColumns;

    public CSVTableWriter(String path, ArrayList<String> headers) throws Exception {
        PrintWriter newPw = null;
        boolean exists = false;
        try {
            File f = new File(path);
            if (f.exists() && !f.isDirectory()) {
                exists = true;
            }
            FileWriter fw = new FileWriter(path, true);
            BufferedWriter bw = new BufferedWriter(fw);
            newPw = new PrintWriter(bw);
        } catch (IOException ex) {
            Logger.getLogger(CSVTableWriter.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            this.pw = newPw;
            this.tableColumns = headers.size();
            if (!exists) {
                this.printHeaders(headers);
            }
        }

    }

    private void printHeaders(ArrayList<String> lista) throws Exception {
        if (pw == null) {
            throw new Exception("Fallo en el printWriter");
        }
        String headers = "";
        for (String columnName : lista) {
            headers = headers + columnName + ";";
        }
        pw.println(headers);
        pw.flush();
    }
    
    public void printRow(ArrayList<String> lista) throws Exception{
        if (pw == null) {
            throw new Exception("Fallo en el printWriter");
        }
        if(lista.size()!= this.tableColumns){
            pw.close();
            throw new Exception("Debe haber tantos datos como columnas");
        }
        String rowData = "";
        for (String columnName : lista) {
            rowData = rowData + columnName + ";";
        }
        pw.println(rowData);
        pw.flush();
        
    }
    
    public void close(){
        pw.close();
    }

}
