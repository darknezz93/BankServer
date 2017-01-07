package domain;

import helper.ObjectIdJaxAdapter;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.Reference;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.List;

/**
 * Created by adam on 06.01.17.
 */

@Entity
@XmlRootElement
public class User {

    @Id
    @XmlJavaTypeAdapter(ObjectIdJaxAdapter.class)
    private ObjectId id;

    @NotNull
    private String password;

    @NotNull
    @Indexed(unique = true)
    private String userName;

    @Reference
    private List<Account> accounts;

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }
}
