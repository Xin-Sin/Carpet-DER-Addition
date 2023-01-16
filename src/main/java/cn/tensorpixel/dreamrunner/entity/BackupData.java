package cn.tensorpixel.dreamrunner.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BackupData {
    private String commit;
    private Date createTime;
    private String creator;

}
