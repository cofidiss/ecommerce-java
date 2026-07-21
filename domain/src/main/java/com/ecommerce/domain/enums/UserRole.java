package com.ecommerce.domain.enums;

public enum UserRole {
    Standart(0),
    Admin(1);

    private final int id;

    UserRole(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static UserRole fromId(int id) {
        for (UserRole role : values()) {
            if (role.id == id) {
                return role;
            }
        }
        throw new IllegalArgumentException("Invalid role id: " + id);
    }

}
