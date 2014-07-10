package it.lma5.incorporesound;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class inCorporeSoundHelper extends SQLiteOpenHelper {

	private static final String DBNAME = "InCorporeSounds";
	private static final String TABLE_NAME_PLAYLIST = "playlists";
	private static final String TABLE_NAME_SONGS = "songs";
	private static final String COLNAME_PLAYLIST_NAME = "name";
	private static final String COLNAME_FADE_IN = "fade_in";
	private static final String COLNAME_RANDOM = "random";
	private static final String COLNAME_LOOP = "loop";
	private static final String COLNAME_ID_SONG = "id";
	private static final String COLNAME_SONG_TITLE = "title";
	private static final String COLNAME_ARTIST = "artist";
	private static final String COLNAME_BEGINNING = "beginning";
	private static final String COLNAME_DURATION= "duration";
	private static final String COLNAME_USER_DURATION= "user_duration";
	private static final String COLNAME_SONG_PLAYLIST= "playist_id";
	private Context context;
	private SQLiteDatabase db;

	public inCorporeSoundHelper(Context context) {
		super(context, "InCorporeSounds", null, 1);
		this.context = context;
		File dbFile = context.getDatabasePath(DBNAME);
		if (!dbFile.exists()) {
			copyDbFromAsset(DBNAME, dbFile);
			Log.v("IN CORPORE SOUNDS", "DB copied from asset");
		}

		db = SQLiteDatabase.openDatabase(dbFile.getPath(), null,
				SQLiteDatabase.OPEN_READWRITE);

		Log.v("IN CORPORE SOUNDS", "Open DB...DONE");

	}

	private void copyDbFromAsset(String fileNameAsset, File out) {
		InputStream myInput;
		OutputStream myOutput;

		try {
			myInput = context.getAssets().open(fileNameAsset);

			// getReadableDatabase() to create database folder
			this.getReadableDatabase();

			myOutput = new FileOutputStream(out);

			byte[] buffer = new byte[1024];
			int length;
			while ((length = myInput.read(buffer)) > 0) {
				myOutput.write(buffer, 0, length);
			}

			// Close the streams
			myOutput.flush();
			myOutput.close();
			myInput.close();

		} catch (IOException e) {
			Log.v("IN CORPORE SOUNDS", "IOException in copy");
			e.printStackTrace();
		}

	}

	@Override
	public void onCreate(SQLiteDatabase db) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	private List<Playlist> getAllPlaylist() {
		return null;
	}

	public void addPlaylist(Playlist playlist) {
		ContentValues values = new ContentValues();
		values.put(COLNAME_PLAYLIST_NAME, playlist.getName());
		values.put(COLNAME_LOOP, playlist.getRound());
		values.put(COLNAME_FADE_IN, playlist.getFadeIn());
		values.put(COLNAME_RANDOM, playlist.is_random());
		try {
			db.insert(TABLE_NAME_PLAYLIST, null, values);
			for (int i=0; i < playlist.getSongList().size(); i++) {
				ContentValues songValues = new ContentValues();
				Song temp;
				temp=playlist.getSongList().get(i);
				songValues.put(COLNAME_ID_SONG, "null");
				songValues.put(COLNAME_ARTIST, temp.getArtist());
				songValues.put(COLNAME_SONG_TITLE, temp.getName());
				songValues.put(COLNAME_DURATION, temp.getDuration());
				songValues.put(COLNAME_USER_DURATION, temp.getUserDuration());
				songValues.put(COLNAME_SONG_PLAYLIST, playlist.getName());
				db.insert(TABLE_NAME_SONGS, null, songValues);

			}
		} catch (Exception e) {
			Log.e("DB_ERROR", e.toString());
			e.printStackTrace();
		}

	}

	private void removePlaylist(Playlist playList) {

	}

	private void updatePlaylist(Playlist playList) {

	}

	private void getPlaylistSong(Playlist playList) {

	}

	private void addSongt(Song playList) {

	}

}
