package be.uantwerpen.fti.ei.distributed.lab3.Server;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SynchronizedFileList {

    public static List<File> fileQueue = new ArrayList<>();

    public static synchronized void addToList(File file){
        fileQueue.add(file);
    }

    public static synchronized void removeFromList(File file){
        fileQueue.remove(file);
    }


}
