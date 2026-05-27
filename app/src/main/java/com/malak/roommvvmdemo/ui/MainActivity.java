package com.malak.roommvvmdemo.ui;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.malak.roommvvmdemo.R;
import com.malak.roommvvmdemo.data.local.MemoCard;
import com.malak.roommvvmdemo.viewmodel.MemoBoardViewModel;

public class MainActivity extends AppCompatActivity {

    private EditText inputHeadline;
    private EditText inputDetails;
    private TextView textEmptyState;
    private MemoBoardViewModel memoBoardViewModel;
    private MemoCardAdapter memoCardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputHeadline = findViewById(R.id.inputHeadline);
        inputDetails = findViewById(R.id.inputDetails);
        textEmptyState = findViewById(R.id.textEmptyState);

        Button buttonSaveMemo = findViewById(R.id.buttonSaveMemo);
        Button buttonClearBoard = findViewById(R.id.buttonClearBoard);

        RecyclerView memoRecycler = findViewById(R.id.memoRecycler);
        memoRecycler.setLayoutManager(new LinearLayoutManager(this));

        memoCardAdapter = new MemoCardAdapter();
        memoRecycler.setAdapter(memoCardAdapter);

        memoBoardViewModel = new ViewModelProvider(this).get(MemoBoardViewModel.class);

        memoBoardViewModel.getMemoList().observe(this, memoCards -> {
            memoCardAdapter.submitMemos(memoCards);

            if (memoCards == null || memoCards.isEmpty()) {
                textEmptyState.setVisibility(View.VISIBLE);
                memoRecycler.setVisibility(View.GONE);
            } else {
                textEmptyState.setVisibility(View.GONE);
                memoRecycler.setVisibility(View.VISIBLE);
            }
        });

        buttonSaveMemo.setOnClickListener(v -> createMemoFromInputs());

        buttonClearBoard.setOnClickListener(v -> {
            memoBoardViewModel.clearBoard();
            Toast.makeText(this, "Toutes les notes ont été supprimées", Toast.LENGTH_SHORT).show();
        });

        memoCardAdapter.setOnMemoActionListener(new MemoCardAdapter.OnMemoActionListener() {
            @Override
            public void onMemoClicked(MemoCard memoCard) {
                Toast.makeText(
                        MainActivity.this,
                        "Note sélectionnée : " + memoCard.getHeadline(),
                        Toast.LENGTH_SHORT
                ).show();
            }

            @Override
            public void onMemoLongPressed(MemoCard memoCard) {
                memoBoardViewModel.removeMemo(memoCard);
                Toast.makeText(MainActivity.this, "Note supprimée", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createMemoFromInputs() {
        String headline = inputHeadline.getText().toString().trim();
        String details = inputDetails.getText().toString().trim();

        if (headline.isEmpty()) {
            inputHeadline.setError("Le titre est obligatoire");
            return;
        }

        if (details.isEmpty()) {
            inputDetails.setError("La description est obligatoire");
            return;
        }

        MemoCard memoCard = new MemoCard(
                headline,
                details,
                System.currentTimeMillis()
        );

        memoBoardViewModel.addMemo(memoCard);

        inputHeadline.setText("");
        inputDetails.setText("");

        Toast.makeText(this, "Note ajoutée avec succès", Toast.LENGTH_SHORT).show();
    }
}