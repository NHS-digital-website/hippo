package uk.nhs.digital;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class OdsUtils {
    public static void main(String[] args) throws IOException {
        OdsUtils util = new OdsUtils();
        int offSet = 1;
        List<Organisation> finalList = new ArrayList();
        while (true) {
            String str = util.extracted(String.valueOf(offSet)).toString().trim();
            System.out.println("Value is " + str.length());
            List<Organisation> tempList = util.jsonFomart(str);
            System.out.println("Value is tempList " + tempList.size());
            if (tempList != null && tempList.size() > 0) {
                finalList.addAll(tempList);
                offSet += 1000;
                System.out.println("Value of offSet is " + offSet);
                System.out.println("Size of finalList is " + finalList.size());
            } else {
                break;
            }
        }
        ObjectMapper objectMapper = new ObjectMapper();
        //Set pretty printing of json
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        ObjectWriter writer = objectMapper.writer(new DefaultPrettyPrinter());

        writer.writeValue(new File("ODS_JSON.json"), finalList);
    }

    public List<Organisation> jsonFomart(String str) throws IOException {
        JSONObject obj = new JSONObject(str);
        List<Organisation> orgCodeName = new ArrayList<Organisation>();
        JSONArray array = obj.getJSONArray("Organisations");
        for (int i = 0; i < array.length(); i++) {
            Organisation org = new Organisation();
            String name = array.getJSONObject(i).getString("Name");
            String key = array.getJSONObject(i).getString("OrgId");
            org.setOrgName(name);
            org.setCode(key);
            orgCodeName.add(org);
        }
        return orgCodeName;
    }

    private StringBuilder extracted(String offSet) {
        StringBuilder strOutput = new StringBuilder();
        try {
            //199672
            URL url = new URL("https://directory.spineservices.nhs.uk/ORD/2-0-0/organisations?Status=active&Limit=1000&Offset=" + offSet);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP Error code : "
                    + conn.getResponseCode());
            }
            InputStreamReader in = new InputStreamReader(conn.getInputStream());
            BufferedReader br = new BufferedReader(in);
            String output;

            while ((output = br.readLine()) != null) {

                strOutput.append(output);
            }
            conn.disconnect();

        } catch (Exception e) {
            System.out.println("Exception in NetClientGet:- " + e);
        }
        return strOutput;
    }

    class Organisation {
        private String orgName;
        private String code;

        public String getOrgName() {
            return orgName;
        }

        public void setOrgName(String orgName) {
            this.orgName = orgName;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }
}


