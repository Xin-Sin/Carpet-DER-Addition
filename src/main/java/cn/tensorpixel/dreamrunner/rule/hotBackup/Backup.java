package cn.tensorpixel.dreamrunner.rule.hotBackup;

import cn.tensorpixel.dreamrunner.entity.BackupData;
import cn.tensorpixel.dreamrunner.util.FileUtilByBackup;

import java.io.File;

public class Backup implements Runnable {
    private BackupData backupData;

    public Backup(BackupData backupData) {
        this.backupData = backupData;
    }

    private void back(BackupData backupData){
        FileUtilByBackup.copyFolder(new File("world"),new File("backup/slot1"),backupData);
    }

    @Override
    public void run() {
        back(this.backupData);
    }
}
