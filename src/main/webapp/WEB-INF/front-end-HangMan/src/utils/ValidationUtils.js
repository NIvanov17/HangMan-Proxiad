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