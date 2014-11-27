/**
 *  
 * ModelWrapper for Morphline-GUI Projects
 *
 * Such a wrapper contains a MLPModel. 
 * 
 **/
package de.bitocean.mm;
 
import net.sf.json.JSONException;
import net.sf.json.JSONSerializer;
 
import java.io.IOException;
import java.io.PrintStream;
import java.util.Vector;
import javax.swing.JTextArea; 
 
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea; 

/**
 * @author kamir
 */
public class MLPModelWrapper {
    
    public JTextArea jaLog = null;

    String basesFolder = null;

    RSyntaxTextArea editorMorphline;

    RSyntaxTextArea _editorSOLRSchema;

    RSyntaxTextArea _editorFlumeCFG;

    Vector<String> formulas = null;

    public MLPModelWrapper(LocalMorphlineStore s) {
        store = s;
    }

    public String load(String ML_Folder) throws IOException {
        return store.loadMorphline(ML_Folder);
    }

//    public void store(String ML_Folder, String code) throws IOException {
//        store.saveMorphline(ML_Folder, code);
//    }

    public LocalMorphlineStore store = null;

    void store() throws IOException {
//        store(basesFolder, editorMorphline.getText());
        store.saveMorphline( basesFolder, editorMorphline.getText());
        store.saveSchema( basesFolder, _editorSOLRSchema.getText());
        store.saveFlumeCFG( basesFolder, _editorFlumeCFG.getText());
    }

    void load() throws IOException {
        editorMorphline.setText(store.loadMorphline(basesFolder));
        _editorSOLRSchema.setText(store.loadSolrSchema(basesFolder));
        _editorFlumeCFG.setText(store.loadFlumeCFG(basesFolder));
    }

    private static boolean isValidJson(String jsonStr) {
        boolean isValid = false;
        try {
            JSONSerializer.toJSON(jsonStr);
            isValid = true;
        } catch (JSONException je) {
            isValid = false;
        }
        return isValid;
    }

     
 

    void replaceSelection(String cap) {
        this.editorMorphline.replaceSelection(cap);
    }

    

    void initTemplate(String s) {
        this.editorMorphline.setText(s);
    }

     

    void setImageFolder(String name) {
        this.basesFolder = name;
    }
 

    String getSelection() {
        return editorMorphline.getSelectedText();
    }
//
    public void runFullMorphlineTest() throws Exception {

       TextAreaAsOutputStream out = new TextAreaAsOutputStream( this.jaLog , "$ " ); 
        
       MLPModel m = new MLPModel();
             
       m.setTestData(messages);
       m.setTestDataBytes(messagesBytes);
       
       System.out.println( "messages      != null : " + (messages != null) );
       System.out.println( "messagesBytes != null : " + (messagesBytes != null) );
       
       
       m.runFullMorphlineTest( this.editorMorphline.getText() , new PrintStream( out ) );

    }

    
    Vector<String> getTestDataFromFile() throws IOException {
        return this.store.loadTestData(basesFolder);
    }

    void validateMorphelineJSON() {
        System.out.println(isValidJson(editorMorphline.getText()));
    }

    Vector<String> messages = null;
    Vector<byte[]> messagesBytes = null;
    
    void setTestData(Vector<String> m) {
       messages = m;    
    }
    
    void setTestDataBytes(Vector<byte[]> m) {
       messagesBytes = m;    
    }

    String getMorphlineFileName() {
        return store.getFlumeCFGFilename(basesFolder).getAbsolutePath();
    }


}
