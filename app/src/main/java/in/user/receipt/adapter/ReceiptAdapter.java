package in.user.receipt.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;



import java.util.ArrayList;

import in.user.R;
import in.user.receipt.Receipt;
import in.user.receipt.model.ReceiptModel;


/**
 * Created by Daffodil on 8/8/2018.
 */

public class ReceiptAdapter extends RecyclerView.Adapter<ReceiptAdapter.ViewHolder> {
    Context context;
    Receipt receipt;
    ArrayList<ReceiptModel> receiptModelList = new ArrayList<>();

    String h = "", m = "", s = "";


    public ReceiptAdapter(Context context, Receipt receipt, ArrayList<ReceiptModel> receiptModelList) {
        this.context = context;
        this.receipt = receipt;
        this.receiptModelList = receiptModelList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(receipt).inflate(R.layout.receipt_item_one, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // holder.bind(receiptModelList.get(position));
        holder.parkingName.setText(receiptModelList.get(position).getParkingName());
        holder.vehicleNo.setText("Vehicle No.        :   " + receiptModelList.get(position).getVehicleNo());


        String bookingDate = receiptModelList.get(position).getBookingDateTime();
        String[] datefinal = bookingDate.split(" ");
        String d = datefinal[0];
        String t = datefinal[1];
        String r = datefinal[2];
        holder.bookingDate.setText("Boarding DateTime :  " + receiptModelList.get(position).getBoardingDateTime());

        if (receiptModelList.get(position).getCancelStatus().equals("") && receiptModelList.get(position).getTimeDifference().equals("")) {
            holder.remaining.setText(receiptModelList.get(position).getParkingStatus());
            holder.remaining.setVisibility(View.VISIBLE);
        }

        if (receiptModelList.get(position).getCancelStatus().equals("") && receiptModelList.get(position).getParkingStatus().equals("")) {
            holder.remaining.setText("Remaining Time    : " + receiptModelList.get(position).getTimeDifference());
            holder.remaining.setVisibility(View.VISIBLE);
        }

        if (receiptModelList.get(position).getTimeDifference().equals("") && receiptModelList.get(position).getParkingStatus().equals("")) {
            holder.remaining.setVisibility(View.VISIBLE);
            holder.remaining.setText(receiptModelList.get(position).getCancelStatus());
            // holder.mCountDown.setVisibility(View.GONE);
        }

//        if (!receiptModelList.get(position).getTimeDifference().equals("")) {
//            String setTimeDifference = receiptModelList.get(position).getTimeDifference();
//            String[] timeDifference = setTimeDifference.split("/");
//
//            h = timeDifference[0];
//            m = timeDifference[1];
//            s = timeDifference[2];
//            holder.mCountDown.setVisibility(View.VISIBLE);
//            holder.remaining.setVisibility(View.GONE);
//        } else {
//            holder.mCountDown.setVisibility(View.GONE);
//            holder.remaining.setVisibility(View.VISIBLE);
//        }


        String boardingDateTime = receiptModelList.get(position).getBoardingDateTime();
        // 10/08/2018 05:00 PM
        //Thu Jan 01 20:00:12 GMT+05:30 1970
        String[] arrOfStr = boardingDateTime.split(" ");

        String date = arrOfStr[0];
        String time = arrOfStr[1];//05:00


    }


    @Override
    public int getItemCount() {
        return receiptModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView parkingName, bookingDate, vehicleNo, book, cancel, remaining;


        public ViewHolder(View itemView) {
            super(itemView);

//            RelativeLayout constraintLayout = (RelativeLayout) itemView.findViewById(R.id.imagelayout);
//            AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
//            animationDrawable.setEnterFadeDuration(3000);
//            animationDrawable.setExitFadeDuration(5000);
//            animationDrawable.start();

            parkingName = (TextView) itemView.findViewById(R.id.parkingName);
            bookingDate = (TextView) itemView.findViewById(R.id.bookingDate);
            vehicleNo = (TextView) itemView.findViewById(R.id.vehicleNo);
            remaining = (TextView) itemView.findViewById(R.id.remaining);
            // book = (TextView) itemView.findViewById(R.id.book);
            cancel = (TextView) itemView.findViewById(R.id.cancel);


        }


//        public void bind(ReceiptModel receiptModel) {
//            parkingName.setText("ParkingName : " + receiptModel.getParkingName());
//            vehicleNo.setText("vehicleNo   : " + receiptModel.getVehicleNo());
//            bookingDate.setText("bookingDate : " + receiptModel.getBoardingDateTime());
//
//        }
    }
}
