import { useState } from "react";
import { useNavigate } from 'react-router-dom';
import hangmanImage from '../../images/hangman.png';
import Error from './../Errors/Error';
import "./Login.css";
import { validateUsername } from "../../utils/ValidationUtils";

const Login = () => {

    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [isPending, setIsPending] = useState(false);
    const navigate = useNavigate();
    const [error, setError] = useState(null);
    const [validationError, setValidationError] = useState('');



    const navigateWelcome = () => {
        navigate('/welcome');
    }

    const handleSubmit = (e) => {
        setIsPending(true);
        e.preventDefault();

        const loginDTO = {
            username: username,
            password: password
        };

        setTimeout(() => {
            fetch('http://localhost:8080/api/v1/players/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(loginDTO)
            },

            ).then((res) => {
                setIsPending(false);
                if (!res.ok) {
                    return res.json().then((errorData) => {
                        throw { message: errorData.message, ...errorData };
                    });
                }
                return res.json();
            })
                .then((data) => {
                    sessionStorage.setItem("token", data.token);
                    sessionStorage.setItem("username", data.username);
                    sessionStorage.setItem("role", data.roles);

                    window.dispatchEvent(new Event('authChange'));
                    navigateWelcome();
                })
                .catch(err => setValidationError("Wrong credentials!"))
        }, 500)
    }

    return (
        <div className="login">
            <div className="login-form">
                <h2>User Log In</h2>
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
                    {validationError && <p id="validation-error">{validationError}</p>}
                    {!isPending && <button>Submit</button>}
                    {isPending && <button disabled>Submitting...</button>}
                </form>
            </div>
        </div>
    );
}

export default Login;