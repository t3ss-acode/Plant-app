package com.example.plant_app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.plant_app.adapters.PlantIdInfoAdapter;
import com.example.plant_app.model.PlantParser;
import com.example.plant_app.util.MsgUtil;

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
    private String URL = "https://api.plant.id/v2/identify";

    private String photoPath;

    private TextView tutorialText;
    private TextView loadingText;
    private RecyclerView recyclerView;
    private PlantIdInfoAdapter mPlantInfoAdapter;

    // Volley
    private RequestQueue mRequestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_id_plant);

        tutorialText = (TextView) findViewById(R.id.tutorialTextView);
        loadingText = (TextView) findViewById(R.id.loading_text);

        // Set up the recyclerView
        recyclerView = findViewById(R.id.plant_info_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mPlantInfoAdapter = new PlantIdInfoAdapter();
        recyclerView.setAdapter(mPlantInfoAdapter);

        mRequestQueue = Volley.newRequestQueue(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mPlantInfoAdapter.notifyDataSetChanged();
        if(mPlantInfoAdapter.getItemCount() != 0)
            tutorialText.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //volley stop pending request. Ta bort resten i kön
        mRequestQueue.cancelAll(this);
    }



    public void identifyPlant(View view) {
        dispatchTakePictureIntent();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            try {
                // Create the File where the photo should go
                File photoFile = createImageFile();

                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.plant_app.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

            } catch (IOException ex) {
                Log.d(ID_PLANT_LOG_TAG, "Unable to create file for image");
                MsgUtil.toast(this, "Unable to save image");
            }
        }else {
            MsgUtil.toast(this, "You don't have a camera");
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        // Save a file: path for use with ACTION_VIEW intents
        photoPath = imageFile.getAbsolutePath();
        return imageFile;
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK) {
            File file = new File(photoPath);
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.fromFile(file));
                if (bitmap != null) {
                    bitmapToJpeg(rotateImage(bitmap));
                    File fl = new File(getFilesDir(), JPG_IMAGE_NAME);

                    tutorialText.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.INVISIBLE);
                    loadingText.setVisibility(View.VISIBLE);

                    postVolleyRequest();
                }else {
                    Log.d(ID_PLANT_LOG_TAG, "Unable to display image");
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(ID_PLANT_LOG_TAG, "Unable to get or display image");
            }
        }
    }

    private boolean bitmapToJpeg(Bitmap bitmap) {
        if (bitmap != null) {
            OutputStream outputStream;
            File file = new File(getFilesDir(), JPG_IMAGE_NAME);
            try {
                file.createNewFile(); // if file already exists will do nothing

                // False so it overwrites and doesn't append
                outputStream = new FileOutputStream(file, false);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream);
                bitmap.recycle();
                outputStream.close();

                Log.d(ID_PLANT_LOG_TAG, "jpg convert done");
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(ID_PLANT_LOG_TAG, "jpg convertion failed");
                MsgUtil.toast(this, "Unable to handle image");
            }

        }
        return false;
    }

    private Bitmap rotateImage(Bitmap bitmap) {
        ExifInterface exifInterface = null;
        try {
            exifInterface = new ExifInterface(photoPath);
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
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }



    protected void postVolleyRequest() {
        String apiKey = "VemQ60qL6Uamrezf42iVCDKRl3VLTGAUApbLVmlkP3vFwoFkez";

        JSONObject data = new JSONObject();
        try {
            data.put("api_key", apiKey);

            String[] flowers = new String[]{JPG_IMAGE_NAME};
            // add images
            JSONArray images = new JSONArray();
            for (String filename : flowers) {
                File file = new File(getFilesDir(), filename);
                byte[] buffer = new byte[(int) file.length() + 100];
                FileInputStream inputStream = new FileInputStream(file);
                int length = inputStream.read(buffer);
                String fileData = Base64.encodeToString(buffer, 0, length, Base64.DEFAULT);

                images.put(fileData);
                inputStream.close();
            }
            data.put("images", images);

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
        }catch(Exception e) {
            Log.d(ID_PLANT_LOG_TAG, "error doin the extra");
        }

        VolleyLog.DEBUG = true;

        JsonObjectRequest plantRequest = new JsonObjectRequest(
                Request.Method.POST,
                URL,
                data,
                responseListener,
                errorListener);
        plantRequest.setTag(this); // mark this request, might have to cancel it in onStop

        mRequestQueue.add(plantRequest); // Volley processes the request on a worker thread
    }

    // executed on main thread
    private Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject responseObj) {
            Log.d(ID_PLANT_LOG_TAG, "got response");

            try {
                Log.d(ID_PLANT_LOG_TAG, "onRepsonse: " + responseObj);

                new PlantParser(mPlantInfoAdapter, loadingText, recyclerView).execute(responseObj);

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
}