package top.xinsin;

import net.minecraft.nbt.NbtCompound;
import org.junit.Test;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NbtDemo {
    @Test
    public void testNbt() throws IOException {
        NbtCompound nbtCompound = new NbtCompound();
        NbtCompound child = new NbtCompound();
        child.putBoolean("abc",false);
        nbtCompound.put("",child);
        File file = new File("demo.nbt");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        DataOutputStream dataOutputStream = new DataOutputStream(fileOutputStream);
        nbtCompound.write(dataOutputStream);
    }

    @Test
    public void textSimpleDate(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(simpleDateFormat.format(new Date()));
    }
}
