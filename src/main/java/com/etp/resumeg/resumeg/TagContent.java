package com.etp.resumeg.resumeg;

import java.util.ArrayList;
import java.util.List;

public class TagContent {

    private static List<TagContent> tagContentList = new ArrayList<>();

    public TagContent() {
    }

    public TagContent(String tagName, List<String> content) {
        this.tagName = tagName;
        this.content = content;
    }

    public static void fillTagContentList(){
        List<String> nameContent = new ArrayList<>();
        nameContent.add("Your Name");
        TagContent name = new TagContent("name", nameContent);

        List<String> streetContent = new ArrayList<>();
        streetContent.add("123 Your Street");
        TagContent street = new TagContent("street", streetContent);

        List<String> cityContent = new ArrayList<>();
        cityContent.add("Your City, ST 12345");
        TagContent city = new TagContent("city", cityContent);

        List<String> phoneContent = new ArrayList<>();
        phoneContent.add("(123) 456-7890");
        TagContent phone = new TagContent("phone", phoneContent);

        List<String> emailContent = new ArrayList<>();
        emailContent.add("no_reply@example.com");
        TagContent email = new TagContent("email", emailContent);

        List<String> titleContent = new ArrayList<>();
        titleContent.add("EXPERIENCE");
        titleContent.add("EDUCATION");
        titleContent.add("PROJECTS");
        titleContent.add("SKILLS");
        titleContent.add("AWARDS");
        titleContent.add("LANGUAGES");
        TagContent title = new TagContent("title", titleContent);

        tagContentList.add(name);
        tagContentList.add(street);
        tagContentList.add(city);
        tagContentList.add(phone);
        tagContentList.add(email);
        tagContentList.add(title);
    }

    public static List<TagContent> getTagContentList() {
        return tagContentList;
    }

    public static void setTagContentList(List<TagContent> tagContentList) {
        TagContent.tagContentList = tagContentList;
    }

    private String tagName;
    private List<String> content;

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public List<String> getContent() {
        return content;
    }

    public void setContent(List<String> content) {
        this.content = content;
    }
}
