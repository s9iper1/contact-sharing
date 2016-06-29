package com.byteshaft.contactsharing.card;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.byteshaft.contactsharing.R;
import com.byteshaft.contactsharing.database.CardsDatabase;
import com.byteshaft.contactsharing.utils.AppGlobals;
import com.byteshaft.contactsharing.utils.Helpers;
import com.github.siyamed.shapeimageview.CircularImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CardElements extends AppCompatActivity {

    private RecyclerView elementRecyclerView;
    private CardsDatabase cardsDatabase;
    private RecyclerView mRecyclerView;
    private CustomView mViewHolder;
    private HashMap<String, String> cardData;
    private ArrayList<String> keysList;
    private int cardId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards_elements);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        assert toolbar != null;
        toolbar.setTitle("Available Elements");
        setSupportActionBar(toolbar);
        keysList = new ArrayList<>();
        cardsDatabase = new CardsDatabase(getApplicationContext());
        cardId = getIntent().getIntExtra(AppGlobals.PROCESS_CARD_ID, 0);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BusinessForm.class);
                startActivity(intent);
            }
        });

        cardData = cardsDatabase.getCardDetails(cardId);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView = (RecyclerView) findViewById(R.id.elements_list);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.canScrollVertically(LinearLayoutManager.VERTICAL);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.canScrollVertically(1);
        mRecyclerView.setHasFixedSize(true);
        printMap(cardData);
        CardElementsAdapter cardsAdapter = new CardElementsAdapter(keysList, cardData);
        mRecyclerView.setAdapter(cardsAdapter);
    }

    public void printMap(Map mp) {
        Iterator it = mp.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            String key = (String) pair.getKey();
            if (!pair.getValue().toString().trim().isEmpty() && !key.equals("is_image") &&
                    !key.equals("design")) {
                keysList.add((String) pair.getKey());
            }
            System.out.println(pair.getKey() + " = " + pair.getValue());
            it.remove();
        }
    }


    class CardElementsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements
            RecyclerView.OnItemTouchListener {

        private ArrayList<String> cardList;
        private GestureDetector mGestureDetector;
        private HashMap<String, String> cardData;
        private OnItemClickListener mListener;

        public CardElementsAdapter(final ArrayList<String> cardList, HashMap<String, String> nameData,
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

        public CardElementsAdapter(ArrayList<String> cardList, HashMap<String, String> nameData) {
            this.cardList = cardList;
            this.cardData = nameData;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;
            Log.i("TAG", "loading one");
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_elements_delegate,
                    parent, false);
            mViewHolder = new CustomView(view);
            return mViewHolder;
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
            holder.setIsRecyclable(false);
            mViewHolder.key.setText(cardList.get(position));
            mViewHolder.value.setText(cardData.get(cardList.get(position)));
            if (Helpers.getElementState((cardId+cardList.get(position)))) {
                mViewHolder.checkBox.setChecked(true);
            } else {
                mViewHolder.checkBox.setChecked(false);
            }
            mViewHolder.elementLogo.setImageDrawable(getResources().getDrawable(
                    Helpers.getDrawable(cardList.get(position))));
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

    // custom class getting view cardList by giving view in constructor.
    public class CustomView extends RecyclerView.ViewHolder {

        private TextView key;
        private TextView value;
        private AppCompatCheckBox checkBox;
        private CircularImageView elementLogo;

        public CustomView(View itemView) {
            super(itemView);
            key = (TextView) itemView.findViewById(R.id.key);
            value = (TextView) itemView.findViewById(R.id.value);
            checkBox = (AppCompatCheckBox) itemView.findViewById(R.id.element_checkbox);
            elementLogo = (CircularImageView) itemView.findViewById(R.id.element_logo);
        }
    }
    public interface OnItemClickListener {
        void onItem(String item);
        void onItemLongClick(Integer position);
    }
}
