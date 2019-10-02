package com.gym.dhc.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.StrictMode;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gym.dhc.R;
import com.gym.dhc.model.payment_History_model;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PaymentHistory_Adapter extends  RecyclerView.Adapter<PaymentHistory_Adapter.ViewHolder>  {
    private List<payment_History_model> itemList;
    private Context context;
    private ArrayList<payment_History_model> arraylist;

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView payid,razori_payid,razori_paid_price,pack_id,pname,pack_price,datetime,status,trainer_name;

        public ViewHolder(View itemView) {
            super( itemView );
            payid = (TextView) itemView.findViewById( R.id.payid );
            razori_payid = (TextView) itemView.findViewById(R.id.razori_payid);
            razori_paid_price = (TextView) itemView.findViewById(R.id.razori_paid_price);
            pack_id = (TextView) itemView.findViewById( R.id.pack_id );
            pname = (TextView) itemView.findViewById(R.id.P_name);
            pack_price = (TextView) itemView.findViewById(R.id.pack_price);
            datetime = (TextView) itemView.findViewById( R.id.datetime );
            status= (TextView) itemView.findViewById( R.id.status );
            trainer_name=(TextView) itemView.findViewById( R.id.trainer_name );
        }
    }

    public PaymentHistory_Adapter(Context context, List<payment_History_model> itemList) {

        this.itemList = itemList;
        this.context = context;
        this.arraylist = new ArrayList<payment_History_model>();
        this.arraylist.addAll(itemList);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

    }

    @Override
    public PaymentHistory_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from( parent.getContext() ).inflate( R.layout.item_payment_history, parent, false );
// set the view's size, margins, paddings and layout parameters
        PaymentHistory_Adapter.ViewHolder vh = new PaymentHistory_Adapter.ViewHolder( v );
        return vh;
    }

    @Override
    public void onBindViewHolder(final PaymentHistory_Adapter.ViewHolder holder, final int position) {
        String id = String.valueOf( itemList.get( position ).getId() );

        String pay_id = String.valueOf( itemList.get( position ).getId() );
        String razori_payment_id =  itemList.get( position ).getPayment_id() ;

        Log.e("data",pay_id+"//"+razori_payment_id);

      //  if(camera_id.isEmpty() && refer_id.isEmpty()){

            holder.payid.setText(pay_id);

        holder.pack_id.setText( itemList.get(position).getPack_id());
        holder.pname.setText(itemList.get(position).getPack_name());
        holder.pack_price.setText(itemList.get(position).getPack_price());
        holder.datetime.setText( itemList.get(position).getDatetime());

            String status = itemList.get(position).getStatus();
            if(status.equals( "Success" )){
                holder.status.setTextColor(Color.parseColor("#00C853"));//GREEN
                holder.status.setText(status);
            }else if(status.equals( "Failure" )){
                holder.status.setTextColor(Color.parseColor("#E53935"));//RED
                holder.status.setText(status);
            }else{
                holder.status.setTextColor(Color.parseColor("#FF8F00"));//AMBER
                holder.status.setText(status);
            }
       // }

            if(itemList.get(position).getPayment_type().equalsIgnoreCase("Cash Payment"))
        {
            String trainername= itemList.get(position).getTrainer_name();
            if (trainername != null && !trainername.isEmpty() && !trainername.equals("null")) {
                holder.trainer_name.setText(trainername);
            }else{
                holder.trainer_name.setText("");
            }
            holder.razori_payid.setText( "Cash Payment");
           // holder.razori_payid.setTextColor(context.getResources().getColor(R.color.grey_700));//grey
            holder.razori_paid_price.setText(" ");
        }else{
            holder.razori_payid.setText( razori_payment_id);
            holder.razori_paid_price.setText(itemList.get(position).getPaid_price());
        }

    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }


    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        itemList.clear();
        if (charText.length() == 0) {
            itemList.addAll(arraylist);
        } else {
            for (payment_History_model wp : arraylist) {
                if (wp.getPack_name().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    itemList.add(wp);
                }
                if (wp.getPayment_id().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    itemList.add(wp);
                }
                if (wp.getStatus().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    itemList.add(wp);
                }

            }
        }
        notifyDataSetChanged();
    }

}

