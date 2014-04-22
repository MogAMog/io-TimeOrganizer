package pl.edu.agh.view.addevent;

import java.util.List;

import android.app.Activity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class SimpleItemSpinner<E> {
	
	private Spinner listSpinner;
	private E noSelectionItem;
	
	public SimpleItemSpinner(Activity activity, int spinnerId, E noSelectionItem, List<E> itemList) {
		this.listSpinner = (Spinner) activity.findViewById(spinnerId);
		this.noSelectionItem = noSelectionItem;
		if(!itemList.contains(noSelectionItem)) {
			itemList.add(noSelectionItem);
		}
		listSpinner.setAdapter(new ArrayAdapter<E>(activity, android.R.layout.simple_spinner_item, itemList));
	}
	
	public boolean checkIfSelectionWasMade() {
		return !getSelectedItem().equals(noSelectionItem);
	}

	@SuppressWarnings("unchecked")
	public E getSelectedItem() {
		return (E)listSpinner.getSelectedItem();
	}
	
	public Spinner getListSpinner() {
		return listSpinner;
	}
	
	public void setSelectedPosition(int position) {
		listSpinner.setSelection(position);
	}
}
