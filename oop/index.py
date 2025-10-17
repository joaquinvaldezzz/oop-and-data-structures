class User:
    """
    Represents a basic user.

    Attributes:
        _name (str): The user's name (encapsulation: private-like).
        _age (int): The user's age (encapsulation: private-like).
    """

    def __init__(self, name: str, age: int):
        # Encapsulation: protected-like attributes
        self._name = name
        self._age = age

    # Abstraction: method hides internal details of how message is built
    def get_introduction(self) -> str:
        return f"Hi, I'm {self._name} and I'm {self._age} years old."

    # Encapsulation: setters and getters control access
    def set_name(self, new_name: str):
        self._name = new_name

    def set_age(self, new_age: int):
        self._age = new_age


class AdminUser(User):
    """
    Represents an admin user (demonstrates inheritance and polymorphism).

    Attributes:
        privileges (list[str]): A list of admin privileges.
    """

    def __init__(self, name: str, age: int, privileges: list[str]):
        super().__init__(name, age)
        self.privileges = privileges

    # Polymorphism: overriding parent behavior
    def get_introduction(self) -> str:
        return f"Hi, I'm Admin {self._name} with privileges: {', '.join(self.privileges)}."


# Example Usage
user = User("Alex", 25)
print(user.get_introduction())  # Hi, I'm Alex and I'm 25 years old.

admin = AdminUser("Sam", 30, ["manage-users", "edit-content"])
# Hi, I'm Admin Sam with privileges: manage-users, edit-content.
print(admin.get_introduction())
