package de.westnordost.streetcomplete.data.osm.persist;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Collection;

import de.westnordost.osmapi.map.data.Element;

public abstract class AOsmElementDao<T extends Element>
{
	private final SQLiteOpenHelper dbHelper;

	public AOsmElementDao(SQLiteOpenHelper dbHelper)
	{
		this.dbHelper = dbHelper;
	}

	public void putAll(Collection<T> objects)
	{
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		db.beginTransaction();
		for(T object : objects)
		{
			executeInsert(object);
		}

		db.setTransactionSuccessful();
		db.endTransaction();
	}

	/* Adds or updates the given object to the database */
	public void put(T object)
	{
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.beginTransaction();
		executeInsert(object);
		db.setTransactionSuccessful();
		db.endTransaction();
	}

	public void delete(long id)
	{
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.delete(getTableName(), getIdColumnName() + " = " + id, null);
	}

	public T get(long id)
	{
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = db.query(getTableName(), null, getIdColumnName() + " = " + id,
					null, null, null, null, "1");
		try
		{
			if (!cursor.moveToFirst()) return null;
			return createObjectFrom(cursor);
		}
		finally
		{
			cursor.close();
		}
	}

	/** Cleans up element entries that are not referenced by any quest anymore. */
	public void deleteUnreferenced()
	{
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		String where = NodeTable.Columns.ID + " NOT IN ( " +
				"SELECT " + OsmQuestTable.Columns.ELEMENT_ID + " AS " + getIdColumnName() + " " +
				"FROM " + OsmQuestTable.NAME + " " +
				"WHERE " + OsmQuestTable.Columns.ELEMENT_TYPE + " = \"" + getElementTypeName() +"\"" +
				")";

		db.delete(getTableName(), where, null);
	}

	protected abstract String getElementTypeName();

	protected abstract String getTableName();
	protected abstract String getIdColumnName();

	protected abstract void executeInsert(T object);
	protected abstract T createObjectFrom(Cursor cursor);
}
