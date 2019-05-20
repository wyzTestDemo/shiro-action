package im.zhaojun.model.dynamic;

import im.zhaojun.model.UploadMusic;
import org.apache.ibatis.jdbc.SQL;

public class BatchUpload {
    public String dynamicUploadMusicUpd(UploadMusic uploadMusic) {
        System.out.println("---------------------->");
        System.out.println(uploadMusic);
        return new SQL() {{

            UPDATE("uploadmusic");
            if(uploadMusic.getMusicName()!=null){
                SET("musicName=#{musicName}");
            }
            if (uploadMusic.getMusicType() != null) {
                SET("musicType = #{musicType}");
            }
            if (uploadMusic.getMusicSize() != null) {
                SET("musicSize = #{musicSize}");
            }
            if (uploadMusic.getMusicPath() != null) {
                SET("musicPath=#{musicPath}");
            }
            if (uploadMusic.getDownloadNum()>0) {
                SET("downloadNum=#{downloadNum}");
            }
            if(uploadMusic.getUploadTime()!=null){
                SET("uploadTime=#{uploadTime}");
            }
            WHERE("id =#{id}");
        }}.toString();
    }
}
