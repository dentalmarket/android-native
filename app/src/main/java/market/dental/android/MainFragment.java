package market.dental.android;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import market.dental.adapter.ProductsRecyclerAdapter;
import market.dental.adapter.ViewPagerAdapter;
import market.dental.util.Resource;
import market.dental.util.Result;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment {

    private View view;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mRecyclerLayoutManager;
    private RecyclerView.Adapter mRecyclerAdapter;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private ImageView[] dots;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
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

        getActivity().setTitle("Generic fragment");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        RequestQueue rq = Volley.newRequestQueue(getContext());
        view = inflater.inflate(R.layout.fragment_main, container, false);

        // *****************************************************************************************
        //                              BANNER IMAGES
        // *****************************************************************************************
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST,
                Resource.ajax_get_banner_images_url, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                onResponseBannerImages(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(Result.LOG_TAG_INFO.getResultText(),"ERROR ON GET DATA");
            }
        });
        rq.add(jsonArrayRequest);


        // *****************************************************************************************
        //                              FEATURED PRODUCTS
        // *****************************************************************************************
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL , false);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                Resource.ajax_get_products_featured_url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.i(Result.LOG_TAG_INFO.getResultText(),response.toString());
                try {
                    JSONArray products = (JSONArray)response.getJSONArray("products");

                    mRecyclerView.setLayoutManager(mRecyclerLayoutManager);
                    mRecyclerAdapter = new ProductsRecyclerAdapter(products);
                    mRecyclerView.setAdapter(mRecyclerAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(Result.LOG_TAG_INFO.getResultText(),"ERROR ON GET DATA");
            }
        });
        rq.add(jsonObjectRequest);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    /**
     * Bu TimerTask class ile image slider otomatik değiştirmesi sağlanıyor
     */
    public class MyTimerTask extends TimerTask{
        @Override
        public void run() {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                if(viewPager.getCurrentItem()+1==viewPagerAdapter.getCount()){
                    viewPager.setCurrentItem(0);
                }else{
                    viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
                }

                }
            });
        }
    }


    /**
     * ViewPagerAdapter volley response sonrası doldurulmasını sağlar
     * @param images
     */
    public void onResponseBannerImages(JSONArray images){
        viewPager = (ViewPager)view.findViewById(R.id.fragment_main_view_pager);
        viewPagerAdapter = new ViewPagerAdapter(getActivity(),images);
        viewPager.setAdapter(viewPagerAdapter);

        LinearLayout sliderDotsPanel = (LinearLayout)view.findViewById(R.id.viewpage_dots_layout);
        dots = new ImageView[viewPagerAdapter.getCount()];
        for(int i=0; i<viewPagerAdapter.getCount(); i++){
            dots[i] = new ImageView(getActivity());

            if(i==0){
                dots[i].setImageDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext() , R.drawable.active_dot));
            }else{
                dots[i].setImageDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext() , R.drawable.nonactive_dot));
            }

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT , LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(8,0,8,0);
            sliderDotsPanel.addView(dots[i],params);
        }

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            @Override
            public void onPageSelected(int position) {
                for(int i=0; i<viewPagerAdapter.getCount(); i++){
                    if(i==position){
                        dots[i].setImageDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext() , R.drawable.active_dot));
                    }else{
                        dots[i].setImageDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext() , R.drawable.nonactive_dot));
                    }
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new MyTimerTask() , 2000 , 4000);
    }
}
