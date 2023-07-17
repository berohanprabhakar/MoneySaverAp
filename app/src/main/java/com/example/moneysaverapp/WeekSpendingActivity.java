package com.example.moneysaverapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.DateTime;
import org.joda.time.Months;
import org.joda.time.MutableDateTime;
import org.joda.time.Weeks;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WeekSpendingActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private TextView totalWeekAmountTextView;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private WeekSpendingAdapter weekSpendingAdapter;
    private List<Data>myDatalist;
    private FirebaseAuth mAuth;
    private String onlineUserId="";
    private DatabaseReference expensesRef;
    private String type="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_spending);
        progressBar=findViewById(R.id.progressBar);
        getSupportActionBar().setTitle("Week Spending");
        totalWeekAmountTextView=findViewById(R.id.totalWeekAmountTextView);
        toolbar=findViewById(R.id.toolbar);
        recyclerView=findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        mAuth=FirebaseAuth.getInstance();
        onlineUserId=mAuth.getCurrentUser().getUid();
        expensesRef= FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
        myDatalist=new ArrayList<>();
        weekSpendingAdapter=new WeekSpendingAdapter(WeekSpendingActivity.this,myDatalist);
        recyclerView.setAdapter(weekSpendingAdapter);
        if(getIntent().getExtras()!=null)
        {
            type=getIntent().getStringExtra("type");
            if(type.equals("week"))
            {
                readWeekSpendingItem();
            }
            else if(type.equals("month"))
            {
                readMonthSpendingItems() ;
            }
        }

    }

    private void readMonthSpendingItems()  {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0);
        DateTime now = new DateTime();
        Months months = Months.monthsBetween(epoch, now);

        expensesRef = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
        Query query = expensesRef.orderByChild("month").equalTo(months.getMonths());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myDatalist.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Data data = dataSnapshot.getValue(Data.class);
                    myDatalist.add(data);
                }

                weekSpendingAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);

                int totalAmount = 0;
                for (DataSnapshot ds: snapshot.getChildren()){
                    Map<String, Object> map = (Map<String, Object>)ds.getValue();
                    Object total = map.get("amount");
                    int pTotal = Integer.parseInt(String.valueOf(total));
                    totalAmount += pTotal;

                    totalWeekAmountTextView.setText("Total Month's Spending: $"+totalAmount);


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    private void readWeekSpendingItem() {
        MutableDateTime epoch=new MutableDateTime();
        epoch.setDate(0);
        DateTime now=new DateTime();
        Weeks weeks=Weeks.weeksBetween(epoch,now);
        expensesRef= FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
        Query query=expensesRef.orderByChild("week").equalTo(weeks.getWeeks());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
              myDatalist.clear();
              for(DataSnapshot dataSnapshot:snapshot.getChildren())
              {
                  Data data=dataSnapshot.getValue(Data.class);
                  myDatalist.add(data);
              }
              weekSpendingAdapter.notifyDataSetChanged();
              progressBar.setVisibility(View.GONE);
                int totalAmount=0;
                for(DataSnapshot ds:snapshot.getChildren()) {
                    Map<String, Object> map = (Map<String, Object>) ds.getValue();
                    Object total = map.get("amount");
                    int pTotal = Integer.parseInt(String.valueOf(total));
                    totalAmount += pTotal;
                    totalWeekAmountTextView.setText("Total Week's Spending: $" + totalAmount);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}