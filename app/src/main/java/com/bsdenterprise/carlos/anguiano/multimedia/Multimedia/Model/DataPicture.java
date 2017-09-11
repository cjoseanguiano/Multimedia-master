package com.bsdenterprise.carlos.anguiano.multimedia.Multimedia.Model;

import java.io.Serializable;

/**
 * Created by Carlos Anguiano on 05/09/17.
 * For more info contact: c.joseanguiano@gmail.com
 */

public class DataPicture implements Serializable {
    private String filePath;
    private String fileType;
    private String bucket;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

}
