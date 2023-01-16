package top.xinsin;

import org.junit.Test;

import java.io.File;

public class CopyTest {
    public static void copy(File source, File dest){
        if (source.isDirectory()){
            for (File file : source.listFiles()) {
//                copy(file, new File(dest, new File(file.getPath().replace(source.getPath(), ""))));
            }
        }

    }
    @Test
    public void testSlotNext(){
//        FileUtilByBackup.slotNext(new File("run/backup"));
//        new File("run/backup/slot1").mkdir();
    }
}
