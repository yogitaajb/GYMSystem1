package com.gym.dhc.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gym.dhc.R;
import com.gym.dhc.model.Receipt_Model;

import org.apache.http.NameValuePair;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Receipt_Adapter extends  RecyclerView.Adapter<Receipt_Adapter.ViewHolder> {

    private List<Receipt_Model> keywordList;
    private Context context;

    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    private Receipt_Adapter.OnItemClickListener mOnItemClickListener;
    public interface OnItemClickListener {
        void onItemClick(View view, Receipt_Model obj, int position);
    }

    public void setOnItemClickListener(final Receipt_Adapter.OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView_Name,textView_price,textView_payment_id,textView_added_date,textView_personal_traning;
       // ImageView imageView1;
        View lyt_parent;

        public ViewHolder(View itemView) {
            super(itemView);

            textView_Name = (TextView) itemView.findViewById( R.id.textView_Name);
            textView_price = (TextView) itemView.findViewById(R.id.textView_price);
            textView_payment_id = (TextView) itemView.findViewById(R.id.textView_payment_id);
            textView_added_date = (TextView) itemView.findViewById(R.id.textView_added_date);
            textView_personal_traning = (TextView)itemView.findViewById(R.id.textView_personal_traning);
            //imageView1 = (ImageView) itemView.findViewById(R.id.image);
            lyt_parent = (View) itemView.findViewById(R.id.lyt_parent);
        }
    }

    public Receipt_Adapter(Context context, List<Receipt_Model> itemList) {

        this.keywordList = itemList;
        this.context = context;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

    }
    @Override
    public Receipt_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate( R.layout.item_receipt, parent, false);
// set the view's size, margins, paddings and layout parameters
        Receipt_Adapter.ViewHolder vh = new Receipt_Adapter.ViewHolder(v);
        return vh;
    }
    @Override
    public void onBindViewHolder(final Receipt_Adapter.ViewHolder holder, final int position){
        holder.textView_Name.setText(keywordList.get(position).getPack_name());
        holder.textView_price.setText("Rs. "+keywordList.get(position).getTotal());

        holder.textView_added_date.setText("Payment Date : " +keywordList.get(position).getAdded_date());
        holder.textView_personal_traning.setText("Personal Traning: "+keywordList.get(position).getPersonal_traning());
       // final String product_image_url = keywordList.get(position).getImage();
String payment_type=keywordList.get(position).getPayment_type();
if (payment_type.equalsIgnoreCase("1"))
{
    holder.textView_payment_id.setText("Cash Payment");
}else if (payment_type.equalsIgnoreCase("0")){
    holder.textView_payment_id.setText("Payment Id : " +keywordList.get(position).getPayment_id());
}


       /* InputStream in = null; //Reads whatever content found with the given URL Asynchronously And returns.
        try {
            in = (InputStream) new URL(product_image_url).getContent();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(in); //Decodes the stream returned from getContent and converts It into a Bitmap Format
        holder.imageView1.setImageBitmap(bitmap); //Sets the Bitmap to ImageView
        try {
            if(in != null)
                in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(view, keywordList.get(position), position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return this.keywordList.size();
    }

}
