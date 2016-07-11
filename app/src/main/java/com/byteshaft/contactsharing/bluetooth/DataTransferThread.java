package com.byteshaft.contactsharing.bluetooth;

import android.bluetooth.BluetoothSocket;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.byteshaft.contactsharing.database.ReceivedCardsDB;
import com.byteshaft.contactsharing.utils.AppGlobals;
import com.byteshaft.contactsharing.utils.Helpers;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

class DataTransferThread extends Thread {
    private final String TAG = "transfer";
    private final BluetoothSocket socket;
    private Handler handler;
    private JSONObject receivedData;

    public DataTransferThread(BluetoothSocket socket, Handler handler) {
        this.socket = socket;
        this.handler = handler;
    }

    public void run() {
        try {
            InputStream inputStream = socket.getInputStream();
            boolean waitingForHeader = true;
            ByteArrayOutputStream dataOutputStream = new ByteArrayOutputStream();
            byte[] headerBytes = new byte[22];
            byte[] digest = new byte[16];
            int headerIndex = 0;
            ProgressData progressData = new ProgressData();

            while (true) {
                if (waitingForHeader) {
                    byte[] header = new byte[1];
                    inputStream.read(header, 0, 1);
                    Log.v(TAG, "Received Header Byte: " + header[0]);
                    headerBytes[headerIndex++] = header[0];

                    if (headerIndex == 22) {
                        if ((headerBytes[0] == Constants.HEADER_MSB) && (headerBytes[1] == Constants.HEADER_LSB)) {
                            Log.v(TAG, "Header Received.  Now obtaining length");
                            byte[] dataSizeBuffer = Arrays.copyOfRange(headerBytes, 2, 6);
                            progressData.totalSize = Utils.byteArrayToInt(dataSizeBuffer);
                            progressData.remainingSize = progressData.totalSize;
                            Log.v(TAG, "Data size: " + progressData.totalSize);
                            digest = Arrays.copyOfRange(headerBytes, 6, 22);
                            waitingForHeader = false;
                            sendProgress(progressData);
                        } else {
                            Log.e(TAG, "Did not receive correct header.  Closing socket");
                            socket.close();
                            handler.sendEmptyMessage(MessageType.INVALID_HEADER);
                            break;
                        }
                    }

                } else {
                    // Read the data from the stream in chunks
                    byte[] buffer = new byte[Constants.CHUNK_SIZE];
                    Log.v(TAG, "Waiting for data.  Expecting " + progressData.remainingSize + " more bytes.");
                    int bytesRead = inputStream.read(buffer);
                    Log.v(TAG, "Read " + bytesRead + " bytes into buffer");
                    dataOutputStream.write(buffer, 0, bytesRead);
                    progressData.remainingSize -= bytesRead;
                    sendProgress(progressData);

                    if (progressData.remainingSize <= 0) {
                        Log.v(TAG, "Expected data has been received.");
                        break;
                    }
                }
            }

            // check the integrity of the data
            final byte[] data = dataOutputStream.toByteArray();

            if (Utils.digestMatch(data, digest)) {
                Log.v(TAG, "Digest matches OK.");
                if (!AppGlobals.sIncomingImage) {
                    Message message = new Message();
                    message.obj = data;
                    message.what = MessageType.DATA_RECEIVED;
                    String str = new String(((byte[]) message.obj), "UTF-8");
                    Log.d("Just received", str);
                    receivedData = new JSONObject(str);
                    if (receivedData.getInt(AppGlobals.IS_IMAGE_SHARE) == 1) {
                        Log.e("TAG", "received");
                        AppGlobals.sIncomingImage = true;
                        AppGlobals.imageOwner = receivedData.getString("name");
                    } else {
                        if (receivedData.getInt(AppGlobals.IS_IMAGE_SHARE) == 0) {
                            if (receivedData.has(AppGlobals.NAME)) {
                                AppGlobals.name = receivedData.getString(AppGlobals.NAME);
                            }
                            if (receivedData.has(AppGlobals.ADDRESS)) {
                                AppGlobals.address = receivedData.getString(AppGlobals.ADDRESS);
                            }
                            if (receivedData.has(AppGlobals.JOB_TITLE)) {
                                AppGlobals.jobTitle = receivedData.getString(AppGlobals.JOB_TITLE);
                            }
                            if (receivedData.has(AppGlobals.JOBZY_ID)) {
                                AppGlobals.jobzyId = receivedData.getString(AppGlobals.JOBZY_ID);
                            }
                            if (receivedData.has(AppGlobals.NUMBER)) {
                                AppGlobals.contectNumber = receivedData.getString(AppGlobals.NUMBER);
                            }
                            if (receivedData.has(AppGlobals.EMAIL)) {
                                AppGlobals.email = receivedData.getString(AppGlobals.EMAIL);
                            }
                            if (receivedData.has(AppGlobals.ORG)) {
                                AppGlobals.org = receivedData.getString(AppGlobals.ORG);
                            }
                            if (receivedData.has(AppGlobals.CARD_DESIGN)) {
                                AppGlobals.design = receivedData.getInt(AppGlobals.CARD_DESIGN);
                            }
                            if (receivedData.has(AppGlobals.KEY_LOGO)) {
                                AppGlobals.sInComingLogo = true;
                            }
                        }
                    }

                    Log.d("Data", str);
                    handler.sendMessage(message);
                } else if (AppGlobals.sInComingLogo) {
                    Message message = new Message();
                    message.obj = data;
                    message.what = MessageType.DATA_RECEIVED;
                    handler.sendMessage(message);
                    Log.i("TAG", "Received image");

                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 2;
                    Bitmap image = BitmapFactory.decodeByteArray(((byte[]) message.obj), 0,
                            ((byte[]) message.obj).length, options);
                        ReceivedCardsDB cardsDatabase = new ReceivedCardsDB(AppGlobals.getContext());
                    String path = Helpers.saveImage(image, AppGlobals.imageOwner);
                        cardsDatabase.createNewEntry(AppGlobals.name, AppGlobals.address, AppGlobals.jobTitle,
                                AppGlobals.contectNumber, AppGlobals.email
                                , AppGlobals.org, AppGlobals.jobzyId, "", 0, AppGlobals.design, path);

                } else {
                    Log.v(TAG, "Digest matches OK.");
                    Message message = new Message();
                    message.obj = data;
                    message.what = MessageType.DATA_RECEIVED;
                    handler.sendMessage(message);
                    Log.i("TAG", "Received image");

                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 2;
                    Bitmap image = BitmapFactory.decodeByteArray(((byte[]) message.obj), 0,
                            ((byte[]) message.obj).length, options);
                    if (!AppGlobals.imageOwner.trim().isEmpty()) {
                        String path = Helpers.saveImage(image, AppGlobals.imageOwner);
                        ReceivedCardsDB cardsDatabase = new ReceivedCardsDB(AppGlobals.getContext());
                        cardsDatabase.createNewEntry(AppGlobals.imageOwner, "", "", "", "", "", "",
                                path, 1, 4, "");
                    }

                    // Send the digest back to the client as a confirmation
                    Log.v(TAG, "Sending back digest for confirmation");
                    OutputStream outputStream = socket.getOutputStream();
                    outputStream.write(digest);
                    AppGlobals.sIncomingImage = false;
                }

                // Send the digest back to the client as a confirmation
                Log.v(TAG, "Sending back digest for confirmation");
                OutputStream outputStream = socket.getOutputStream();
                outputStream.write(digest);

            } else {
                Log.e(TAG, "Digest did not match.  Corrupt transfer?");
                handler.sendEmptyMessage(MessageType.DIGEST_DID_NOT_MATCH);
            }
                Log.v(TAG, "Closing server socket");
                socket.close();

        } catch (Exception ex) {
            if (BluetoothActivity.getInstance().progressDialog != null &&
                    BluetoothActivity.getInstance().progressDialog.isShowing()) {
                BluetoothActivity.getInstance().progressDialog.dismiss();
                BluetoothActivity.getInstance().progressDialog = null;
            }
            Log.d(TAG, ex.toString());
            ex.printStackTrace();
        }
    }

    private void sendProgress(ProgressData progressData) {
        Message message = new Message();
        message.obj = progressData;
        message.what = MessageType.DATA_PROGRESS_UPDATE;
        handler.sendMessage(message);
    }
}
