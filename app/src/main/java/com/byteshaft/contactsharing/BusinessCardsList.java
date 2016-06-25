package com.byteshaft.contactsharing;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.byteshaft.contactsharing.bluetooth.BluetoothActivity;
import com.byteshaft.contactsharing.database.CardsDatabase;
import com.byteshaft.contactsharing.utils.AppGlobals;
import com.github.siyamed.shapeimageview.CircularImageView;

import org.json.JSONException;
import org.json.JSONObject;

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
    private HashMap<Integer, String> colorHashMap;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private boolean gridView = false;
    private HashMap<String , String[]> cardData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.business_card_list, container, false);
        cardsDatabase = new CardsDatabase(getActivity().getApplicationContext());
        colorHashMap = new HashMap<>();
        mBaseView.setTag("RecyclerViewFragment");
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        staggeredGridLayoutManager.setSpanCount(2);
        gridView = true;
        mRecyclerView = (RecyclerView) mBaseView.findViewById(R.id.card_list);
        mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
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
        Log.i("TAG", "" +cardsDatabase.getBusinessCard());
        cardData = cardsDatabase.getBusinessCard();
        mRecyclerView.setAdapter(null);
        idsList = new ArrayList<>();
        idsList = cardsDatabase.getIdOfSavedCards();
        mCardsAdapter = new CardsAdapter(idsList, cardData);
        mRecyclerView.setAdapter(mCardsAdapter);
        return mBaseView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.grid_normal_view, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_change_view:
                if (gridView) {
                    item.setIcon(getResources().getDrawable(R.drawable.grid));
                    staggeredGridLayoutManager.setSpanCount(1);
                    mRecyclerView.setAdapter(null);
                    idsList = cardsDatabase.getIdOfSavedCards();
                    nameData = cardsDatabase.getNamesOfSavedCards();
                    mCardsAdapter = new CardsAdapter(idsList, cardData);
                    mRecyclerView.setAdapter(mCardsAdapter);
                    gridView = false;
                } else {
                    item.setIcon(getResources().getDrawable(R.drawable.normal));
                    staggeredGridLayoutManager.setSpanCount(2);
                    mRecyclerView.setAdapter(null);
                    idsList = cardsDatabase.getIdOfSavedCards();
                    nameData = cardsDatabase.getNamesOfSavedCards();
                    mCardsAdapter = new CardsAdapter(idsList, cardData);
                    mRecyclerView.setAdapter(mCardsAdapter);
                    gridView = true;
                }
                return true;
        }
        return false;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        mRecyclerView.addOnItemTouchListener(new CardsAdapter(idsList, cardData,
                getActivity()
                .getApplicationContext(), new OnItemClickListener() {
            @Override
            public void onItem(Integer item, String color) {
                JSONObject jsonObject = new JSONObject();
                if (Integer.valueOf(cardData.get(item)[6]) == 1) {
                    try {
                        jsonObject.put(AppGlobals.IS_IMAGE_SHARE, 1);
                        jsonObject.put(AppGlobals.NAME, cardData.get(item)[0]);
                        jsonObject.put(AppGlobals.IMG_URI, cardData.get(item)[7]);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        jsonObject.put(AppGlobals.IS_IMAGE_SHARE, 0);
                        jsonObject.put(AppGlobals.NAME, cardData.get(item)[0]);
                        jsonObject.put(AppGlobals.ADDRESS, cardData.get(item)[1]);
                        jsonObject.put(AppGlobals.EMAIL, cardData.get(item)[4]);
                        jsonObject.put(AppGlobals.JOB_TITLE, cardData.get(item)[2]);
                        jsonObject.put(AppGlobals.ORG, cardData.get(item)[5]);
                        jsonObject.put(AppGlobals.JOBZY_ID, cardData.get(item)[8]);
                        jsonObject.put(AppGlobals.NUMBER, cardData.get(item)[3]);
                        jsonObject.put(AppGlobals.CARD_DESIGN, cardData.get(item)[9]);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Intent intent = new Intent(getActivity().getApplicationContext(), BluetoothActivity.class);
                intent.putExtra(AppGlobals.DATA_TO_BE_SENT, jsonObject.toString());
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
                                cardData = new HashMap<>();
                                cardData = cardsDatabase.getBusinessCard();
                                mCardsAdapter = new CardsAdapter(idsList, cardData);
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
        private HashMap<String, String[]> cardData;
        private int pos = 0;

        public CardsAdapter(final ArrayList<Integer> cardList, HashMap<String, String[]> nameData,
                            Context context,
                            final OnItemClickListener listener) {
            mListener = listener;
            this.cardList = cardList;
            this.cardData = nameData;
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

        public CardsAdapter(ArrayList<Integer> cardList, HashMap<String, String[]> nameData)   {
            this.cardList = cardList;
            this.cardData = nameData;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;
            if (Integer.valueOf(cardData.get(String.valueOf(cardList.get(pos)))[9]) == 0) {
                Log.i("TAG", "loading one");
                if (staggeredGridLayoutManager.getSpanCount() == 1) {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_one,
                        parent, false);
                } else {
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_one_small,
                            parent, false);
                }
            } else if (Integer.valueOf(cardData.get(String.valueOf(cardList.get(pos)))[9]) == 1) {
                Log.i("TAG", "loading two");
                if (staggeredGridLayoutManager.getSpanCount() == 1) {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_two,
                        parent, false);
                } else {
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_two_small,
                            parent, false);
                }
            } else {
                Log.i("TAG", "loading three");
                if (staggeredGridLayoutManager.getSpanCount() == 1) {
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_three,
                            parent, false);
                } else {
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_three_small,
                            parent, false);
                }
            }
            mViewHolder = new CustomView(view);
            return mViewHolder;
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
            holder.setIsRecyclable(false);
            int currentIndex = cardList.get(position);
            mViewHolder.hiddenId.setText(String.valueOf(currentIndex));
            if (cardData.get(String.valueOf(cardList.get(pos)))[6].equals("0")) {
                mViewHolder.personName.setText(cardData.get(String.valueOf(cardList.get(pos)))[0]);
                mViewHolder.address.setText(cardData.get(String.valueOf(cardList.get(pos)))[1]);
                mViewHolder.jobTitle.setText(cardData.get(String.valueOf(cardList.get(pos)))[2]);
                mViewHolder.phoneNumber.setText(cardData.get(String.valueOf(cardList.get(pos)))[3]);
                mViewHolder.emailAddress.setText(cardData.get(String.valueOf(cardList.get(pos)))[4]);
                mViewHolder.organization.setText(cardData.get(String.valueOf(cardList.get(pos)))[5]);
                mViewHolder.jobzyId.setText(cardData.get(String.valueOf(cardList.get(pos)))[8]);

                if (staggeredGridLayoutManager.getSpanCount() == 2) {
                    mViewHolder.personName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                    mViewHolder.address.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    mViewHolder.jobTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                    mViewHolder.phoneNumber.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    mViewHolder.emailAddress.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    mViewHolder.organization.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    mViewHolder.jobzyId.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    mViewHolder.logo.setVisibility(View.GONE);
                } else if (staggeredGridLayoutManager.getSpanCount() == 1){
                    mViewHolder.personName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                    mViewHolder.logo.setVisibility(View.VISIBLE);
                    mViewHolder.personName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                    mViewHolder.address.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                    mViewHolder.jobTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                    mViewHolder.phoneNumber.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                    mViewHolder.emailAddress.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                    mViewHolder.organization.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                    mViewHolder.jobzyId.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                }

                mViewHolder.address.setTypeface(AppGlobals.regularTypeface);
                mViewHolder.personName.setTypeface(AppGlobals.regularTypeface);
                mViewHolder.jobTitle.setTypeface(AppGlobals.regularTypeface);
                mViewHolder.phoneNumber.setTypeface(AppGlobals.regularTypeface);
                mViewHolder.emailAddress.setTypeface(AppGlobals.regularTypeface);
                mViewHolder.organization.setTypeface(AppGlobals.regularTypeface);
                mViewHolder.jobzyId.setTypeface(AppGlobals.regularTypeface);
            } else if (cardData.get(String.valueOf(cardList.get(pos)))[6].equals("1")) {
                mViewHolder.mainLayout.setBackgroundColor(Color.TRANSPARENT);
                mViewHolder.personName.setVisibility(View.GONE);
                mViewHolder.jobTitle.setVisibility(View.GONE);
                mViewHolder.phoneNumber.setVisibility(View.GONE);
                mViewHolder.emailAddress.setVisibility(View.GONE);
                mViewHolder.address.setVisibility(View.GONE);
                mViewHolder.organization.setVisibility(View.GONE);
                mViewHolder.jobzyId.setVisibility(View.GONE);
                mViewHolder.cardImage.setVisibility(View.VISIBLE);
                Uri imgUri = Uri.parse(cardData.get(String.valueOf(cardList.get(pos)))[7]);
                Bitmap bitmap = BitmapFactory.decodeFile(imgUri.getPath());
                int height = 1920;
                int width = 1080;
                Bitmap scaled = Bitmap.createScaledBitmap(bitmap, height, width, true);
                mViewHolder.cardImage.setImageBitmap(scaled);
            }
            pos = pos+1;
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

        private TextView personName;
        private TextView jobTitle;
        private TextView phoneNumber;
        private TextView emailAddress;
        private TextView address;
        private TextView organization;
        private TextView jobzyId;
        private ImageView cardImage;
        private RelativeLayout mainLayout;
        private TextView hiddenId;
        private CircularImageView logo;

        public CustomView(View itemView) {
            super(itemView);
            hiddenId = (TextView) itemView.findViewById(R.id.id);
            personName = (TextView) itemView.findViewById(R.id.tv_name);
            jobTitle = (TextView) itemView.findViewById(R.id.job_title);
            phoneNumber = (TextView) itemView.findViewById(R.id.phone_number);
            emailAddress = (TextView) itemView.findViewById(R.id.email_address);
            address = (TextView) itemView.findViewById(R.id.location);
            organization = (TextView) itemView.findViewById(R.id.tv_organization);
            jobzyId = (TextView) itemView.findViewById(R.id.tv_jobzy_id);
            cardImage = (ImageView) itemView.findViewById(R.id.card_image);
            mainLayout = (RelativeLayout) itemView.findViewById(R.id.main_layout);
            logo = (CircularImageView) itemView.findViewById(R.id.image_view);
        }
    }

    public interface OnItemClickListener {
        void onItem(Integer item, String color);
        void onItemLongClick(Integer position);
    }
}
