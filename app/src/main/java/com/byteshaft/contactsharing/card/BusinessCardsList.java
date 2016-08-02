package com.byteshaft.contactsharing.card;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.byteshaft.contactsharing.CreateBusinessCard;
import com.byteshaft.contactsharing.MainActivity;
import com.byteshaft.contactsharing.R;
import com.byteshaft.contactsharing.database.CardsDatabase;
import com.byteshaft.contactsharing.utils.AppGlobals;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class BusinessCardsList extends Fragment implements View.OnClickListener {

    private View mBaseView;
    private CardsAdapter mCardsAdapter;
    private ArrayList<HashMap<Integer, String>> nameData;
    private ArrayList<Integer> idsList;
    private RecyclerView mRecyclerView;
    private CustomView mViewHolder;
    private CardsDatabase cardsDatabase;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private boolean gridView = false;
    private HashMap<String, String[]> cardData;
    public static final int MY_PERMISSIONS_REQUEST_STORAGE = 0;
    public OnItemClickListener mListener;
    public AppCompatButton newCardButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.business_card_list, container, false);
        newCardButton = (AppCompatButton) mBaseView.findViewById(R.id.new_card);
        newCardButton.setOnClickListener(this);
        cardsDatabase = new CardsDatabase(getActivity().getApplicationContext());
        mBaseView.setTag("RecyclerViewFragment");
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        gridView = true;
        mRecyclerView = (RecyclerView) mBaseView.findViewById(R.id.card_list);
        mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.canScrollVertically(1);
        mRecyclerView.setHasFixedSize(true);
        idsList = cardsDatabase.getIdOfSavedCards();
        loadData();
        return mBaseView;
    }

    @Override
    public void onResume() {
        super.onResume();
        AppGlobals.sIsEdit = false;
        mRecyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i("TAG", "Permission granted");
                    loadData();
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Permission denied!"
                            , Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    private void loadData() {
        cardData = new HashMap<>();
        cardData = cardsDatabase.getBusinessCard();
        if (cardData.isEmpty()) {
            cardData.put("0", new String[] {"Dummy Card", "0", "" , "0"});
        }
        mRecyclerView.setAdapter(null);
        if (idsList.size() == 0) {
            idsList.add(0, 0);
        } else {
            idsList = cardsDatabase.getIdOfSavedCards();
        }
        Log.i("idsList", String.valueOf(idsList));
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
//        if (ContextCompat.checkSelfPermission(getActivity(),
//                Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED) {
//            requestPermissions(
//                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                    MY_PERMISSIONS_REQUEST_STORAGE);
//        } else {
//            loadData();
//        }
        mCardsAdapter = new CardsAdapter(idsList, cardData);
        mRecyclerView.setAdapter(mCardsAdapter);
    }


//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.grid_normal_view, menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_change_view:
//                if (gridView) {
//                    item.setIcon(getResources().getDrawable(R.drawable.grid));
//                    staggeredGridLayoutManager.setSpanCount(1);
//                    mRecyclerView.setAdapter(null);
//                    idsList = cardsDatabase.getIdOfSavedCards();
//                    nameData = cardsDatabase.getNamesOfSavedCards();
//                    mCardsAdapter = new CardsAdapter(idsList, cardData);
//                    mRecyclerView.setAdapter(mCardsAdapter);
//                    gridView = false;
//                } else {
//                    item.setIcon(getResources().getDrawable(R.drawable.normal));
//                    staggeredGridLayoutManager.setSpanCount(2);
//                    mRecyclerView.setAdapter(null);
//                    idsList = cardsDatabase.getIdOfSavedCards();
//                    nameData = cardsDatabase.getNamesOfSavedCards();
//                    mCardsAdapter = new CardsAdapter(idsList, cardData);
//                    mRecyclerView.setAdapter(mCardsAdapter);
//                    gridView = true;
//                }
//                return true;
//        }
//        return false;
//    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        mRecyclerView.addOnItemTouchListener(new CardsAdapter(idsList, cardData,
                getActivity()
                        .getApplicationContext(), new OnItemClickListener() {
            @Override
            public void onItem(Integer item) {
                Log.i("IdsList", String.valueOf(item));
                if (!cardData.get(String.valueOf(item))[0].equals("Dummy Card")) {
                    Intent intent = new Intent(getActivity().getApplicationContext(), CardInfo.class);
                    intent.putExtra("card_id", item);
                    startActivity(intent);
                }
                JSONObject jsonObject = new JSONObject();
//                if (Integer.valueOf(cardData.get(String.valueOf(item))[6]) == 1) {
//                    try {
//                        jsonObject.put(AppGlobals.IS_IMAGE_SHARE, 1);
//                        jsonObject.put(AppGlobals.NAME, cardData.get(String.valueOf(item))[0]);
//                        jsonObject.put(AppGlobals.IMG_URI, cardData.get(String.valueOf(item))[7]
//                                .replaceAll("/", "_"));
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    try {
//                        jsonObject.put(AppGlobals.IS_IMAGE_SHARE, 0);
//                        jsonObject.put(AppGlobals.NAME, cardData.get(String.valueOf(item))[0]);
//                        jsonObject.put(AppGlobals.ADDRESS, cardData.get(String.valueOf(item))[1]);
//                        jsonObject.put(AppGlobals.EMAIL, cardData.get(String.valueOf(item))[4]);
//                        jsonObject.put(AppGlobals.JOB_TITLE, cardData.get(String.valueOf(item))[2]);
//                        jsonObject.put(AppGlobals.ORG, cardData.get(String.valueOf(item))[5]);
//                        jsonObject.put(AppGlobals.JOBZY_ID, cardData.get(String.valueOf(item))[8]);
//                        jsonObject.put(AppGlobals.NUMBER, cardData.get(String.valueOf(item))[3]);
//                        jsonObject.put(AppGlobals.CARD_DESIGN, cardData.get(String.valueOf(item))[9]);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//                Intent intent = new Intent(getActivity().getApplicationContext(),
//                        BluetoothActivity.class);
//                intent.putExtra(AppGlobals.DATA_TO_BE_SENT, jsonObject.toString());
//                startActivity(intent);
            }

            @Override
            public void onEditClick(View view, Integer cardId) {
                Log.i("TAG", "onEditClick" + cardId);
                if (!cardData.get(String.valueOf((cardId)))[0].equals("Dummy Card")) {
                    AppGlobals.sIsEdit = true;
                    Intent intent = new Intent(getActivity().getApplicationContext(), CardInfo.class);
                    intent.putExtra(AppGlobals.PROCESS_CARD_ID, cardId);
                    startActivity(intent);
                }
            }

            @Override
            public void onShareClick(View view, Integer cardId) {
                Log.i("TAG", "onShareClick" + cardId);

            }

            @Override
            public void onItemLongClick(final Integer position) {
                System.out.println("2nd Long click ");
            }
        }));
    }

    private void enterNameDialog() {
        final EditText input;
        android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(getActivity());
        // Setting Dialog Title
        alertDialog.setTitle("Enter Your Name");

        // outside touch disable

        input = new EditText(getActivity());
        InputMethodManager imm = (InputMethodManager) AppGlobals.getContext().
                getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(input, InputMethodManager.SHOW_IMPLICIT);
        input.requestFocus();
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        input.setHint("ex. John");
        alertDialog.setView(input);

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

        final android.support.v7.app.AlertDialog dialog = alertDialog.create();
        dialog.show();
        // Showing Alert Message
        dialog.setCancelable(false);
        dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = input.getText().toString().trim();

                if (name.equals("")) {
                    Toast.makeText(getActivity(), "Please enter a name", Toast.LENGTH_SHORT).show();
                } else if (cardsDatabase.hasObject(name)) {
                    Toast.makeText(getActivity(), "Name already exists", Toast.LENGTH_SHORT).show();
                }
                else {
                    dialog.dismiss();
                    AppGlobals.toBeCreatedCardName = name;
                    getActivity().startActivity(new Intent(getActivity().getApplicationContext(),
                            CardInfo.class));

                }
            }
        });

        dialog.getButton(android.app.AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.new_card:
                enterNameDialog();
                break;
        }
    }

    class CardsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements
            RecyclerView.OnItemTouchListener {

        private ArrayList<Integer> cardList;
        private GestureDetector mGestureDetector;
        private HashMap<String, String[]> cardData;

        public CardsAdapter(final ArrayList<Integer> cardList, HashMap<String, String[]> nameData,
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
                            final View childView = mRecyclerView.findChildViewUnder(e.getX(), e.getY());
                            if (childView != null && mListener != null) {
                                mListener.onItemLongClick(cardList.get(mRecyclerView.getChildPosition(childView)));
                            }

                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                            alertDialogBuilder.setMessage("Do you want to delete this card?");
                            alertDialogBuilder.setPositiveButton("Ok",
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1) {
                                            cardsDatabase.deleteEntry(cardList.get(mRecyclerView.getChildPosition(childView)));
                                            cardList.remove(cardList.get(mRecyclerView.getChildPosition(childView)));
                                            mRecyclerView.getAdapter().notifyDataSetChanged();
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
                    });
        }

        public CardsAdapter(ArrayList<Integer> cardList, HashMap<String, String[]> nameData) {
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
            int currentIndex = cardList.get(pos);
            mViewHolder.hiddenId.setText(String.valueOf(currentIndex));
            Log.i("TAG", "onBindViewHolder" + cardData);
            if (cardData.get(String.valueOf(cardList.get(pos)))[3].equals("0")) {
                Log.i("TAG", "onBindViewHolder");
                Log.i("TAG", "this"+ cardData.get(String.valueOf(cardList.get(pos)))[0]);
                mViewHolder.personName.setText(cardData.get(String.valueOf(cardList.get(pos)))[0]);
//                mViewHolder.address.setText(cardData.get(String.valueOf(cardList.get(pos)))[1]);
//                mViewHolder.jobTitle.setText(cardData.get(String.valueOf(cardList.get(pos)))[2]);
//                mViewHolder.phoneNumber.setText(cardData.get(String.valueOf(cardList.get(pos)))[3]);
//                mViewHolder.emailAddress.setText(cardData.get(String.valueOf(cardList.get(pos)))[4]);
//                mViewHolder.organization.setText(cardData.get(String.valueOf(cardList.get(pos)))[5]);
//                mViewHolder.jobzyId.setText(cardData.get(String.valueOf(cardList.get(pos)))[8]);
                mViewHolder.personName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                mViewHolder.cardImage.setVisibility(View.VISIBLE);
                mViewHolder.cardImage.setBackground(getResources().getDrawable(getDrawable(Integer.valueOf(cardData.
                        get(String.valueOf(cardList.get(pos)))[1]))));
//                    mViewHolder.address.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
//                    mViewHolder.jobTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
//                    mViewHolder.phoneNumber.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
//                    mViewHolder.emailAddress.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
//                    mViewHolder.organization.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
//                    mViewHolder.jobzyId.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

//                mViewHolder.address.setTypeface(AppGlobals.regularTypeface);
                mViewHolder.personName.setTypeface(AppGlobals.regularTypeface);
//                mViewHolder.jobTitle.setTypeface(AppGlobals.regularTypeface);
//                mViewHolder.phoneNumber.setTypeface(AppGlobals.regularTypeface);
//                mViewHolder.emailAddress.setTypeface(AppGlobals.regularTypeface);
//                mViewHolder.organization.setTypeface(AppGlobals.regularTypeface);
//                mViewHolder.jobzyId.setTypeface(AppGlobals.regularTypeface);
            } else if (cardData.get(String.valueOf(cardList.get(pos)))[3].equals("1")) {
                mViewHolder.mainLayout.setBackgroundColor(Color.TRANSPARENT);
                mViewHolder.personName.setVisibility(View.GONE);
//                mViewHolder.jobTitle.setVisibility(View.GONE);
//                mViewHolder.phoneNumber.setVisibility(View.GONE);
//                mViewHolder.emailAddress.setVisibility(View.GONE);
//                mViewHolder.address.setVisibility(View.GONE);
//                mViewHolder.organization.setVisibility(View.GONE);
//                mViewHolder.jobzyId.setVisibility(View.GONE);
                mViewHolder.cardImage.setVisibility(View.VISIBLE);
                Uri imgUri = Uri.parse(cardData.get(String.valueOf(cardList.get(pos)))[2]);
                Bitmap bitmap = BitmapFactory.decodeFile(imgUri.getPath());
                int height = 1920;
                int width = 1080;
                Bitmap scaled = Bitmap.createScaledBitmap(bitmap, height, width, true);
                mViewHolder.cardImage.setImageBitmap(scaled);
            }
        }

        private int getDrawable(int design) {
            switch (design) {
                case 0:
                    mViewHolder.personName.setTextColor(getResources().getColor(android.R.color.black));
                    return R.drawable.background_one;
                case 1:
                    mViewHolder.personName.setTextColor(getResources().getColor(android.R.color.white));
                    return R.drawable.background_two;
                case 2:
                    mViewHolder.personName.setTextColor(getResources().getColor(android.R.color.white));
                    return R.drawable.background_three;
                default:
                    mViewHolder.personName.setTextColor(getResources().getColor(android.R.color.black));
                    return R.drawable.background_one;
            }
        }

        @Override
        public int getItemCount() {
            return cardList.size();
        }

        @Override
        public boolean onInterceptTouchEvent(final RecyclerView rv, MotionEvent e) {
//            final View childView = rv.findChildViewUnder(e.getX(), e.getY());
//            if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
//                mListener.onItem(cardList.get(rv.getChildPosition(childView)));
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

        private TextView personName;
        private ImageView cardImage;
        private RelativeLayout mainLayout;
        private TextView hiddenId;
        private ImageButton editButton;
        private ImageButton shareButton;

        public CustomView(View itemView) {
            super(itemView);
            hiddenId = (TextView) itemView.findViewById(R.id.invisible_id);
            personName = (TextView) itemView.findViewById(R.id.person_name);
            cardImage = (ImageView) itemView.findViewById(R.id.background);
            mainLayout = (RelativeLayout) itemView.findViewById(R.id.image_layout);
            editButton = (ImageButton) itemView.findViewById(R.id.edit);
            shareButton = (ImageButton) itemView.findViewById(R.id.share);

            cardImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onItem(idsList.get(getAdapterPosition()));
                }
            });

            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("Cilck", "done");
                    mListener.onEditClick(view, idsList.get(getAdapterPosition()));
                }
            });

            shareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("Click", "done");
                    mListener.onShareClick(view, idsList.get(getAdapterPosition()));
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItem(Integer item);
        void onEditClick(View view, Integer cardId);
        void onShareClick(View view, Integer cardId);
        void onItemLongClick(Integer position);
    }
}
