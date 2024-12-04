package com.sjsu.cmpe272.prodwell.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    private String id;
    private String oid;
    private String email;
    private String given_name;
    private String family_name;
    private String jobRole;
    private String jobLevel;
    private String projectCode;

    // Getters
    public String getId() {
        return this.id;
    }

    public String getOid() {
        return this.oid;
    }

    public String getEmail() {
        return this.email;
    }


    public String getGiven_name() {
        return this.given_name;
    }

    public String getFamily_name() {
        return this.family_name;
    }

    public String getJobRole() {
        return this.jobRole;
    }

    public String getJobLevel() {
        return this.jobLevel;
    }

    public String getProjectCode() {
        return this.projectCode;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public void setGiven_name(String given_name) {
        this.given_name = given_name;
    }

    public void setFamily_name(String family_name) {
        this.family_name = family_name;
    }

    public void setJobRole(String jobTitle) {
        this.jobRole = jobTitle;
    }
    public void setJobLevel(String jobTitle) {
        this.jobLevel = jobTitle;
    }
    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }
}