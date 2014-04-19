package pl.edu.agh.view.fragments.dialogs;

import java.util.List;

import com.example.ioproject.R;

import pl.edu.agh.errors.FormValidationError;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class ErrorDialog {

	public static Dialog createDialog(Context context, List<FormValidationError> errors) {
		final Dialog dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_errors);
		StringBuilder builder = new StringBuilder();
		for(FormValidationError error : errors) {
			builder.append(context.getResources().getString(error.getMessageKey())).append("\n");
		}
		((TextView)dialog.findViewById(R.id.ErrorDialog_errorListTextView)).setText(builder.toString());
		((TextView)dialog.findViewById(R.id.ErrorDialog_okButton)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		return dialog;
	}
	
	
}
