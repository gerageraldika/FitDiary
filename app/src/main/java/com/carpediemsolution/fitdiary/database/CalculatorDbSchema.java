package com.carpediemsolution.fitdiary.database;

public class CalculatorDbSchema {

    public static final class CalculatorTable {
        public static final String NAME = "weight";
        public static final String NAME_PERSON = "person";
        public static final String NAME_REMEMBERING = "reminder";
        public static final String NAME_COUNTER = "counter";



        public static final class Cols {
            public static final String KEY_ID = "_id";
            public static final String UUID = "uuid";
            public static final String WEIGHT = "weight";
            public static final String DATE = "date";
            public static final String NOTES = "notes";
            public static final String CALORIES = "calories";
            public static final String PHOTOFILE = "photofile";

            public static final String ID = "id";
            public static final String NAME = "name";
            public static final String HEIGHT = "height";
            public static final String PERSON_WEIGHT = "person_weight";


            public static final String REM_ID = "_rem_id";
            public static final String REM_UUID = "rem_uuid";
            public static final String REM_DATE = "rem_date";
            public static final String REM_NOTES= "rem_note";
            public static final String REM_FLAG= "rem_flag";
            public static final String REM_COUNTER= "rem_counter";


            public static final String COUNTER_ID = "_counter_id";
            public static final String COUNTER_UUID = "counter_uuid";
            public static final String COUNTER_DATE = "counter_date";
            public static final String COUNTER_FLAG = "counter_flag";


        }
    }
}
