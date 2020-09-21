public class SystemCall {

    private final Types[] arguments;

    public SystemCall(Types[] arguments) {
        this.arguments = arguments;
    }

    public int getArgumentCount() {
        return arguments.length;
    }

    public Types[] getArguments() {
        return arguments;
    }

    public String getArgumentsString() {
        StringBuilder argumentString = new StringBuilder();
        for(Types type : arguments) {
            argumentString.append(Types.getString(type)).append("; ");
        }
        return argumentString.toString();
    }
}
