package Lab1;

public enum Types {
    Integer,
    String;

    public static String getString(Types type) {
        switch (type) {
            case Integer -> {
                return "Integer";
            }
            case String -> {
                return "String";
            }
            default -> {
                return null;
            }
        }
    }
}
