package com.example.ol.assignment2;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ol.assignment2.adapter.RecyclerViewReviewsAdapter;
import com.example.ol.assignment2.model.Book;
import com.example.ol.assignment2.model.Review;
import com.example.ol.assignment2.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Book_Activity extends AppCompatActivity {

    private TextView tvTitle, tvAuthor, tvGenre, tvPages, tvDownload;
    private ImageView ivBookCover;
    private Button btnBuy, btnReview, btnDownload;
    private ImageView ivSignOutBook, ivShoppingBook, ivLibraryBook;
    private ImageView ivClose;
    private TextView tvBookName;
    private EditText etTextReview;
    private Button btnSubmitReview;
    private RatingBar ratingReviewBar;
    private ArrayList<Review> lstReview;
    private FirebaseUser fbUser;
    private User user;
    private Book theBook;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);


        tvTitle = findViewById(R.id.txtTitle);
        tvAuthor = findViewById(R.id.txtAuthor);
        tvGenre = findViewById(R.id.txtGenre);
        tvPages = findViewById(R.id.txtPages);
        tvDownload = findViewById(R.id.txtDownload);
        ivBookCover = findViewById(R.id.ivBookCover);
        btnBuy = findViewById(R.id.btnBuy);
        btnReview = findViewById(R.id.btnReview);
        btnDownload = findViewById(R.id.btnDownload);

        lstReview = new ArrayList<>();
        lstReview.add(new Review("Elad Ben-Moshe", 1, "12-05-2017", "Geate Book!, Excellent wrtiting", 4.5));
        lstReview.add(new Review("Ofir Levy", 2, "19-01-2016", "One of the Best I Read!", 4.2));
        lstReview.add(new Review("Rotem Masud", 3, "22-12-2012", "WORST BOOK EVER! DONT READ!", 1.2));

        initRecyclerView();

        ivSignOutBook = findViewById(R.id.ivSignOutBook);
        ivShoppingBook = findViewById(R.id.ivShoppingBook);
        ivLibraryBook = findViewById(R.id.ivLibraryBook);

        Log.d("Book_Activity", "Intent intent=getIntent();");
        Intent intent = getIntent();

        theBook = (Book) intent.getSerializableExtra("choseBook");
        user = (User) intent.getSerializableExtra("user");

        final int idBook = theBook.getId();
        final String Title = theBook.getTitle();
        String Author = theBook.getAuthor();
        String Genre = theBook.getGenre();
        int Pages = theBook.getPages();
        int Downloads = theBook.getDownloads();
        String Image = theBook.getImg();
        double Rating = theBook.getRating();
        double Price = theBook.getPrice();
        String pdfDownload= theBook.getPdfDownload();

        tvTitle.setText(Title);
        tvAuthor.setText(Author);
        tvGenre.setText(Genre);
        tvPages.setText(Integer.toString(Pages) + " Pages");
        tvDownload.setText(Integer.toString(Downloads) + " Downloads");
        Uri myUri = Uri.parse(Image);
        Picasso.with(Book_Activity.this).load(myUri).into(ivBookCover);
        ivBookCover.setScaleType(ImageView.ScaleType.FIT_XY);

        boolean Bought = checkIfBought();
        boolean WroteReview = false; // Need to check it.

        buttonsSetChecker(Bought, WroteReview, Price);

        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.getMyBooks() == null) {
                    List<Integer> myBooksID = new ArrayList<>();
                    myBooksID.add(theBook.getId());
                    user.setMyBooks(myBooksID);
                } else {
                    user.getMyBooks().add(theBook.getId());
                }
                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users");
                userRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user);
                buttonsSetChecker(true,false,theBook.getPrice());
            }
        });

        btnReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(Book_Activity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.add_review);

                ivClose = (ImageView) dialog.findViewById(R.id.ivCloseReview);
                tvBookName = (TextView) dialog.findViewById(R.id.tvBookNameReview);
                etTextReview = (EditText) dialog.findViewById(R.id.etTextReview);
                btnSubmitReview = (Button) dialog.findViewById(R.id.btnSubmitReview);
                ratingReviewBar = (RatingBar) dialog.findViewById(R.id.ratingBarReview);

                tvBookName.setText(Title);

                btnSubmitReview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(Book_Activity.this, "Stars: " + ratingReviewBar.getRating(), Toast.LENGTH_SHORT).show();
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
        ivSignOutBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        ivShoppingBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(Book_Activity.this, Orders_Activity.class);
                startActivity(intent2);
            }
        });

        ivLibraryBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(Book_Activity.this, BookLibrary.class);
                startActivity(intent2);
            }
        });
    }

    private void initRecyclerView() {
        RecyclerView rvReviewList = (RecyclerView) findViewById(R.id.rvReviewList);
        rvReviewList.setLayoutManager(new LinearLayoutManager(this));
        Context context = rvReviewList.getContext();
        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_fall_down);
        RecyclerViewReviewsAdapter myAdapter = new RecyclerViewReviewsAdapter(this, lstReview);
        rvReviewList.setAdapter(myAdapter);

        rvReviewList.setLayoutAnimation(controller);
        rvReviewList.getAdapter().notifyDataSetChanged();
        rvReviewList.scheduleLayoutAnimation();
    }

    private boolean checkIfBought() {
        boolean isBought = false;
        if(user.getMyBooks() != null) {
            List<Integer> theBooksIDList = user.getMyBooks();
            for (Integer bookNum : theBooksIDList) {
                if (bookNum == theBook.getId()) {
                    isBought = true;
                    break;
                }
            }
        }
        return isBought;
    }

    private void buttonsSetChecker(boolean Bought, boolean WroteReview, double Price)
    {
        if (Bought) {
            //change icon to download
            btnDownload.setText("Download");
            btnDownload.setVisibility(View.VISIBLE);
            btnBuy.setVisibility(View.GONE);

            if (!WroteReview) {//gone to review
                btnReview.setVisibility(View.VISIBLE);
            }
        } else {
            btnBuy.setText("Buy it for " + Double.toString(Price) + "$");
            btnBuy.setVisibility(View.VISIBLE);
        }
    }
}



