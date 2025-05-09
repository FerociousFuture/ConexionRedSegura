import java.io.Serial;
import java.io.Serializable;

//Ehh esta clase podria haber sido mejor pero sirve

public class Persona implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String fullname;
    private int age;
    private String uuid;

    public Persona() {}

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        return "Persona{" +
                "fullname='" + fullname + '\'' +
                ", age=" + age +
                ", uuid='" + uuid + '\'' +
                '}';
    }
}

//Bastante simple y bastante funcional