package courier;

public class ClientGenerator {
    public static Courier checkCreateCourier() {
        return new Courier("Courier111" + (int) (Math.random() * (1000)),
                "Password222" + (int) (Math.random() * (1000)),
                "LoginName333" + (int) (Math.random() * (1000)));
    }

    public static Courier checkCourierWithoutFirstName() {
        return new Courier("Courier111" + (int) (Math.random() * (1000)),
                "Password222" + (int) (Math.random() * (1000)),
                null);
    }

    public static Courier checkCourierWithoutLogin() {
        return new Courier(null, "Password222" + (int) (Math.random() * (1000)),
                "LoginName333" + (int) (Math.random() * (1000)));
    }

    public static Courier checkCourierWithoutPassword() {
        return new Courier("Courier111" + (int) (Math.random() * (1000)),
                null,
                "LoginName333" + (int) (Math.random() * (1000)));
    }

}
