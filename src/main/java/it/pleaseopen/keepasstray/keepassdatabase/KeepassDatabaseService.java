package it.pleaseopen.keepasstray.keepassdatabase;

import de.slackspace.openkeepass.KeePassDatabase;
import de.slackspace.openkeepass.domain.Entry;
import de.slackspace.openkeepass.domain.KeePassFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

// from https://ourcodeworld.com/articles/read/1009/how-to-manipulate-keepass-databases-kdbx-in-java
public class KeepassDatabaseService {
    private static KeepassDatabaseService _instance = new KeepassDatabaseService();
    KeePassFile database;
    List<Entry> entries;

    private KeepassDatabaseService(){};

    public static KeepassDatabaseService getInstance(){return _instance;  }

    public void loadDatabase(String credentials, File key, File databaseFile){
        if(key == null){
            database = KeePassDatabase
                    .getInstance(databaseFile)
                    .openDatabase(credentials);
        }
        if(credentials == null){
            database = KeePassDatabase
                    .getInstance(databaseFile)
                    .openDatabase(key);
        }
        if(credentials != null && key != null){
            database = KeePassDatabase
                    .getInstance(databaseFile)
                    .openDatabase(credentials, key);
        }
        entries = database.getEntries();
    }

    public List<EntryWithPrint> search(String text){
        List<EntryWithPrint> entries = new ArrayList<>();
        for(Entry entry :  database.getEntriesByTitle(text, false)){
            entries.add(new EntryWithPrint(entry));
        }
        return entries;
    }
}
