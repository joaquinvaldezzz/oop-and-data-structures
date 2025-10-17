package oop;

/**
 * Represents a basic user.
 */
class User {
    // Encapsulation: private fields
    private String name;
    private int age;

    /**
     * Constructor for User.
     *
     * @param name The user's name.
     * @param age  The user's age.
     */
    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }

    // Abstraction: hides the details of how message is formatted
    /**
     * Get the user's introduction.
     *
     * @return A formatted introduction string.
     */
    public String getIntroduction() {
        return "Hi, I'm " + name + " and I'm " + age + " years old.";
    }

    // Encapsulation: controlled access via setters
    public void setName(String newName) {
        this.name = newName;
    }

    public void setAge(int newAge) {
        this.age = newAge;
    }
}

/**
 * Represents an admin user.
 * Demonstrates inheritance and polymorphism.
 */
class AdminUser extends User {
    private String[] privileges;

    /**
     * Constructor for AdminUser.
     *
     * @param name       The admin's name.
     * @param age        The admin's age.
     * @param privileges Array of admin privileges.
     */
    public AdminUser(String name, int age, String[] privileges) {
        super(name, age);
        this.privileges = privileges;
    }

    // Polymorphism: overriding parent's method
    @Override
    public String getIntroduction() {
        return "Hi, I'm Admin " + getName() + " with privileges: " + String.join(", ", privileges) + ".";
    }

    // Helper getter (used in polymorphism example)
    private String getName() {
        // we can expose 'name' in a controlled way
        return this.getClass().getSuperclass().getDeclaredFields()[0].getName();
    }
}

/**
 * Example program demonstrating OOP concepts.
 */
public class Main {
    public static void main(String[] args) {
        User user = new User("Alex", 25);
        System.out.println(user.getIntroduction());

        AdminUser admin = new AdminUser("Sam", 30, new String[] { "manage-users", "edit-content" });
        System.out.println(admin.getIntroduction());
    }
}
