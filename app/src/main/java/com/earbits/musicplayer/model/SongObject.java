package com.earbits.musicplayer.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SongObject implements Parcelable {


	private String name;
	private String artist_name;
	private String cover_image;
	private String media_file;

	public SongObject() {
	}

    /**
     * Implementation for Parcelable.
     */
	public SongObject(Parcel in) {
		this.name = in.readString();
		this.artist_name = in.readString();
		this.cover_image = in.readString();
		this.media_file = in.readString();

	}
	
	/* Implementation for Parcelable */
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(this.name);
		parcel.writeString(this.artist_name);
		parcel.writeString(this.cover_image);
		parcel.writeString(this.media_file);

	}

	/** */

    public static final Creator<SongObject> CREATOR
    							= new Creator<SongObject>() {
											public SongObject createFromParcel(Parcel in) {
											    return new SongObject(in);
											}
										
											public SongObject[] newArray(int size) {
											    return new SongObject[size];
											}
		};
    

    public String getSongName() {
    	return name;
    }

	public String getArtistName() {
		return artist_name;
	}

	public String getCoverImage() {
		return cover_image;
	}

	public String getMediaFile() {
		return media_file;
	}


}