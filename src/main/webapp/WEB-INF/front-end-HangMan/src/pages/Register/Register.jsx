import { useState } from "react";
import { useNavigate } from 'react-router-dom';
import hangmanImage from '../../images/hangman.png';
import { validatePasswords, validateUsername } from "../../utils/ValidationUtils";
import "./Register.css";

const Register = () => {

    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [isPending, setIsPending] = useState(false);
    const [usernameErr, setUsernameErr] = useState('');
    const [passwordErr, setPasswordErr] = useState('');

    const handleSubmit = (e) => {
        setIsPending(true);
        e.preventDefault();
        const usernameErr = validateUsername(username);
        if (usernameErr) {
            setUsernameErr(usernameErr);
            setIsPending(false);
            return;
        }

        setUsernameErr('');
        const passwordErr = validatePasswords(password, confirmPassword);
        if (passwordErr) {
            setPasswordErr(validatePasswords);
            setIsPending(false);
            return;
        }
        setPasswordErr('');
    }

    return (
        <div className="register">
            <h2>Register</h2>
            <img src={hangmanImage} alt="" />
            <form onSubmit={handleSubmit}>
                <label>Enter username:</label>
                <input
                    type="text"
                    required
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                ></input>
                <label>Password:</label>
                <input
                    type="password"
                    required
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                ></input>
                <label>Confirm Password:</label>
                <input
                    type="password"
                    required
                    value={confirmPassword}
                    onChange={(e) => setConfirmPassword(e.target.value)}
                ></input>
                {validateUsername && <p className="validation-error">{usernameErr}</p>}
                {validatePasswords && <p className="validation-error">{passwordErr}</p>}
                {!isPending && <button>Register</button>}
                {isPending && <button disabled>Registering...</button>}
            </form>
        </div>
    );
}

export default Register;