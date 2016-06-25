package com.byteshaft.contactsharing.bluetooth;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.byteshaft.contactsharing.utils.AppGlobals;

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
                Message message = new Message();
                message.obj = data;
                message.what = MessageType.DATA_RECEIVED;
                String str = new String(((byte[]) message.obj), "UTF-8");
                Log.d("Just received", str);
                receivedData = new JSONObject(str);
                if (receivedData.getBoolean("image")) {
                    Log.e("TAG", "received");
                    AppGlobals.sIncomingImage = true;
                }
                Log.d("Data", str);
                handler.sendMessage(message);

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
