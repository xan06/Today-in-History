package com.example.getevent;

import java.util.List;

public class Event {
    private String year;
    private String html;
    private String text;
    private List<Link> links;

    public void setYear(String setYear) {
        year = setYear;
    }

    public void setHtml(String setHtml) {
        html = setHtml;
    }

    public void setText(String setText) {
        text = setText;
    }

    public void setLinks(List<Link> setLinks) {
        links = setLinks;
    }

    public String getYear() {
        return year;
    }

    public String getHtml() {
        return html;
    }

    public String getText() {
        return text;
    }

    public List<Link> getLinks() {
        return links;
    }

    public static class Link {
        private String title;
        private String link;

        public void setTitle(String setTitle) {
            title = setTitle;
        }

        public void setLink(String setLink) {
            link = setLink;
        }

        public String getTitle() {
            return title;
        }

        public String  getLink() {
            return link;
        }
    }

    @Override
    public String toString() {
        return year + "-" + text;
    }
}
