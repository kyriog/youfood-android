package com.supinfo.youfood.handler;

import android.os.Handler;

public abstract class BaseHandler extends Handler {
	public final static int STATUS_START = 1;
	public final static int STATUS_ERROR = 2;
	public final static int STATUS_OK = 3;
	public final static int STATUS_NOK = 4;
	
	public final static int ERROR_HTTP = 5;
	public final static int ERROR_TIMEOUT = 6;
	public final static int ERROR_JSON = 7;
	public final static int ERROR_UNKNOWN = 8;
}
