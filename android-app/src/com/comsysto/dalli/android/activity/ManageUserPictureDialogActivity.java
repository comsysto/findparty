package com.comsysto.dalli.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import com.comsysto.dalli.android.R;
import com.comsysto.findparty.User;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created with IntelliJ IDEA.
 * User: stefandjurasic
 * Date: 07.03.13
 * Time: 17:46
 * To change this template use File | Settings | File Templates.
 */
public class ManageUserPictureDialogActivity extends AbstractActivity {


    private Bitmap bitmap;

    private ImageView userPicture;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        User user = getPartyManagerApplication().getUser();

        setContentView(R.layout.manage_user_picture_dialog);

        Button deleteButton = (Button)findViewById(R.id.DELETE_PICTURE_BUTTON);
        Button manageButton = (Button)findViewById(R.id.SELECT_PICTURE_BUTTON);

        userPicture = (ImageView) findViewById(R.id.userPicture);


        if (user.getPicture() == null) {

            //hide delete button
        }
        else {
            //fill userPicture from user
        }

        manageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, 0);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == Activity.RESULT_OK)
            try {
                // We need to recyle unused bitmaps
                if (bitmap != null) {
                    bitmap.recycle();
                }
                InputStream stream = getContentResolver().openInputStream(
                        data.getData());
                bitmap = BitmapFactory.decodeStream(stream);
                stream.close();
                userPicture.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
