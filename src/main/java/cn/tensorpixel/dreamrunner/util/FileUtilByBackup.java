package cn.tensorpixel.dreamrunner.util;

import cn.tensorpixel.dreamrunner.entity.BackupData;
import lombok.SneakyThrows;
import net.minecraft.nbt.NbtCompound;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.UUID;

import static cn.tensorpixel.dreamrunner.CarpetDERAddition.log;
import static cn.tensorpixel.dreamrunner.util.StringConstant.SESSION_LOCK;

public class FileUtilByBackup {
    @SneakyThrows
    public static boolean copyFolder(File source,File copy,BackupData backupData){
        if (source.isDirectory()){
//            第一次使用时创建
            if (!copy.getParentFile().isDirectory()) {
                copy.getParentFile().mkdir();
            }
            if (!(copy.getParentFile().listFiles().length == 0)){
                slotNext(copy.getParentFile());
            }
            if (!copy.isDirectory()){
                copy.mkdir();
            }
            writeCommit(backupData,new File(copy,StringConstant.DESCRIPTION_NBT));
            return files(source,new File(copy,"world"));
        }
        return false;
    }
    @SneakyThrows
    private static boolean files(File source,File dest) {
        if (source.isDirectory()){
            dest.mkdir();
            for (File file : source.listFiles()) {
                files(file, new File(dest, file.getName()));
            }
        }else{
            if(!dest.getName().equals(SESSION_LOCK)){
                FileChannel inChannel = FileChannel.open(Paths.get(source.getPath()), StandardOpenOption.READ);
                FileChannel outChannel = FileChannel.open(Paths.get(dest.getPath()),StandardOpenOption.READ,StandardOpenOption.WRITE, StandardOpenOption.CREATE);
                MappedByteBuffer inMapped = inChannel.map(FileChannel.MapMode.READ_ONLY, 0, inChannel.size());
                MappedByteBuffer outMapped = outChannel.map(FileChannel.MapMode.READ_WRITE, 0, inChannel.size());
                byte[] dst = new byte[inMapped.limit()];
                inMapped.get(dst);
                outMapped.put(dst);
                inChannel.close();
                outChannel.close();
            }
        }
        log.info("{} -> {}", source.getPath(), dest.getPath());
        return true;
    }
    private static void delete(File src){
        if(src.isDirectory()){
            for (File file : src.listFiles()) {
                delete(file);
            }
        }
        src.delete();
    }
//    backup
    public static boolean slotNext(File src){
        for (int i = 5; i >= 1; i--) {
            File file = new File(src, "slot" + i);
            if(!file.exists()){
                file.mkdir();
            }
            if(i != 5){
                files(file, new File(src, "slot" + (i + 1)));
                delete(file);
                continue;
            }
            delete(file);
        }
        return false;
    }
    @SneakyThrows
    private static void writeCommit(BackupData backupData,File src){
        if (!src.createNewFile()){
            return;
        }
        NbtCompound compound = new NbtCompound();
        NbtCompound child = new NbtCompound();
        child.putString("commit",backupData.getCommit());
        UUID creator = backupData.getCreator();
        long leastSignificantBits = creator.getLeastSignificantBits();
        long mostSignificantBits = creator.getMostSignificantBits();
        child.putLongArray("creator", new long[]{mostSignificantBits, leastSignificantBits});
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        child.putString("date",simpleDateFormat.format(backupData.getCreateTime()));
        compound.put("",child);
        DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(src));
        compound.write(dataOutputStream);
        dataOutputStream.close();
        log.info("备份描述文件写入成功");
    }
}
