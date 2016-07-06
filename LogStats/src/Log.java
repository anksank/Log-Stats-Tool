

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import javax.swing.*;

public class Log {

	public FileChooserDemo filePickObject = new FileChooserDemo();
	private JFrame mainFrame = new JFrame("Log Statistics");
	
	private JLabel headerLabel = new JLabel("", JLabel.CENTER);
	//private JLabel fileLabel = new JLabel("Hi", JLabel.CENTER);
	private JLabel countLabel = new JLabel("", JLabel.CENTER);
	
	JPanel uploadPanel = new JPanel();
	JPanel statsPanel = new JPanel();
	JPanel headerPanel = new JPanel();
	JPanel controlPanel = new JPanel();
	JPanel filePanel = new JPanel();
	
	JButton uploadButton = new JButton("Upload");
	JTextField keyWord = new JTextField("Enter the keyword");
	
	String path = "";
	String[] allPaths = new String[1000];
	int count_keyWord;
	int sessionId;
	int vendingSuccessCount;
	int vendingFailCount;
	
	HashMap<String, Integer> configMap;
	HashMap<String, String> activityMap;
	
	public static String LOGIN_STRING = "Login activity for user";
			
	public static String LOGOUT_STRING = "Logout activity for user";
	
	public static String VENDING_SUCCESS = "Has vending succeeded : true";
	
	public static String VENDING_FAIL = "Has vending succeeded : false";
	
	
	public Log(){
		prepareGUI();
		count_keyWord = 0;
		configMap = new HashMap<String, Integer>();
		activityMap =  new HashMap<String, String>();
		sessionId = 0;
		vendingFailCount = 0;
		vendingSuccessCount = 0;
		populateConfigMap();
		//System.out.println(configMap);
	}
	
	public void populateConfigMap()
	{
		//read the file..
		//put it to array buffer
		//put all the keywords as keys in the map with count as 0.
		
		FileInputStream fstream = null ;
		BufferedReader br = null;
		try
		{
			
			fstream = new FileInputStream("src/config.txt");
			br = new BufferedReader(new InputStreamReader(fstream));
			String strLine;
			while ((strLine = br.readLine()) != null)
			{
				String[] activities = strLine.split("::");
				// 0 -> activity
				// 1 -> keyword
				
				//put activity as key and keyword as value to the activityMap
				//put keyword as key and initialize count to 0 as value to the configMap
				
				activityMap.put(activities[0], activities[1]);
				configMap.put(activities[1], 0);
				
			}
			
			
		}
		catch(Exception ex)
		{
			System.out.println("Exception1: "+ ex);
		}
			
		
		
		
	}

	public static void main(String[] args){
		Log  LogObject = new Log();  
		LogObject.showFrame();
	}

