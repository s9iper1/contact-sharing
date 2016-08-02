package com.byteshaft.contactsharing.card;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
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
import android.widget.CompoundButton;
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
    private CustomView mViewHolder;
    private int cardId;
    private CardElements.OnItemClickListener mListener;
    public ArrayList<String> mCardList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards_elements);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        assert toolbar != null;
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.mipmap.delete3);
        }
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
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        elementRecyclerView = (RecyclerView) findViewById(R.id.elements_list);
        elementRecyclerView.setLayoutManager(linearLayoutManager);
        elementRecyclerView.canScrollVertically(LinearLayoutManager.VERTICAL);
        elementRecyclerView.setItemAnimator(new DefaultItemAnimator());
        elementRecyclerView.canScrollVertically(1);
        elementRecyclerView.setHasFixedSize(true);
//        printMap(CardInfo.cardData);
        Log.e("TAG", String.valueOf(CardInfo.keysList));
        CardElementsAdapter cardsAdapter = new CardElementsAdapter(CardInfo.keysList,
                CardInfo.cardData);
        elementRecyclerView.setAdapter(cardsAdapter);
        elementRecyclerView.addOnItemTouchListener(new CardElementsAdapter(CardInfo.keysList,
                CardInfo.cardData, getApplicationContext(), new OnItemClickListener() {
            @Override
            public void onItem(String item) {

            }

            @Override
            public void onCheckChange(String checkedId) {
                Log.i("TAG", "check change" + checkedId);

            }

            @Override
            public void onItemLongClick(Integer position) {

            }
        }));
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
                String name = "";
                String address = "";
                String jobTitle = "";
                String jobzyId = "";
                String contactNumber = "";
                String email = "";
                String org = "";
                if (CardInfo.cardData.get(AppGlobals.KEY_FULL_NAME) != null) {
                    name = CardInfo.cardData.get(AppGlobals.KEY_FULL_NAME);
                }
                if (CardInfo.cardData.get(AppGlobals.KEY_ADDRESS) != null) {
                    address = CardInfo.cardData.get(AppGlobals.KEY_ADDRESS);
                }
                if (CardInfo.cardData.get(AppGlobals.KEY_JOB_TITLE) != null) {
                    jobTitle = CardInfo.cardData.get(AppGlobals.KEY_JOB_TITLE);
                }
                if (CardInfo.cardData.get(AppGlobals.KEY_JOBZY_ID) != null) {
                    jobzyId = CardInfo.cardData.get(AppGlobals.KEY_JOBZY_ID);
                }
                if (CardInfo.cardData.get(AppGlobals.KEY_CONTACT_NUMBER) != null) {
                    contactNumber = CardInfo.cardData.get(AppGlobals.KEY_CONTACT_NUMBER);
                }
                if (CardInfo.cardData.get(AppGlobals.KEY_MAIL) != null) {
                    email = CardInfo.cardData.get(AppGlobals.KEY_MAIL);
                }
                if (CardInfo.cardData.get(AppGlobals.KEY_ORG) != null) {
                    org = CardInfo.cardData.get(AppGlobals.KEY_ORG);
                }
                Log.e("TAg", "name " + name);
                cardsDatabase.createNewEntry(name, address, jobTitle, contactNumber, email,
                        org, jobzyId, "", 0, 0, "");
                finish();
                break;
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        elementRecyclerView.getAdapter().notifyDataSetChanged();
    }

    public void printMap(Map mp) {
        Iterator it = mp.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            String key = (String) pair.getKey();
            if (!CardInfo.keysList.contains(pair.getKey())) {
                CardInfo.keysList.add((String) pair.getKey());
                CardInfo.cardData.put((String) pair.getKey(), (String) pair.getValue());
            }
            System.out.println(pair.getKey() + " = " + pair.getValue());
            it.remove();
        }
    }


    class CardElementsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements
            RecyclerView.OnItemTouchListener {

        private GestureDetector mGestureDetector;
        private HashMap<String, String> cardData;

        public CardElementsAdapter(final ArrayList<String> cardList, HashMap<String, String> nameData,
                                   Context context,
                                   final OnItemClickListener listener) {
            mListener = listener;
            mCardList = cardList;
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
            mCardList = cardList;
            this.cardData = nameData;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;

            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_elements_delegate,
                    parent, false);
            mViewHolder = new CustomView(view);
            return mViewHolder;
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
            holder.setIsRecyclable(false);
            mViewHolder.key.setText(mCardList.get(position));
            Log.i("TAG", mCardList.get(position));
            mViewHolder.value.setText(cardData.get(mCardList.get(position)));
            if (Helpers.getElementState((cardId + mCardList.get(position)))) {
                mViewHolder.checkBox.setChecked(true);
            } else {
                mViewHolder.checkBox.setChecked(false);
            }
            mViewHolder.elementLogo.setImageResource(Helpers.getDrawable(mCardList.get(position)));
        }


        @Override
        public int getItemCount() {
            return mCardList.size();
        }

        @Override
        public boolean onInterceptTouchEvent(final RecyclerView rv, MotionEvent e) {
            final View childView = rv.findChildViewUnder(e.getX(), e.getY());
//            if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
////                mListener.onItem(mCardList.get(rv.getChildPosition(childView)));
//                return true;
//            }
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
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    Log.i("TAG", "check" + b);
                    Log.i("TAG", String.valueOf(mCardList));
                    mListener.onCheckChange(mCardList.get(getAdapterPosition()));
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItem(String item);
        void onCheckChange(String checkedId);
        void onItemLongClick(Integer position);
    }
}
