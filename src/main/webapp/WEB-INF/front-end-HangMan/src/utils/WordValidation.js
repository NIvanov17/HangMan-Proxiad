export const wordValidation = (word) => {
    if (word.length < 3) {
        return "Word must be at least 3 characters.";
    } else if (!/^[a-zA-Z]+$/.test(word)) {
        return "Word must contain only letters.";
    } else if (/\s/.test(word)) {
        return "Word must not contain spaces.";
    }
    return null;
};
