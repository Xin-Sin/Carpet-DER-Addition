package top.xinsin;

import cn.tensorpixel.dreamrunner.rule.hotBackup.BackupRule;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class testFile {
    @Test
    public void testFile() throws IOException {
        File source = new File("run/world");
        File file = new File("run/backup/bk1");
        file.mkdir();
        File bk1 = new File(file, "bk1");
        bk1.mkdir();
        files(source,bk1);
    }
    private boolean files(File file,File cp) throws IOException {
        if (file.isDirectory()){
            File[] files = file.listFiles();
            assert files != null;
            if (files.length == 0){
//                todo 空文件夹输出
                String replace = file.getPath().replace("run\\", "");
                File file1 = new File(cp, replace);
                file1.getParentFile().mkdir();
                return file1.mkdir();
            }
            for (File file1 : files) {
                String s = file.getPath().replace("run\\", "") + File.separator + file1.getName();
                if (file1.isDirectory()){
                    File parentFile = new File(cp.getPath(), s);
                    parentFile.mkdir();
                    files(file1,cp);
                }else{
//                    todo 递归文件夹中输出
                    FileChannel inChannel = FileChannel.open(Paths.get(file.getPath() + File.separator + file1.getName()), StandardOpenOption.READ);
                    FileChannel outChannel = FileChannel.open(Paths.get(cp.getPath() + File.separator + s), StandardOpenOption.WRITE, StandardOpenOption.READ, StandardOpenOption.CREATE);
                    MappedByteBuffer inMapped = inChannel.map(FileChannel.MapMode.READ_ONLY, 0, inChannel.size());
                    MappedByteBuffer outMapped = outChannel.map(FileChannel.MapMode.READ_WRITE, 0, inChannel.size());
                    byte[] dst = new byte[inMapped.limit()];
                    inMapped.get(dst);  // 把数据读取到 dst 这个字节数组中去
                    outMapped.put(dst); // 把字节数组中的数据写出去
                    inChannel.close();
                    outChannel.close();
                }
            }
        }
        return false;
    }
    @Test
    public void testList(){
//        new BackupRule().list();
    }
}
