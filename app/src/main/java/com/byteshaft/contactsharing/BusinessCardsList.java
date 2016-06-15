package com.byteshaft.contactsharing;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
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
import com.byteshaft.contactsharing.utils.AppGlobals;
import com.byteshaft.contactsharing.utils.BitmapWithCharacter;
import com.byteshaft.contactsharing.utils.SquareImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


public class BusinessCardsList extends Fragment {

    private View mBaseView;
    private CardsAdapter mCardsAdapter;
    private ArrayList<HashMap<Integer, String>> nameData;
    private ArrayList<Integer> idsList;
    private RecyclerView mRecyclerView;
    private CustomView mViewHolder;
    private CardsDatabase cardsDatabase;
    private HashMap<Integer, String> colorHashMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.business_card_list, container, false);
        cardsDatabase = new CardsDatabase(getActivity().getApplicationContext());
        colorHashMap = new HashMap<>();
        mBaseView.setTag("RecyclerViewFragment");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView = (RecyclerView) mBaseView.findViewById(R.id.card_list);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.canScrollVertically(1);
        mRecyclerView.setHasFixedSize(true);
        idsList = cardsDatabase.getIdOfSavedCards();
        if (idsList.size() == 0) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                alertDialogBuilder.setTitle("Create Business card");
                alertDialogBuilder
                        .setMessage("Create your new Business card now")
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                MainActivity.getInstance().loadFragment(new CreateBusinessCard());
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
        }
        nameData = cardsDatabase.getNamesOfSavedCards();
        return mBaseView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mRecyclerView.setAdapter(null);
        idsList = new ArrayList<>();
        nameData = new ArrayList<>();
        idsList = cardsDatabase.getIdOfSavedCards();
        nameData = cardsDatabase.getNamesOfSavedCards();
        mCardsAdapter = new CardsAdapter(idsList, nameData);
        mRecyclerView.setAdapter(mCardsAdapter);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView.addOnItemTouchListener(new CardsAdapter(idsList, nameData,
                getActivity()
                .getApplicationContext(), new OnItemClickListener() {
            @Override
            public void onItem(Integer item, String color) {
                Intent intent = new Intent(getActivity().getApplicationContext(),
                        CardDetailsActivity.class);
                intent.putExtra(AppGlobals.CARD_ID, item);
                Log.i("TAG", " "+ color);
                intent.putExtra(AppGlobals.CURRENT_COLOR, color);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(final Integer position) {
                System.out.println("2nd Long click ");
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                alertDialogBuilder.setMessage("Do you want to delete this card?");
                alertDialogBuilder.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                cardsDatabase.deleteEntry(position);
                                mRecyclerView.setAdapter(null);
                                idsList = new ArrayList<>();
                                nameData = new ArrayList<>();
                                idsList = cardsDatabase.getIdOfSavedCards();
                                nameData = cardsDatabase.getNamesOfSavedCards();
                                mCardsAdapter = new CardsAdapter(idsList, nameData);
                                mRecyclerView.setAdapter(mCardsAdapter);

                            }
                        });

                alertDialogBuilder.setNegativeButton("cancel",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        }));
    }

    class CardsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements
            RecyclerView.OnItemTouchListener {

        private ArrayList<Integer> cardList;
        private OnItemClickListener mListener;
        private GestureDetector mGestureDetector;
        private ArrayList<HashMap<Integer, String>> nameData;

        public CardsAdapter(final ArrayList<Integer> cardList, ArrayList<HashMap<Integer, String>> nameData,
                            Context context,
                            final OnItemClickListener listener) {
            mListener = listener;
            this.cardList = cardList;
            this.nameData = nameData;
            mGestureDetector = new GestureDetector(context,
                    new GestureDetector.SimpleOnGestureListener() {
                        @Override
                        public boolean onSingleTapUp(MotionEvent e) {
                            return true;
                        }

                        @Override
                        public void onLongPress(MotionEvent e) {
                            super.onLongPress(e);
                            System.out.println("Long press detected");
                            View childView = mRecyclerView.findChildViewUnder(e.getX(), e.getY());
                            if (childView != null && mListener != null) {
                                mListener.onItemLongClick(cardList.get(mRecyclerView.getChildPosition(childView)));
                            }
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
            int[] array = getResources().getIntArray(R.array.letter_tile_colors);
            final BitmapWithCharacter tileProvider = new BitmapWithCharacter();
            final String color = String.valueOf(array[new Random().nextInt(array.length)]);
            int constantColor = tileProvider.pickColor(color);
            String hexColor = "#" + Integer.toHexString(constantColor).substring(2);
            colorHashMap.put(currentIndex, hexColor);
            final Bitmap letterTile = tileProvider.getLetterTile(card,
                    constantColor, 100, 100);
            mViewHolder.squareImageView.setImageBitmap(letterTile);
        }

        @Override
        public int getItemCount() {
            return cardList.size();
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View childView = rv.findChildViewUnder(e.getX(), e.getY());
            if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
                mListener.onItem(cardList.get(rv.getChildPosition(childView)),
                        String.valueOf(colorHashMap.get(cardList.get(rv.getChildPosition(childView)))));
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
        public SquareImageView squareImageView;

        public CustomView(View itemView) {
            super(itemView);
            hiddenId = (TextView) itemView.findViewById(R.id.hidden_id);
            textView = (TextView) itemView.findViewById(R.id.card_owner_name);
            squareImageView = (SquareImageView) itemView.findViewById(R.id.square_image_view);
        }
    }

    public interface OnItemClickListener {
        void onItem(Integer item, String color);
        void onItemLongClick(Integer position);
    }
}
