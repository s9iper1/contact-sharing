package com.byteshaft.contactsharing;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.byteshaft.contactsharing.database.CardsDatabase;

import java.util.ArrayList;
import java.util.HashMap;


public class BusinessCardsList extends Fragment {

    private View mBaseView;
    private CardsAdapter mCardsAdapter;
    private ArrayList<HashMap<Integer, String>> nameData;
    private ArrayList<Integer> idsList;
    private RecyclerView mRecyclerView;
    private CustomView mViewHolder;
    private CardsDatabase cardsDatabase;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.business_card_list, container, false);
        cardsDatabase = new CardsDatabase(getActivity().getApplicationContext());
        mBaseView.setTag("RecyclerViewFragment");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView = (RecyclerView) mBaseView.findViewById(R.id.card_list);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.canScrollVertically(1);
        mRecyclerView.setHasFixedSize(true);
        idsList = cardsDatabase.getIdOfSavedCards();
        nameData = cardsDatabase.getNamesOfSavedCards();
//        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        return mBaseView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCardsAdapter = new CardsAdapter(idsList, nameData);
        mRecyclerView.setAdapter(mCardsAdapter);
        mRecyclerView.addOnItemTouchListener(new CardsAdapter(idsList, nameData,
                getActivity()
                .getApplicationContext(), new OnItemClickListener() {
            @Override
            public void onItem(Integer item) {
                Log.i("TAG", String.valueOf(item));
            }
        }));
    }

    class CardsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements
            RecyclerView.OnItemTouchListener {

        private ArrayList<Integer> cardList;
        private OnItemClickListener mListener;
        private GestureDetector mGestureDetector;
        private ArrayList<HashMap<Integer, String>> nameData;

        public CardsAdapter(ArrayList<Integer> cardList, ArrayList<HashMap<Integer, String>> nameData,
                            Context context,
                             OnItemClickListener listener) {
            mListener = listener;
            this.cardList = cardList;
            this.nameData = nameData;
            mGestureDetector = new GestureDetector(context,
                    new GestureDetector.SimpleOnGestureListener() {
                        @Override
                        public boolean onSingleTapUp(MotionEvent e) {
                            return true;
                        }
                    });
        }

        public CardsAdapter(ArrayList<Integer> cardList, ArrayList<HashMap<Integer, String>> nameData)   {
            this.cardList = cardList;
            this.nameData = nameData;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_delegate,
                    parent, false);
            mViewHolder = new CustomView(view);
            return mViewHolder;
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
            holder.setIsRecyclable(false);
            int currentIndex = cardList.get(position);
            mViewHolder.hiddenId.setText(String.valueOf(currentIndex));
            String itemFromArray = nameData.get(position).get(currentIndex);
            String card =  itemFromArray.substring(0, 1).toUpperCase() + itemFromArray.substring(1);
            mViewHolder.textView.setText(card);
            mViewHolder.textView.setTypeface(AppGlobals.typeface);
        }

        @Override
        public int getItemCount() {
            return cardList.size();
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View childView = rv.findChildViewUnder(e.getX(), e.getY());
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
    public static class CustomView extends RecyclerView.ViewHolder {

        public TextView hiddenId;
        public TextView textView;

        public CustomView(View itemView) {
            super(itemView);
            hiddenId = (TextView) itemView.findViewById(R.id.hidden_id);
            textView = (TextView) itemView.findViewById(R.id.card_owner_name);
        }
    }

    public interface OnItemClickListener {
        void onItem(Integer item);
    }
}