	private void prepareGUI(){
		
		mainFrame.setSize(400,400);
		mainFrame.setLayout(new GridLayout(0, 1));
		mainFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent){
				System.exit(0);
			}        
		});    
		      
		headerPanel.add(headerLabel);
		//statsPanel.add(countLabel);
		//filePanel.add(new JLabel("OK Fine"));
		controlPanel.setLayout(new FlowLayout());
		
		uploadButton.setSize(50, 20);
		uploadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				allPaths = filePickObject.selectPerformed();
				
				for(int p = 0; p < allPaths.length; ++p)
				{
				try
				{
					FileInputStream fstream = new FileInputStream(allPaths[p]);
					BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
					getCount(br );
					br.close();
				}
				catch(Exception ex)
				{
					//System.out.println("Exception2: "+ ex);
				}
					
				
				
				}
				display();
			}
		});

		uploadPanel.add(uploadButton);
		

		//mainFrame.add(headerPanel);
		mainFrame.add(uploadPanel);
		//mainFrame.add(filePanel);
		//mainFrame.add(controlPanel);

		mainFrame.add(statsPanel);
		
		mainFrame.setVisible(true);  
	}

	private void showFrame(){
		headerLabel.setText("Pick the log File(s)");   



		final JFrame frame = new JFrame();
		frame.setSize(300, 300);
		frame.setLayout(new FlowLayout());
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent){
				frame.dispose();
			}        
		});    
		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//System.out.println("Action to be performed");
				
				
				//countLabel.setText("A Frame shown to the user.");
				//frame.setVisible(true);
			}
		});


		controlPanel.add(keyWord);
		controlPanel.add(okButton);


		mainFrame.setVisible(true);  
	}

	public void display()
	{
		
		//String displayStr = "";
		statsPanel.removeAll();
		Set<String> set = activityMap.keySet();
		for(String key: set)
		{
			JPanel panel = new JPanel();
			JLabel label = new JLabel( key + " : " + configMap.get(activityMap.get(key)).toString());
			panel.add(label);
			statsPanel.add(panel);
			
		}
		
		mainFrame.revalidate();
		
		
	}

	public int getCount(BufferedReader br)
	{

		String strLine;

		int i = 1;  //for line number
		int count = 0;

		//ArrayList<String> list = new ArrayList<String>();

		try
		{
			Set<String> keyWordsSet = configMap.keySet();

			
			
			while ((strLine = br.readLine()) != null) 
			{
				//check the strLine contains the keys of the map and update the map.
				
				for(String key: keyWordsSet)
				{
					
					if(strLine.contains(key))
					{
						
						if(key.equalsIgnoreCase(LOGIN_STRING))
						{
							sessionId += 1;
							writeToSessionLog("Successful Vend: "+ vendingSuccessCount + "\n");
							writeToSessionLog("failed Vend: "+ vendingFailCount + "\n");
							writeToSessionLog("SessionId "+ sessionId + " : Login\n");
							
							
							//reset it for every session start.
							vendingSuccessCount = 0;
							vendingFailCount = 0;
						}
						else if(key.equalsIgnoreCase(LOGOUT_STRING))
						{
							writeToSessionLog("SessionId "+ sessionId + " : Logout\n");
						}
						else if(key.equalsIgnoreCase(VENDING_SUCCESS))
						{
							vendingSuccessCount += 1;
						}
						else if(key.equalsIgnoreCase(VENDING_FAIL))
						{
							vendingFailCount += 1;
						}
						writeToLog(strLine+"\n");
						configMap.put(key, (Integer)(configMap.get(key) + 1));
						
					}
				}
				i = i+1;
			}
			System.out.println(activityMap);
			System.out.println(configMap);
		}
		catch(Exception e)
		{
			System.out.println("Some exception occured.");
		}

		return count;

	}

	private void writeToSessionLog(String strLine) {
		String path = "C:\\Users\\I306378\\Desktop\\SessionLog.txt";
		try
		{
				FileOutputStream stream = new FileOutputStream(path,true);
				stream.write(strLine.getBytes());
						
				stream.close();
		}
		catch(Exception e)
		{
			System.out.println("Exception in writing to Session log: "+ e);
		}
	}

	public void writeToLog(String strLine) {
		String path = "C:\\Users\\I306378\\Desktop\\log.txt";
		try
		{
				FileOutputStream stream = new FileOutputStream(path,true);
				stream.write(strLine.getBytes());
						
				stream.close();
		}
		catch(Exception e)
		{
			System.out.println("Exception in writing to log: "+ e);
		}
		
	}

	public String getPath()
	{

		//System.out.println("Enter the filename: ");

		//String filename = "";
		String filePath = "";

		try{
			//filename = scanner.nextLine();
			//filePath = "C:\\Users\\I306378\\Desktop\\"+ filename;
			//System.out.println(path);

		}
		catch(Exception e)
		{
			System.out.println("Exception3: "+ e);
		}

		return filePath;

	}

	public String getKeyword()
	{
		//System.out.println("Enter the keyword");


		//String keyword = scanner.nextLine();
		String keyword = keyWord.getText();


		return keyword;

	}
}