package com.woodywoodpecker.startmotion.imageslist;

import com.telerik.everlive.sdk.core.model.base.DataItem;
import com.telerik.everlive.sdk.core.serialization.ServerProperty;

public class FileObject extends DataItem {
	@ServerProperty("Name")
	private String downloadURI;

	public String getDownloadURI() {
		return downloadURI;
	}

	public void setDownloadURI(String downloadURI) {
		this.downloadURI = downloadURI;
	}
}