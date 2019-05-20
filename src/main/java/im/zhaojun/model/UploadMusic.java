package im.zhaojun.model;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UploadMusic {
    private String id;
    private String musicName;
    private String musicType;
    private String musicSize;
    private String musicPath;
    private int downloadNum;
    private Date uploadTime;
    private User user;

    public UploadMusic() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMusicName() {
        return musicName;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }

    public String getMusicType() {
        return musicType;
    }

    public void setMusicType(String musicType) {
        this.musicType = musicType;
    }

    public String getMusicSize() {
        double v = 0;
        if (musicSize != null) {
            v = Double.parseDouble(musicSize);
            v = v / 1024 / 1024;
            BigDecimal bg = new BigDecimal(v);
            v = bg.setScale(2, BigDecimal.ROUND_CEILING).doubleValue();
        }
        return v+"M";
    }

    public void setMusicSize(String musicSize) {
        this.musicSize = musicSize;
    }

    public String getMusicPath() {
        return musicPath;
    }

    public void setMusicPath(String musicPath) {
        this.musicPath = musicPath;
    }

    public int getDownloadNum() {
        return downloadNum;
    }

    public void setDownloadNum(int downloadNum) {
        this.downloadNum = downloadNum;
    }

    public String getUploadTime() {

        String newTime = null;
        if (uploadTime != null) {
            String strFor = "";
            int hours = uploadTime.getHours();
            if (hours < 12) {
                strFor = "yy年MM月dd日 上午HH:mm";
            } else if (hours == 12) {
                strFor = "yy年MM月dd日 中午HH:mm";
            } else if (hours < 18) {
                strFor = "yy年MM月dd日 下午HH:mm";
            } else {
                strFor = "yy年MM月dd日 晚上HH:mm";
            }
            SimpleDateFormat format = new SimpleDateFormat(strFor);
            newTime = format.format(uploadTime);
        }

        return newTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "UploadMusic{" +
                "id='" + id + '\'' +
                ", musicName='" + musicName + '\'' +
                ", musicType='" + musicType + '\'' +
                ", musicSize='" + musicSize + '\'' +
                ", musicPath='" + musicPath + '\'' +
                ", downloadNum=" + downloadNum +
                ", uploadTime=" + uploadTime +
                '}';
    }
}
