package com.sudoajay.whatsapp_media_mover_to_sdcard;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader;
    private HashMap<String, List<String>> _listDataChild;
    private List<Integer> arrow_Image_Resource, count_The_Size;
    private List<Long> get_The_No_File;
    private List<File> file_Data;
    private List<Boolean> check_Array;
    private List<File> selected_File;
    public ExpandableListAdapter(List<File> selected_File){
        this.selected_File=selected_File;
    }
    public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<String>> listChildData, List<Integer> arrow_Image_Resource
                                , List<Integer> count_The_Size , List<Long> get_The_No_File, List<File> file_Data,
                                 List<Boolean> check_Array,List<File> selected_File) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        this.arrow_Image_Resource = arrow_Image_Resource;
        this.count_The_Size = count_The_Size;
        this.get_The_No_File = get_The_No_File;
        this.file_Data = file_Data;
        this.check_Array=check_Array;
        this.selected_File=selected_File;


    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.activity_my_list_style, null);
        }
        final TextView nameTextView = convertView.findViewById(R.id.nameTextView);
        final TextView sizeTextView = convertView.findViewById(R.id.sizeTextView);
        final ImageView coverImageView = convertView.findViewById(R.id.coverImageView);
        final CheckBox checkBoxView = convertView.findViewById(R.id.checkBoxView);


        checkBoxView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(groupPosition != 0) {
                    if (checkBoxView.isChecked() && check_Array.get(count_The_Size.get(groupPosition - 1) + childPosition)) {
                        check_Array.set(count_The_Size.get(groupPosition - 1) + childPosition, true);
                        find_And_Remove(file_Data.get(count_The_Size.get(groupPosition - 1) + childPosition));
                    } else {
                        check_Array.set(count_The_Size.get(groupPosition - 1) + childPosition, false);
                        selected_File.add(file_Data.get(count_The_Size.get(groupPosition - 1) + childPosition));
                    }
                }else{
                    if (checkBoxView.isChecked() && check_Array.get(childPosition)) {
                        check_Array.set(childPosition, true);
                        find_And_Remove(file_Data.get(childPosition));
                    } else {
                        check_Array.set(childPosition,false);
                       selected_File.add(file_Data.get(childPosition));

                    }
                }
            }
        });


        if(groupPosition != 0) {
            if (check_Array.get(count_The_Size.get(groupPosition - 1) + childPosition)) {
                checkBoxView.setChecked(true);

            } else {
                checkBoxView.setChecked(false);
            }
            Check_For_Extension(file_Data.get(count_The_Size.get(groupPosition - 1) + childPosition).getAbsolutePath(), coverImageView);
            sizeTextView.setText(Convert_It(file_Data.get(count_The_Size.get(groupPosition - 1) + childPosition).length()));
        } else {
            if (check_Array.size() > childPosition) {
                if (check_Array.get(childPosition)) {
                    checkBoxView.setChecked(true);

                } else {
                    checkBoxView.setChecked(false);

                }
                Check_For_Extension(file_Data.get(childPosition).getAbsolutePath(), coverImageView);
                sizeTextView.setText(Convert_It(file_Data.get(childPosition).length()));
            }
        }


        nameTextView.setText(childText);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infaltInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infaltInflater.inflate(R.layout.activity_my_changes_layout, null);
        }
        ImageView imageView = convertView.findViewById(R.id.media_Mover_Image_View);
        TextView date_Text_View = convertView.findViewById(R.id.date_Text_View);
        TextView size_Of_Text_View = convertView.findViewById(R.id.size_Of_Text_View);
        date_Text_View.setTypeface(null, Typeface.BOLD);
        date_Text_View.setText(headerTitle);
        size_Of_Text_View.setText(get_The_No_File.get(groupPosition)+"");
        imageView.setImageResource(arrow_Image_Resource.get(groupPosition));
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    public void find_And_Remove(File file) {
        for (int i = selected_File.size() - 1; i >= 0; i--) {
            if (file.equals(selected_File.get(i)))
                selected_File.remove(i);

        }
    }
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void Check_For_Extension(String path,ImageView imageView){
        int i = path.lastIndexOf('.');
        String extension="";
        if (i > 0) {
            extension = path.substring(i+1);
        }
        if(extension.equals("jpg") || extension.equals("mp4") || extension.equals("jpeg")){
            // Images || Videos
            Glide.with(_context)
                    .asBitmap()
                    .load(Uri.fromFile(new File(path)))
                    .into(imageView);
        }else if(extension.equals("mp3") ||extension.equals("m4a") || extension.equals("amr") || extension.equals("aac")){
            // Audiio
            getAudioAlbumImageContentUri(imageView,path);

        }else if(extension.equals("pptx") ||extension.equals("pdf")
                ||extension.equals("docx") || extension.equals("txt") )
            imageView.setImageResource(R.drawable.document_icon);

        else if(extension.equals("opus")){
            imageView.setImageResource(R.drawable.voice_icon);
        }else{
            imageView.setImageResource(R.drawable.file_icon);
        }

    }
    public void getAudioAlbumImageContentUri(ImageView imageView ,String filePath) {
        try {
            Uri audioUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.DATA + "=? ";
        String[] projection = new String[] { MediaStore.Audio.Media._ID , MediaStore.Audio.Media.ALBUM_ID};

        Cursor cursor = _context.getContentResolver().query(
                audioUri,
                projection,
                selection,
                new String[] { filePath }, null);

        if (cursor != null && cursor.moveToFirst()) {

                long albumId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                    if(get_Cover(albumId) != null) {
                        Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
                        Uri imgUri = ContentUris.withAppendedId(sArtworkUri,
                                albumId);

                        Glide.with(_context)
                                .load(imgUri)
                                .into(imageView);
                    }
                else {
                    imageView.setImageResource(R.drawable.audio_icon);
                }
            cursor.close();
            }
        } catch (Exception e){

            }
        }
    public Bitmap get_Cover(long album_id) {
        Bitmap artwork = null;
        Bitmap resizedBitmap = null;
        try {
            Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
            Uri uri = ContentUris.withAppendedId(sArtworkUri, album_id);
            ContentResolver res = _context.getContentResolver();
            InputStream in = res.openInputStream(uri);
            artwork = BitmapFactory.decodeStream(in);

            int width = artwork.getWidth();
            int height = artwork.getHeight();
            float scaleWidth = ((float) 500) / width;
            float scaleHeight = ((float) 500) / height;
            // CREATE A MATRIX FOR THE MANIPULATION
            Matrix matrix = new Matrix();
            // RESIZE THE BIT MAP
            matrix.postScale(scaleWidth, scaleHeight);

            // "RECREATE" THE NEW BITMAP
            resizedBitmap = Bitmap.createBitmap(
                    artwork, 0, 0, width, height, matrix, false);

        } catch (Exception ignored) {

        }

        return resizedBitmap;
    }

    public String Convert_It(long size) {
        if (size > (1024 * 1024 * 1024)) {
            // GB
            return Convert_To_Decimal((float) size / (1024 * 1024 * 1024)) + " GB";
        } else if (size > (1024 * 1024)) {
            // MB
            return Convert_To_Decimal((float) size / (1024 * 1024)) + " MB";

        } else {
            // KB
            return Convert_To_Decimal((float) size / (1024)) + " KB";
        }

    }

    public List<File> getSelected_File() {
        return selected_File;
    }

    public void setSelected_File(List<File> selected_File) {
        this.selected_File = selected_File;
    }

    public String Convert_To_Decimal(float value) {
        String size = value + "";
        if (value >= 1000) {
            return size.substring(0, 4);
        } else if (value >= 100) {
            return size.substring(0, 3);
        } else {
            if (size.length() == 2 || size.length() == 3) {
                return size.substring(0, 1);
            }
            return size.substring(0, 4);

        }

    }
}

