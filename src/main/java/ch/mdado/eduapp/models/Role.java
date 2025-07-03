package ch.mdado.eduapp.models;

public enum Role {
    ADMIN(1, "ROLE_ADMIN"),
    TEACHER(2, "ROLE_TEACHER"),
    STUDENT(3, "ROLE_STUDENT");

    private Integer code;
    private String description;

    Role(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static Role toEnum(Integer code){
        if(code == null){
            return null;
        }
        for(Role role : Role.values()){
            if(code.equals(role.getCode())){
                return role;
            }
        }
        throw new IllegalArgumentException("Invalid id: " + code);
    }
}