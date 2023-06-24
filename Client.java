
//Java Program to create a text editor using java
import javax.swing.*;
import java.awt.*;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.OceanTheme;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
class Client extends JFrame implements ActionListener, KeyListener {
    public static Socket socClient;
    public static ObjectInputStream ClientInput;
    public static ObjectOutputStream ClientOutput;
    public static DataInputStream din;
    public String SelectedText;
    public String ClientIDToShare;
// Text component
    JTextArea t;
// Frame
    JFrame f;
// Main class
    public static void main(String[] args) {
        try {
            socClient = new Socket("localhost", 9999); // named argument , to make client server on two laptop, just replace localhost with server IP address and give same port number on client laptop
            System.out.println("Connected!");
            Client c1 = new Client();
            Scanner scn = new Scanner(System.in);
            ClientOutput = new ObjectOutputStream(socClient.getOutputStream());
            ClientInput = new ObjectInputStream(socClient.getInputStream());
            
            din = new DataInputStream(socClient.getInputStream());
            System.out.print("Write your ID : ");
            String id = scn.nextLine();
            ClientOutput.writeUTF(id); //output stream writes ID in itself
            ClientOutput.flush();
            System.out.println("Write the name for your frame");
            String filename = scn.nextLine();
            c1.ClientGUI(filename); //attaching the framename to frame top
            System.out.print("Now You Start your Real Connection");
            while (true) {
                String NewDataInTextArea = ClientInput.readUTF();

//input given by user everytime continuously so while loop
                c1.ChangeText(NewDataInTextArea); //display it on text area
//server to receiver, to display text on receiver text area

            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
            e.getMessage();
        } catch (IOException e) {
            
            e.printStackTrace(); //the exceptions occuring on every stage is displayed
            e.getMessage();
        }
    }
    public void ClientGUI(String str) {
// Create a frame
        f = new JFrame(str); //Jframe is created with given name
        try {
// Set look appearance of window and feel behaviour
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
// Set theme to ocean
            MetalLookAndFeel.setCurrentTheme(new OceanTheme());
        } catch (Exception e) {
            e.printStackTrace();
        }

// Text component
        t = new JTextArea();
        t.setLineWrap(true);


        
// Create a menu bar
        JMenuBar mb = new JMenuBar();
// Create a menu for menu
        JMenu m1 = new JMenu("File");
// Create menu items
        JMenuItem mi1 = new JMenuItem("New");
        JMenuItem mi2 = new JMenuItem("Open");
        JMenuItem mi3 = new JMenuItem("Save");
        JMenuItem mi10 = new JMenuItem("Share");
        JMenuItem upmenui = new JMenuItem("Upload");

// Add action listener
        mi1.addActionListener(this);
        mi2.addActionListener(this);
        mi3.addActionListener(this);
        mi10.addActionListener(this);
        upmenui.addActionListener(this);
        //downmenui.addActionListener(this);
        m1.add(mi1);
        
        m1.add(mi2);
        m1.add(mi3);
        m1.add(mi10);
        m1.add(upmenui);


// Create a menu for menu
        JMenu m2 = new JMenu("Edit");
// Create menu items
        JMenuItem mi4 = new JMenuItem("cut");
        JMenuItem mi5 = new JMenuItem("copy");
        JMenuItem mi6 = new JMenuItem("paste");
// Add action listener
        mi4.addActionListener(this);
        mi5.addActionListener(this);
        mi6.addActionListener(this);
        m2.add(mi4);
        m2.add(mi5);
        m2.add(mi6);
        JMenuItem mc = new JMenuItem("close");
        mc.addActionListener(this);
        mb.add(m1);
        mb.add(m2);
        mb.add(mc);
        f.setJMenuBar(mb);
        f.add(t);
        f.setSize(500, 500);
        f.show();
        t.addKeyListener(this);
    }
    public void ChangeText(String str) {
        t.setText(str);
    }
// If a button is pressed
    public void actionPerformed(ActionEvent e) {
        String s = e.getActionCommand();
        if (s.equals("cut")) {
            t.cut(); //inbuilt function
        } else if (s.equals("copy")) {
            t.copy();
        } else if (s.equals("paste")) {
            t.paste();
 }
 else if (s.equals("Save")) {
 // Create an object of JFileChooser class
 JFileChooser j = new JFileChooser("c:"); //shows default drive save location as c
 // Invoke the showsSaveDialog function to show the save dialog
 int r;
 r = j.showSaveDialog(null);
 if (r == JFileChooser.APPROVE_OPTION) { //if u select file location where u want to store
 // Set the label to the path of the selected directory
 File fi = new File(j.getSelectedFile().getAbsolutePath());
 try {
 // Create a file writer that doesn't append
 FileWriter wr = new FileWriter(fi, false); //false so, that if u select a file that al ready has text, new text will overwrite previous text
//if true, new text will be appended to old text

 // Create buffered writer to write
 BufferedWriter w = new BufferedWriter(wr); //writing the content in desired location
 // Write
 w.write(t.getText());
 w.flush();
 w.close();
 } catch (Exception evt) {
 JOptionPane.showMessageDialog(f, evt.getMessage());
 }
 }
 // If the user cancelled the operation
 else
 JOptionPane.showMessageDialog(f, "the user cancelled the operation");//Popup
 }
 else if (s.equals("Open")) {
 // Create an object of JFileChooser class
 JFileChooser j = new JFileChooser("f:"); //default location will be f drive
 // Invoke the showsOpenDialog function to show the save dialog
 int r;
 r = j.showOpenDialog(null);

 // If the user selects a file
 if (r == JFileChooser.APPROVE_OPTION) {
 // Set the label to the path of the selected directory
 File fi = new File(j.getSelectedFile().getAbsolutePath());
 try {
 // String
 String s2;
 // File reader
 FileReader fr = new FileReader(fi);
 // Buffered reader
 BufferedReader br = new BufferedReader(fr); //to display a saved file
 // Initailise sl
 String sl;
 sl = br.readLine();
 // Take the input from the file
 while ((s2 = br.readLine()) != null) {
 sl = sl + "\n" + s2;
 }
 // Set the text

 t.setText(sl); //the fetched text from the opened file is displayed in text box
 } catch (Exception evt) {
 JOptionPane.showMessageDialog(f, evt.getMessage());
 }
 }
 // If the user cancelled the operation
 else
 JOptionPane.showMessageDialog(f, "the user cancelled the operation");
 }
 else if (s.equals("New")) {
 t.setText("");
 }
 else if (s.equals("close")) {
 f.setVisible(false); //frame vanishes
 try {
 ClientInput.close();
 socClient.close();
 ClientOutput.close();
 } catch (IOException e1) {
 e1.printStackTrace();
 }
 } else if (s.equals("Share")) {
 try {
 ClientOutput.writeUTF("Share"); //sender to server
 ClientOutput.flush();


} catch (IOException e1) {
 e1.printStackTrace();
 }
 ClientIDToShare = JOptionPane.showInputDialog("Enter the ID's of the Client to send this text..");
 try {
 ClientOutput.writeUTF(ClientIDToShare); //tells server that this id will get shared contents
 ClientOutput.flush();
 } catch (IOException e1) {
 e1.printStackTrace(); //exception s stack trace
 }
 }
 else if(s.equals("Upload"))
 {
 JFileChooser j = new JFileChooser("f:"); //default location will be f drive
 // Invoke the showsOpenDialog function to show the save dialog
 int r;
 r = j.showOpenDialog(null);
 // If the user selects a file
 if (r == JFileChooser.APPROVE_OPTION) {
 // Set the label to the path of the selected directory

 File fi = new File(j.getSelectedFile().getAbsolutePath());
 
 try {
ClientOutput.writeUTF("Upload");
ClientOutput.flush();
ClientOutput.writeUTF(fi.getName());
ClientOutput.flush();
} catch (IOException e2) {
// TODO Auto-generated catch block
e2.printStackTrace();
}
 try {
 // String
 String s2;
 // File reader
 FileReader fr = new FileReader(fi);
 // Buffered reader
 BufferedReader br = new BufferedReader(fr); //to display a saved file
 // Initailise sl
 String sl;
 sl = br.readLine();

 // Take the input from the file
 while ((s2 = br.readLine()) != null) {
 sl = sl + "\n" + s2;
 }
 // Set the text
 //t.setText(sl);
 //the fetched text from the opened file is displayed in text box
 
 
 //SelectedText = t.getText();
 try {
 
 ClientOutput.writeUTF(sl); //to server (as u sharing text )
 ClientOutput.flush();

 } catch (IOException e1) {
 e1.printStackTrace();
 }
 
 
 } catch (Exception evt) {
 JOptionPane.showMessageDialog(f, evt.getMessage());
 }
 }

 }
 
 
 // If the user cancelled the operation
 else
 JOptionPane.showMessageDialog(f, "the user cancelled the operation");
 }
 public void keyTyped(KeyEvent e) {}
 public void keyPressed(KeyEvent e) {}
 public void keyReleased(KeyEvent e) {
 SelectedText = t.getText();
 try {
 
 ClientOutput.writeUTF(SelectedText); //to server (as u sharing text )
 ClientOutput.flush();
 } catch (IOException e1) {
 e1.printStackTrace();
 }
 }

}