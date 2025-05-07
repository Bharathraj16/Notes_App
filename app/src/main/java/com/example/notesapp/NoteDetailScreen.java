package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

public class NoteDetailScreen extends AppCompatActivity {
    EditText titleEditText,contentEditText;
    ImageButton saveNoteBtn;
    TextView pageTitleTextView;
    String title,content,docId;
    boolean isEditMode=false;
    TextView deleteNoteTextViewBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail_screen);

        titleEditText=findViewById(R.id.notes_title_text);
        contentEditText=findViewById(R.id.notes_content_text);
        saveNoteBtn=findViewById(R.id.save_note_btn);
        pageTitleTextView=findViewById(R.id.page_title);
        deleteNoteTextViewBtn=findViewById(R.id.delete_note_text_view_btn);

        saveNoteBtn.setOnClickListener((v)->saveNote());

        //Receive the data

        title=getIntent().getStringExtra("title");
        content=getIntent().getStringExtra("content");
        docId=getIntent().getStringExtra("docId");

        if(docId!=null && !docId.isEmpty()){
            isEditMode=true;
        }
        titleEditText.setText(title);
        contentEditText.setText(content);
        if (isEditMode){
            pageTitleTextView.setText("Edit your note");
            deleteNoteTextViewBtn.setVisibility(View.VISIBLE);
        }
        saveNoteBtn.setOnClickListener((v)->saveNote());
        deleteNoteTextViewBtn.setOnClickListener((v)->deleteNoteFromFirebase());
    }

    private void deleteNoteFromFirebase() {
        DocumentReference documentReference;
        documentReference=Utility.getCollectionReferenceForNotes().document(docId);
        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    // note delete from firebase
                    Utility.showText(NoteDetailScreen.this,"Note deleted successfully");
                    finish();
                }else {
                    //Note delete time error made
                    Utility.showText(NoteDetailScreen.this,"Failed while deleting note");
                }
            }
        });
    }

    void saveNote() {
        String noteTitle=titleEditText.getText().toString();
        String noteContent=contentEditText.getText().toString();
        if (noteTitle==null||noteTitle.isEmpty()){
            titleEditText.setError("Title is required");
            return;
        }
        Note note=new Note();
        note.setTitle(noteTitle);
        note.setContent(noteContent);
        note.setTimestamp(Timestamp.now());
        //notes saved to firebase
        saveNoteToFirebase(note);
    }

    void saveNoteToFirebase(Note note){
        DocumentReference documentReference;
        if (isEditMode){
            documentReference=Utility.getCollectionReferenceForNotes().document(docId);
        }else {
            documentReference=Utility.getCollectionReferenceForNotes().document();
        }


        documentReference.set(note).addOnCompleteListener(
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            //note is added
                            Utility.showText(NoteDetailScreen.this,"Note added successfully");
                            finish();
                        }else {
                            Utility.showText(NoteDetailScreen.this,"Failed while adding note");
                        }
                    }
                }
        );
    }
}