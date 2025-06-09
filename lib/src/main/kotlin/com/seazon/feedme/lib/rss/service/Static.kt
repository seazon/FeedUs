package com.seazon.feedme.lib.rss.service

import kotlinx.serialization.json.Json

object Static {

    val defaultJson = Json { ignoreUnknownKeys = true }

    const val ACCOUNT_TYPE_FEEDLY = "Feedly"
    const val ACCOUNT_TYPE_BAZQUX_READER = "BazQux Reader"
    const val ACCOUNT_TYPE_FEED_WRANGLER = "Feed Wrangler"
    const val ACCOUNT_TYPE_FEEDBIN = "Feedbin"
    const val ACCOUNT_TYPE_FEEDHQ = "FeedHQ"
    const val ACCOUNT_TYPE_FEEDJA = "Feedja"
    const val ACCOUNT_TYPE_FEVER = "Fever"
    const val ACCOUNT_TYPE_TTRSS = "Tiny Tiny RSS"
    const val ACCOUNT_TYPE_INOREADER_OAUTH2 = "InoReaderOAuth2"
    const val ACCOUNT_TYPE_INOREADER = "InoReader"
    const val ACCOUNT_TYPE_THE_OLD_READER = "The Old Reader"
    const val ACCOUNT_TYPE_FRESH_RSS = "FreshRSS"
    const val ACCOUNT_TYPE_GOOGLE_READER_API = "Google Reader API"
    const val ACCOUNT_TYPE_LOCAL_RSS = "Local RSS"
    const val ACCOUNT_TYPE_MINIFLUX = "Miniflux"
    const val ACCOUNT_TYPE_FOLO = "Folo"

    //    public static SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
    const val REDIRECT_URI_OLD: String = "feedme://oauth"

    //    public static final String REDIRECT_URI_OLD = "http://localhost";
    const val REDIRECT_URI: String = "feedme://oauth" //    public static final String URI_INDEX = "https://github.com/seazon/FeedMe/blob/master/README.md";
    //    public static final String URI_PRIVACY_AND_SECURITY = "https://github.com/seazon/FeedMe/blob/master/privacy_and_security.md";
    //    public static final String URI_PATCH = "https://github.com/seazon/FeedMe/blob/master/doc/en/patches.md";
    //    public static final String URI_LICENSES = "https://github.com/seazon/FeedMe/blob/master/doc/en/licenses.md";
    //    public static final String URI_CREDITS = "https://github.com/seazon/FeedMe/blob/master/doc/en/credits.md";
    //
    //    public static final String BASE64_ENCODED_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAicda+pvLKPnhVPHxDru46E3rVnJnBERXlH4Au1rmNdaBBLD94L+Mi03Y2onC0WIafWBoXxhNZcw4uemKCIoXG9NHo74vsnvVgJIHN2IzeP8lR6I1nSN9GploXhk9n4Et/dt27A/dgJJ+CPlr30WQL2jo2kYw6Ow/+MOqH/yLY0kBQOSuH6+huOn8xfDhkCfQVy5OogfEUnHuK0u4daRuVyaYnIITddtgjoTJzxfELKjzWEQCGMkEhf+ExxBnxt3uSlTRk0ScL0HUBDVJE7mEJMahkH4sVJ6HdYZejfb/w9fa5fnF3ATDJ5IMGF44Zm06yr4BvlFR1p6zFDzu0BB7QQIDAQAB";
    //    public static final String FEEDME_ITEM_1 = "feedme_item_1";
    //    public static final String FEEDME_ITEM_2 = "feedme_item_2";
    //    public static final String FEEDME_ITEM_3 = "feedme_item_3";
    //    public static final String FEEDME_ITEM_101 = "feedme_item_101";
    //    public static final String FEEDME_ITEM_102 = "feedme_item_102";
    //    public static final int REQUEST_CODE_IAB = 888;
    //
    //    public static final long PREVIEW_TIME = 30 * 24 * 3600 * 1000l;
    //    public static final int PAGE_MAX_SIZE = 1024 * 1024 * 3;
    //    public static final int IMAGE_MAX_SIZE = 1024 * 1024 * 15;
    //
    //    public static final int FONT_SIZE_DELTA_STEP = 2;
    //    public final static int FONT_SIZE_MIN = 8;
    //    public final static int FONT_SIZE_MAX = 32;
    //    public final static int FONT_SIZE_SUBHEAD = 16;
    //    public final static int FONT_SIZE_CAPTION = 12;
    //    public final static int FONT_SIZE_ARTICLE = 18;
    //
    //    public static final int LINE_HEIGHT_DELTA_STEP = 10;
    //    public final static int LINE_HEIGHT_MIN = 110;
    //    public final static int LINE_HEIGHT_DEFAULT = 150;
    //    public final static int LINE_HEIGHT_MAX = 200;
    //
    //    public static final int MARGIN_DELTA_STEP = 1;
    //    public final static int MARGIN_MIN = 2;
    //    public final static int MARGIN_DEFAULT = 5;
    //    public final static int MARGIN_MAX = 8;
    //
    //    public final static String ALIGN_ARTICLE_CENTER = "center";
    //    public final static String ALIGN_ARTICLE_START = "start";
    //    public final static String ALIGN_ARTICLE_END = "end";
    //    public final static String ALIGN_ARTICLE_JUSTIFY = "justify";
    //
    //    public final static String ACTION_TEXT = "com.seazon.feedme.intent.extra.TEXT";
    //
    //    public final static String SHARED_PREFS_PLAY_LIST = "play_list";
    //
    //    public final static String PTR_0 = "0"; // Disable
    //    public final static String PTR_1 = "1"; // Sync
    //    public final static String PTR_2 = "2"; // Hide read items
    //    public final static String PTR_3 = "3"; // Hide read items and sync
    //
    //    public final static int PLAY_LIST_MAX = 100; // 播放列表最多100个item
    //
    //    public final static int PLAY_SEEK_BAR_MAX = 1000;
    //
    //    public final static int TEMPORARY_TAG_MAX = 5;
}
