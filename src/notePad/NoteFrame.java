package notePad;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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
    private JMenuItem close;

    private JTextArea textField;
    private JScrollPane scrollPane;

    private Clipboard clip;

    public NoteFrame(){
        this.setTitle("untitled* \nNotePad");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
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
        close = new JMenuItem("Close");
        copy = new JMenuItem("Copy");
        cut = new JMenuItem("Cut");
        paste = new JMenuItem("Paste");
        about = new JMenuItem("About");

        this.open.addActionListener(this);
        this.save.addActionListener(this);
        this.close.addActionListener(this);

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
        this.file.add(close);
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
                this.setTitle(filePath);
                this.open(filePath);
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
        }
    }

    private void open(String path){
        StringBuilder str = new StringBuilder();
        try(FileInputStream fis = new FileInputStream(new File(path))) {
            byte[] bites = new byte[8];
            while (fis.read(bites) != -1){
                for(byte b : bites){
                    str.append((char)b);
                }
                bites = new byte[8];
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        //System.out.println(str.toString());
        this.textField.setText(str.toString());
    }
}
