package com.wb.sc.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class ImagesItem implements Parcelable{ 
	public String name;
	public int imageNum;
	public String[] images;
	
	public static final Parcelable.Creator<ImagesItem> CREATOR = new Creator<ImagesItem>() {
		
		@Override
		public ImagesItem[] newArray(int size) {
			return new ImagesItem[size];
		}
		
		@Override
		public ImagesItem createFromParcel(Parcel source) {
			ImagesItem imagesItem = new ImagesItem();
			imagesItem.name = source.readString();
			imagesItem.imageNum = source.readInt();
			imagesItem.images = new String[imagesItem.imageNum];
			source.readStringArray(imagesItem.images);
			return imagesItem;
		}
	};
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeInt(imageNum);
		dest.writeStringArray(images);
	}
}
