package pl.edu.agh.view.help;

import java.io.Serializable;

import com.example.ioproject.R;

public enum HelpItem implements Serializable {
	
	LOCATION_HELP(R.string.HelpView_HelpItemTitle_ShowLocation, R.drawable.globe, R.layout.help_content_localization),
	ADD_EVENT_HELP(R.string.HelpView_HelpItemTitle_AddEvent, R.drawable.add, R.layout.help_content_addevent),
	DELETE_EVENT_HELP(R.string.HelpView_HelpItemTitle_DeleteEvent, R.drawable.delete_event, R.layout.help_content_delete_event),
	ADD_DEFAULT_LOCATION(R.string.HelpView_HelpItemTitle_DefaultLocations, R.drawable.location_map, R.layout.help_content_default_locations),
	SHOW_EVENT_INFO(R.string.HelpView_HelpItemTitle_ShowEventInfo, R.drawable.calendar_info, R.layout.help_content_show_event_info),
	GO_BACK(R.string.HelpView_HelpItemTitle_GoBack, R.drawable.arrow_back, R.layout.help_content_goback);
	
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
