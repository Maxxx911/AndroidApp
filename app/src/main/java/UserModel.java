import com.orm.SugarRecord;

public class UserModel extends SugarRecord {
    String Name;
    String LastName;
    String Email;
    String PhoneNumber;
    String URI;
    public UserModel()
    {

    }
    public UserModel(String name, String lastName, String email, String phoneNumber, String uri)
    {
        this.URI = uri;
        this.Name = name;
        this.LastName = lastName;
        this.Email = email;
        this.PhoneNumber = phoneNumber;
    }
    public UserModel(String name, String lastName, String email, String phoneNumber)
    {
        this.Name = name;
        this.LastName = lastName;
        this.Email = email;
        this.PhoneNumber = phoneNumber;
    }
}
