package com.byteshaft.contactsharing.card;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.ImageView;
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

public class CardInfo extends AppCompatActivity {

    private ImageView squareImage;
    private CardsDatabase cardsDatabase;
    private RecyclerView mRecyclerView;
    private CustomView mViewHolder;
    public static HashMap<String, String> cardData;
    public static ArrayList<String> keysList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_info);
        keysList = new ArrayList<>();
        cardsDatabase = new CardsDatabase(getApplicationContext());
        final int cardId = getIntent().getIntExtra(AppGlobals.PROCESS_CARD_ID, 0);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        squareImage = (ImageView) findViewById(R.id.backdrop);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_card_info);
        assert fab != null;
        Log.i("TAG", "condition " + fab.isShown());
        if (AppGlobals.toBeCreatedCardName == null) {
            fab.show();
        } else {
            fab.hide();
        }
        if (AppGlobals.sIsEdit) {
            Log.i("TAG", "condition if");
            fab.show();
        } else {
            Log.i("TAG", "condition else if");
            fab.hide();
        }
        Log.i("TAG", "condition " + fab.isShown());
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CardElements.class);
                intent.putExtra(AppGlobals.PROCESS_CARD_ID, cardId);
                startActivity(intent);
            }
        });
        cardData = cardsDatabase.getCardDetails(cardId);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView = (RecyclerView) findViewById(R.id.card_details_info_recycler_view);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.canScrollVertically(LinearLayoutManager.VERTICAL);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.canScrollVertically(1);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);
        Log.e("card data!", String.valueOf(cardData));
        printMap(cardData);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("TAG", String.valueOf(CardInfo.cardData));
        Log.i("TAG", String.valueOf(CardInfo.keysList));
        CardsAdapter cardsAdapter = new CardsAdapter(CardInfo.keysList, CardInfo.cardData);
        mRecyclerView.setAdapter(cardsAdapter);
    }

    public void printMap(Map mp) {
        Iterator it = mp.entrySet().iterator();
        while (it.hasNext()) {
            if (!keysList.contains("Full Name")) {
                CardInfo.keysList.add("Full Name");
                CardInfo.cardData.put("Full Name", (String) mp.get("Full Name"));
            }
            Map.Entry pair = (Map.Entry)it.next();
            String key = (String) pair.getKey();
            Log.e("TAH", "" + pair.getValue().toString() + " " + String.valueOf(!CardInfo.keysList
                    .contains(pair.getKey()) && !pair.getValue().toString().trim().isEmpty()));
            if (!key.equals("is_image") &&
                    !key.equals("design")) {
                if (!CardInfo.keysList.contains(pair.getKey()) && !pair.getValue().toString()
                        .trim().isEmpty() && !pair.getValue().toString().equals("0")) {
                    Log.e("Adding", "" + pair.getValue());
                    CardInfo.keysList.add((String) pair.getKey());
                    Log.e("TAG", " "+ pair.getValue());
                    CardInfo.cardData.put((String) pair.getKey(), (String) pair.getValue());
                }
            }
            System.out.println(pair.getKey() + " = " + pair.getValue());
//            it.remove(); // avoids a ConcurrentModificationException
        }
        Log.e("HashMap", String.valueOf(CardInfo.cardData));
        if (!it.hasNext()) {
            if (AppGlobals.toBeCreatedCardName != null) {
                if (!CardInfo.keysList.contains(AppGlobals.KEY_FULL_NAME)) {
                    Log.e("toBeCreatedCardName", "adding video");
                    CardInfo.keysList.add(AppGlobals.KEY_FULL_NAME);
                    CardInfo.cardData.put(AppGlobals.KEY_FULL_NAME, AppGlobals.toBeCreatedCardName);
                }
            }
        }
    }

    private void initToolbar() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_card);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    ///////////////////

    class CardsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements
            RecyclerView.OnItemTouchListener {

        private ArrayList<String> cardList;
        private OnItemClickListener mListener;
        private GestureDetector mGestureDetector;
        private HashMap<String, String> cardData;

        public CardsAdapter(final ArrayList<String> cardList, HashMap<String, String> nameData,
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
//                            final View childView = mRecyclerView.findChildViewUnder(e.getX(), e.getY());
//                            if (childView != null && mListener != null) {
//                                mListener.onItemLongClick(cardList.get(mRecyclerView.getChildPosition(childView)));
//                            }
//
//                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CardInfo.this);
//                            alertDialogBuilder.setMessage("Do you want to delete this card?");
//                            alertDialogBuilder.setPositiveButton("Ok",
//                                    new DialogInterface.OnClickListener() {
//
//                                        @Override
//                                        public void onClick(DialogInterface arg0, int arg1) {
//                                            cardsDatabase.deleteEntry(cardList.get(mRecyclerView.getChildPosition(childView)));
//                                            cardList.remove(cardList.get(mRecyclerView.getChildPosition(childView)));
//                                            notifyDataSetChanged();
//                                        }
//                                    });
//
//                            alertDialogBuilder.setNegativeButton("cancel",
//                                    new DialogInterface.OnClickListener() {
//
//                                        @Override
//                                        public void onClick(DialogInterface arg0, int arg1) {
//
//                                        }
//                                    });
//
//                            AlertDialog alertDialog = alertDialogBuilder.create();
//                            alertDialog.show();
                        }
                    });
        }

        public CardsAdapter(ArrayList<String> cardList, HashMap<String, String> nameData) {
            this.cardList = cardList;
            this.cardData = nameData;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;
            Log.i("TAG", "loading one");
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_info_delegate,
                    parent, false);
            mViewHolder = new CustomView(view);
            return mViewHolder;
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
            holder.setIsRecyclable(false);
            Log.e("TAG", "test "+ cardData.get(cardList.get(position)));
            mViewHolder.image.setImageDrawable(getResources().getDrawable(
                    Helpers.getDrawable(cardList.get(position))));
            mViewHolder.textViewKey.setText(cardList.get(position));
            mViewHolder.textViewValue.setText(cardData.get(cardList.get(position)));
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

        private CircularImageView image;
        private TextView textViewKey;
        private TextView textViewValue;

        public CustomView
                (View itemView) {
            super(itemView);
            image = (CircularImageView) itemView.findViewById(R.id.field_image);
            textViewKey = (TextView) itemView.findViewById(R.id.field_name);
            textViewValue = (TextView) itemView.findViewById(R.id.field_value);
        }
    }

    public interface OnItemClickListener {
        void onItem(String item);
        void onItemLongClick(Integer position);
    }
}
