package com.example.patcareteam2;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class CommentFragment extends Fragment implements OnClickListener{
	
	EditText insertedComment;
	TextView tvComment;
	Button post,load;
	// no change other activitis can access 	
	public static String fileName="Comments";
	SharedPreferences pref;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment_comment, container, false);
      
    	return rootView;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		pref=getActivity().getSharedPreferences(fileName,0);
		tvComment=(TextView)view.findViewById(R.id.tvComment);
		insertedComment=(EditText)view.findViewById(R.id.etComment);
		post=(Button)view.findViewById(R.id.btnPOSTcoment);
		post.setOnClickListener(this);
		load=(Button)view.findViewById(R.id.btnLoad);
		load.setOnClickListener(this);
		
		
	}

	@Override
	public void onClick(View v) {
		insertedComment.setText("Before");
		switch(v.getId())
		{
		case R.id.btnPOSTcoment : 
			String stringData=insertedComment.getText().toString();
			SharedPreferences.Editor editor=pref.edit();
			editor.putString("comment",stringData);
			//finalize 
			editor.commit();
			break;
		case R.id.btnLoad :
			pref=getActivity().getSharedPreferences(fileName,0);
			String dataReturned=pref.getString("comment", "error");
			tvComment.setText(dataReturned);
			break;
		 default:
	            break;
		
		}
}
}