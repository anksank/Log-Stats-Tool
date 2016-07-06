
import javax.swing.JFileChooser;
import javax.swing.JFrame;


public class FileChooserDemo extends JFrame {

	private static final long serialVersionUID = 1L;

	public FileChooserDemo() {

	}

	public String[] selectPerformed() {
		
		String[] fileNames = new String[20];
		JFileChooser chooser = new JFileChooser();
		chooser.setMultiSelectionEnabled(true);
		int option = chooser.showOpenDialog(FileChooserDemo.this);

		

		if(option == JFileChooser.APPROVE_OPTION) {
			
			fileNames = getFileNames(chooser);
			
		}

		
		if(option == JFileChooser.CANCEL_OPTION)  {  
			System.out.println("You canceled.");
		}
		
		return fileNames;
	}

	public String[] getFileNames(JFileChooser chose){

		String[] fileNames = new String[1000];
		for (int i = 0; i < chose.getSelectedFiles().length; i++){
			fileNames[i] = chose.getSelectedFiles()[i].getPath();
		}
		return fileNames;
	}
	
} 