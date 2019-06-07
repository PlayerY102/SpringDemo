//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//
package hello.Fabric;

import java.io.Serializable;
import java.util.Set;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.User;

public class HFUser implements User, Serializable {
    private String name;
    private String account;
    private String affiliation;
    private String mspId;
    private Set<String> roles;
    private Enrollment enrollment;

    public HFUser(String name, String affiliation, String mspId, Enrollment enrollment) {
        this.name = name;
        this.affiliation = affiliation;
        this.mspId = mspId;
        this.enrollment = enrollment;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccount() {
        return this.account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAffiliation() {
        return this.affiliation;
    }

    public void setAffiliation(String affiliation) {
        this.affiliation = affiliation;
    }

    public String getMspId() {
        return this.mspId;
    }

    public void setMspId(String mspId) {
        this.mspId = mspId;
    }

    public Set<String> getRoles() {
        return this.roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public Enrollment getEnrollment() {
        return this.enrollment;
    }

    public void setEnrollment(Enrollment enrollment) {
        this.enrollment = enrollment;
    }
}
