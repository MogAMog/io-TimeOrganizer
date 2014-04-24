package pl.edu.agh.view.help;

import com.example.ioproject.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HelpContentFragment extends Fragment {

	public static final String HELP_POSITION_SELECTED = "HelpPositionSelected";
	private int currentPosition = 0;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		currentPosition = ((HelpListFragment)getActivity().getFragmentManager().findFragmentById(R.id.HelpView_List)).getCurrentlySelectedId();
		HelpItem item = HelpItem.getHelpItemById(currentPosition);
		return inflater.inflate(item.getLayoutId(), container, false);
	}
	
	
}
