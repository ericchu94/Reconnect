package net.ericchu.reconnect;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.EditText;

public class NameDialogFragment extends DialogFragment {
    public static final String ARGS_NAME = "name";

    private NameDialogListener mListener;
    private EditText mEditText;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (NameDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement NameDialogListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mEditText = (EditText) getActivity().getLayoutInflater().inflate(R.layout.dialog_name, null);

        Bundle args = getArguments();
        if (args != null) {
            mEditText.setText(args.getString(ARGS_NAME, ""));
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialog_enter_name)
                .setView(mEditText)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onNameChange(mEditText.getText().toString());
                    }
                });
        return builder.create();
    }

    public interface NameDialogListener {
        public void onNameChange(String name);
    }
}
