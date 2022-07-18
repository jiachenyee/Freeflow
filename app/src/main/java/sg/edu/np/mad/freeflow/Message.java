package sg.edu.np.mad.freeflow;

import com.google.type.DateTime;

import java.sql.Date;
import java.sql.Time;
import java.util.HashMap;
import java.util.Map;

public class Message {
    String msgContent;
    String msgUserID;
    Long msgTimeStamp;


    public Message(){}


    public Message(String msgContent, String msgUserID, Long msgTimeStamp){
        this.msgContent = msgContent;
        this.msgUserID = msgUserID;
        this.msgTimeStamp = msgTimeStamp;
    }


    public void setMsgContent(String msgContent) { this.msgContent = msgContent; }
    public void setMsgUserID(String msgUserID) { this.msgUserID = msgUserID; }
    public void setMsgTimeStamp(Long msgTimeStamp) { this.msgTimeStamp = msgTimeStamp; }


    public String getMsgContent() { return msgContent; }
    public String getMsgUserID() { return msgUserID; }
    public Long getMsgTimeStamp() { return msgTimeStamp; }



    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("MsgContent", msgContent);
        result.put("MsgUserId", msgUserID);
        result.put("MsgTimeStamp", msgTimeStamp);
        return result;
    }
}