package com.xbird.olddriver.olddriver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.xbird.olddriver.olddriver.NET.GETTRUEADDRESS;
import com.xbird.olddriver.olddriver.NET.GetInfoHash;
import com.xbird.olddriver.olddriver.NET.ParseCILI;
import com.xbird.olddriver.olddriver.NET.VIDEO;
import com.xbird.olddriver.olddriver.NET.VideoSearch;
import com.xbird.olddriver.olddriver.NET.zz.FindCILI;

import java.io.InputStream;
import java.sql.RowIdLifetime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentSearch.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentSearch#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentSearch extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ListView listView;
    private EditText edittext;
    private Button button;
    private HashMap<String,String> map;
    private ArrayList<String> list;

    private VIDEO[] videos;
    private Context context;
    private int clickItem;

//   / private OnFragmentInteractionListener mListener;

    public FragmentSearch() {
        // Required empty public constructor

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentSearch.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentSearch newInstance(String param1, String param2) {
        FragmentSearch fragment = new FragmentSearch();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_search, container, false);
        init(v);

        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.context = context;

    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
    }

    public void init(View v)
    {
        listView = (ListView)v.findViewById(R.id.listView);
        edittext = (EditText)v.findViewById(R.id.search_edit);
        button = (Button)v.findViewById(R.id.search_button) ;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = edittext.getText().toString();
                if(!s.equals(""))
                {
                    Toast.makeText(getActivity(), "正在搜索...", Toast.LENGTH_LONG).show();
                    new search().execute(s);
                }
            }
        });

        showNullResult();
        //new search().execute("大圣归来");
    }
    public void showNullResult()
    {
        list = new ArrayList<String>();
        String[] content = {"无结果"};
        for(int n=0 ;n<content.length;n++)
        {
            list.add(content[n]);
        }
        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this.getContext(),android.R.layout.simple_list_item_1,list);
        listView.setAdapter(listAdapter);
        listView.setEnabled(false);
    }

class search extends AsyncTask<String,String,String>
{
   // private VideoSearch vs;
    private FindCILI vs;
//    private VIDEO[] videos;
    @Override
    protected String doInBackground(String... strings) {
        vs = new FindCILI(strings[0]);

        if(vs.getInfo())
        {
            videos = vs.getVideos();
            publishProgress("1");
        }
        else
        {
            publishProgress("0");
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(final String... values) {
        if(values[0].equals("1")) {
            list = new ArrayList<String>();
            for (int n = 0; n < videos.length; n++) {
                list.add(videos[n].name);
            }
            ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, list);

            listView.setAdapter(listAdapter);

            listView.setEnabled(true);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    clickItem = position;
                    if(videos[clickItem].CILI.equals("")||videos[clickItem].size.equals(""))
                    {
                        Toast.makeText(getActivity(),"正在获得种子信息。。。",Toast.LENGTH_LONG).show();
                        new GETCILI().execute();
                    }
                    else
                    {
                        Intent intent = new Intent(getActivity(),VIDEOINFO_activity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("name",videos[clickItem].name);
                        bundle.putString("id",videos[clickItem].ID);
                        bundle.putString("cili",videos[clickItem].CILI);
                        bundle.putSerializable("size",videos[clickItem].size);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }
            });
        }
        else {
            Toast.makeText(context,"没有找到0_0",Toast.LENGTH_SHORT);
            showNullResult();
        }
        super.onProgressUpdate(values);
    }
}

    class GETCILI extends AsyncTask<String,String,String>
    {

        private GetInfoHash getInfoHash;
        private String CILI;
        private String size;
        @Override
        protected String doInBackground(String... params) {
            getInfoHash = new GetInfoHash(videos[clickItem].ID);
            if(getInfoHash.getInfo())
            {
                CILI = getInfoHash.getCili();
                size = getInfoHash.getSize();
                videos[clickItem].CILI = CILI;
                videos[clickItem].size = size;
                publishProgress();
            }
            else
            {
                publishProgress("1");
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            if(values.length==0)
            {
                Intent intent = new Intent(getActivity(),VIDEOINFO_activity.class);

                Bundle bundle = new Bundle();
                bundle.putString("name",videos[clickItem].name);
                bundle.putString("id",videos[clickItem].ID);
                bundle.putString("cili",videos[clickItem].CILI);
                bundle.putSerializable("size",videos[clickItem].size);
                intent.putExtras(bundle);
                startActivity(intent);
            }
            else
            {
                showWrong();
            }
            super.onProgressUpdate(values);
        }
    }

    public void showWrong()
    {
        Toast.makeText(getActivity(),"解析失败",Toast.LENGTH_LONG).show();
    }
//
//
}


