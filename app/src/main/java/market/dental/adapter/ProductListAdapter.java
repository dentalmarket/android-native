package market.dental.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import market.dental.android.R;
import market.dental.model.Product;
import market.dental.util.Resource;
import market.dental.model.Currency;

/**
 * Created by kemalsamikaraca on 18.01.2018.
 */

public class ProductListAdapter extends ArrayAdapter {

    private Context context;
    private List<Product> productList;
    private int currentPage;
    private JSONArray images;
    private ViewPagerAdapter viewPagerAdapter;
    private ViewPager viewPager;

    public ProductListAdapter(@NonNull Context context) {
        super(context, R.layout.activity_product_list_main_items);
        this.context  = context;
        this.currentPage = 1;
        this.productList = new ArrayList<>();
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList){
        this.productList = productList;
    }

    public void addProductList(List<Product> productList){
        this.productList.addAll(productList);
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public JSONArray getImages() {
        return images;
    }

    public void setImages(JSONArray images) {
        this.images = images;
    }

    @Override
    public int getCount(){
        return productList.size();
    }

    @Override
    public Object getItem(int position){
        return this.productList.get(position);
    }

    @Override
    public View getView(int position, View view, ViewGroup viewgroup){

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView;

        if(position==0 && images!=null && images.length()>0){

            customView = inflater.inflate(R.layout.view_pager_layout,viewgroup,false);
            viewPager = (ViewPager)customView.findViewById(R.id.layout_view_pager);
            viewPagerAdapter = new ViewPagerAdapter((Activity) context,images);
            viewPager.setAdapter(viewPagerAdapter);

            LinearLayout sliderDotsPanel = (LinearLayout)customView.findViewById(R.id.layout_view_pager_dots);
            final ImageView[] dots = new ImageView[viewPagerAdapter.getCount()];
            for(int i=0; i<viewPagerAdapter.getCount(); i++){
                dots[i] = new ImageView(context);

                if(i==0){
                    dots[i].setImageDrawable(ContextCompat.getDrawable(context , R.drawable.active_dot));
                }else{
                    dots[i].setImageDrawable(ContextCompat.getDrawable(context , R.drawable.nonactive_dot));
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
                            dots[i].setImageDrawable(ContextCompat.getDrawable(context , R.drawable.active_dot));
                        }else{
                            dots[i].setImageDrawable(ContextCompat.getDrawable(context , R.drawable.nonactive_dot));
                        }
                    }
                }
                @Override
                public void onPageScrollStateChanged(int state) {}
            });

        }else{

            customView = inflater.inflate(R.layout.activity_product_list_main_items,viewgroup,false);
            TextView textView = customView.findViewById(R.id.activity_product_list_item_product_name);
            textView.setText(productList.get(position).getName());

            TextView brandTextView = customView.findViewById(R.id.activity_product_list_item_product_brand);
            brandTextView.setText(productList.get(position).getBrand()!=null ? productList.get(position).getBrand().getName() : "bilinmiyor");

            Typeface font = Typeface.createFromAsset(context.getAssets(),"fonts/fontawesome-webfont.ttf");
            TextView priceTextView = customView.findViewById(R.id.activity_product_list_item_product_price);
            priceTextView.setTypeface(font);
            priceTextView.setText("" +productList.get(position).getSalePrice() + " " +
                    Currency.getCurrencyString( context.getResources(),productList.get(position).getCurrencyId()));

            ImageView imageView = customView.findViewById(R.id.activity_product_list_item_image);
            Picasso.with(context)
                    .load(productList.get(position).getImageUrl())
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(imageView);

        }
        return customView;
    }

}
