package it.pleaseopen.keepasstray.ui;

import it.pleaseopen.keepasstray.keepassdatabase.EntryWithPrint;
import it.pleaseopen.keepasstray.keepassdatabase.KeepassDatabaseService;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.List;

public class SearchPanel extends JFrame {

    PlaceholderTextField searchField = new PlaceholderTextField(15);
    JButton exit = new JButton("Exit");
    JButton open = new JButton("Open Database");
    JList<EntryWithPrint> searchResults = new JList();
    JFrame frame;

    public SearchPanel(){
        super("Please-open.it keepass extension");
        searchField.setPlaceholder("Search ...");
        this.frame = this;
        setUndecorated(true);
        setAlwaysOnTop(true);
        setLayout(new BorderLayout());
        setSize(50, 100);

        searchResults.setPreferredSize(new Dimension(100, 100));

        searchResults.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                EntryWithPrint entryWithPrint = searchResults.getSelectedValue();
                // copy password in the clipboard
                // from https://stackoverflow.com/questions/6710350/copying-text-to-the-clipboard-using-java
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                StringSelection stringSelection = new StringSelection(entryWithPrint.getEntry().getPassword());
                clipboard.setContents(stringSelection, null);
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }

                frame.dispose();
            }
        });

        searchField.selectAll();
        searchField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                List<EntryWithPrint> entries = KeepassDatabaseService.getInstance().search(searchField.getText());
                searchResults.setListData(entries.toArray(new EntryWithPrint[entries.size()]));
            }

            @Override
            public void keyPressed(KeyEvent e) { }

            @Override
            public void keyReleased(KeyEvent e) { }
        });
        add(searchField, BorderLayout.NORTH);
        add(searchResults, BorderLayout.CENTER);

        JPanel panelButtons = new JPanel();
        panelButtons.setLayout(new FlowLayout());
        panelButtons.add(open);

        open.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File databseFile = getKeypassDatabase();
                File key = getKey();

                // from https://stackoverflow.com/questions/8881213/joptionpane-to-get-password
                JPanel panel = new JPanel();
                JLabel label = new JLabel("Password:");
                JPasswordField pass = new JPasswordField(10);
                panel.add(label);
                panel.add(pass);
                String[] options = new String[]{"OK", "Cancel"};
                int option = JOptionPane.showOptionDialog(null, panel, "Keepass database password",
                        JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE,
                        null, options, options[0]);
                if(option == 0) // pressing OK button
                {
                    char[] password = pass.getPassword();
                    String passwordString = new String(password);
                    KeepassDatabaseService.getInstance().loadDatabase(passwordString, key, databseFile);
                }else{
                    KeepassDatabaseService.getInstance().loadDatabase(null, key, databseFile);
                }
            }
        });

        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(1);
            }
        });
        panelButtons.add(exit);
        add(panelButtons, BorderLayout.SOUTH);
        pack();
    }

    private File getKeypassDatabase(){
        JFileChooser fileChooser = new JFileChooser(
                FileSystemView
                        .getFileSystemView()
                        .getHomeDirectory()
        );
        fileChooser.setDialogTitle("Open Keypass database");
        fileChooser.addChoosableFileFilter(new FileFilter() {
            public String getDescription() {
                return "Keepass database (*.kdbx)";
            }

            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                } else {
                    return f.getName().toLowerCase().endsWith(".kdbx");
                }
            }
        });
        int res = fileChooser.showOpenDialog(null);
        if (res == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }
        return null;
    }

    private File getKey(){
        JFileChooser fileChooser = new JFileChooser(
                FileSystemView
                        .getFileSystemView()
                        .getHomeDirectory()
        );
        fileChooser.setDialogTitle("Open private Key (optional)");
        fileChooser.addChoosableFileFilter(new FileFilter() {
            public String getDescription() {
                return "private Key (key)";
            }

            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                } else {
                    return f.getName().toLowerCase().endsWith(".key");
                }
            }
        });
        int res = fileChooser.showOpenDialog(null);
        if (res == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }
        return null;
    }

}
