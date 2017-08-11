package com.app.truxapp.leasedriver.Model;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;

import java.io.File;

public class UploadImage {
	public TransferObserver observer;
	public String imageUrl;
	public File UPLOADING_IMAGE;
	public File UPLOADING_THUMB_IMAGE;
	public String imagePath;
	public String imageThumbPath;
	public String thumbImageUrl;
	public boolean isThumb;
    public String directoryName;
	public String key;
	public String pasword;
}
