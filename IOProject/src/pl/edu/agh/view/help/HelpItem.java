package pl.edu.agh.view.help;

import java.io.Serializable;

import com.example.ioproject.R;

public enum HelpItem implements Serializable {
	
	LOCATION_HELP(R.string.EventDate_Location, R.drawable.globe, R.layout.help_content_localization),
	ADD_EVENT_HELP(R.string.Event_Title, R.drawable.add, R.layout.help_content_addevent);
	
	private final int titleID; //string.xml
	private final int pictureID;	//res/drawable/*
	private final int layoutId;		//res/layout/*
	
	private HelpItem(int titleID, int pictureID, int layoutId) {
		this.titleID = titleID;
		this.pictureID = pictureID;
		this.layoutId = layoutId;
	}
	public int getTitleID() {
		return titleID;
	}
	
	public int getPictureID() {
		return pictureID;
	}

	public int getLayoutId() {
		return layoutId;
	}
	
	public static HelpItem getHelpItemById(int id) {
		return HelpItem.values()[id];
	}
	
}
