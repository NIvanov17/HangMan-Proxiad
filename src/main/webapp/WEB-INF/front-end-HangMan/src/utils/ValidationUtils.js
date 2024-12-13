export const validateUsername = (username) => {
    if (username.length < 3) {
        return "Username must be at least 3 characters.";
    } else if (!/[a-zA-Z]/.test(username)) {
        return "Username must contain at least one letter.";
    } else if (/\s/.test(username)) {
        return "Username must not contain spaces.";
    }
    return null;
};

export const validatePasswords = (password, confirmPasswords) => {
    if (password.length < 6) {
        return "Password should be at least 6 characters long!";
    }
    if (password !== confirmPasswords) {
        return "Passwords should match!"
    }
    return null;
}