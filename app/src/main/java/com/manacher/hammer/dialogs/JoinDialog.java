package com.manacher.hammer.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.Fragment;

import com.manacher.hammer.R;

public class JoinDialog extends AppCompatDialogFragment {
    private Button yesButton;
    private Button noButton;
    private EditText editText;
    private JoinDialogListener listener;
    private Fragment context;

    public JoinDialog(Fragment fragment) {
        this.context = fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity(), R.style.CustomAlertDialog);
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.join_dialog, null);
        builder.setView(view);

        listener = (JoinDialogListener) context;
        yesButton = view.findViewById(R.id.yesButton);
        noButton = view.findViewById(R.id.noButton);
        editText = view.findViewById(R.id.editText);

        listener.joinDialogButton(editText, yesButton, noButton);

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {

        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement ExampleDialogListener");
        }
    }
    public interface JoinDialogListener {
        void joinDialogButton(EditText editText, Button yesButton, Button noButton);
    }
}
