import { useState } from "react";
import { useNavigate } from 'react-router-dom';
import hangmanImage from '../../images/hangman.png';
import { validateUsername } from "../../utils/ValidationUtils";
import "./Register.css";

const Register = () => {

    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [isPending, setIsPending] = useState(false);
    const [validationError, setValidationError] = useState('');

    const handleSubmit = (e) => {
        setIsPending(true);
        e.preventDefault();
        const validationMessage = validateUsername(username);
        if (validationMessage) {
            setValidationError(validationMessage);
            setIsPending(false);
            return;
        }
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
                {validationError && <p className="validation-error">{validationError}</p>}
                {!validationError && <p className="validation-error"></p>}
                {!isPending && <button>Submit</button>}
                {isPending && <button disabled>Submitting...</button>}
            </form>
        </div>
    );
}

export default Register;