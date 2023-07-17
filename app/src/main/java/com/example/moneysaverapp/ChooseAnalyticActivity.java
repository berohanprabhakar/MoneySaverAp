package com.example.moneysaverapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ChooseAnalyticActivity extends AppCompatActivity {
    CardView todayCardView,weekCardView,monthCardView1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_analytic);
        todayCardView=findViewById(R.id.todayCardView);
        weekCardView=findViewById(R.id.weekCardView);
        monthCardView1=findViewById(R.id.monthCardView1);
        todayCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ChooseAnalyticActivity.this,DailyAnalyticActivity.class);
                startActivity(intent);
            }
        });
        weekCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ChooseAnalyticActivity.this,WeeklyAnalyticsActivity.class);
                startActivity(intent);
            }
        });
        monthCardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ChooseAnalyticActivity.this,MonthlyAnalyticsActivity.class);
                startActivity(intent);
            }
        });

    }
}