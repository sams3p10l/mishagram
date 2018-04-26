package com.example.specter.mishagram;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//TODO: Messages pull sender/receiver data from Contacts table

public class DatabaseHelper extends SQLiteOpenHelper
{
	public static final String DATABASE_NAME = "mishagramDB.db";
	public static final int DATABASE_VERSION = 1;

	public static final String TABLE_NAME_CONTACTS = "contact";
	public static final String COLUMN_ID_C = "contact_id";
	public static final String COLUMN_USERNAME_C = "username";
	public static final String COLUMN_FIRST_NAME_C = "firstname";
	public static final String COLUMN_LAST_NAME_C = "lastname";

	public static final String TABLE_NAME_MESSAGES = "message";
	public static final String COLUMN_ID_M = "message_id";
	public static final String COLUMN_SENDER_M = "sender_id";
	public static final String COLUMN_RECEIVER_M = "receiver_id";
	public static final String COLUMN_MESSAGE_M = "message";

	public DatabaseHelper(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase)
	{
		sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME_CONTACTS + " (" +
							COLUMN_ID_C + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_USERNAME_C + " TEXT, " +
							COLUMN_FIRST_NAME_C + " TEXT, " + COLUMN_LAST_NAME_C + " TEXT); " );

		sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME_MESSAGES + " (" +
							COLUMN_ID_M + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_SENDER_M + " TEXT, " +
							COLUMN_RECEIVER_M + " TEXT, " + COLUMN_MESSAGE_M + " TEXT); " );
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
	{

	}

	/*~~~~~~~~~~~~~~~~~~~~~~~CONTACT METHODS BLOCK~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

	public void insertContact(Contact contact)
	{
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();

		values.put(COLUMN_USERNAME_C, contact.getUsername());
		values.put(COLUMN_FIRST_NAME_C, contact.getFirstName());
		values.put(COLUMN_LAST_NAME_C, contact.getLastName());

		db.insert(TABLE_NAME_CONTACTS, null, values);
		db.close();
	}

	public Contact[] readContacts()
	{
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME_CONTACTS, null, null, null, null, null, null, null);

		if(cursor.getCount() <= 0)
			return null;

		Contact contacts[] = new Contact[cursor.getCount()];

		int i = 0;

		for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
			contacts[i++] = createContact(cursor);

		cursor.close();
		db.close();

		return contacts;
	}

	public Contact readContact(int searchID)
	{
		SQLiteDatabase db = getReadableDatabase();

		//TODO: Ako ne radi, id tip string
		Cursor cursor = db.query(TABLE_NAME_CONTACTS, null, COLUMN_ID_C + "=?", new String[] {Integer.toString(searchID)}, null, null, null);

		if (cursor.getCount() <= 0)
			return null;
		cursor.moveToFirst();

		Contact contact = createContact(cursor);

		cursor.close();
		db.close();

		return contact;
	}

	public Contact readContact(String searchUser)
	{
		SQLiteDatabase db = getReadableDatabase();

		//TODO: Ako ne radi, id tip string
		Cursor cursor = db.query(TABLE_NAME_CONTACTS, null, COLUMN_USERNAME_C + "=?", new String[] {searchUser}, null, null, null);

		if (cursor.getCount() <= 0)
			return null;
		cursor.moveToFirst();

		Contact contact = createContact(cursor);

		cursor.close();
		db.close();

		return contact;
	}

	private Contact createContact(Cursor cursor)
	{
		int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID_C));
		String username = cursor.getString(cursor.getColumnIndex(COLUMN_USERNAME_C));
		String firstName = cursor.getString(cursor.getColumnIndex(COLUMN_FIRST_NAME_C));
		String lastName = cursor.getString(cursor.getColumnIndex(COLUMN_LAST_NAME_C));

		return new Contact(id, username, firstName, lastName);
	}

	public void deleteContact(String searchCriteria)
	{
		SQLiteDatabase db = getWritableDatabase();
		db.delete(TABLE_NAME_CONTACTS, COLUMN_ID_C + "=?", new String[] {searchCriteria});
		db.close();
	}


	/*~~~~~~~~~~~~~~~~~~~~~~~MESSAGE METHODS BLOCK~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

	public void insertMessage(Message message)
	{
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();

		values.put(COLUMN_SENDER_M, message.getSender());
		values.put(COLUMN_RECEIVER_M, message.getReceiver());
		values.put(COLUMN_MESSAGE_M, message.getMsg());

		db.insert(TABLE_NAME_MESSAGES, null, values);
		db.close();
	}

	public Message readMessage(int id)
	{
		SQLiteDatabase db = getReadableDatabase();

		//TODO: Ako ne radi, id tip string
		Cursor cursor = db.query(TABLE_NAME_MESSAGES, null, COLUMN_ID_M + "=?", new String[] {Integer.toString(id)}, null, null, null);
		if (cursor.getCount() <= 0)
			return null;
		cursor.moveToFirst();

		Message message = createMessage(cursor);
		cursor.close();
		db.close();

		return message;
	}

	private Message createMessage(Cursor cursor)
	{
		int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID_M));
		String sender = cursor.getString(cursor.getColumnIndex(COLUMN_SENDER_M));
		String receiver = cursor.getString(cursor.getColumnIndex(COLUMN_RECEIVER_M));
		String msg = cursor.getString(cursor.getColumnIndex(COLUMN_MESSAGE_M));

		return new Message(id, sender, receiver, msg);
	}

	public void deleteMessage(int id)
	{
		SQLiteDatabase db = getWritableDatabase();
		db.delete(TABLE_NAME_MESSAGES, COLUMN_ID_M + "=?", new String[] {String.valueOf(id)});
		db.close();
	}

	public Message[] readMessages(int senderID, int receiverID)
	{
		SQLiteDatabase db = getReadableDatabase();

		//TODO: Obrni okreni
		Cursor cursor = db.query(TABLE_NAME_MESSAGES, null,  "(" + COLUMN_SENDER_M + "=? and " + COLUMN_RECEIVER_M + "=?) or " +
				"(" + COLUMN_SENDER_M + "=? and " + COLUMN_RECEIVER_M + "=?)", new String[] {Integer.toString(senderID), Integer.toString(receiverID),
				Integer.toString(receiverID), Integer.toString(senderID)}, null, null, null, null);

		if (cursor.getCount() <= 0)
			return null;

		Message messages[] = new Message[cursor.getCount()];
		int i = 0;

		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
			messages[i++] = createMessage(cursor);

		cursor.close();
		db.close();

		return messages;
	}
}
