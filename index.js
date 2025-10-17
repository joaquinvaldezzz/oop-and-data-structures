/** Represents a basic user. */
class User {
  // Public field (accessible everywhere)
  static species = "Human";

  // Private fields (enforced by # syntax)
  #age;

  /**
   * @param {string} name Public or protected by convention
   * @param {number} age Private field
   */
  constructor(name, age) {
    // Protected by convention (underscore prefix)
    this._name = name; // protected-like
    this.#age = age; // private
  }

  // Abstraction: public method encapsulating internal data access
  getIntroduction() {
    return `Hi, I'm ${this._name} and I'm ${this.#age} years old.`;
  }

  // Encapsulation: public setter and getter for private field
  setAge(newAge) {
    if (newAge > 0) {
      this.#age = newAge;
    }
  }

  getAge() {
    return this.#age;
  }
}

/** Represents an admin user, inheriting from User. */
class AdminUser extends User {
  /**
   * @param {string} name
   * @param {number} age
   * @param {string[]} privileges
   */
  constructor(name, age, privileges) {
    super(name, age);
    // Public field
    this.privileges = privileges;
  }

  // Polymorphism: overriding the base class method
  getIntroduction() {
    return `Hi, I'm Admin ${this._name} with privileges: ${this.privileges.join(", ")}.`;
  }
}

// Example usage
const user = new User("Alex", 25);
console.log(user.getIntroduction()); // Hi, I'm Alex and I'm 25 years old.
console.log(user.getAge()); // 25
user.setAge(26);
console.log(user.getAge()); // 26

const admin = new AdminUser("Sam", 30, ["manage-users", "edit-content"]);
console.log(admin.getIntroduction()); // Hi, I'm Admin Sam with privileges: manage-users, edit-content.
