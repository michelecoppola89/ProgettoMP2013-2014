package it.lma5.incorporesound.Activities;

import it.lma5.incorporesound.R;
import it.lma5.incorporesound.Adapters.SongListAdapter;
import it.lma5.incorporesound.AsyncTasks.DbTaskAddPlaylist;
import it.lma5.incorporesound.AsyncTasks.DbTaskDeleteSongs;
import it.lma5.incorporesound.AsyncTasks.DbTaskUpdatePlaylist;
import it.lma5.incorporesound.Entities.Playlist;
import it.lma5.incorporesound.Entities.Song;
import it.lma5.incorporesound.SqliteHelpers.InCorporeSoundHelper;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Activity for creating/updating a new playlist.
 * 
 * @author Andrea Di Lonardo, Luca Fanelli, Michele Coppola
 * 
 */
@SuppressLint({ "UseValueOf", "InlinedApi" })
public class CreatePlaylistActivity extends Activity implements OnClickListener {

	private Button btAddSong;
	private EditText etPlaylistName;
	private EditText etRepetitionNum;
	private RadioGroup rgOrder;
	private RadioGroup rgRepetition;
	private Spinner spFadeIn;
	private ListView lvSongList;
	private ArrayList<Song> songList = new ArrayList<Song>();
	private SongListAdapter slAdapter;
	private boolean isUpdated;
	private String playListToUpdate;
	private Integer fadeInUpd;
	private Integer repetitionUpd;
	private boolean isRandomUpd;

