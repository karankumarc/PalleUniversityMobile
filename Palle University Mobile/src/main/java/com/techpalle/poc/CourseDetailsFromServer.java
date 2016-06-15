package com.techpalle.poc;

/**
 * Created by skillgun on 12/26/2015.
 */

        //String assignment_hours = jo.getString("Assignment_Hours");
        //String avg_rating = jo.getString("Avg_rating");
        //String course_id = jo.getString("CourseId");
        //String course_fee_inr_after_discount = jo.getString("Course_Fee_INR_After_Discount");
        //String course_fee_usd_after_discount = jo.getString("Course_Fee_Usd_After_Discount");
        //String course_display_name = jo.getString("Course_displayname");
        //String course_fee_inr = jo.getString("Course_fee_inr");
        //String course_fee_usd = jo.getString("Course_fee_usd");
        //String course_hours = jo.getString("Course_hours");
        //String total_tests = jo.getString("TotalTests");
        //String total_views = jo.getString("Total_views");

public class CourseDetailsFromServer {
    private String course_id;
    private String avg_rating;
    private String total_views;
    private String course_display_name;
    private String course_hours;//HOURS
    private String assignment_hours;//HOURS
    private String total_tests;//HOURS
    private String course_fee_inr;
    private String course_fee_inr_after_discount;
    private String course_fee_usd;
    private String course_fee_usd_after_discount;
    private String courseTotalLectures;//number of videos


    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    public String getAvg_rating() {
        return avg_rating;
    }

    public void setAvg_rating(String avg_rating) {
        this.avg_rating = avg_rating;
    }

    public String getTotal_views() {
        return total_views;
    }

    public void setTotal_views(String total_views) {
        this.total_views = total_views;
    }

    public String getCourse_display_name() {
        return course_display_name;
    }

    public void setCourse_display_name(String course_display_name) {
        this.course_display_name = course_display_name;
    }

    public String getCourse_hours() {
        return course_hours;
    }

    public void setCourse_hours(String course_hours) {
        this.course_hours = course_hours;
    }

    public String getAssignment_hours() {
        return assignment_hours;
    }

    public void setAssignment_hours(String assignment_hours) {
        this.assignment_hours = assignment_hours;
    }

    public String getTotal_tests() {
        return total_tests;
    }

    public void setTotal_tests(String total_tests) {
        this.total_tests = total_tests;
    }

    public String getCourse_fee_inr() {
        return course_fee_inr;
    }

    public void setCourse_fee_inr(String course_fee_inr) {
        this.course_fee_inr = course_fee_inr;
    }

    public String getCourse_fee_inr_after_discount() {
        return course_fee_inr_after_discount;
    }

    public void setCourse_fee_inr_after_discount(String course_fee_inr_after_discount) {
        this.course_fee_inr_after_discount = course_fee_inr_after_discount;
    }

    public String getCourse_fee_usd() {
        return course_fee_usd;
    }

    public void setCourse_fee_usd(String course_fee_usd) {
        this.course_fee_usd = course_fee_usd;
    }

    public String getCourse_fee_usd_after_discount() {
        return course_fee_usd_after_discount;
    }

    public void setCourse_fee_usd_after_discount(String course_fee_usd_after_discount) {
        this.course_fee_usd_after_discount = course_fee_usd_after_discount;
    }

    public String getCourseTotalLectures() {
        return courseTotalLectures;
    }

    public void setCourseTotalLectures(String courseTotalLectures) {
        this.courseTotalLectures = courseTotalLectures;
    }
}
