package com.example.ol.assignment2;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.ol.assignment2.adapter.RecyclerViewAdapter;
import com.example.ol.assignment2.model.Book;
import com.example.ol.assignment2.model.User;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

public class BookLibrary extends AppCompatActivity {

    private ImageView ivSignOutLibrary, ivShoppingLibrary, ivSearchLibrary;
    private RadioButton rbPopularity, rbTopRated, rbLowToHigh, rbHighToLow;
    private RadioGroup radioGroup;
    private Button btnSort;
    private EditText etSearch;
    private Boolean imageShowing = false;
    private ImageView ivClose;
    private ArrayList<Book> lstBook = new ArrayList<>();
    private FirebaseUser fbUser;
    private User user;
    private DatabaseReference myUserRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_library);

        fbUser = FirebaseAuth.getInstance().getCurrentUser();

        if(fbUser == null) {
            Intent intent = new Intent(BookLibrary.this, SignIn.class);
            startActivity(intent);
        }
        else {
                myUserRef = FirebaseDatabase.getInstance().getReference("Users/" + fbUser.getUid());
                myUserRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        user = snapshot.getValue(User.class);
                        readingDataFromDatabase();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

        }


        ivSearchLibrary = (ImageView) findViewById(R.id.ivSearchLibrary);
        ivShoppingLibrary = (ImageView) findViewById(R.id.ivShoppingLibrary);
        ivSignOutLibrary = (ImageView) findViewById(R.id.ivSignOutLibrary);
        etSearch = (EditText) findViewById(R.id.etSearch);
        btnSort = (Button) findViewById(R.id.btnSort);


        ivSignOutLibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                Intent intent = new Intent(BookLibrary.this, SignIn.class);
                startActivity(intent);
            }
        });

        ivShoppingLibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(BookLibrary.this, Orders_Activity.class);
                startActivity(intent2);
            }
        });
        ivSearchLibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!imageShowing) {
                    imageShowing = true;
                    etSearch.setVisibility(View.VISIBLE);
                } else {
                    imageShowing = false;
                    etSearch.setVisibility(View.GONE);
                }
            }
        });


        btnSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(BookLibrary.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.sort_by_book_library);

                radioGroup = (RadioGroup) dialog.findViewById(R.id.rgRadiosButtons);
                ivClose = (ImageView) dialog.findViewById(R.id.ivCloseReview);
                rbPopularity = (RadioButton) dialog.findViewById(R.id.rbPopularity);
                rbTopRated = (RadioButton) dialog.findViewById(R.id.rbTopRated);
                rbHighToLow = (RadioButton) dialog.findViewById(R.id.rbHighToLow);
                rbLowToHigh = (RadioButton) dialog.findViewById(R.id.rbLowToHigh);
                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        SortArrayListFields salf = new SortArrayListFields();

                        if (checkedId == rbPopularity.getId()) {
                            lstBook = salf.sortPopularity(lstBook);
                        }

                        if (checkedId == rbTopRated.getId()) {
                            lstBook = salf.sortRating(lstBook);
                        }

                        if (checkedId == rbHighToLow.getId()) {
                            lstBook = salf.sortPriceHighToLow(lstBook);
                        }

                        if (checkedId == rbLowToHigh.getId()) {
                            lstBook = salf.sortPriceLowToHigh(lstBook);
                        }
                        dialog.dismiss();
                        initRecyclerView();
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
    }

    private void readingDataFromDatabase() {

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Books_Objects");

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Book newBook = ds.getValue(Book.class);
                    lstBook.add(newBook);
                }

                initRecyclerView();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void initRecyclerView() {
        RecyclerView rvBookList = (RecyclerView) findViewById(R.id.books_list);
        rvBookList.setLayoutManager(new GridLayoutManager(this, 2));
        Context context = rvBookList.getContext();
        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_slide_from_left);
        RecyclerViewAdapter myAdapter = new RecyclerViewAdapter(this, lstBook, user);
        rvBookList.setAdapter(myAdapter);
        rvBookList.setLayoutAnimation(controller);
        rvBookList.getAdapter().notifyDataSetChanged();
        rvBookList.scheduleLayoutAnimation();
    }

}