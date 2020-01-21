package com.mpc.merchant.helper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jline.utils.Log;

import java.util.Map;
import java.util.TreeMap;

public class QRReaderHelper {

    Logger log = LogManager.getLogger(getClass());

    // configure which ID is used here (including Sub ID)
    String[] contentId = new String[] {
            "00", "01",
            "02",
            "04", // static only
            "26", "00", "01", "02", "03", // sub
            "27", "00", "01", "02", // static only, sub
            "51", "00", "02", // static only, sub
            "52", "53", "54", "55", "58", "59", "60", "61",
            "62", //"01", // sub
            "63"
    };
    // configure which Root ID have a Sub ID here
    String[] rootWithSub = new String[] { "26", "27", "51" };//"62" };

    // Map to be returned
    Map<String, String> result = new TreeMap<>();

    // to access array ID by index
    int index = 0;

    public QRReaderHelper(String raw) {

        // to parse as another type, please make a new method
        this.parseAsMap(raw);
    }

    public Map<String, String> getResult() {
        return result;
    }

    private void parseAsMap(String raw) {

        Content content = new Content();

        for (int i = 0; i < raw.length(); i++) {

            // avoid OutOfBound
            if (index == contentId.length) {
                return;
            }

            // compare the substring with ID
            if ( contentId[index].equals( raw.substring(i, i + 2) ) ) {

                // copy content
                String tempData = content.getContent(raw);
                log.info("ID ".concat(contentId[index]).concat(" : ").concat(tempData));

                // put to map
                result.put(contentId[index], tempData.substring(4));

                // this is to get content which has sub data
                if ( isHaveSub(contentId[index]) ) {

                    // will strip string inside method
                    //log.info("Sub of ".concat(contentId[index]));
                    getSubContent( raw.substring(i + 4), contentId[index], content );
                    index--;
                }

                // strip the content copied
                raw = content.getRemainingString();
                //log.info("remain -> ".concat(raw));
            }

            // this would make sure we still at starting point of string comparison (loop thing)
            i--;

            // increase index by 1 to compare with the next ID
            index++;
        }

    }

    // pass the content (without root id and content length which is 1st 4 digit) and the root ID
    private void getSubContent(String raw, String rootID, Content content) {

        // go to sub index
        index++;

        // set how many sub id of root id
        int limit = getMaxSub(rootID);
        for(int i = 0; i < raw.length(); i++) {

            if ( contentId[index].equals( raw.substring(i, i + 2) ) ) {

                // copy content
                String tempData = content.getContent(raw);

                // strip remaining string
                raw = content.getRemainingString();

                // put to map
                result.put(rootID.concat("-").concat(contentId[index]), tempData.substring(4));

                limit--;
            }

            // this would make sure we still at starting point of string comparison (loop thing)
            i--;

            // stop looping
            if (limit == 0) {
                return;
            }

            // next ID
            index++;
        }
    }

    // check if the root id have sub id
    private boolean isHaveSub(String rootID) {

        // check from array rootWithSub
        for (String value : rootWithSub) {
            if (rootID.equals(value)) {
                return true;
            }
        }
        return false;
    }

    // return number of sub of root id
    private int getMaxSub(String rootID) {
        int limit = 0;
        // each root ID has a number of sub id, determine it here
        switch(rootID) {
            case "26" :
                limit = 4;
                break;
            case "27" :
                limit = 3;
                break;
            case "51" :
                limit = 2;
                break;
            case "62" :
                limit = 1;
                break;
        }
        return limit;
    }

    /*
    This class purpose solely to get content and strip the remaining string
     */
    private static class Content {

        private String strippedString;

        //Logger log = LogManager.getLogger(getClass());

        private String getContent(String raw) {

            // get length of content
            int contentLength = Integer.parseInt(raw.substring(2, 4));
            //Log.info("length:".concat(String.valueOf(contentLength)));

            // copy data start after 1st 4 digit
            String data = raw.substring(0, 4 + contentLength);
            //Log.info("data:".concat(data));

            // delete the copied data
            strippedString = raw.substring( 4 + contentLength);

            return data;
        }

        private String getRemainingString() {
            return strippedString;
        }
    }
}