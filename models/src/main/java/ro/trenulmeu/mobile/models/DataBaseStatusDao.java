package ro.trenulmeu.mobile.models;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import ro.trenulmeu.mobile.models.DataBaseStatus;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "DataBaseStatus".
*/
public class DataBaseStatusDao extends AbstractDao<DataBaseStatus, Long> {

    public static final String TABLENAME = "DataBaseStatus";

    /**
     * Properties of entity DataBaseStatus.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "Id");
        public final static Property Date = new Property(1, java.util.Date.class, "Date", false, "Date");
        public final static Property NeedDeploy = new Property(2, Boolean.class, "NeedDeploy", false, "NeedDeploy");
        public final static Property ValidFrom = new Property(3, java.util.Date.class, "ValidFrom", false, "ValidFrom");
        public final static Property ValidTo = new Property(4, java.util.Date.class, "ValidTo", false, "ValidTo");
    };


    public DataBaseStatusDao(DaoConfig config) {
        super(config);
    }
    
    public DataBaseStatusDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"DataBaseStatus\" (" + //
                "\"Id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"Date\" INTEGER," + // 1: Date
                "\"NeedDeploy\" INTEGER," + // 2: NeedDeploy
                "\"ValidFrom\" INTEGER," + // 3: ValidFrom
                "\"ValidTo\" INTEGER);"); // 4: ValidTo
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"DataBaseStatus\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, DataBaseStatus entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        java.util.Date Date = entity.getDate();
        if (Date != null) {
            stmt.bindLong(2, Date.getTime());
        }
 
        Boolean NeedDeploy = entity.getNeedDeploy();
        if (NeedDeploy != null) {
            stmt.bindLong(3, NeedDeploy ? 1L: 0L);
        }
 
        java.util.Date ValidFrom = entity.getValidFrom();
        if (ValidFrom != null) {
            stmt.bindLong(4, ValidFrom.getTime());
        }
 
        java.util.Date ValidTo = entity.getValidTo();
        if (ValidTo != null) {
            stmt.bindLong(5, ValidTo.getTime());
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public DataBaseStatus readEntity(Cursor cursor, int offset) {
        DataBaseStatus entity = new DataBaseStatus( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : new java.util.Date(cursor.getLong(offset + 1)), // Date
            cursor.isNull(offset + 2) ? null : cursor.getShort(offset + 2) != 0, // NeedDeploy
            cursor.isNull(offset + 3) ? null : new java.util.Date(cursor.getLong(offset + 3)), // ValidFrom
            cursor.isNull(offset + 4) ? null : new java.util.Date(cursor.getLong(offset + 4)) // ValidTo
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, DataBaseStatus entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setDate(cursor.isNull(offset + 1) ? null : new java.util.Date(cursor.getLong(offset + 1)));
        entity.setNeedDeploy(cursor.isNull(offset + 2) ? null : cursor.getShort(offset + 2) != 0);
        entity.setValidFrom(cursor.isNull(offset + 3) ? null : new java.util.Date(cursor.getLong(offset + 3)));
        entity.setValidTo(cursor.isNull(offset + 4) ? null : new java.util.Date(cursor.getLong(offset + 4)));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(DataBaseStatus entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(DataBaseStatus entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
