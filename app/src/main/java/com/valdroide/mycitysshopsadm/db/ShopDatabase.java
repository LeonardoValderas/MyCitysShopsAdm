package com.valdroide.mycitysshopsadm.db;

import com.raizlabs.android.dbflow.annotation.Database;

@Database(name = ShopDatabase.NAME, version = ShopDatabase.VERSION, foreignKeysSupported = true)
public class ShopDatabase {
    public static final int VERSION = 1;
    public static final String NAME = "ShopDatabase";
}