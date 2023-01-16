package cn.tensorpixel.dreamrunner.util;

import cn.tensorpixel.dreamrunner.entity.BackupData;
import lombok.SneakyThrows;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.UUID;

import static cn.tensorpixel.dreamrunner.CarpetDERAddition.log;

public class FileUtilByBackup {
    @SneakyThrows
    public static boolean copyFolder(File source,File copy,BackupData backupData){
        copy = new File(copy,"slot1");
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
            if(!dest.getName().equals(StringConstant.SESSION_LOCK)){
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
        log.debug("{} -> {}", source.getPath(), dest.getPath());
        return true;
    }
    public static void delete(File src){
        if(src.isDirectory()){
            for (File file : src.listFiles()) {
                delete(file);
            }
        }
        src.delete();
    }
//    backup
    private static boolean slotNext(File src){
        for (int i = 5; i >= 1; i--) {
            File file = new File(src, StringConstant.SLOT + i);
            if(!file.exists()){
                file.mkdir();
            }
            if(i != 5){
                files(file, new File(src, StringConstant.SLOT + (i + 1)));
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
        child.putString("creator", backupData.getCreator());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        child.putString("date",simpleDateFormat.format(backupData.getCreateTime()));
        compound.put("",child);
        DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(src));
        compound.write(dataOutputStream);
        dataOutputStream.close();
        log.debug("备份描述文件写入成功");
    }

    public static StringBuilder list(File src){
        StringBuilder sb = new StringBuilder();
        sb.append("====================================================")
            .append("\n");
        File[] files = src.listFiles();
        assert files != null;
        for (int i = 0; i <files.length ; i++) {
            File file1 = new File(src, files[i].getName());
            File[] files1 = file1.listFiles();
            assert files1 != null;
            if (files1.length != 2){
                sb.append("槽位")
                        .append(i + 1)
                        .append("-")
                        .append("空")
                        .append("\n");
            }
            for (File file : files1) {
                if (file.getName().equals(StringConstant.DESCRIPTION_NBT)){
                    NbtCompound read = null;
                    try {
                        read = NbtIo.read(new File(file.getPath()));
                        if (read != null){
                            String date = read.getString("date");
                            String creator = read.getString("creator");
                            String commit = read.getString("commit");
                            sb.append("槽位")
                                    .append(i + 1)
                                    .append("-")
                                    .append("创建者: ")
                                    .append(creator)
                                    .append(" ")
                                    .append("注释: ")
                                    .append(commit.equals("") ? "空" : commit)
                                    .append(" ")
                                    .append("日期: ")
                                    .append(date)
                                    .append("\n");
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        sb.append("====================================================");
        return sb;
    }
}
