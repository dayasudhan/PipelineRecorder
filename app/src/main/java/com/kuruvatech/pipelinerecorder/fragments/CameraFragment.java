package com.kuruvatech.pipelinerecorder.fragments;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.kuruvatech.pipelinerecorder.MainActivity;
import com.kuruvatech.pipelinerecorder.R;
import com.kuruvatech.pipelinerecorder.utils.Constants;

import static android.app.Activity.RESULT_OK;

public class CameraFragment extends Fragment {
    Button btpic, btnup;
    private Uri fileUri;
    String picturePath;
    Uri selectedImage;
    ImageView imageView;
    Bitmap photo;
    String ba1;
    public static String URL = "Paste your URL here";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.image_layout, container, false);
        ((MainActivity) getActivity())
                .setActionBarTitle("Take Picture");
        imageView = (ImageView) view.findViewById(R.id.Imageprev);
        btpic = (Button) view.findViewById(R.id.cpic);
        btpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickpic();
            }
        });

        btnup = (Button) view.findViewById(R.id.up);
        btnup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //upload();
            }
        });
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
    }
    private void clickpic() {
        // Check Camera
        if (getContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // Open default camera
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

            // start the image capture Intent
            startActivityForResult(intent, 100);

        } else {
            Toast.makeText(getContext(), "Camera not supported", Toast.LENGTH_LONG).show();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 && resultCode == RESULT_OK) {
            Toast.makeText(getContext(), "Camera onActivityResult", Toast.LENGTH_LONG).show();
            selectedImage = data.getData();
            photo = (Bitmap) data.getExtras().get("data");

            // Cursor to get image uri to display

            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();

            Bitmap photo = (Bitmap) data.getExtras().get("data");

            imageView.setImageBitmap(photo);
        }
    }
}
