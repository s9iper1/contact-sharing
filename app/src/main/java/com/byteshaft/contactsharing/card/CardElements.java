package com.byteshaft.contactsharing.card;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.byteshaft.contactsharing.R;
import com.github.siyamed.shapeimageview.CircularImageView;

import java.util.ArrayList;
import java.util.HashMap;

public class CardElements extends AppCompatActivity {


    private CustomView mViewHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards_elements);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        assert toolbar != null;
        toolbar.setTitle("Available Elements");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.mipmap.delete3);
        }
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Fab Clicked");
            }
        });
    }

    // TODO: 28/06/2016 still need to work on that Adapter

    class CardElementsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements
            RecyclerView.OnItemTouchListener {

        private ArrayList<Integer> cardList;
        private GestureDetector mGestureDetector;
        private HashMap<String, String[]> cardData;
        private OnItemClickListener mListener;

        public CardElementsAdapter(final ArrayList<Integer> cardList, HashMap<String, String[]> nameData,
                            Context context,
                            final OnItemClickListener listener) {
            mListener = listener;
            this.cardList = cardList;
            this.cardData = nameData;
            this.mGestureDetector = new GestureDetector(context,
                    new GestureDetector.SimpleOnGestureListener() {
                        @Override
                        public boolean onSingleTapUp(MotionEvent e) {
                            return true;
                        }

                        @Override
                        public void onLongPress(MotionEvent e) {
                            super.onLongPress(e);
                            System.out.println("Long press detected");
                        }
                    });
        }

        public CardElementsAdapter(ArrayList<Integer> cardList, HashMap<String, String[]> nameData) {
            this.cardList = cardList;
            this.cardData = nameData;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;
            Log.i("TAG", "loading one");
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_one,
                    parent, false);
            mViewHolder = new CustomView(view);
            return mViewHolder;
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int pos) {
            holder.setIsRecyclable(false);

        }


        @Override
        public int getItemCount() {
            return cardList.size();
        }

        @Override
        public boolean onInterceptTouchEvent(final RecyclerView rv, MotionEvent e) {
            final View childView = rv.findChildViewUnder(e.getX(), e.getY());
            if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
                mListener.onItem(cardList.get(rv.getChildPosition(childView)));
                return true;
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.card_element, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.done_button:
                break;
        }
        return false;
    }

    // custom class getting view cardList by giving view in constructor.
    public class CustomView extends RecyclerView.ViewHolder {

        private TextView textKey;
        private TextView textValue;
        private AppCompatCheckBox checkBox;
        private CircularImageView imageView;

        public CustomView(View itemView) {
            super(itemView);
            textKey = (TextView) itemView.findViewById(R.id.text_key);
            textValue = (TextView) itemView.findViewById(R.id.text_value);
            checkBox = (AppCompatCheckBox) itemView.findViewById(R.id.element_checkbox);
            imageView = (CircularImageView) itemView.findViewById(R.id.element_logo);
        }
    }
    public interface OnItemClickListener {
        void onItem(Integer item);
        void onItemLongClick(Integer position);
    }
}
