package market.dental.android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import market.dental.adapter.ProductListAdapter;
import market.dental.model.Product;
import market.dental.util.Resource;
import market.dental.util.Result;

public class ProductListActivity extends BaseActivity {

    private ProductListAdapter productListAdapter;
    private RequestQueue requestQueue;
    private boolean isLastPage;
    private boolean isLoading = false;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        view = findViewById(android.R.id.content);
        isLastPage = false;
        isLoading = false;

        // Get Params
        Intent intent = getIntent();
        boolean recentProducts = intent.getBooleanExtra(Resource.KEY_GET_RECENT_PRODUCTS , false);

        // Initialization
        requestQueue = Volley.newRequestQueue(this);
        productListAdapter = new ProductListAdapter(this);

        if(recentProducts){
            this.getRecentProducts(0);
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }


    public void getRecentProducts(final int page){

        // *****************************************************************************************
        //                        GET PRODUCTS - USER HISTORY
        // *****************************************************************************************
        if(!isLoading && !isLastPage){
            isLoading = true;
            StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST,
                    Resource.ajax_get_products_by_user_history, new Response.Listener<String>() {

                @Override
                public void onResponse(String responseString) {

                    try {
                        JSONObject response = new JSONObject(responseString);
                        if(Result.SUCCESS.checkResult(new Result(response))){

                            JSONObject content = response.getJSONObject("content");
                            if(content.getJSONArray("data").length()>0){
                                productListAdapter.addProductList(Product.ProductListWithExt(content.getJSONArray("data")));
                                productListAdapter.setCurrentPage(content.getInt("current_page"));

                                ListView listView = view.findViewById(R.id.activity_product_list_main);
                                if(listView.getAdapter()==null)
                                    listView.setAdapter(productListAdapter);
                                else{
                                    productListAdapter.notifyDataSetChanged();
                                }

                                // -- EVENTS --
                                /*
                                listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                                    @Override
                                    public void onScrollStateChanged(AbsListView view, int scrollState) {}

                                    @Override
                                    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                                        if(view.getLastVisiblePosition() == totalItemCount-1 && !isLoading){
                                            getRecentProducts(productListAdapter.getCurrentPage()+1);
                                        }
                                    }
                                });
                                */

                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        int productId = ((Product) parent.getItemAtPosition(position)).getId();
                                        Bundle bundle = new Bundle();
                                        bundle.putInt(Resource.KEY_PRODUCT_ID, productId);
                                        Intent intent = new Intent(view.getContext(),ProductDetailActivity.class);
                                        intent.putExtras(bundle);
                                        view.getContext().startActivity(intent);
                                    }
                                });

                            } else{
                                isLastPage = false;
                            }

                        }else if(Result.FAILURE_TOKEN.checkResult(new Result(response))){
                            Resource.setDefaultAPITOKEN();
                            Intent intent = new Intent(getApplicationContext() , LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "Beklenmedik bir durum ile karşılaşıldı" , Toast.LENGTH_LONG).show();
                            Crashlytics.log(Log.INFO , Result.LOG_TAG_INFO.getResultText() , this.getClass().getName() + " >> " + Resource.ajax_get_products_by_user_history + " >> responseString = " + responseString);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Crashlytics.log(Log.INFO ,Result.LOG_TAG_INFO.getResultText(), Resource.ajax_get_products_by_user_history );
                    } finally {
                        isLoading = false;
                    }
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    Crashlytics.log(Log.ERROR , Result.LOG_TAG_INFO.getResultText() , this.getClass().getName() + " >> " + "onErrorResponse");
                    isLoading = false;
                }
            }){
                @Override
                protected Map<String, String> getParams()  {
                    Map<String, String> params = new HashMap<>();
                    params.put(Resource.KEY_API_TOKEN, Resource.VALUE_API_TOKEN);
                    params.put("page", String.valueOf(page));
                    return params;
                }
                @Override
                public Map<String, String> getHeaders() {
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("Content-Type","application/x-www-form-urlencoded");
                    return params;
                }
            };
            requestQueue.add(jsonObjectRequest);
        }
    }

}
