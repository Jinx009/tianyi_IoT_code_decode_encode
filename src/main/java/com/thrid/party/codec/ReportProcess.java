package com.thrid.party.codec;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ReportProcess {
    private String identifier = "zhanway_device";
    private String msgType = "deviceReq";
    private int hasMore = 0;
    private String serviceId = "data";
    private String serviceData = "0";
    
    public ReportProcess(byte[] binaryData) {

        /*
        如果是设备上报数据，返回格式为
        {
            "identifier":"123",
            "msgType":"deviceReq",
            "hasMore":0,
            "data":[{"serviceId":"Brightness",
                      "serviceData":{"brightness":50},
                      {
                      "serviceId":"Electricity",
                      "serviceData":{"voltage":218.9,"current":800,"frequency":50.1,"powerfactor":0.98},
                      {
                      "serviceId":"Temperature",
                      "serviceData":{"temperature":25},
                      ]
	    }
	    */
    	 msgType = "deviceReq";
         hasMore = 0;
         identifier = "zhanway_device";
         serviceData = bytesToHexString(binaryData);
         serviceId = "data";
    }

    public static String bytesToHexString(byte[] src){   
        StringBuilder stringBuilder = new StringBuilder("");   
        if (src == null || src.length <= 0) {   
            return null;   
        }   
        for (int i = 0; i < src.length; i++) {   
            int v = src[i] & 0xFF;   
            String hv = Integer.toHexString(v); 
            if (hv.length() < 2) {   
                stringBuilder.append(0);   
            }   
            stringBuilder.append(hv.toUpperCase());   
        }   
        return stringBuilder.toString();   
    }   
    
	@SuppressWarnings("deprecation")
	public ObjectNode toJsonNode() {
        try {
            //组装body体
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode root = mapper.createObjectNode();

            root.put("identifier", this.identifier);
            root.put("msgType", this.msgType);

            //根据msgType字段组装消息体
            if (this.msgType.equals("deviceReq")) {
                root.put("hasMore", this.hasMore);
                ArrayNode arrynode = mapper.createArrayNode();

                //serviceId=Brightness 数据组装
                ObjectNode service = mapper.createObjectNode();
                service.put("serviceId", this.serviceId);
                service.put("serviceData", serviceData);
                arrynode.add(service);
                root.put("data", arrynode);
                return root;
            } else {
            }
            return root;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
	
    private static byte[] initDeviceReqByte() {
        /**
         * 本例入参： AA 72 00 00 32 08 8D 03 20 62 33 99
         */
        byte[] byteDeviceReq = new byte[12];
        byteDeviceReq[0] = (byte) 0xAA;
        byteDeviceReq[1] = (byte) 0x72;
        byteDeviceReq[2] = (byte) 0x00;
        byteDeviceReq[3] = (byte) 0x00;
        byteDeviceReq[4] = (byte) 0x32;
        byteDeviceReq[5] = (byte) 0x08;
        byteDeviceReq[6] = (byte) 0x8D;
        byteDeviceReq[7] = (byte) 0x03;
        byteDeviceReq[8] = (byte) 0x20;
        byteDeviceReq[9] = (byte) 0x62;
        byteDeviceReq[10] = (byte) 0x33;
        byteDeviceReq[11] = (byte) 0x99;
        return byteDeviceReq;
    }
	
	public static void main(String[] args) {
		byte[] b = initDeviceReqByte();
		ReportProcess zhanwayProcess = new ReportProcess(b);
        ObjectNode objectNode = zhanwayProcess.toJsonNode();
        System.out.println(objectNode.toString());
	}
}