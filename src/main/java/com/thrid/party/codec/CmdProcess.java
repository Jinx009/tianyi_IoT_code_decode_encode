package com.thrid.party.codec;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class CmdProcess {

    private String msgType = "deviceReq";
    private String cmd = "command";
    private int hasMore = 0;
    private int errcode = 0;
    private JsonNode paras;


    public CmdProcess() {
    }

    public CmdProcess(ObjectNode input) {
        try {
        	this.errcode = 0;
        	this.hasMore = 0;
            this.msgType = input.get("msgType").asText();
            this.cmd = input.get("cmd").asText();
            this.paras = input.get("paras");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    

    public byte[] toByte() {
        try {
            if (this.msgType.equals("cloudReq")) {
                /*
                应用服务器下发的控制命令，本例只有一条控制命令：CMD_DATA
                如果有其他控制命令，增加判断即可。
                * */
                if (this.cmd.equals("command")) {
                    String data = paras.get("CMD_DATA").asText();
                    return toBytes(data);
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static byte[] toBytes(String str) {
        if(str == null || str.trim().equals("")) {
            return new byte[0];
        }
        byte[] bytes = new byte[str.length() / 2];
        for(int i = 0; i < str.length() / 2; i++) {
            String subStr = str.substring(i * 2, i * 2 + 2);
            bytes[i] = (byte) Integer.parseInt(subStr, 16);
        }
        return bytes;
    }

    
    
    
    
	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public String getCmd() {
		return cmd;
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}

	public int getHasMore() {
		return hasMore;
	}

	public void setHasMore(int hasMore) {
		this.hasMore = hasMore;
	}

	public int getErrcode() {
		return errcode;
	}

	public void setErrcode(int errcode) {
		this.errcode = errcode;
	}

	public JsonNode getParas() {
		return paras;
	}

	public void setParas(JsonNode paras) {
		this.paras = paras;
	}

}
