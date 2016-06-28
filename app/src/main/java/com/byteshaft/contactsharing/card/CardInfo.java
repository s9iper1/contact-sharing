package com.byteshaft.contactsharing.card;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.byteshaft.contactsharing.R;
import com.byteshaft.contactsharing.database.CardsDatabase;
import com.byteshaft.contactsharing.utils.AppGlobals;
import com.byteshaft.contactsharing.utils.SquareImage;
import com.github.siyamed.shapeimageview.CircularImageView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CardInfo extends AppCompatActivity {

    private SquareImage squareImage;
    private CardsDatabase cardsDatabase;
    private RecyclerView mRecyclerView;
    private CustomView mViewHolder;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private HashMap<String, String> cardData;
    private ArrayList<String> keysList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_info);
        keysList = new ArrayList<>();
        cardsDatabase = new CardsDatabase(getApplicationContext());
        int cardId = getIntent().getIntExtra("card_id", 0);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        squareImage = (SquareImage) findViewById(R.id.square_image);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("fab clicked");
            }
        });
        cardData = cardsDatabase.getCardDetails(cardId);
        mRecyclerView = (RecyclerView) findViewById(R.id.card_details_info_recycler_view);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView = (RecyclerView) findViewById(R.id.card_list);
        mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.canScrollVertically(1);
        mRecyclerView.setHasFixedSize(true);
        printMap(cardData);
    }

    public void printMap(Map mp) {
        Iterator it = mp.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            String key = (String) pair.getKey();
            if (!pair.getValue().toString().trim().isEmpty() && !key.equals("is_image") &&
                    !key.equals("design")) {
                keysList.add((String) pair.getKey());
            }
            System.out.println(pair.getKey() + " = " + pair.getValue());
            it.remove(); // avoids a ConcurrentModificationException
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
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int pos) {
            holder.setIsRecyclable(false);
            String currentIndex = cardList.get(pos);
            mViewHolder.image.setImageDrawable();
        }

        private int getDrawable(String design) {
            switch (design) {
                case "Name":
                    return R.drawable.background_one;
                case "Address":
                    return R.drawable.background_two;
                case "Job Title":
                    return R.drawable.background_three;
                case "Jobzy Id":
                    return R.drawable.background_three;
                case "Phone Number":
                    return R.drawable.background_three;
                case "Email":
                    return R.drawable.background_three;
                case "Organization":
                    return R.drawable.background_three;
                default:
                    return R.drawable.background_one;
            }
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
        void onItem(Integer item);
        void onItemLongClick(Integer position);
    }
}
