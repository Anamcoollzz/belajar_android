package anamapp.pro.belajar.models;

public class UserModel {

    int id;
    String name;
    String tall;

    // contrustor(empty)
    public UserModel() {
    }

    // constructor
    public UserModel(int id, String tall, String name) {
        this.id = id;
        this.tall = tall;
        this.name = name;
    }

    /*Setter and Getter*/

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTall() {
        return tall;
    }

    public void setTall(String tall) {
        this.tall = tall;
    }
}
