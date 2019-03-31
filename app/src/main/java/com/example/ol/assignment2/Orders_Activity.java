package com.example.ol.assignment2;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.ol.assignment2.model.Order;
import com.example.ol.assignment2.adapter.RecyclerViewOrdersAdapter;
import com.example.ol.assignment2.model.Book;

import java.util.ArrayList;

public class Orders_Activity extends AppCompatActivity {

    private ImageView ivSignOutOrders,ivLibraryOrders,ivSearchOrders;
    private RadioButton rbBookName,rbAuthorName,rbPrice,rbDate;
    private RadioGroup radioGroup;
    private EditText etSearch;
    private Button btnSort;
    boolean imageShowing=false;
    private ImageView ivClose;

    ArrayList<Book> lstBook;
    ArrayList<Order> lstOrders;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        Log.d("Orders_Activity",">>onCreate");
        ivSearchOrders= (ImageView)findViewById(R.id.ivSearchOrders);
        ivLibraryOrders= (ImageView)findViewById(R.id.ivLibraryOrders);
        ivSignOutOrders= (ImageView)findViewById(R.id.ivSignOutOrders);
        etSearch= (EditText) findViewById(R.id.etSearchOrders);
        btnSort=(Button) findViewById(R.id.btnSort);

        lstBook=new ArrayList<Book>();
        /*
        lstBook.add(new Book(R.drawable.elon_musk, "Elon Musk", "Ashlee Vance", "Biography", 3.99, 4.12, 2050,11021));
        lstBook.add(new Book(R.drawable.gandhi, "Gandhi Autobiography", "Mahatma Gandhi", "Biography", 5.99, 4.0, 269,27367));
        lstBook.add(new Book(R.drawable.great_business_ideas, "100 Great Business Ideas", "Jeremy Kourdi", "Biography", 2.99, 2.11, 2050,11021));
        lstBook.add(new Book(R.drawable.the_power_of_your_sebconscious_mind, "The Power of Your Subconscious Mind", "Joseph Murphy", "Biography", 3.99, 4.12, 2050,11021));
        lstBook.add(new Book(R.drawable.you_are_a_badass, "You Are a Badass", "Jen Sincer", "Biography", 1.59, 4.5, 2050,11021));
        lstBook.add(new Book(R.drawable.the_5_second_rule, "The 5 Second Rule", "Mel Robbins", "Biography", 2.0, 5.0, 2050,11021));
        lstBook.add(new Book(R.drawable.the_7_habits_of_highly_effective_people, "The 7 Habits of Highly Effective People", "Stephen R. Covey", "Biography", 4.99, 4.12, 2050,11021));
        lstBook.add(new Book(R.drawable.how_to_talk_to_anyone, "How to Talk to Anyone", "Leil Lowndes", "Biography", 7.49, 1.12, 2050,11021));
        lstBook.add(new Book(R.drawable.rich_dad_poor_dad, "Rich Dad, Poor Dad", "Robert T. Kiyosaki", "Biography", 1.99, 3.12, 2050,11021));
        lstBook.add(new Book(R.drawable.the_alchemist, "The Alchemist", "Paulo Coelho", "Biography", 3.99, 2.15, 2050,11021));
*/
        lstOrders=new ArrayList<Order>();
        lstOrders.add(new Order(lstBook.get(2),"12-08-2018",false));
        lstOrders.add(new Order(lstBook.get(6),"14-12-2018",true));
        lstOrders.add(new Order(lstBook.get(1),"07-11-2015",true));
        lstOrders.add(new Order(lstBook.get(4),"19-04-2013",false));
        lstOrders.add(new Order(lstBook.get(3),"10-02-2018",true));

        initRecyclerView();


        /*ivSignOutOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });*/

        ivLibraryOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3=new Intent(Orders_Activity.this, BookLibrary.class);
                startActivity(intent3);
            }
        });

        ivSearchOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!imageShowing) {
                    imageShowing = true;
                    etSearch.setVisibility(View.VISIBLE);
                }
                else{
                    imageShowing = false;
                    etSearch.setVisibility(View.GONE);
                }
            }
        });

        btnSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog sortOrderDialog = new Dialog(Orders_Activity.this);
                sortOrderDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                sortOrderDialog.setContentView(R.layout.sort_by_orders);

                radioGroup = (RadioGroup) sortOrderDialog.findViewById(R.id.rgRadiosButtons);
                ivClose = (ImageView) sortOrderDialog.findViewById(R.id.ivCloseReview);
                rbBookName = (RadioButton) sortOrderDialog.findViewById(R.id.rbBookName);
                rbAuthorName = (RadioButton) sortOrderDialog.findViewById(R.id.rbAuthorNAme);
                rbDate = (RadioButton) sortOrderDialog.findViewById(R.id.rbDate);
                rbPrice = (RadioButton) sortOrderDialog.findViewById(R.id.rbPrice);
                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        SortArrayListFields salf=new SortArrayListFields();

                        if(checkedId==rbBookName.getId()) {
                            lstOrders = salf.sortBookName(lstOrders);
                        }

                        if(checkedId==rbAuthorName.getId())
                        {
                            lstOrders = salf.sortAuthorName(lstOrders);
                        }

                        if(checkedId==rbDate.getId())
                        {
                            lstOrders=salf.sortDate(lstOrders);
                        }

                        if(checkedId==rbPrice.getId())
                        {
                            lstOrders=salf.sortPrice(lstOrders);
                        }
                        sortOrderDialog.dismiss();
                        initRecyclerView();
                    }
                });

                ivClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sortOrderDialog.dismiss();
                    }
                });

                sortOrderDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                sortOrderDialog.show();
            }
        });
    }

    private void initRecyclerView(){
        RecyclerView rvOrdersList = (RecyclerView) findViewById(R.id.orders_list);
        rvOrdersList.setLayoutManager(new LinearLayoutManager(this));
        Context context = rvOrdersList.getContext();
        LayoutAnimationController controller= AnimationUtils.loadLayoutAnimation(context,R.anim.layout_fall_down);
        RecyclerViewOrdersAdapter myAdapter = new RecyclerViewOrdersAdapter(this,lstOrders);
        rvOrdersList.setAdapter(myAdapter);

        rvOrdersList.setLayoutAnimation(controller);
        rvOrdersList.getAdapter().notifyDataSetChanged();
        rvOrdersList.scheduleLayoutAnimation();
    }

}
