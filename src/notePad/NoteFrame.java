package notePad;

import javax.swing.*;
import java.awt.*;
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
    private JMenu copy;
    private JMenu paste;
    private JMenu about;
    private JMenuItem open;
    private JMenuItem save;
    private JMenuItem close;

    private JTextArea textField;
    private JScrollPane scrollPane;

    public NoteFrame(){
        this.setTitle("NotePad Application\nok");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLayout(new BorderLayout());
        this.setSize(new Dimension(frameWidth, frameHeight));
        this.setPreferredSize(new Dimension(frameWidth, frameHeight));
        this.setLocationRelativeTo(null);
        this.pack();

        textField = new JTextArea();
        this.textField.setFont(new Font("",Font.PLAIN,18));
        scrollPane = new JScrollPane(textField);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setVisible(true);
        menu = new JMenuBar();
        //Menus
        file = new JMenu("File");
        copy = new JMenu("Copy");
        paste = new JMenu("Paste");
        about = new JMenu("About");
        //Menu items
        open = new JMenuItem("Open");
        save = new JMenuItem("Save");
        close = new JMenuItem("Close");

        this.open.addActionListener(this);
        this.save.addActionListener(this);
        this.close.addActionListener(this);


        this.menu.add(file);
        this.menu.add(copy);
        this.menu.add(paste);
        this.menu.add(about);
        //
        this.file.add(open);
        this.file.add(save);
        this.file.add(close);
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
