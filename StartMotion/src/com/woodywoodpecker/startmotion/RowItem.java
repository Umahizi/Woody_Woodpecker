package com.woodywoodpecker.startmotion;

import android.R.string;
import android.graphics.Bitmap;
import android.net.Uri;


public class RowItem {

	private CharSequence frameNumber;
	private int image;
	

	
	public CharSequence getFrameNumber() {
		
		return this.frameNumber;
	}
	
	public void setFrameNumber(int count) {
		if(count<10){
			this.frameNumber="00"+String.valueOf(count);
		}
		else{
			this.frameNumber="0"+String.valueOf(count);
		}
		
		
		
	}

	public int getImage() {
		return image;
	}

	public void setImage(int icLauncher) {
		this.image = icLauncher;
	}
	
	
}
