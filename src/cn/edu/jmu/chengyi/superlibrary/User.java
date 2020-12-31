package cn.edu.jmu.chengyi.superlibrary;

public final class User {
    public static final User EMPTY = new User(-1, "", "", UserPermission.USER);

    private final int id;
    private final String name;
    private final String pwd;
    private final UserPermission permission;

    User(int id, String name, String pwd, UserPermission permission) {
        this.id = id;
        this.name = name;
        this.pwd = pwd;
        this.permission = permission;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPwd() {
        return pwd;
    }

    public UserPermission getPermission() {
        return permission;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        User other = (User) obj;
        if (id != other.id)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", name=" + name + ", pwd=" + pwd + ", permission=" + permission + "]";
    }
}
