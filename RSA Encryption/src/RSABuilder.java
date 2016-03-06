import javax.swing.JFrame;
import javax.swing.JTabbedPane;

public class RSABuilder extends JFrame{
	private JTabbedPane options;
	private KeyTab kt;
	private BlockTab bt;
	private UnblockTab ut;
	private EncryptDecryptTab et;
	
	public RSABuilder(){
		options = new JTabbedPane();
		kt = new KeyTab();
		options.addTab("Generate Key", kt);
	}
	
	public static void main(String args[]){
		
	}
}
