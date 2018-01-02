package com.zerter.teamconnect.Message;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.zerter.teamconnect.Controlers.Data;
import com.zerter.teamconnect.Controlers.MyTextView;
import com.zerter.teamconnect.Models.Template;
import com.zerter.teamconnect.R;

import java.util.List;

public class Message extends Fragment {


    private final String TAG = getClass().getName();
    Button wyslij;
    EditText editTextTrescWiadomosci;
    ListView listView;
    Data data;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_message,container,false);

        wyslij = (Button) view.findViewById(R.id.button_wyslij);
        editTextTrescWiadomosci = (EditText) view.findViewById(R.id.editText_tresc_smsa);
        listView = (ListView) view.findViewById(R.id.ListViewListaGrupKontaktow);
        return view;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        data = new Data(getActivity());
        setAdapter();
        wyslij.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextTrescWiadomosci.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(), R.string.message_empty_error,Toast.LENGTH_SHORT).show();
                }else {
                    FragmentManager manager = getFragmentManager();
                    SelectGroupToSendMessage sendMessaegeDialog = new SelectGroupToSendMessage();
                    Bundle bundle = new Bundle();
                    bundle.putString("message", editTextTrescWiadomosci.getText().toString());
                    sendMessaegeDialog.setArguments(bundle);
                    sendMessaegeDialog.show(manager, "SelectGroupToSendMessage");
                    editTextTrescWiadomosci.setText("");
                }
            }
        });

        hideSoftKeyboard(getActivity());
    }


    @Override
    public void onStart() {
        super.onStart();

    }

    private void setAdapter(){
        if (data.getTemplates() != null){
            ListAdapterTemplates adapter = new ListAdapterTemplates(getActivity(),data.getTemplates());
            listView.setAdapter(adapter);
        }
    }
    private class ListAdapterTemplates extends ArrayAdapter<Template> {

        public ListAdapterTemplates(@NonNull Context context, List<Template> templates) {
            super(context, 0, templates);
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            final Template template = getItem(position);

            if (convertView == null){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.template_item_list,parent,false);
            }
            final MyTextView myTextView = (MyTextView) convertView.findViewById(R.id.templateTextView);
            if (template != null) {
                myTextView.setText(template.getText());
            }else {
                myTextView.setText("n/a");
            }

            myTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editTextTrescWiadomosci.setText(myTextView.getText().toString());
                }
            });
            return convertView;
        }


    }

    public void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null && activity.getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }else {
//            Toast.makeText(activity, "error hideSoftKeyboard", Toast.LENGTH_SHORT).show();
            Log.e("hideSoftKeyboard","nputMethodManager == null && activity.getCurrentFocus() == null");
        }
    }
}
