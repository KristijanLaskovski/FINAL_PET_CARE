package com.example.patcareteam2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CommentActivity extends Activity {

	EditText insertedcomment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment);
		insertedcomment=(EditText)findViewById(R.id.ETforEnteringCommnets);
		
	}

	public void makeThePost(View v){
		Intent i=new Intent();
		Bundle backpack=new Bundle();
		backpack.putString("answer", insertedcomment.getText().toString());
		i.putExtras(backpack);
		setResult(RESULT_OK, i);
		finish();
	}

}