	private InCorporeSoundHelper helper;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_playlist);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();

		}

		btAddSong = (Button) findViewById(R.id.btAddSong);
		btAddSong.setOnClickListener(this);

		lvSongList = (ListView) findViewById(R.id.lvSongList);

		slAdapter = new SongListAdapter(this, R.layout.song_list_row_layout,
				songList);
		lvSongList.setAdapter(slAdapter);
		etPlaylistName = (EditText) findViewById(R.id.etNewPlaylistName);
		etRepetitionNum = (EditText) findViewById(R.id.etRepetitionNum);
		rgOrder = (RadioGroup) findViewById(R.id.rgOrder);
		rgRepetition = (RadioGroup) findViewById(R.id.rgRepetition);
		spFadeIn = (Spinner) findViewById(R.id.spFadeIn);
		ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.arFadeIn, R.layout.spinner_item);
		adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
		spFadeIn.setAdapter(adapter);

		rgRepetition.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == R.id.rbRepeated) {
					etRepetitionNum.setEnabled(true);
				} else
					etRepetitionNum.setEnabled(false);

			}
		});

		helper = new InCorporeSoundHelper(this);

		isUpdated = getIntent().getBooleanExtra("IS_UPDATED", false);

		if (isUpdated) {
			playListToUpdate = getIntent().getStringExtra("PLAYLIST_ID");
			Playlist tempPlaylist = helper.getPlaylistFromId(playListToUpdate);

			etPlaylistName.setText(playListToUpdate);

			if (tempPlaylist.getFadeIn() == 1)
				spFadeIn.setSelection(0);
			else if (tempPlaylist.getFadeIn() == 2)
				spFadeIn.setSelection(1);
			else
				spFadeIn.setSelection(2);
			fadeInUpd = tempPlaylist.getFadeIn();

			if (tempPlaylist.is_random()) {
				rgOrder.check(R.id.rbRandom);
			} else
				rgOrder.check(R.id.rbStraight);
			isRandomUpd = tempPlaylist.is_random();

			if (tempPlaylist.getRound() == 0) {
				rgRepetition.check(R.id.rbLoop);
			} else if (tempPlaylist.getRound() == 1) {
				rgRepetition.check(R.id.rbOneTime);
			} else {
				rgRepetition.check(R.id.rbRepeated);
				etRepetitionNum.setText(tempPlaylist.getRound().toString());
			}
			repetitionUpd = tempPlaylist.getRound();

			for (int i = 0; i < tempPlaylist.getSongList().size(); i++) {
				slAdapter.add(tempPlaylist.getSongList().get(i));
			}

		}

		// check if songs exist

		MediaMetadataRetriever retriever = new MediaMetadataRetriever();
		ArrayList<Integer> missingSongPosition = new ArrayList<Integer>();
		ArrayList<Song> toDelete = new ArrayList<Song>();

		for (int i = 0; i < songList.size(); i++) {
			Song temp = songList.get(i);
			try {
				retriever.setDataSource(this, temp.getPath());
			} catch (IllegalArgumentException e) {
				missingSongPosition.add(i);
				toDelete.add(temp);
			}
		}

		for (int i = missingSongPosition.size() - 1; i >= 0; i--) {
			slAdapter.remove(songList.get(missingSongPosition.get(i)));
		}

		if (missingSongPosition.size() > 0) {

			DbTaskDeleteSongs dbTaskDeleteSongs = new DbTaskDeleteSongs(helper);
			dbTaskDeleteSongs.execute(toDelete);

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(getString(R.string.sErrorMessage))
					.setCancelable(false)
					.setPositiveButton(getString(R.string.sOk),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {

								}
							});
			AlertDialog alert = builder.create();
			alert.show();

		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_playlist, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.

		int id = item.getItemId();
		if (id == R.id.action_settings) {
			if (songList.isEmpty()) {
				Toast.makeText(this, getString(R.string.sToastNoSong),
						Toast.LENGTH_SHORT).show();
				return true;
			}
			String playListName = etPlaylistName.getText().toString();
			if (playListName.isEmpty()) {
				Toast.makeText(this, getString(R.string.sToastNoTitle),
						Toast.LENGTH_SHORT).show();
				return true;
			}

			boolean isRandom;
			Integer repetition;
			int selectedRepetition = rgRepetition.getCheckedRadioButtonId();
			if (selectedRepetition == R.id.rbOneTime)
				repetition = 1;
			else if (selectedRepetition == R.id.rbLoop)
				repetition = 0;
			else {
				String temp = etRepetitionNum.getText().toString();
				if (temp.isEmpty()) {
					Toast.makeText(this,
							getString(R.string.sInsertNumberOfRepetitions),
							Toast.LENGTH_SHORT).show();
					return true;
				}
				if (temp.equals("0")) {
					Toast.makeText(this, getString(R.string.sZeroRepetetition),
							Toast.LENGTH_SHORT).show();
					return true;
				}

				repetition = Integer.parseInt(temp);
			}
			int selectedOrder = rgOrder.getCheckedRadioButtonId();
			if (selectedOrder == R.id.rbRandom)
				isRandom = true;
			else
				isRandom = false;
			Integer fadeIn = Integer.parseInt(spFadeIn.getSelectedItem()
					.toString());

			if (!isUpdated) {
				Playlist playListToInsert = new Playlist(playListName,
						songList, repetition, isRandom, fadeIn);

				if (helper.getPlaylistFromId(playListToInsert.getName()) != null) {
					Toast.makeText(this,
							getString(R.string.sPlayListNameExist),
							Toast.LENGTH_SHORT).show();
					return true;
				}

				DbTaskAddPlaylist runner = new DbTaskAddPlaylist(helper);
				runner.execute(playListToInsert);
			} else {
				// update playlist
				Playlist toUpdate = new Playlist(playListToUpdate, null,
						repetitionUpd, isRandomUpd, fadeInUpd);
				Playlist modified = new Playlist();

				if (!playListName.equals(playListToUpdate)) {

					if (helper.getPlaylistFromId(playListName) != null) {
						Toast.makeText(this,
								getString(R.string.sPlayListNameExist),
								Toast.LENGTH_SHORT).show();
						return true;
					}
					modified.setName(playListName);
				}
				if (repetition != repetitionUpd)
					modified.setRound(repetition);
				if (isRandomUpd != isRandom)
					modified.setIs_random(isRandom);
				else
					modified.setIs_random(isRandomUpd);
				if (fadeIn != fadeInUpd)
					modified.setFadeIn(fadeIn);

				modified.setSongList(songList);

				DbTaskUpdatePlaylist runner = new DbTaskUpdatePlaylist(helper);
				runner.execute(toUpdate, modified);

			}

			setResult(RESULT_OK);

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(getString(R.string.sPlaylistSaved))
					.setCancelable(false)
					.setPositiveButton(getString(R.string.sOk),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {

									returnToMainActivity();
								}
							});
			AlertDialog alert = builder.create();
			alert.show();

			return true;

		}

		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_create_playlist,
					container, false);
			return rootView;
		}
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btAddSong) {
			if (Build.VERSION.SDK_INT < 19) {
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
				intent.setType("audio/*");
				startActivityForResult(intent, 10);
			} else {
				Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
				intent.addCategory(Intent.CATEGORY_OPENABLE);
				intent.setType("audio/*");
				startActivityForResult(intent, 10);
			}
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == RESULT_OK && requestCode == 10) {
			Uri uriSong;
			uriSong = data.getData();
			MediaMetadataRetriever retriever = new MediaMetadataRetriever();
			try {
				retriever.setDataSource(this, uriSong);
				String mimetype = retriever
						.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE);
				if (!mimetype.contains("audio")) {
					Toast.makeText(this, getString(R.string.sWrongFormat),
							Toast.LENGTH_SHORT).show();
				} else {

					String name = retriever
							.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);

					Integer duration = new Integer(
							retriever
									.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));

					String artist = retriever
							.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);

					if (duration < 15000) {
						Toast.makeText(this,
								getString(R.string.sDurationTooShort),
								Toast.LENGTH_SHORT).show();

					} else {
						Song songTi = new Song(name, uriSong, 0, 15000,
								duration, artist);
						slAdapter.add(songTi);
					}

				}
			} catch (RuntimeException e) {
				Toast.makeText(this, getString(R.string.sWrongFormat),
						Toast.LENGTH_SHORT).show();
			}

		}
	}

	private void returnToMainActivity() {
		finish();

	}
}
