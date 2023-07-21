package ddwu.mobile.finalproject.ma02_20201028.NaverAPI;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

public class NaverSearchXmlParser {
    public enum TagType { NONE, TITLE, DESCRIPTION, LINK };

    private final static String FAULT_RESULT = "faultResult";
    private final static String ITEM_TAG = "item";
    private final static String TITLE_TAG = "title";
    private final static String DESCRIPTION_TAG = "description";
    private final static String LINK_TAG = "link";
    private XmlPullParser parser;

    public NaverSearchXmlParser() {
        try {
            parser = XmlPullParserFactory.newInstance().newPullParser();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<NaverBlogDTO> parse(String xml) {

        ArrayList<NaverBlogDTO> resultList = new ArrayList();
        NaverBlogDTO dto = null;

        TagType tagType = TagType.NONE;

        try {
            parser.setInput(new StringReader(xml));
            int eventType = parser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals(ITEM_TAG)) {
                            dto = new NaverBlogDTO();
                        } else if (parser.getName().equals(TITLE_TAG)) {
                            if (dto != null) tagType = TagType.TITLE;
                        } else if (parser.getName().equals(DESCRIPTION_TAG)) {
                            if (dto != null) tagType = TagType.DESCRIPTION;
                        } else if (parser.getName().equals(LINK_TAG)) {
                            if (dto != null) tagType = TagType.LINK;
                        } else if(parser.getName().equals(FAULT_RESULT)) {
                            return null;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals(ITEM_TAG)) {
                            resultList.add(dto);
                            dto = null;
                        }
                        break;
                    case XmlPullParser.TEXT:
                        switch(tagType) {
                            case TITLE:
                                dto.setTitle(parser.getText());
                                break;
                            case DESCRIPTION:
                                dto.setDescription(parser.getText());
                                break;
                            case LINK:
                                if(parser.getText() == null){
                                    dto.setLink("정보 없음");
                                }else{
                                    dto.setLink(parser.getText());
                                }
                                break;
                        }
                        tagType = TagType.NONE;
                        break;
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultList;
    }
}

