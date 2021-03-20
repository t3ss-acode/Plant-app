package com.example.plant_app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.plant_app.model.plantParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;


public class IdPlantActivity extends AppCompatActivity {

    private static final String ID_PLANT_LOG_TAG = "log_idplant";
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private String JPG_IMAGE_NAME = "converted_image.jpg";

    private String currentPhotoPath;

    private ImageView takenImage;
    private TextView tutorialText;

    // Volley
    private RequestQueue mRequestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_id_plant);

        takenImage = (ImageView) findViewById(R.id.takenImageView);
        tutorialText = (TextView) findViewById(R.id.tutorialTextView);

        mRequestQueue = Volley.newRequestQueue(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Volley, cancel pending requests
        mRequestQueue.cancelAll(this);
        //volley stop pending request. Ta bort resten i kön
    }





    public void identifyPlant(View view) {
        dispatchTakePictureIntent();
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();

                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(this,
                            "com.example.plant_app.fileprovider",
                            photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            } catch (IOException ex) {
                Log.d(ID_PLANT_LOG_TAG, "Unable to create file for image");
            }
        }else {
            Toast toast = Toast.makeText(this, "You don't have a camera", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        try {
            if (resultCode == RESULT_OK) {
                File file = new File(currentPhotoPath);
                Bitmap bitmap = MediaStore.Images.Media
                        .getBitmap(this.getContentResolver(), Uri.fromFile(file));
                if (bitmap != null) {
                    //takenImage.setImageBitmap(bitmap);
                    bitmapToJpeg(rotateImage(bitmap));
                    File fl = new File(getFilesDir(), JPG_IMAGE_NAME);
                    takenImage.setImageBitmap(BitmapFactory.decodeFile(fl.getPath()));


                    takenImage.setVisibility(View.VISIBLE);
                    tutorialText.setVisibility(View.GONE);

                    //plantIdTime(bitmap);
                    String url = "https://api.plant.id/v2/identify";
                    postVolleyRequest(url);
                }else {
                    Log.d(ID_PLANT_LOG_TAG, "Unable to display image");
                }
            }

        } catch (Exception error) {
            error.printStackTrace();
        }
    }

    protected void postVolleyRequest(String url) {
        String apiKey = "VemQ60qL6Uamrezf42iVCDKRl3VLTGAUApbLVmlkP3vFwoFkez";


        JSONObject data = new JSONObject();

        try {
            String[] flowers = new String[]{JPG_IMAGE_NAME};

            data.put("api_key", apiKey);

            Log.d(ID_PLANT_LOG_TAG, "adding images");
            // add images
            JSONArray images = new JSONArray();
            for (String filename : flowers) {

                Log.d(ID_PLANT_LOG_TAG, "in loop");

                //String fileData = base64EncodeFromFile(filename);
                File file = new File(getFilesDir(), filename);
                byte[] buffer = new byte[(int) file.length() + 100];
                FileInputStream inputStream = new FileInputStream(file);
                int length = inputStream.read(buffer);
                //int length = new FileInputStream(file).read(buffer);
                String fileData = Base64.encodeToString(buffer, 0, length, Base64.DEFAULT);


                images.put(fileData);

                inputStream.close();
            }
            Log.d(ID_PLANT_LOG_TAG, "before put images");
            data.put("images", images);

            Log.d(ID_PLANT_LOG_TAG, "adding modifiers");
            // add modifiers
            JSONArray modifiers = new JSONArray()
                    .put("crops_fast")
                    .put("similar_images");
            data.put("modifiers", modifiers);

            // add language
            data.put("plant_language", "en");

            // add details
            JSONArray plantDetails = new JSONArray()
                    .put("common_names")
                    .put("url")
                    .put("name_authority")
                    .put("wiki_description")
                    .put("taxonomy")
                    .put("synonyms");
            data.put("plant_details", plantDetails);

            Log.d(ID_PLANT_LOG_TAG, "before send post");


        }catch(Exception e) {
            Log.d(ID_PLANT_LOG_TAG, "error doin the extra");
        }

        Log.d(ID_PLANT_LOG_TAG, "connection open");
        /*
        con.setDoOutput(true);
        con.setDoInput(true);
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");

         */

        VolleyLog.DEBUG = true;
        /*

        JsonObjectRequest plantRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                data,
                responseListener,
                errorListener);
        plantRequest.setTag(this); // mark this request, might have to cancel it in onStop
        mRequestQueue.add(plantRequest); // Volley processes the request on a worker thread

         */
    }

    // executed on main thread
    private Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject responseObj) {
            Log.d(ID_PLANT_LOG_TAG, "got response");

            try {
                Log.d(ID_PLANT_LOG_TAG, "onRepsonse: " + responseObj);

                new plantParser().execute(responseObj);



                // cancel pending requests
                mRequestQueue.cancelAll(this);
            } catch (Exception e) {
                Log.d(ID_PLANT_LOG_TAG, "Unable to parse data");
                Log.i("error while parsing", e.toString());
            }
        }
    };
    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.d(ID_PLANT_LOG_TAG, "Volley error");
            Log.i("Volley error", error.toString());
        }
    };









    private boolean bitmapToJpeg(Bitmap bitmap) {
        Log.d(ID_PLANT_LOG_TAG, "in bitmap to jpg");

        if (bitmap != null) {
            OutputStream outputStream;
            try {
                //File file = new File(JPG_IMAGE_NAME);
                File file = new File(getFilesDir(), JPG_IMAGE_NAME);
                file.createNewFile(); // if file already exists will do nothing
                //FileOutputStream fileOut = new FileOutputStream(file);
                FileOutputStream fileOut = new FileOutputStream(file, false);



                outputStream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream);
                bitmap.recycle();

                Log.d(ID_PLANT_LOG_TAG, "jpg convert done");
                return true;

            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    private Bitmap rotateImage(Bitmap bitmap) {
        ExifInterface exifInterface = null;
        try {
            exifInterface = new ExifInterface(currentPhotoPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
        Matrix matrix = new Matrix();
        switch(orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            default:
        }
        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return rotatedBitmap;
    }


}