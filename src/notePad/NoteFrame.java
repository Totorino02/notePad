package notePad;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.stream.IntStream;

public class NoteFrame extends JFrame implements ActionListener {
    private final int frameWidth = 800;
    private final int frameHeight = 500;

    private String filePath;
    private JMenuBar menu;
    private JMenu file;
    private JMenu edit;
    private JMenu help;
    private JMenuItem copy;
    private JMenuItem cut;
    private JMenuItem paste;
    private JMenuItem about;
    private JMenuItem open;
    private JMenuItem save;
    private JMenuItem exit;

    private JTextArea textField;
    private JScrollPane scrollPane;

    private Clipboard clip;

    public NoteFrame(){
        this.setTitle("untitled* \nNotePad");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //this.setResizable(false);
        this.setLayout(new BorderLayout());
        this.setSize(new Dimension(frameWidth, frameHeight));
        this.setPreferredSize(new Dimension(frameWidth, frameHeight));
        this.setLocationRelativeTo(null );
        this.pack();

        this.clip = getToolkit().getSystemClipboard();

        this.textField = new JTextArea();
        this.textField.setFont(new Font("",Font.PLAIN,18));
        this.textField.setEditable(true);
        this.textField.setLineWrap(true);
        this.textField.setWrapStyleWord(true);
        scrollPane = new JScrollPane(textField);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setVisible(true);
        menu = new JMenuBar();

        //Menus
        file = new JMenu("File");
        edit = new JMenu("Edit");
        help = new JMenu("Help");
        //Menu items
        open = new JMenuItem("Open");
        save = new JMenuItem("Save");
        exit = new JMenuItem("Exit");
        copy = new JMenuItem("Copy");
        cut = new JMenuItem("Cut");
        paste = new JMenuItem("Paste");
        about = new JMenuItem("About");

        this.open.addActionListener(this);
        this.save.addActionListener(this);
        this.exit.addActionListener(this);

        this.copy.addActionListener(this);
        this.cut.addActionListener(this);
        this.paste.addActionListener(this);
        this.about.addActionListener(this);

        this.menu.add(file);
        this.menu.add(edit);
        this.menu.add(help);
        //
        this.file.add(open);
        this.file.add(save);
        this.file.add(exit);
        this.edit.add(copy);
        this.edit.add(cut);
        this.edit.add(paste);
        this.help.add(about);
        //
        this.setJMenuBar(menu);
        this.add(scrollPane);

        this.setVisible(true);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == this.open){
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File("."));
            int response = fileChooser.showOpenDialog(this);
            if(response == JFileChooser.APPROVE_OPTION){
                filePath = fileChooser.getSelectedFile().getAbsolutePath();
                this.setTitle(fileChooser.getSelectedFile().getName()+"\nNotePad");
                this.textField.setText("");
                this.open(filePath);
            }
        }else if(e.getSource() == this.save){
            JFileChooser fileChooser = new JFileChooser();
            int response = fileChooser.showSaveDialog(this);
            if(response == JFileChooser.APPROVE_OPTION){
                filePath = fileChooser.getSelectedFile().getAbsolutePath();
                this.setTitle(fileChooser.getSelectedFile().getName()+"\nNotePad");
                saveFile(filePath);
            }
        }else if(e.getSource() == this.copy){
            this.textField.copy();
            String toCopy = this.textField.getText();
            StringSelection stringSelection = new StringSelection(toCopy);
            clip.setContents(stringSelection, stringSelection);
        }else if(e.getSource() == this.cut){
            this.textField.cut();
            String toCopy = this.textField.getText();
            StringSelection stringSelection = new StringSelection(toCopy);
            clip.setContents(stringSelection, stringSelection);
        }else if (e.getSource() == this.paste){
            this.textField.paste();
        }else if(e.getSource() == this.about){
            String str = "Cloned NotePad application using Java\nCopyright HOUNSI Antoine\nAll Right Reserved";
            JOptionPane.showMessageDialog(this,str,"information",JOptionPane.INFORMATION_MESSAGE);
            //showHelp
        }else if(e.getSource() == this.exit){
            String str = "Are you sure you want to exit?";
            int response = JOptionPane.showConfirmDialog(this,str,"Confirm Exit",JOptionPane.YES_NO_OPTION);
            if(response == 0){
                this.dispose();
            }
        }
    }

    private void open(String path){
        StringBuilder str = new StringBuilder();
        try(BufferedReader fis = new BufferedReader(new InputStreamReader(new FileInputStream(new File(path))))) {
           this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            String data;
            while((data = fis.readLine() )!= null){
                this.textField.setText(textField.getText()+data+"\r\n");
            }
            this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void saveFile(String path){
        try(DataOutputStream out = new DataOutputStream(new FileOutputStream(new File(path)))) {
            this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            out.writeBytes(this.textField.getText());
            this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
