package com.example.tarotdairy;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class My extends AppCompatActivity {

    private final int PICK_IMAGE = 1111;
    private final int REQUEST_IMAGE_CAPTURE = 111;
    private int STORAGE_PERMISSION_CODE = 1;

    private long backKeyPressedTime = 0;
    private Toast toast;

    public static final int IMAGE_CODE = 1;

    ImageView btn1;
    ImageView btn2;
    ImageView btn3;
    ImageView btn4;
    TextView btnlogin;
    Button option;
    Button reminder;
    Button email;
    Button logout;
    Button activity;
    Button tarotcards;
    CircleImageView circle;
    TextView nick;

    LinearLayout profilecard;
    LinearLayout logincard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("MY");
        setContentView(R.layout.my);

        btn1 = findViewById(R.id.bottom_home);
        btn2 = findViewById(R.id.bottom_diary);
        btn3 = findViewById(R.id.bottom_qna);
        btn4 = findViewById(R.id.bottom_setting);

        tarotcards = findViewById(R.id.my_tarotcards);

        reminder = findViewById(R.id.my_reminder);
        btnlogin = findViewById(R.id.my_login);
        option = findViewById(R.id.my_option);
        email = findViewById(R.id.inquiry);

        logout = findViewById(R.id.my_logout);
        activity = findViewById(R.id.my_activity);

        circle = findViewById(R.id.my_circle);
        nick = findViewById(R.id.my_nick);

        profilecard = findViewById(R.id.my_profilecard);
        logincard = findViewById(R.id.my_logincard);




        if(SaveSharedPreference.getLoggedStatus(getApplicationContext())){
                    logincard.setVisibility(View.GONE);
        }else{
                    profilecard.setVisibility(View.GONE);
        }



        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(My.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(My.this, HistoryDiary.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(My.this, HistoryQna.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(My.this, "?????? ???????????????.", Toast.LENGTH_SHORT).show();
            }
        });

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(My.this, Login.class);
                startActivity(intent);
            }
        });

        option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(My.this, MySettings.class);
                startActivity(intent);
            }
        });

        reminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(My.this, Remind.class);
                startActivity(intent);
            }
        });

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(My.this, Inquiry.class);
                startActivity(intent);
            }
        });

        activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        nick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText editText = new EditText(My.this);

                AlertDialog.Builder dlg = new AlertDialog.Builder(My.this);
                dlg.setTitle("????????? ??????");
                dlg.setView(editText);
                dlg.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        nick.setText(editText.getText().toString());
                    }
                });
                dlg.show();
            }
        });

        circle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateListDialog();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                logoutDialog();

            }
        });

        tarotcards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(My.this, TarotCards78.class);
                startActivity(intent);
            }
        });
    }

    public void CreateListDialog() {
        final List<String> ListItems = new ArrayList<>();
        ListItems.add("?????????");
        ListItems.add("?????????");

        final String[] items = ListItems.toArray(new String[ListItems.size()]);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("");
        builder.setItems(items, (dialog, pos) -> {
            String selectedText = items[pos];
            if (selectedText.equals("?????????")) {
                // "????????? ??????????????? ??????-1???" ?????? ??? ????????? ????????? ???????????? ?????????.
                if (ContextCompat.checkSelfPermission(My.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    fromCamera();
                } else {
                    requestCameraPermission();
                }

            } else if (selectedText.equals("?????????")) {
                if (ContextCompat.checkSelfPermission(My.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                    fromGallery();

                } else {
                    requestStoragePermission();
                }
            }
        });
        builder.show();
    }

    private void fromCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }


    private void fromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        startActivityForResult(intent, PICK_IMAGE);
    }

    private void logoutDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(My.this);
        builder.setTitle("??????");
        builder.setMessage("???????????????????????????????");
        builder.setIcon(android.R.drawable.ic_menu_info_details);

        builder.setPositiveButton("???", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(My.this, "???????????? ??????", Toast.LENGTH_SHORT).show();

                //????????????
                FirebaseAuth.getInstance().signOut();

                //????????? ?????? false??? ???????????????
                SaveSharedPreference.setLoggedIn(getApplicationContext(), false);

                overridePendingTransition(0, 0);

                finish();
                startActivity(new Intent(My.this, My.class));
            }
        });

        builder.setNegativeButton("?????????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(My.this, "???????????? ??????", Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

            new AlertDialog.Builder(this)
                    .setTitle("?????? ??????")
                    .setMessage("TarotDiary??? ?????? ????????? ????????????????????????? ?????? ??????, ?????????, ?????? ?????????")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(My.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();

        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }


    private void requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("TarotDiary??? ?????? ????????? ????????????????????????? ?????? ??? ????????? ??????")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(My.this, new String[]{Manifest.permission.CAMERA}, STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();

        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "?????? ?????? ??????", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "?????? ??????", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(this, "\'??????\' ????????? ?????? ??? ???????????? ???????????????.", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            finish();
            toast.cancel();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainset, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                Intent intent = new Intent(My.this, MySettings.class);
                startActivity(intent);
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
//            if (data == null) {
//                return;
//            }
//            else {
//
//            }
//            Uri selectedImage = data.getData();
//        }
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
            if (resultCode == RESULT_OK) {
                try {
                    InputStream in = getContentResolver().openInputStream(data.getData());

                    Bitmap img = BitmapFactory.decodeStream(in);
                    in.close();

                    circle.setImageBitmap(img);
                } catch (Exception e) {

                }
            }

        }
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            try {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                ((CircleImageView) findViewById(R.id.my_circle)).setImageBitmap(imageBitmap);

            } catch (Exception e) {

            }
        } else if (resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "?????? ?????? ??????", Toast.LENGTH_LONG).show();
        }
    }


}
