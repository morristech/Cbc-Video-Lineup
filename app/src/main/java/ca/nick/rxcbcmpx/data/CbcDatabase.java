package ca.nick.rxcbcmpx.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import ca.nick.rxcbcmpx.models.VideoItem;

@Database(entities = { VideoItem.class }, version = 1)
public abstract class CbcDatabase extends RoomDatabase {

    private static final String NAME = "cbcDatabase.db";
    private static CbcDatabase sInstance;

    public abstract VideoDao videoDao();

    public static CbcDatabase getInstance(Context context) {
        if (sInstance == null) {
            sInstance = create(context);
        }

        return sInstance;
    }

    private static CbcDatabase create(Context context) {
        return Room.databaseBuilder(context.getApplicationContext(), CbcDatabase.class, NAME)
                .build();
    }

}
