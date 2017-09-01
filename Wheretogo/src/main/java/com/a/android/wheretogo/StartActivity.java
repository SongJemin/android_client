package com.a.android.wheretogo;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class StartActivity extends AppCompatActivity {

   // @Override
  //  protected  void attachBaseContext(Context newBase) {

        //super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
  //  }

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        ImageView image=new ImageView(this);
        image.setImageResource(R.drawable.mark2);

        TextView markView = (TextView) findViewById(R.id.markView);
        Button nextButton = (Button) findViewById(R.id.nextButton);
        TextView registerButton = (TextView) findViewById(R.id.registerButton);
        TextView nonmembersButton = (TextView) findViewById(R.id.nonmembersButton);

        Typeface typeFace = Typeface.createFromAsset(getAssets(), "fonts/NotoSansCJKkr-Bold.otf");  //asset > fonts 폴더 내 폰트파일 적용
        markView.setTypeface(typeFace);



        nextButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                Intent login2Intent = new Intent(StartActivity.this,LoginActivity.class);
                StartActivity.this.startActivity(login2Intent);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                //Snackbar snackbar;
                //Snackbar.make(view,"Hi! I’m Snackbar!",Snackbar.LENGTH_INDEFINITE).show();
                Intent registerIntent = new Intent(StartActivity.this,RegisterActivity.class);
                StartActivity.this.startActivity(registerIntent);
            }
        });

        nonmembersButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                Intent registerIntent = new Intent(StartActivity.this,MainActivity.class);
                StartActivity.this.startActivity(registerIntent);
            }
        });


    }
}
