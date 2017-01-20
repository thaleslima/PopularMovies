package com.thaleslima.android.popularmovies.data;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.thaleslima.android.popularmovies.data.local.MovieContract;
import com.thaleslima.android.popularmovies.data.local.MovieDbHelper;
import com.thaleslima.android.popularmovies.data.local.MovieProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

/**
 * Created by thales on 18/01/17.
 */

@RunWith(AndroidJUnit4.class)
public class TestMovieContentProvider {

    private final Context mContext = InstrumentationRegistry.getTargetContext();

    @Before
    public void setUp() {
        MovieDbHelper dbHelper = new MovieDbHelper(mContext);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        database.delete(MovieContract.MovieEntry.TABLE_NAME, null, null);
    }


    @Test
    public void testProviderRegistry() {
        String packageName = mContext.getPackageName();
        String movieProviderClassName = MovieProvider.class.getName();
        ComponentName componentName = new ComponentName(packageName, movieProviderClassName);

        try {

            /*
             * Get a reference to the package manager. The package manager allows us to access
             * information about packages installed on a particular device. In this case, we're
             * going to use it to get some information about our ContentProvider under test.
             */
            PackageManager pm = mContext.getPackageManager();

            /* The ProviderInfo will contain the authority, which is what we want to test */
            ProviderInfo providerInfo = pm.getProviderInfo(componentName, 0);
            String actualAuthority = providerInfo.authority;
            String expectedAuthority = packageName;

            /* Make sure that the registered authority matches the authority from the Contract */
            String incorrectAuthority =
                    "Error: MovieProvider registered with authority: " + actualAuthority +
                            " instead of expected authority: " + expectedAuthority;
            assertEquals(incorrectAuthority,
                    actualAuthority,
                    expectedAuthority);

        } catch (PackageManager.NameNotFoundException e) {
            String providerNotRegisteredAtAll =
                    "Error: MovieProvider not registered at " + mContext.getPackageName();
            /*
             * This exception is thrown if the ContentProvider hasn't been registered with the
             * manifest at all. If this is the case, you need to double check your
             * AndroidManifest file
             */
            fail(providerNotRegisteredAtAll);
        }
    }


    private static final Uri TEST_MOVIES = MovieContract.MovieEntry.CONTENT_URI;

    @Test
    public void testUriMatcher() {

        UriMatcher testMatcher = MovieProvider.buildUriMatcher();

        String uriDoesNotMatch = "Error: The MOVIES URI was matched incorrectly.";
        int actualMatchCode = testMatcher.match(TEST_MOVIES);
        int expectedMatchCode = MovieProvider.CODE_MOVIE;
        assertEquals(uriDoesNotMatch,
                actualMatchCode,
                expectedMatchCode);
    }


    @Test
    public void testInsert() {
        ContentValues testValues = new ContentValues();
        testValues.put(MovieContract.MovieEntry._ID, 1);
        testValues.put(MovieContract.MovieEntry.COLUMN_AVERAGE, "AVERAGE");
        testValues.put(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE, "ORIGINAL_TITLE");
        testValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, "OVERVIEW");
        testValues.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, "POSTER_PATH");
        testValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, "RELEASE_DATE");

        TestUtilities.TestContentObserver observer = TestUtilities.getTestContentObserver();

        ContentResolver contentResolver = mContext.getContentResolver();
        contentResolver.registerContentObserver(
                MovieContract.MovieEntry.CONTENT_URI,
                true,
                observer);

        Uri uri = contentResolver.insert(MovieContract.MovieEntry.CONTENT_URI, testValues);

        Uri expectedUri = ContentUris.withAppendedId(MovieContract.MovieEntry.CONTENT_URI, 1);
        String insertProviderFailed = "Unable to insert item through Provider";
        assertEquals(insertProviderFailed, uri, expectedUri);

        observer.waitForNotificationOrFail();
        contentResolver.unregisterContentObserver(observer);
    }

    @Test
    public void testQuery() {
        MovieDbHelper dbHelper = new MovieDbHelper(mContext);
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        ContentValues testValues = new ContentValues();
        testValues.put(MovieContract.MovieEntry._ID, 2);
        testValues.put(MovieContract.MovieEntry.COLUMN_AVERAGE, "AVERAGE");
        testValues.put(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE, "ORIGINAL_TITLE");
        testValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, "OVERVIEW");
        testValues.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, "POSTER_PATH");
        testValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, "RELEASE_DATE");

        long rowId = database.insert(
                MovieContract.MovieEntry.TABLE_NAME,
                null,
                testValues);

        String insertFailed = "Unable to insert directly into the database";
        assertTrue(insertFailed, rowId != -1);

        database.close();

        Cursor cursor = mContext.getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null);

        String queryFailed = "Query failed to return a valid Cursor";
        assertTrue(queryFailed, cursor != null);
        cursor.close();
    }

    @Test
    public void testDelete() {
        MovieDbHelper dbHelper = new MovieDbHelper(mContext);
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        ContentValues testValues = new ContentValues();
        testValues.put(MovieContract.MovieEntry._ID, 3);
        testValues.put(MovieContract.MovieEntry.COLUMN_AVERAGE, "AVERAGE");
        testValues.put(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE, "ORIGINAL_TITLE");
        testValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, "OVERVIEW");
        testValues.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, "POSTER_PATH");
        testValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, "RELEASE_DATE");

        long rowId = database.insert(
                MovieContract.MovieEntry.TABLE_NAME,
                null,
                testValues);

        database.close();

        String insertFailed = "Unable to insert into the database";
        assertTrue(insertFailed, rowId != -1);

        TestUtilities.TestContentObserver observer = TestUtilities.getTestContentObserver();

        ContentResolver contentResolver = mContext.getContentResolver();

        contentResolver.registerContentObserver(
                MovieContract.MovieEntry.CONTENT_URI,
                true,
                observer);

        Uri uriToDelete = MovieContract.MovieEntry.CONTENT_URI.buildUpon().appendPath("3").build();
        int deleted = contentResolver.delete(uriToDelete, null, null);

        String deleteFailed = "Unable to delete item in the database";
        assertTrue(deleteFailed, deleted != 0);

        observer.waitForNotificationOrFail();
        contentResolver.unregisterContentObserver(observer);
    }
}
