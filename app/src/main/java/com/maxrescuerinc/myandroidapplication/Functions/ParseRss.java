package com.maxrescuerinc.myandroidapplication.Functions;

import android.util.Log;
import android.util.Xml;

import com.maxrescuerinc.myandroidapplication.Models.NewsItem;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ParseRss {

    private static final String ns = null;

    public static List<NewsItem> parseFeed(InputStream inputStream) throws XmlPullParserException, IOException {
        try {
            XmlPullParser xmlPullParser = Xml.newPullParser();
            xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            xmlPullParser.setInput(inputStream, null);
            xmlPullParser.nextTag();
            return reedFeed(xmlPullParser);
        } finally {
            inputStream.close();
        }
    }

    private static List<NewsItem> reedFeed(XmlPullParser xmlPullParser) throws IOException, XmlPullParserException {
        List<NewsItem> items = new ArrayList<>();
        while (xmlPullParser.next() != xmlPullParser.END_DOCUMENT){
            if (xmlPullParser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

                String name = xmlPullParser.getName();
            if (name.equals("channel")){
                continue;
            }
            if(name.equals("item")){
                    items.add(ReadItem(xmlPullParser));
                }else{
                    skip(xmlPullParser);
                }
            }
            return items;
        }

    private static NewsItem ReadChannel(XmlPullParser xmlPullParser) throws IOException, XmlPullParserException {
       // xmlPullParser.require(XmlPullParser.START_TAG,ns,"channel");
        String title = null;
        String description = null;
        String link = null;
        String pubDate = null;
        String mediaLink = null;
        while (xmlPullParser.next()!= XmlPullParser.END_TAG){
            if(xmlPullParser.getEventType()!= XmlPullParser.START_TAG){
                continue;
            }
            String name = xmlPullParser.getName();
            if(name.equals("title")){
                title =  ReadTitle(xmlPullParser);
            }else if(name.equals("description")){
                description = ReadDescription(xmlPullParser);
            }else if(name.equals("link")){
                link = ReadLink(xmlPullParser);
            }else if(name.equals("pubDate")){
                pubDate = ReadPubDate(xmlPullParser);
            }else if(name.equals("media:content")){
                mediaLink = ReadMedia(xmlPullParser);
            }
        }
        return new NewsItem(title,link,description,pubDate,mediaLink);
    }


    private static NewsItem ReadItem(XmlPullParser xmlPullParser) throws IOException, XmlPullParserException {
            String title = null;
            String description = null;
            String link = null;
            String pubDate = null;
            String mediaLink = null;
            while (xmlPullParser.next()!= XmlPullParser.END_TAG){
                if (xmlPullParser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                String name = xmlPullParser.getName();
                switch (name) {
                    case "title":
                        title = ReadTitle(xmlPullParser);
                        break;
                    case "description":
                        description = ReadDescription(xmlPullParser);
                        break;
                    case"link":
                        link = ReadLink(xmlPullParser);
                        break;
                    case "pubDate":
                        pubDate = ReadPubDate(xmlPullParser);
                        break;
                    case "media:content":
                        mediaLink = ReadMedia(xmlPullParser);
                        break;
                    default:
                      skip(xmlPullParser);
                        break;
                }
            }
            return new NewsItem(title,link,description,pubDate,mediaLink);
    }

    private static String ReadMedia(XmlPullParser xmlPullParser) throws IOException, XmlPullParserException {
        String mediaLink = "";
       // xmlPullParser.require(XmlPullParser.START_TAG, ns, "media:content");
        String tag = xmlPullParser.getName();
        String relType = xmlPullParser.getAttributeValue(null, "medium");
        if (tag.equals("media:content")) {
            if (relType.equals("image")){
                mediaLink = xmlPullParser.getAttributeValue(null, "url");
                xmlPullParser.nextTag();
            }
        }
        return mediaLink;
    }

    private static void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

    private static String ReadPubDate(XmlPullParser xmlPullParser) throws IOException, XmlPullParserException {
        xmlPullParser.next();
        String pubDate = xmlPullParser.getText();
        xmlPullParser.next();
        return pubDate;
    }

    private static String ReadLink(XmlPullParser xmlPullParser) throws IOException, XmlPullParserException {
       // xmlPullParser.require(XmlPullParser.START_TAG,ns,"link");
        xmlPullParser.next();
        String link = xmlPullParser.getText();
        //xmlPullParser.require(XmlPullParser.END_TAG,ns,"link");
        xmlPullParser.next();
        return link;
    }

    private static String ReadDescription(XmlPullParser xmlPullParser) throws IOException, XmlPullParserException {
        //xmlPullParser.require(XmlPullParser.START_TAG,ns,"description");
        String description = readText(xmlPullParser);
        //xmlPullParser.require(XmlPullParser.END_TAG,ns,"description");
        xmlPullParser.next();
        return description;
    }

    private static String ReadTitle(XmlPullParser xmlPullParser) throws IOException, XmlPullParserException {
        //xmlPullParser.require(XmlPullParser.START_TAG,ns,"title");
        String title = readText(xmlPullParser);
        //xmlPullParser.require(XmlPullParser.END_TAG, ns, "title");
        xmlPullParser.next();
        return title;
    }

    private static String readText(XmlPullParser xmlPullParser) throws IOException, XmlPullParserException {
        String result = "";
        if(xmlPullParser.next() == XmlPullParser.TEXT){
            result = ModifyText(xmlPullParser.getText());
        }
        return result;
    }

    private static String ModifyText(String text) {
        text =  text.replaceAll("<p>","\t");
        text = text.replaceAll("<[^<]*/>","");
        text = text.replaceAll("</p>", "");
        text = text.replaceAll("<b>", "");
        text = text.replaceAll("/<b>", "");
        return text;
    }
}
