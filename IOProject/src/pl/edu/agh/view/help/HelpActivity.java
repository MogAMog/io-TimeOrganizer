package pl.edu.agh.view.help;

import com.example.ioproject.R;

import android.os.Bundle;
import android.app.Activity;
import android.app.FragmentTransaction;

public class HelpActivity extends Activity implements HelpListFragment.OnHelpItemSelectedListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);
		setupActionBar();
		
		getFragmentManager().beginTransaction()
			.add(R.id.HelpView_List, new HelpListFragment())
			.add(R.id.HelpView_Content, new HelpContentFragment())
		.commit();
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public void onHelpItemSelected() {
		FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
			fragmentTransaction.add(R.id.HelpView_Content, new HelpContentFragment());
		fragmentTransaction.commit();
	}
}
