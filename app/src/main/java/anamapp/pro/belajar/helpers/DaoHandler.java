package anamapp.pro.belajar.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import anamapp.pro.belajar.models.DaoMaster;
import anamapp.pro.belajar.models.DaoSession;

public class DaoHandler {
    public static DaoSession getInstance(Context context) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "catatpengeluaran_db", null);
        SQLiteDatabase db = helper.getWritableDatabase();

        DaoMaster daoMaster = new DaoMaster(db);
        return daoMaster.newSession();
    }
}