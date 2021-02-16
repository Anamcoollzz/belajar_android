package anamapp.pro.belajar;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import anamapp.pro.belajar.crud.DataActivity;
import anamapp.pro.belajar.helpers.CallbackHelper;
import anamapp.pro.belajar.helpers.PhotoHelper;
import anamapp.pro.belajar.maps.MapMenuActivity;
import anamapp.pro.belajar.recyclerview.KawalCoronaActivity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import id.zelory.compressor.Compressor;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BasicActivity extends AppCompatActivity {

    private ImageView imageView;
    private Activity activity;
    static final int REQUEST_TAKE_PHOTO = 1;
    String currentPhotoPath;
    File imageFile;
    private int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;

    //    initialization view
    private Button mapMenuButton, formButton, recyclerViewButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Here, thisActivity is the current activity
                if (ContextCompat.checkSelfPermission(activity,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

                    // Permission is not granted
                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                            Manifest.permission.ACCESS_COARSE_LOCATION)) {
                        ActivityCompat.requestPermissions(activity,
                                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.
                    } else {
                        // No explanation needed; request the permission
                        ActivityCompat.requestPermissions(activity,
                                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);

                        // MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }
                } else {
                    // Permission has already been granted
                    Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                    startActivity(intent);
                }
            }
        });

        Button tombolCrud = findViewById(R.id.tombolCrud);
        tombolCrud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DataActivity.class);
                startActivity(intent);
            }
        });
        Button nav = findViewById(R.id.navigationDrawer);
        nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), NavigationActivity.class);
                startActivity(intent);
            }
        });

        activity = this;
        Button takePictureBtn = findViewById(R.id.buka_kamera_btn);
        takePictureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                PhotoHelper photoHelper = new PhotoHelper(activity);
//                photoHelper.dispatchTakePictureIntent(new CallbackHelper() {
//                    @Override
//                    public void onTaked() {
//                        Toast.makeText(activity, "halo", Toast.LENGTH_LONG).show();
//                    }
//                });
                dispatchTakePictureIntent();
            }
        });

        imageView = findViewById(R.id.image_view);
        initView();
        initAction();
    }

    private void initView() {
        mapMenuButton = findViewById(R.id.map_menu_button);
        formButton = findViewById(R.id.form_button);
        recyclerViewButton = findViewById(R.id.recycler_view_button);
    }

    private void initAction() {
        mapMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, MapMenuActivity.class);
                startActivity(intent);
            }
        });
        formButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BasicActivity.this, FormActivity.class);
                startActivity(intent);
            }
        });
        recyclerViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BasicActivity.this, RecyclerViewActivity.class);
                startActivity(intent);
            }
        });
        triggerIntent();
    }

    private void triggerIntent(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart() {
//        Toast.makeText(this, "INI START", Toast.LENGTH_LONG).show();
        super.onStart();
    }

    @Override
    protected void onRestart() {
//        Toast.makeText(this, "INI RESTART", Toast.LENGTH_LONG).show();
        super.onRestart();
    }

    @Override
    protected void onStop() {
//        Toast.makeText(this, "INI STOP", Toast.LENGTH_LONG).show();
        super.onStop();
    }

    @Override
    protected void onResume() {
//        Toast.makeText(this, "INI RESUME", Toast.LENGTH_LONG).show();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
//        Toast.makeText(this, "INI DESTROY", Toast.LENGTH_LONG).show();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
//        Toast.makeText(this, "INI PAUSE", Toast.LENGTH_LONG).show();
        super.onPause();
    }

    public void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            galleryAddPic();
            setPic();
            resizeImage();
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        imageFile = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = imageFile.getAbsolutePath();
        return imageFile;
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private void setPic() {
        // Get the dimensions of the View
        int targetW = imageView.getWidth();
        int targetH = imageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
        imageView.setImageBitmap(bitmap);
    }

    private void resizeImage() {
        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        Bitmap bitmap = null;
        try {
            bitmap = PhotoHelper.rotateImage(currentPhotoPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int destinationWidth = 800;
        Bitmap resizedBitmap = PhotoHelper.resizeImage(bitmap, destinationWidth);
        PhotoHelper.saveImageToDir(resizedBitmap, dir, PhotoHelper.getFilename("resized"));
    }

}
