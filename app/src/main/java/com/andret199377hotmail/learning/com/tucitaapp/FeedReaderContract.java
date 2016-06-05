package com.andret199377hotmail.learning.com.tucitaapp;

import android.provider.BaseColumns;

/**
 * Created by stevendrumm on 13/05/16.
 */
public final class FeedReaderContract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public FeedReaderContract() {}

    /* Inner class that defines the table contents */
    public static abstract class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "Login";
        public static final String COLUMN_NAME_TIPO = "tipodocumento";
        public static final String COLUMN_NAME_DOCUMENTO = "documento";
        public static final String COLUMN_NAME_PRIMERNOMBRE = "primernombre";
        public static final String COLUMN_NAME_SEGUNDONOMBRE = "segundonombre";
        public static final String COLUMN_NAME_PRIMERAPELLIDO = "primerapellido";
        public static final String COLUMN_NAME_SEGUNDOAPELLIDO = "segundoapellido";
        public static final String COLUMN_NAME_STATE_LOGIN = "estadologin";
        public static final String COLUMN_NAME_NUNNABLE = null;

    }

}
