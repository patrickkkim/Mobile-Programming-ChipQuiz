package com.koreatech.chipquiz.chipquiz;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.koreatech.chipquiz.chipquiz.QuizAddActivity.QuizMetaData;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class MainActivity extends BaseActivity {

    // 카테고리, 정렬 스피너
    private Spinner spinner_category;
    private Spinner spinner_orderby;
    Intent intent;
    TextView Player;
    private String temp="";
    private String orderby="";
    private final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String uid = user !=null?user.getUid():null;
    DatabaseReference databaseReference = firebaseDatabase.getReference();
    DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference();

    //sdfgsdf
    List <QuizMetaData> quizList = new ArrayList<QuizMetaData>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 앱바 이름 바꾸기
        ActionBar bar = getSupportActionBar();
        bar.setTitle("퀴즈 홈");

        Spinner categorySpinner = (Spinner) findViewById(R.id.spinner_category);
        Spinner orderbySpinner = (Spinner) findViewById(R.id.spinner_orderby);
        ArrayAdapter<CharSequence> categoryAdapter =
                ArrayAdapter.createFromResource(this, R.array.category_array, android.R.layout.simple_spinner_item);

        ArrayAdapter<CharSequence> orderbyAdapter =
                ArrayAdapter.createFromResource(this, R.array.orderBy, android.R.layout.simple_spinner_item);

        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        orderbyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);
        orderbySpinner.setAdapter(orderbyAdapter);
        //getRanking();
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String[] categoryArray = getResources().getStringArray(R.array.category_array);

                ValueEventListener valueEventListener= new ValueEventListener() {


                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        clearQuizForm(false);

                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            QuizMetaData quizInfo=ds.getValue(QuizMetaData.class);
                            quizList.add(quizInfo);

                        }
                        if(orderby.equals("추천순")){
                            Collections.sort(quizList, new SortbyLikes());
                        }
                        else{
                            Collections.sort(quizList, new SortbyDate());
                        }
                        for ( QuizMetaData quizInfo : quizList) {

                            String type = quizInfo.MC ? "4지선다 퀴즈" : (quizInfo.SA ? "단답형 퀴즈" : "OX 퀴즈");

                            addListForm(quizInfo.name, type, quizInfo.likes);

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                };

                if (categoryArray[i].equals("전체")){
                    databaseReference.child("Quizs").addListenerForSingleValueEvent(valueEventListener);
                    temp=categoryArray[i];
                }
                else{
                    databaseReference.child("Quizs").orderByChild("category").equalTo(categoryArray[i]).addListenerForSingleValueEvent(valueEventListener);
                    temp=categoryArray[i];
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                adapterView.getItemAtPosition(0);
                orderbySpinner.setSelection(0);
            }

        });

        orderbySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String[] orderbyArray = getResources().getStringArray(R.array.orderBy);
                orderby=orderbyArray[i];
                if(orderby.equals("추천순")) {

                    Collections.sort(quizList, new SortbyLikes());
                }
                else {
                    Toast.makeText(MainActivity.this, orderby, Toast.LENGTH_SHORT).show();
                    Collections.sort(quizList, new SortbyDate());
                }

                clearQuizForm(true);
                for ( QuizMetaData quizInfo : quizList) {

                    String type = quizInfo.MC ? "4지선다 퀴즈" : (quizInfo.SA ? "단답형 퀴즈" : "OX 퀴즈");

                    addListForm(quizInfo.name, type, quizInfo.likes);

                }








            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                adapterView.getItemAtPosition(0);
            }

        });


    }

    // 퀴즈 이동 버튼
    public void onClick(View view) {
        // 클릭 이벤트 발생 id 갖고오기
        int viewId = view.getId();

        if( viewId== R.id.QuizButton){
            String name = view.getTag(R.id.quizName).toString();
            String type = view.getTag(R.id.quizType).toString();
            intent = new Intent(this, SolveQuizActivity.class);
            intent.putExtra("QuizName", name);
            intent.putExtra("QuizType", type);
            startActivity(intent);
        }
        // 인텐트 선언
        //intent = new Intent(MainActivity.this, SolveQuizActivity.class);

    }




    public void getRanking() {

        DatabaseReference reference = mDatabase.child("Users");
        // 사용하고자 하는 데이터를 reference가 가리킴
        // 여기선 rank 데이터 셋에 접근
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String nickname = snapshot.getValue(String.class);
                // Log.w("FireBaseData","getData" +nickname);
                Player.setText(nickname);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //Log.w("FireBaseData","loadname:onCancelled"+Player.getText());
            }

        });
    }

    private void addListForm(String name, String type, int number) {
        LinearLayout dynamicLayout = findViewById(R.id.quiz_solve_layout);

        View formView = getLayoutInflater().inflate(
                R.layout.quiz_solve_list, dynamicLayout, false
        );

        TextView Number = formView.findViewById(R.id.QuizLikes);
        Number.setText(String.valueOf(number));

        TextView title = formView.findViewById(R.id.QuizName);
        title.setText(name);

        TextView Type = formView.findViewById(R.id.QuizType);
        Type.setText(type);

        Button editBtn = formView.findViewById(R.id.QuizButton);
        editBtn.setTag(R.id.quizName, name);
        editBtn.setTag(R.id.quizType, type);

        dynamicLayout.addView(formView);
    }


    class SortbyLikes implements Comparator<QuizMetaData>{


            public int compare(QuizMetaData a, QuizMetaData b)
            {
                return b.likes - a.likes;
            }

    }

    class SortbyDate implements Comparator<QuizMetaData>{
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");


        public int compare(QuizMetaData a, QuizMetaData b)
        {
            try {
                return formatter.parse(b.datetime).compareTo(formatter.parse(a.datetime));
            }catch(ParseException e){
                throw new IllegalArgumentException(e);
            }
        }

    }
    private void clearQuizForm(boolean isorderby) {
        if(!isorderby){ quizList.clear();}
        LinearLayout dynamicLayout = (LinearLayout) findViewById(R.id.quiz_solve_layout);
        dynamicLayout.removeAllViews();
    }

}