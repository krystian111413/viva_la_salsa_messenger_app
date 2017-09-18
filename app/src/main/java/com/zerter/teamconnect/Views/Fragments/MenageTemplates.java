package com.zerter.teamconnect.Views.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.gson.Gson;
import com.zerter.teamconnect.Controlers.Data;
import com.zerter.teamconnect.Controlers.MyButton;
import com.zerter.teamconnect.Controlers.MyEditText;
import com.zerter.teamconnect.Models.Template;
import com.zerter.teamconnect.R;

import java.util.ArrayList;
import java.util.List;

public class MenageTemplates extends Fragment {
    private String TAG = getClass().getName();
    public ListView listView;
    public MyButton buttonDodajTemplate;

    Data data;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.templates_view, container, false);
        listView = (ListView) view.findViewById(R.id.ListViewTemplate);
        buttonDodajTemplate = (MyButton) view.findViewById(R.id.buttonAddTemplate);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        data = new Data(getActivity());
        setAdapter(data.getTemplates());
        buttonDodajTemplate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Template> templates;
                if (data.getTemplates() != null){
                    templates = data.getTemplates();
                }else {
                    templates = new ArrayList<Template>();
                }
                templates.add(new Template());
                setAdapter(templates, true);


            }
        });

    }
    public void setAdapter(List<Template> templates , Boolean focus) {
        if (templates != null){
            ListAdapterTemplates adapter = new ListAdapterTemplates(getActivity(), templates, focus);
            listView.setAdapter(adapter);
        }
    }
    public void setAdapter(List<Template> templates) {
        if (templates != null){
            ListAdapterTemplates adapter = new ListAdapterTemplates(getActivity(), templates);
            listView.setAdapter(adapter);
        }
    }
    private class ListAdapterTemplates extends ArrayAdapter<Template> {
        Boolean focus = false;
        List<Template> templates = null;
        public ListAdapterTemplates(@NonNull Context context, List<Template> templates) {
            super(context, 0, templates);
            this.templates = templates;
        }
        public ListAdapterTemplates(@NonNull Context context, List<Template> templates, Boolean focus) {
            super(context, 0, templates);
            this.templates = templates;
            this.focus = focus;
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            final Template template = getItem(position);

            if (convertView == null){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.template_edit_item_list,parent,false);
            }
            final MyEditText text = (MyEditText) convertView.findViewById(R.id.templateEditText);
            final ImageButton delete = (ImageButton) convertView.findViewById(R.id.buttonDeleteTemplate);
            if (template != null) {
                text.setText(template.getText());
            }

            text.setOnFocusChangeListener(new View.OnFocusChangeListener() {

                @Override
                public void onFocusChange(View v, boolean hasFocus) {
    /* When focus is lost check that the text field
    * has valid values.
    */
                    if (!hasFocus) {
                        if (text.getText() != null) {
                            templates.get(position).setText(text.getText().toString());
                            data.setTemplates(new Gson().toJson(templates));
//                            setAdapter(templates);
                        }
                    }
                }
            });

//            text.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//                @Override
//                public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
//                    if (actionId == EditorInfo.IME_ACTION_SEARCH ||
//                            actionId == EditorInfo.IME_ACTION_DONE ||
//                            event.getAction() == KeyEvent.ACTION_DOWN &&
//                                    event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
//                        if (!event.isShiftPressed()) {
//                            // the user is done typing.
////                            templates.get(position).setText(textView.getText().toString());
////                            data.setTemplates(new Gson().toJson(templates));
////                            setAdapter(templates);
//                            Toast.makeText(getActivity(),"diala",Toast.LENGTH_SHORT).show();
//                            return true; // consume.
//                        }
//                    }
//                    return false;
//                }
//            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    templates.remove(position);
                    data.setTemplates(new Gson().toJson(templates));
                    setAdapter(templates);
                }
            });

            if (focus && (templates.size()-1) == position){
                text.requestFocus();
            }

            return convertView;
        }
    }
}
