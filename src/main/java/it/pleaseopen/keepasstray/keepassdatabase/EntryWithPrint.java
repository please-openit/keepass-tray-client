package it.pleaseopen.keepasstray.keepassdatabase;

import de.slackspace.openkeepass.domain.Entry;

public class EntryWithPrint {
    Entry entry;

    public EntryWithPrint(Entry entry){
        this.entry = entry;
    }
    public String toString(){
        return this.entry.getTitle() + " | " + this.entry.getUsername();
    }

    public Entry getEntry() {
        return entry;
    }
}
