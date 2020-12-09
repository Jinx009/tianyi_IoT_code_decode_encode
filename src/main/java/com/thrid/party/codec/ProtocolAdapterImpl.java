package com.thrid.party.codec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.huawei.m2m.cig.tup.modules.protocol_adapter.IProtocolAdapter;


public class ProtocolAdapterImpl implements IProtocolAdapter {

    private static final Logger logger = LoggerFactory.getLogger(ProtocolAdapterImpl.class);
    // 厂商名称
    private static final String MANU_FACTURERID = "Zhanway";
    // 设备型号
    private static final String MODEL = "ZWMNB01";

    @Override
    public String getManufacturerId() {
        return MANU_FACTURERID;
    }

    @Override
    public String getModel() {
        return MODEL;
    }

    public void activate() {
        logger.info("Codec demo HttpMessageHander activated.");
    }

    public void deactivate() {
        logger.info("Codec demo HttpMessageHander deactivated.");
    }

    public byte[] encode(ObjectNode input) throws Exception {
        System.out.println("dynamic lrbtest " + input.toString());
        try {
            CmdProcess cmdProcess = new CmdProcess(input);
            byte[] byteNode = cmdProcess.toByte();
            return byteNode;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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
	private static ObjectNode initCloudReqObjectNode() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode cloudReqObjectNode = mapper.createObjectNode();
        ObjectNode paras = mapper.createObjectNode();
        paras.put("CMD_DATA", "10");
        cloudReqObjectNode.put("identifier", "123");
        cloudReqObjectNode.put("msgType", "cloudReq");
        cloudReqObjectNode.put("cmd", "command");
        cloudReqObjectNode.put("paras", paras);
        cloudReqObjectNode.put("hasMore", 0);
        cloudReqObjectNode.put("mid", 2016);
        return cloudReqObjectNode;
    }
    
    public static void main(String[] args) throws Exception {
		System.out.println(bytesToHexString(new ProtocolAdapterImpl().encode(initCloudReqObjectNode() )));
	}


    public ObjectNode decode(byte[] binaryData) throws Exception {
        try {
            ReportProcess zhanwayProcess = new ReportProcess(binaryData);
            ObjectNode objectNode = zhanwayProcess.toJsonNode();
            logger.info("dynamic zhanwayProcess " + objectNode.toString());
            return objectNode;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
