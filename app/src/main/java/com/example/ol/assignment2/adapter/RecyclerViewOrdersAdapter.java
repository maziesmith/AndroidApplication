package com.example.ol.assignment2.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ol.assignment2.Book_Activity;
import com.example.ol.assignment2.R;
import com.example.ol.assignment2.model.Order;

import java.util.List;

public class RecyclerViewOrdersAdapter extends  RecyclerView.Adapter<RecyclerViewOrdersAdapter.MyViewHolder> {
    private Context mContext;
    private List<Order> mData;
    private ImageView ivClose;
    private TextView tvBookName;
    private EditText etTextReview;
    private Button btnSubmitReview;
    private RatingBar ratingReviewBar;


    public RecyclerViewOrdersAdapter(Context mcontext, List<Order> mdata) {
        this.mContext = mcontext;
        this.mData = mdata;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);

        view = mInflater.inflate(R.layout.item_orderd_book, viewGroup, false);
        return new RecyclerViewOrdersAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {
        //myViewHolder.iv_BookCover.setImageResource(mData.get(i).getBook().getImg());
        myViewHolder.iv_BookCover.setScaleType(ImageView.ScaleType.FIT_XY);
        myViewHolder.tvTitle.setText(mData.get(i).getBook().getTitle());
        myViewHolder.tvAuthor.setText(mData.get(i).getBook().getAuthor());
        myViewHolder.tvPrice.setText(Double.toString(mData.get(i).getBook().getPrice()) + "$");
        //myViewHolder.tvRating.setText(Double.toString(mData.get(i).getBook().getRating()));
        myViewHolder.tvDate.setText(mData.get(i).getDate());
        if(!mData.get(i).isWroteReview())
        {
            myViewHolder.btnReview.setVisibility(View.VISIBLE);
        }
        myViewHolder.btnReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(mContext);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.add_review);

                ivClose=(ImageView) dialog.findViewById(R.id.ivCloseReview);
                tvBookName=(TextView) dialog.findViewById(R.id.tvBookNameReview);
                etTextReview=(EditText) dialog.findViewById(R.id.etTextReview);
                btnSubmitReview=(Button) dialog.findViewById(R.id.btnSubmitReview);
                ratingReviewBar=(RatingBar) dialog.findViewById(R.id.ratingBarReview);

                tvBookName.setText(mData.get(i).getBook().getTitle());

                btnSubmitReview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(mContext,"Stars: "+ratingReviewBar.getRating(),Toast.LENGTH_SHORT).show();
                    }
                });

                ivClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });


                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        myViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext,Book_Activity.class);
                intent.putExtra("Title",mData.get(i).getBook().getTitle());
                intent.putExtra("Author",mData.get(i).getBook().getAuthor());
                intent.putExtra("Genre",mData.get(i).getBook().getGenre());
                intent.putExtra("Image",mData.get(i).getBook().getImg());
                intent.putExtra("Pages",mData.get(i).getBook().getPages());
                intent.putExtra("Downloads",mData.get(i).getBook().getDownloads());
                intent.putExtra("Rating",mData.get(i).getBook().getRating());
                intent.putExtra("Price",mData.get(i).getBook().getPrice());
                intent.putExtra("Bought",true);
                intent.putExtra("WroteReview",mData.get(i).isWroteReview());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_BookCover;
        TextView tvTitle, tvAuthor, tvPrice, tvRating, tvDate;
        Button btnReview,btnDownload;
        CardView cardView;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_BookCover = (ImageView) itemView.findViewById(R.id.ivBookCover);
            tvTitle = (TextView) itemView.findViewById(R.id.txtTitle);
            tvAuthor = (TextView) itemView.findViewById(R.id.txtAuthor);
            tvPrice = (TextView) itemView.findViewById(R.id.txtPrice);
            tvDate = (TextView) itemView.findViewById(R.id.txtDate);
            btnReview=(Button) itemView.findViewById(R.id.btnReview);
            btnDownload=(Button) itemView.findViewById(R.id.btnDownload);

            cardView = (CardView) itemView.findViewById(R.id.cardviewIDOrder);
        }
    }
}