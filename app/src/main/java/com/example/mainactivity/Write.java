package com.example.mainactivity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;


public class Write extends AppCompatActivity {

    SharedPreferences sharedPreferences;
        private String content_id,image_path,imgName;
        private EditText title, context, picture;
        private Button btn_send,btn_upload;
        private static final int PICK_FROM_ALBUM = 1;
        private File tempFile;
        private boolean image_status;
    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_write);
            NetworkUtil.setNetworkPolicy();
            checkSelfPermission();
            tedPermission();
            sharedPreferences = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
            title = (EditText)findViewById(R.id.editText);
            context = (EditText)findViewById(R.id.editText2);
            content_id=((Travel_info)Travel_info.mContext).publicMethod();
            final String id=sharedPreferences.getString(getResources().getString(R.string.IdState), "");
            btn_send = (Button)findViewById(R.id.btn_send);
            btn_upload = (Button)findViewById(R.id.btn_upload);

            image_status=false;
            btn_send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
              if (image_status== true) {
                  doFileUpload();
              }
                    try {
                        PHPRequest request = new PHPRequest("http://ahtj1234.dothome.co.kr/insert.php");
                        String result = request.PhPtest(String.valueOf(title.getText()),String.valueOf(context.getText()),imgName,content_id,id);
                        if(result.equals("1")){
                            Toast.makeText(getApplication(),"글 작성이 완료되었습니다",Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else{
                            Toast.makeText(getApplication(),"글 작성이 실패하였습니다.",Toast.LENGTH_SHORT).show();
                        }
                    }catch (MalformedURLException e){
                        e.printStackTrace();
                    }

                }
            });

        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToAlbum();

            }
        });
        }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //권한을 허용 했을 경우
        if(requestCode == 1){
            int length = permissions.length;
            for (int i = 0; i < length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    // 동의
                    Log.d("Write","권한 허용 : " + permissions[i]);
                }
            }
        }


    }
    public void checkSelfPermission() {

        String temp = "";

        //파일 읽기 권한 확인
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            temp += Manifest.permission.READ_EXTERNAL_STORAGE + " ";
        }

        //파일 쓰기 권한 확인
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            temp += Manifest.permission.WRITE_EXTERNAL_STORAGE + " ";
        }


        if (TextUtils.isEmpty(temp) == false) {
            // 권한 요청
            ActivityCompat.requestPermissions(this, temp.trim().split(" "),1);
        }else {
            // 모두 허용 상태
          //  Toast.makeText(this, "권한이 모두 허용", Toast.LENGTH_SHORT).show();
        }
    }
    private void goToAlbum() {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }
    private void tedPermission() {

        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                // 권한 요청 성공

            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                // 권한 요청 실패
            }
        };

        TedPermission.with(this)
                .setPermissionListener(permissionListener)
                //  .setRationaleMessage(getResources().getString(R.string.permission_2))
            //   .setDeniedMessage(getResources().getString(R.string.permission_1))
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();

    }
    private void doFileUpload() {

            HttpURLConnection conn = null;
        DataOutputStream dos = null;
        DataInputStream inStream = null;
        String existingFileName =image_path;//"/storage/emulated/0/DCIM/Camera/20200531_231754.jpg";// Environment.getExternalStorageDirectory().getAbsolutePath() + "/tmp_1436920364055.jpg";
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1* 1440 * 1440;
        String responseFromServer = "";
        String urlString = "http://ahtj1234.dothome.co.kr/image.php";

        try {
            System.out.println("errorpoint:1");
            //------------------ CLIENT REQUEST
            FileInputStream fileInputStream = new FileInputStream(new File(existingFileName)); //error point **
            System.out.println("errorpoint:2");
            // open a URL connection to the Servlet
            URL url = new URL(urlString);
            System.out.println("errorpoint:3");
            // Open a HTTP connection to the URL
            conn = (HttpURLConnection) url.openConnection();
            System.out.println("errorpoint:4");
            // Allow Inputs
            conn.setDoInput(true);
            // Allow Outputs
            conn.setDoOutput(true);
            // Don't use a cached copy.
            conn.setUseCaches(false);
            // Use a post method.
            System.out.println("errorpoint:5");
            conn.setRequestMethod("POST");
            System.out.println("errorpoint:6");
            conn.setRequestProperty("Connection", "Keep-Alive");
            System.out.println("errorpoint:7");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            System.out.println("errorpoint:8");


            dos = new DataOutputStream(conn.getOutputStream());
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + existingFileName + "\"" + lineEnd);
            dos.writeBytes(lineEnd);
            // create a buffer of maximum size
            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];
            // read file and write it into form...
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0) {

                dos.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            }

            // send multipart form data necesssary after file data...
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
            // close streams
            Log.e("Debug", "File is written");
            fileInputStream.close();
            dos.flush();
            dos.close();

        } catch (MalformedURLException ex) {
            Log.e("Debug", "error: " + ex.getMessage(), ex);
        } catch (IOException ioe) {
            Log.e("Debug", "error: " + ioe.getMessage(), ioe);
        }

        //------------------ read the SERVER RESPONSE
        try {

            inStream = new DataInputStream(conn.getInputStream());
            String str;

            while ((str = inStream.readLine()) != null) {

                Log.e("Debug", "Server Response " + str);

            }

            inStream.close();

        } catch (IOException ioex) {
            Log.e("Debug", "error: " + ioex.getMessage(), ioex);
        }

}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FROM_ALBUM) {
            if (data != null) {
                Uri photoUri = data.getData();

                Cursor cursor = null;

                try {

                    /*
                     *  Uri 스키마를
                     *  content:/// 에서 file:/// 로  변경한다.
                     */

                    String[] proj = {MediaStore.Images.Media.DATA};

                    assert photoUri != null;
                    cursor = getContentResolver().query(photoUri, proj, null, null, null);

                    assert cursor != null;
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

                    cursor.moveToFirst();

                    tempFile = new File(cursor.getString(column_index));

                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
                image_path = getRealPathFromURI(photoUri);
                setImage();

            }
        }
        else {

        }
    }
    private void setImage() {
        image_status=true;
        ImageView imageView = findViewById(R.id.photo1);

        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap originalBm = BitmapFactory.decodeFile(tempFile.getAbsolutePath(), options);

        imageView.setImageBitmap(originalBm);

    }
    private String getRealPathFromURI(Uri contentURI) {



        String result;

        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);



        if (cursor == null) { // Source is Dropbox or other similar local file path

            result = contentURI.getPath();



        } else {

            cursor.moveToFirst();

            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);

            result = cursor.getString(idx);
            imgName = result.substring(result.lastIndexOf("/")+1);

            cursor.close();

        }



        return result;

    }
}




