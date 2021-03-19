package com.example.plant_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class IdPlantActivity extends AppCompatActivity {

    private static final String ID_PLANT_LOG_TAG = "log_idplant";
    static final int REQUEST_IMAGE_CAPTURE = 1;

    private String currentPhotoPath;

    private ImageView takenImage;
    private TextView tutorialText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_id_plant);

        takenImage = (ImageView) findViewById(R.id.takenImageView);
        tutorialText = (TextView) findViewById(R.id.tutorialTextView);
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
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
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
                    takenImage.setImageBitmap(bitmap);
                    takenImage.setVisibility(View.VISIBLE);
                    tutorialText.setVisibility(View.GONE);
                }else {
                    Log.d(ID_PLANT_LOG_TAG, "Unable to display image");
                }
            }

        } catch (Exception error) {
            error.printStackTrace();
        }
    }
}