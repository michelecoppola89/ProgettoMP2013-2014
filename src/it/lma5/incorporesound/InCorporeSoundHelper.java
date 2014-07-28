package it.lma5.incorporesound;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

public class InCorporeSoundHelper extends SQLiteOpenHelper {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String DBNAME = "InCorporeSounds.sqlite";
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
	private static final String COLNAME_DURATION = "duration";
	private static final String COLNAME_USER_DURATION = "user_duration";
	private static final String COLNAME_SONG_PLAYLIST = "playlist_id";
	private static final String COLNAME_SONG_URI = "uri";
	private Context context;
	private SQLiteDatabase db;

	public InCorporeSoundHelper(Context context) {
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

	public List<Playlist> getAllPlaylists() {
		ArrayList<Playlist> ret = new ArrayList<Playlist>();
		String[] columns = { COLNAME_PLAYLIST_NAME, COLNAME_RANDOM,
				COLNAME_FADE_IN, COLNAME_LOOP };

		Cursor cursor = db.query(TABLE_NAME_PLAYLIST, columns, null, null,
				null, null, COLNAME_PLAYLIST_NAME);

		if (cursor != null) {
			cursor.moveToPosition(-1);

			while (cursor.moveToNext()) {
				ret.add(getPlaylistFromRow(cursor));
			}
		}
		cursor.close();
		return ret;
	}

	public Playlist getPlaylistFromId(String playlistName) {

		String[] columns = { COLNAME_PLAYLIST_NAME, COLNAME_RANDOM,
				COLNAME_FADE_IN, COLNAME_LOOP };

		String[] varargs = new String[1];
		varargs[0] = playlistName;
		Cursor cursor = db.query(TABLE_NAME_PLAYLIST, columns,
				COLNAME_PLAYLIST_NAME + "= ?", varargs, null, null, null);

		cursor.moveToPosition(-1);
		if (cursor.moveToNext()) {
			Playlist ret = getPlaylistFromRow(cursor);
			ret.setSongList(getSongsFromPlaylistId(playlistName));
			return ret;
		} else
			return null;

	}

	private Playlist getPlaylistFromRow(Cursor cursor) {

		Integer round = Integer.parseInt(cursor.getString(cursor
				.getColumnIndex(COLNAME_LOOP)));
		Integer fadeIn = Integer.parseInt(cursor.getString(cursor
				.getColumnIndex(COLNAME_FADE_IN)));
		Integer isRandom = Integer.parseInt(cursor.getString(cursor
				.getColumnIndex(COLNAME_RANDOM)));
		boolean rand;
		if(isRandom==0)
			rand = false;
		else
			rand = true;
		String name = cursor.getString(cursor
				.getColumnIndex(COLNAME_PLAYLIST_NAME));

		Playlist ret = new Playlist(name, null, round, rand, fadeIn);

		return ret;

	}

	public Playlist addPlaylist(Playlist playlist) {

		ContentValues values = new ContentValues();
		values.put(COLNAME_PLAYLIST_NAME, playlist.getName());
		values.put(COLNAME_LOOP, playlist.getRound());
		values.put(COLNAME_FADE_IN, playlist.getFadeIn());
		if (playlist.is_random())
			values.put(COLNAME_RANDOM, 1);
		else
			values.put(COLNAME_RANDOM, 0);

		try {
			long ret = db.insert(TABLE_NAME_PLAYLIST, null, values);
			Log.v("DBERR", Long.toString(ret));
			if (ret == -1) {
				return null;
			}
			for (int i = 0; i < playlist.getSongList().size(); i++) {
				Song temp;
				temp = playlist.getSongList().get(i);
				addSongToPlaylist(temp, playlist.getName());

			}

		} catch (Exception e) {
			Log.e("DB_ERROR", e.toString());
			e.printStackTrace();
		}

		return playlist;

	}

	public ArrayList<Song> getSongsFromPlaylistId(String playlistId) {

		ArrayList<Song> ret = new ArrayList<Song>();

		String[] columns = { COLNAME_ID_SONG, COLNAME_SONG_TITLE,
				COLNAME_ARTIST, COLNAME_BEGINNING, COLNAME_DURATION,
				COLNAME_USER_DURATION, COLNAME_SONG_PLAYLIST, COLNAME_SONG_URI };

		String[] varargs = new String[1];
		varargs[0] = playlistId;

		Cursor cursor = db.query(TABLE_NAME_SONGS, columns,
				COLNAME_SONG_PLAYLIST + "= ?", varargs, null, null, null);

		if (cursor != null) {
			cursor.moveToPosition(-1);
			while (cursor.moveToNext()) {
				ret.add(getSongFromRow(cursor));
			}
		}
		cursor.close();

		return ret;
	}

	public void deletePlaylist(String playlistName) {

		// remove all playlist's songs
		ArrayList<Song> listSong = getSongsFromPlaylistId(playlistName);
		for (int i = 0; i < listSong.size(); i++) {
			Song toRemove = listSong.get(i);
			deleteSong(toRemove.getId());
		}

		try {
			String[] varargs = new String[1];
			varargs[0] = playlistName;
			db.delete(TABLE_NAME_PLAYLIST, COLNAME_PLAYLIST_NAME + "= ?",
					varargs);
		} catch (Exception e) {
			Log.v("DB_ERROR", "errore delete playlist");
		}

	}

	public void addSongToPlaylist(Song song, String playlistName) {

		ContentValues songValues = new ContentValues();
		// songValues.put(COLNAME_ID_SONG, ""); // null per
		// autoincremento
		songValues.put(COLNAME_ARTIST, song.getArtist());
		songValues.put(COLNAME_SONG_TITLE, song.getName());
		songValues.put(COLNAME_DURATION, song.getDuration());
		songValues.put(COLNAME_USER_DURATION, song.getUserDuration());
		songValues.put(COLNAME_SONG_PLAYLIST, playlistName);
		songValues.put(COLNAME_SONG_URI, song.getPath().toString());
		songValues.put(COLNAME_BEGINNING, song.getBeginTime());

		db.insert(TABLE_NAME_SONGS, null, songValues);
	}

	public void updatePlaylist(Playlist toUpdate, String playlistName,
			Integer round, Boolean isRandom, Integer fadeIn,
			ArrayList<Song> songList) {

		if (round != null) {

			ContentValues values = new ContentValues();
			values.put(COLNAME_LOOP, round);

			try {
				String[] varargs = new String[1];
				varargs[0] = toUpdate.getName();
				db.update(TABLE_NAME_PLAYLIST, values, COLNAME_PLAYLIST_NAME
						+ "= ?", varargs);
			} catch (Exception e) {
				Log.v("DB_ERROR", "Error update playlist round");

			}

		}

		if (isRandom != null) {

			ContentValues values = new ContentValues();
			if (isRandom)
				values.put(COLNAME_RANDOM, 1);
			else
				values.put(COLNAME_RANDOM, 0);
			try {
				String[] varargs = new String[1];
				varargs[0] = toUpdate.getName();
				db.update(TABLE_NAME_PLAYLIST, values, COLNAME_PLAYLIST_NAME
						+ "= ?", varargs);
			} catch (Exception e) {
				Log.v("DB_ERROR", "Error update playlist random");

			}

		}

		if (fadeIn != null) {

			ContentValues values = new ContentValues();
			values.put(COLNAME_FADE_IN, fadeIn);

			try {
				String[] varargs = new String[1];
				varargs[0] = toUpdate.getName();
				db.update(TABLE_NAME_PLAYLIST, values, COLNAME_PLAYLIST_NAME
						+ "= ?", varargs);
			} catch (Exception e) {
				Log.v("DB_ERROR", "Error update playlist fadeIn");

			}
		}

		deleteAllSongsFromPlaylistId(toUpdate.getName());

		if (playlistName == null) {
			for (int i = 0; i < songList.size(); i++)
				addSongToPlaylist(songList.get(i), toUpdate.getName());
		} else {
			for (int i = 0; i < songList.size(); i++)
				addSongToPlaylist(songList.get(i), playlistName);
		}

		if (playlistName != null) {

			// deletePlaylist(toUpdate.getName());
			// toUpdate.setName(playlistName);
			// addPlaylist(toUpdate);

			ContentValues values = new ContentValues();
			values.put(COLNAME_PLAYLIST_NAME, playlistName);

			try {
				String[] varargs = new String[1];
				varargs[0] = toUpdate.getName();
				db.update(TABLE_NAME_PLAYLIST, values, COLNAME_PLAYLIST_NAME
						+ "= ?", varargs);
			} catch (Exception e) {
				Log.v("DB_ERROR", "Error update playlist round");

			}

		}
	}

	private Song getSongFromRow(Cursor cursor) {

		Log.v("RICOGNIZIONE ERRORE",
				Integer.toString(cursor.getColumnIndex(COLNAME_SONG_URI)));
		Uri uri = Uri.parse(cursor.getString(cursor
				.getColumnIndex(COLNAME_SONG_URI)));

		Integer begin = Integer.parseInt(cursor.getString(cursor
				.getColumnIndex(COLNAME_BEGINNING)));
		Integer userDuration = Integer.parseInt(cursor.getString(cursor
				.getColumnIndex(COLNAME_USER_DURATION)));
		Integer duration = Integer.parseInt(cursor.getString(cursor
				.getColumnIndex(COLNAME_DURATION)));
		String title = cursor.getString(cursor
				.getColumnIndex(COLNAME_SONG_TITLE));
		String artist = cursor.getString(cursor.getColumnIndex(COLNAME_ARTIST));
		Integer id = Integer.parseInt(cursor.getString(cursor
				.getColumnIndex(COLNAME_ID_SONG)));

		Song ret = new Song(title, uri, begin, userDuration, duration, artist);
		ret.setId(id);

		return ret;
	}

	public void deleteSong(Integer id) {
		try {
			String[] varargs = new String[1];
			varargs[0] = id.toString();
			db.delete(TABLE_NAME_SONGS, COLNAME_ID_SONG + "= ?", varargs);
		} catch (Exception e) {
			Log.v("DB_ERROR", "errore delete song");
		}
	}

	public void deleteAllSongsFromPlaylistId(String playlistName) {
		try {
			String[] varargs = new String[1];
			varargs[0] = playlistName;
			db.delete(TABLE_NAME_SONGS, COLNAME_SONG_PLAYLIST + "= ?", varargs);
		} catch (Exception e) {
			Log.v("DB_ERROR", "errore delete all song from playlist id");
		}
	}

	public void updateSong(Song toUpdate, String name, Uri path,
			Integer beginTime, Integer userDuration, Integer duration,
			String artist) {

		if (name != null) {

			ContentValues values = new ContentValues();
			values.put(COLNAME_SONG_TITLE, name);

			try {
				String[] varargs = new String[1];
				varargs[0] = toUpdate.getId().toString();

				db.update(TABLE_NAME_SONGS, values, COLNAME_ID_SONG + "= ?",
						varargs);
			} catch (Exception e) {
				Log.v("DB_ERROR", "Error update song title");

			}
		}

		if (path != null) {

			ContentValues values = new ContentValues();
			values.put(COLNAME_SONG_URI, path.toString());

			try {
				String[] varargs = new String[1];
				varargs[0] = toUpdate.getId().toString();
				db.update(TABLE_NAME_SONGS, values, COLNAME_ID_SONG + "= ?",
						varargs);
			} catch (Exception e) {
				Log.v("DB_ERROR", "Error update song uri");

			}
		}

		if (beginTime != null) {

			ContentValues values = new ContentValues();
			values.put(COLNAME_BEGINNING, beginTime);

			try {
				String[] varargs = new String[1];
				varargs[0] = toUpdate.getId().toString();
				db.update(TABLE_NAME_SONGS, values, COLNAME_ID_SONG + "= ?",
						varargs);
			} catch (Exception e) {
				Log.v("DB_ERROR", "Error update song begin time");

			}
		}

		if (userDuration != null) {

			ContentValues values = new ContentValues();
			values.put(COLNAME_USER_DURATION, userDuration);

			try {
				String[] varargs = new String[1];
				varargs[0] = toUpdate.getId().toString();
				db.update(TABLE_NAME_SONGS, values, COLNAME_ID_SONG + "= ?",
						varargs);
			} catch (Exception e) {
				Log.v("DB_ERROR", "Error update song userDuration");

			}
		}

		if (duration != null) {

			ContentValues values = new ContentValues();
			values.put(COLNAME_SONG_TITLE, name);

			try {
				String[] varargs = new String[1];
				varargs[0] = toUpdate.getId().toString();
				db.update(TABLE_NAME_SONGS, values, COLNAME_ID_SONG + "= ?",
						varargs);
			} catch (Exception e) {
				Log.v("DB_ERROR", "Error update song duration");

			}
		}

		if (artist != null) {

			ContentValues values = new ContentValues();
			values.put(COLNAME_ARTIST, artist);

			try {
				String[] varargs = new String[1];
				varargs[0] = toUpdate.getId().toString();
				db.update(TABLE_NAME_SONGS, values, COLNAME_ID_SONG + "= ?",
						varargs);
			} catch (Exception e) {
				Log.v("DB_ERROR", "Error update song artist");

			}

		}
	}

}
