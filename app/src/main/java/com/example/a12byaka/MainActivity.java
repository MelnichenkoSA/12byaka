package com.example.a12byaka;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    Button buttonAdd, buttonDelete, buttonClear, buttonRead,
            buttonUpdate;
    EditText etName, etEmail, etID;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonAdd = (Button) findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(this);
        buttonRead = (Button) findViewById(R.id.buttonRead);
        buttonRead.setOnClickListener(this);
        buttonClear = (Button) findViewById(R.id.buttonClear);
        buttonClear.setOnClickListener(this);
        buttonUpdate = (Button) findViewById(R.id.buttonUpdate);
        buttonUpdate.setOnClickListener(this);
        buttonDelete = (Button) findViewById(R.id.buttonDelete);

        buttonDelete.setOnClickListener(this);
        etID = (EditText) findViewById(R.id.etID);
        etName = (EditText) findViewById(R.id.etName);
        dbHelper = new DBHelper(this);
    }
    @Override
    public void onClick(View v)
    {
        String ID = etID.getText().toString();
        String name = etName.getText().toString();
        String email = etEmail.getText().toString();
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        if(v.getId() == R.id.buttonAdd )
        {
            contentValues.put(DBHelper.KEY_NAME, name);
            contentValues.put(DBHelper.KEY_MAIL, email);
            database.insert(DBHelper.TABLE_PERSONS, null,
                    contentValues);
        }
        else if(v.getId() == R.id.buttonRead) {
            Cursor cursor = database.query(DBHelper.TABLE_PERSONS, null,
                    null, null,
                    null, null, null);
            if (cursor.moveToFirst()) {
                int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
                int nameIndex =
                        cursor.getColumnIndex(DBHelper.KEY_NAME);
                int emailIndex =
                        cursor.getColumnIndex(DBHelper.KEY_MAIL);
                do {
                    Log.d("mLog", "ID =" + cursor.getInt(idIndex) +
                            ", name = " + cursor.getString(nameIndex) + ", email = " +
                            cursor.getString(emailIndex));
                } while (cursor.moveToNext());
            } else
                Log.d("mLog", "0 rows");
            cursor.close(); // освобождение памяти
        }
        else if(v.getId() == R.id.buttonClear)
        {
            database.delete(DBHelper.TABLE_PERSONS, null, null);
        }
        else if(v.getId() == R.id.buttonDelete)
        {
            if (ID.equalsIgnoreCase("")) {
                return;
            }
            int delCount = database.delete(DBHelper.TABLE_PERSONS,
                    DBHelper.KEY_ID + "= " + ID, null);
            Log.d("mLog", "Удалено строк = " + delCount);
        }
        else if(v.getId() == R.id.buttonUpdate)
        {
            if (ID.equalsIgnoreCase(""))
            {
                return;
            }
            contentValues.put(DBHelper.KEY_MAIL, email);
            contentValues.put(DBHelper.KEY_NAME, name);
            int updCount = database.update(DBHelper.TABLE_PERSONS,
                    contentValues, DBHelper.KEY_ID + "= ?", new String[] {ID});
            Log.d("mLog", "Обновлено строк = " + updCount);
        }
        dbHelper.close(); // закрываем соединение с БД
    }
}
