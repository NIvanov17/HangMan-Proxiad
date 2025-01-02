import { useState } from "react";
import { useNavigate } from 'react-router-dom';
import hangmanImage from '../../images/hangman.png';
import { validatePasswords, validateUsername } from "../../utils/ValidationUtils";
import "./Register.css";
import Error from './../Errors/Error';

const Register = () => {

    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [isPending, setIsPending] = useState(false);
    const [usernameErr, setUsernameErr] = useState('');
    const [passwordErr, setPasswordErr] = useState('');
    const [error, setError] = useState(null);
    const navigate = useNavigate();

    const navigateToLogIn = () => {
        navigate('/login');
    }

    const handleSubmit = (e) => {
        e.preventDefault();
        setIsPending(true);

        const registerDTO = {
            username: username,
            password: password,
            confirmPassword: confirmPassword
        };

        const usernameErr = validateUsername(username);
        if (usernameErr) {
            setIsPending(false);
            setUsernameErr(usernameErr);
            return;
        }

        setUsernameErr('');
        const passwordErr = validatePasswords(password, confirmPassword);
        if (passwordErr) {
            setIsPending(false);
            setPasswordErr(validatePasswords);
            return;
        }
        setPasswordErr('');

        fetch('http://localhost:8080/api/v1/players/registration', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(registerDTO)
        }).then(res => {
            if (!res.ok) {
                setIsPending(false);
                return res.json().then((errData) => {
                    throw { message: errData.message, ...errData };
                })
            }
            return res.text();
        }).then(data => {
            setIsPending(false);
            alert(data);
            navigateToLogIn();
        }).catch(err => {
            setError(err.message);
        })

        if (error) {
            return <Error error={error} />;
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
                {validateUsername && <p id="validation-error">{usernameErr}</p>}
                {validatePasswords && <p id="validation-error">{passwordErr}</p>}
                {error && (

                    <p className="validation-error">{error}</p>

                )}
                {!isPending && <button>Register</button>}
                {isPending && <button disabled>Registering...</button>}

            </form>
        </div>
    );
}

export default Register;